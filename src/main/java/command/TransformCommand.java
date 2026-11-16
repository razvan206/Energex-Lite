package command;

import fileio.CommandInput;

public final class TransformCommand {
    public TransformCommand() {
    }

    /**
     * We use this method to convert the brute information into ...
     * our own Command abstract class that is extended ...
     * by other ones (start,import,get,etc)
     * @param input
     * @return
     */
    public static Command transformCommand(final CommandInput input) {
        return switch (input.getCommand()) {
            case "startImport" -> new StartImportCommand(input);
            case "stopImport" -> new StopImportCommand(input);
            case "import" -> new ImportCommand(input);
            case "patch" -> new PatchCommand(input);
            case "get" -> new GetCommand(input);
            default -> throw new IllegalArgumentException(
                    "Comandă necunoscută: " + input.getCommand());
        };
    }
}
