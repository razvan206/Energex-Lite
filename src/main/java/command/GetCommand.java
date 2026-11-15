package command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.DataType;
import domain.MarketPoint;
import fileio.CommandInput;
import fileio.CommandParametersInput;
import main.Main;
import repository.MemoryRepository;
import util.SimulationClock;
import util.TimeConversionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public final class GetCommand extends Command {
    private static final int MINUTES_PER_INTERVAL = 15;

    public GetCommand(final CommandInput input) {
        super(input);
    }

    @Override
    public void executeCommand(final MemoryRepository memoryRepository,
                               final SimulationClock simulationClock,
                               final ArrayNode output) {
        CommandParametersInput params = this.input.getParameters();
        String clientTier = params.getClientTier();
        String dataTypeStr = params.getDataType();
        DataType dataType = DataType.valueOf(dataTypeStr);
        String target = params.getTarget();
        String start = params.getStart();
        String end = params.getEnd();

        String from = null;
        String to = null;
        if (start != null && end != null) {
            from = start;
            to = end;
        } else if ("LATEST".equals(target)) {
            MarketPoint latest = memoryRepository.getLatestPoint(dataType);
            if (latest == null) {
                from = null;
                to = null;
            } else {
                int intervalIndex = latest.getIntervalIndex();
                LocalDateTime startInterval = LocalDate.parse(
                        latest.getDate()).atStartOfDay().plusMinutes(
                        (long) (intervalIndex - 1) * MINUTES_PER_INTERVAL);
                LocalDateTime endInterval = startInterval.plusMinutes(
                        MINUTES_PER_INTERVAL);
                from = startInterval.toString();
                to = endInterval.toString();
            }
        } else if (TimeConversionUtils.isDateValid(target)) {
            from = target;
            to = LocalDateTime.parse(target).plusMinutes(
                    MINUTES_PER_INTERVAL).toString();
        }

        if (clientTier.equals("FREE")
                && dataType == DataType.DAM_PRICE_PREDICTION) {
            ObjectNode errorNode = Main.MAPPER.createObjectNode();
            errorNode.put("command", "get");
            errorNode.put("timestamp", this.input.getTimestamp());
            errorNode.put("status", "ERROR");
            errorNode.put("from", from);
            errorNode.put("to", to);
            errorNode.put("message", "Access denied: DAM_PRICE_PREDICTION"
                    + " requires PREMIUM subscription.");
            output.add(errorNode);
            return;
        }

        if (from == null || to == null) {
            ObjectNode warningNode = Main.MAPPER.createObjectNode();
            warningNode.put("command", "get");
            warningNode.put("timestamp", this.input.getTimestamp());
            warningNode.put("status", "WARNING");
            warningNode.put("from", from);
            warningNode.put("to", to);
            warningNode.putNull("data");
            warningNode.put("message",
                    "Data is not yet available. Try again later.");
            output.add(warningNode);
            return;
        }
        LocalDateTime fromTime = LocalDateTime.parse(from);
        LocalDateTime toTime = LocalDateTime.parse(to);

        Map<String, List<MarketPoint>> pointsDay =
                memoryRepository.getPointsInRange(dataType, fromTime, toTime);

        if (pointsDay.isEmpty()) {
            ObjectNode warningNode = Main.MAPPER.createObjectNode();
            warningNode.put("command", "get");
            warningNode.put("timestamp", this.input.getTimestamp());
            warningNode.put("status", "WARNING");
            warningNode.put("from", from);
            warningNode.put("to", to);
            warningNode.putNull("data");
            warningNode.put("message",
                    "Data is not yet available. Try again later.");
            output.add(warningNode);
            return;
        }
        ObjectNode successNode = Main.MAPPER.createObjectNode();
        successNode.put("command", "get");
        successNode.put("timestamp", this.input.getTimestamp());
        successNode.put("status", "SUCCESS");
        successNode.put("from", from);
        successNode.put("to", to);

        ArrayNode daysArray = Main.MAPPER.createArrayNode();
        for (Map.Entry<String, List<MarketPoint>> entry : pointsDay.entrySet()) {
            ObjectNode dayNode = Main.MAPPER.createObjectNode();
            dayNode.put("id", dataTypeStr);
            dayNode.put("date", entry.getKey());

            ArrayNode pointsArray = Main.MAPPER.createArrayNode();

            for (MarketPoint p : entry.getValue()) {
                ObjectNode marketPointNode = Main.MAPPER.createObjectNode();
                p.fillJsonPoint(marketPointNode);
                pointsArray.add(marketPointNode);
            }
            dayNode.set("data", pointsArray);
            daysArray.add(dayNode);
        }
        successNode.set("data", daysArray);
        output.add(successNode);
    }
}
