package command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CommandInput;
import repository.MemoryRepository;
import util.SimulationClock;

public final class StartImportCommand extends Command {
    public StartImportCommand(final CommandInput input) {
        super(input);
    }

    @Override
    public void executeCommand(final MemoryRepository memoryRepository,
                               final SimulationClock simulationClock,
                               final ArrayNode output) {
        simulationClock.startImport(this.input.getTimestamp());
    }
}
