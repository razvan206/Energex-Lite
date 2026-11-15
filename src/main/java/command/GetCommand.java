package command;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class GetCommand extends Command{
    public GetCommand(CommandInput input){
        super(input);
    }

    @Override
    public void executeCommand(MemoryRepository memoryRepository, SimulationClock simulationClock, ArrayNode output) {
        CommandParametersInput params = this.input.getParameters();
        String clientTier = params.getClientTier();
        String dataTypeStr = params.getDataType();
        DataType dataType = DataType.valueOf(dataTypeStr);
        String target = params.getTarget();
        String start = params.getStart();
        String end = params.getEnd();
        // from -> start time for get command
        // to -> end time for get command
        String from = null;
        String to = null;
        if(start != null && end != null){
            from = start;
            to = end;
        }else if("LATEST".equals(target)){
            MarketPoint latest = memoryRepository.getLatestPoint(dataType);
            if(latest == null){
                from = null;
                to = null;
            }
            else{
                String[] hours = TimeConversionUtils.indexToInterval(latest.getIntervalIndex()).split(" - ");
                from = latest.getDate() + "T" + hours[0];
                to = latest.getDate() + "T" + hours[1];
            }
        }else if(TimeConversionUtils.isDateValid(target)){
            from = target;
            to = LocalDateTime.parse(target).plusMinutes(15).toString();
        }

        if(clientTier.equals("FREE") && dataType == DataType.DAM_PRICE_PREDICTION){
            ObjectNode errorNode = Main.MAPPER.createObjectNode();
            errorNode.put("command", "get");
            errorNode.put("timestamp", this.input.getTimestamp());
            errorNode.put("status", "ERROR");
            errorNode.put("from", from);
            errorNode.put("to", to);
            errorNode.put("message", "Access denied: DAM_PRICE_PREDICTION requires PREMIUM subscription.");
            output.add(errorNode);
            return;
        }
        // get latest on a empty repository :
        if (from == null || to == null){
            ObjectNode warningNode = Main.MAPPER.createObjectNode();
            warningNode.put("command", "get");
            warningNode.put("timestamp", this.input.getTimestamp());
            warningNode.put("status", "WARNING");
            warningNode.put("from", from);
            warningNode.put("to", to);
            warningNode.putNull("data");
            warningNode.put("message", "Data is not yet available. Try again later.");
            output.add(warningNode);
            return;
        }
        LocalDateTime fromTime = LocalDateTime.parse(from);
        LocalDateTime toTime = LocalDateTime.parse(to);
        // we got the interval that satisfies the get command in pointsDay
        Map<String, List<MarketPoint>> pointsDay = memoryRepository.getPointsInRange(dataType, fromTime, toTime);

        if(pointsDay.isEmpty()){
            ObjectNode warningNode = Main.MAPPER.createObjectNode();
            warningNode.put("command", "get");
            warningNode.put("timestamp", this.input.getTimestamp());
            warningNode.put("status", "WARNING");
            warningNode.put("from", from);
            warningNode.put("to", to);
            warningNode.putNull("data");
            warningNode.put("message", "Data is not yet available. Try again later.");
            output.add(warningNode);
            return;
        }
        ObjectNode successNode = Main.MAPPER.createObjectNode();
        successNode.put("command", "get");
        successNode.put("timestamp", this.input.getTimestamp());
        successNode.put("from", from);
        successNode.put("to", to);
        successNode.put("status", "SUCCESS");

        ArrayNode daysArray = Main.MAPPER.createArrayNode();
        for(Map.Entry<String, List<MarketPoint>> entry : pointsDay.entrySet()){
            ObjectNode  dayNode = Main.MAPPER.createObjectNode();
            dayNode.put("id", dataTypeStr);
            dayNode.put("date", entry.getKey());

            ArrayNode pointsArray = Main.MAPPER.createArrayNode();

            for(MarketPoint p : entry.getValue()){
                ObjectNode marketPointNode = Main.MAPPER.createObjectNode();
                // we use helper method fillJsonPoint for every type of ID
                p.fillJsonPoint(marketPointNode);
                pointsArray.add(marketPointNode);
            }
            dayNode.set("data",pointsArray);
            daysArray.add(dayNode);
        }
        successNode.set("data", daysArray);
        output.add(successNode);
    }
}
