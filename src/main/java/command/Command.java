package command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import repository.MemoryRepository;
import util.SimulationClock;

import java.io.File;
import java.time.LocalDateTime;

public abstract class Command {
    protected final CommandInput input;
    protected Command(CommandInput input){
        this.input = input;
    }
    public final void time(MemoryRepository memoryRepository, SimulationClock simulationClock){
        simulationClock.timeSimulation(LocalDateTime.parse(this.input.getTimestamp()), memoryRepository);
    }
    public abstract void execute(MemoryRepository memoryRepository, SimulationClock simulationClock, ArrayNode output, ObjectMapper mapper);
}
