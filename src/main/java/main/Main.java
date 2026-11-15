package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import command.Command;
import fileio.CommandInput;
import fileio.InputLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import repository.MemoryRepository;
import util.SimulationClock;

import static command.TransformCommand.transformCommand;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {

    private Main() {
    }

    public static final ObjectMapper MAPPER = new ObjectMapper();
    public static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    /**
     * @param inputPath input file path
     * @param outputPath output file path
     * @throws IOException when files cannot be loaded.
     */
    public static void action(final String inputPath,
                              final String outputPath) throws IOException {

        InputLoader inputLoader = new InputLoader(inputPath);
        ArrayNode output = MAPPER.createArrayNode();

        /*
         * TODO Implement your function here
         *
         * How to add output to the output array?
         * There are multiple ways to do this, here is one example:
         *
         *
         * ObjectNode objectNode = MAPPER.createObjectNode();
         * objectNode.put("field_name", "field_value");
         *
         * ArrayNode arrayNode = MAPPER.createArrayNode();
         * arrayNode.add(objectNode);
         *
         * output.add(arrayNode);
         * output.add(objectNode);
         *
         */
        MemoryRepository repository = new MemoryRepository();
        SimulationClock clock = new SimulationClock();
        List<Command> commands = new ArrayList<>();

        for (CommandInput cmd : inputLoader.getCommands()) {
            commands.add(transformCommand(cmd));
        }

        for (Command cmd : commands) {
            cmd.execute(repository, clock, output);
        }
        // we put the MAPPER parameter for efficiency to reduce
        // the MAPPER object that we would've create in
        // every class to put the json output

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }
}
