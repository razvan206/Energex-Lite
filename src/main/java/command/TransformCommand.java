package command;

import fileio.CommandInput;

public class TransformCommand {
    public static Command transformCommand(CommandInput input){
       return switch(input.getCommand()){
           case "startImport" -> new StartImportCommand(input);
           case "stopImport" -> new StopImportCommand(input);
           case "import" -> new ImportCommand(input);
           case "patch" -> new PatchCommand(input);
           case "get" -> new GetCommand(input);
       }
    }
}
