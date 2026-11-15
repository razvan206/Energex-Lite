package command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CommandInput;
import repository.MemoryRepository;
import util.SimulationClock;

public class StartImportCommand extends Command{
        public StartImportCommand(CommandInput input){
            super(input);
        }

    @Override
    public void executeCommand(MemoryRepository memoryRepository, SimulationClock simulationClock, ArrayNode output) {
        simulationClock.startImport(this.input.getTimestamp());
    }
}
