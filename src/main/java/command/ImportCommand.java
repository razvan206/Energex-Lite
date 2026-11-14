package command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CommandInput;
import repository.MemoryRepository;
import util.SimulationClock;

import java.time.LocalDateTime;

public class ImportCommand extends Command{
    public ImportCommand(CommandInput input){
        super(input);
    }

}
