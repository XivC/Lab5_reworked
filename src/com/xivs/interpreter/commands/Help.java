package com.xivs.interpreter.commands;

import com.xivs.interpreter.Interpreter;
import com.xivs.io.OutputManager;

import java.util.HashMap;

public class Help extends InterpreterCommand{
    public Help(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        OutputManager outputManager = this.interpreter.getOutputManager();
        HashMap<String, InterpreterCommand> commands = this.interpreter.getExecutors();
        for(String c: commands.keySet()){
            outputManager.println(c + ": "+ commands.get(c).info());

        }
    }
    public String info(){
        return "вывести справку по доступным командам";
    }
}
