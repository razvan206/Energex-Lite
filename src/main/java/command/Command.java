package command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CommandInput;
import repository.MemoryRepository;
import util.SimulationClock;

import java.time.LocalDateTime;

public abstract class Command {
    protected final CommandInput input;

    protected Command(final CommandInput input) {
        this.input = input;
    }

    public final void execute(final MemoryRepository memoryRepository,
                             final SimulationClock simulationClock,
                             final ArrayNode output) {
        simulationClock.timeSimulation(
                LocalDateTime.parse(this.input.getTimestamp()),
                memoryRepository);
        this.executeCommand(memoryRepository, simulationClock, output);
    }

    public abstract void executeCommand(MemoryRepository memoryRepository,
                                        SimulationClock simulationClock,
                                        ArrayNode output);
}
