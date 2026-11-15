package command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.DataType;
import domain.MarketPoint;
import domain.MarketPointCreate;
import fileio.CommandInput;
import fileio.MarketDataInput;
import main.Main;
import repository.MemoryRepository;
import util.SimulationClock;
import util.TimeConversionUtils;

public final class PatchCommand extends Command {
    public PatchCommand(final CommandInput input) {
        super(input);
    }

    @Override
    public void executeCommand(final MemoryRepository memoryRepository,
                               final SimulationClock simulationClock,
                               final ArrayNode output) {
        if (this.input.getParameters() == null
                || this.input.getParameters().getData() == null) {
            return;
        }
        for (MarketDataInput dataInput : this.input.getParameters().getData()) {
            DataType type = DataType.valueOf(dataInput.id());
            Double prevQuantity = null;
            if (type == DataType.GRID_LOAD) {
                int currentIndex = TimeConversionUtils.intervalToIndex(
                        dataInput.interval());
                prevQuantity = memoryRepository.getPreviousGridLoad(
                        dataInput.date(), currentIndex);
            }
            MarketPoint point = MarketPointCreate.createPoint(type, dataInput);
            String validation = point.validate(prevQuantity);
            if (validation != null) {
                ObjectNode node = Main.MAPPER.createObjectNode();
                node.put("command", "patch");
                node.put("timestamp", this.input.getTimestamp());
                node.put("status", "WARNING");
                node.put("id", dataInput.id());
                node.put("date", dataInput.date());
                node.put("interval", TimeConversionUtils.indexToInterval(
                        point.getIntervalIndex()));
                node.put("intervalIndex", point.getIntervalIndex());
                node.put("message", validation);
                output.add(node);
            } else {
                memoryRepository.savePoint(type, dataInput.date(), point);
            }
        }
    }
}
