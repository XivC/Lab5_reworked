package com.xivs.interpreter.commands;

import com.xivs.WorkersManager;
import com.xivs.interpreter.Interpreter;
import com.xivs.io.OutputManager;

import java.util.HashMap;

public class Info extends InterpreterCommand{
    public Info(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        OutputManager outputManager = this.interpreter.getOutputManager();
        WorkersManager workersManager = this.interpreter.getWorkersManager();
        HashMap<String, String> info = workersManager.info().body;
        for(String c: info.keySet()){
            outputManager.println(c + ": " + info.get(c));
        }

    }
    @Override
    public String info(){
        return "Вывести информацию о коллекции";
    }
}
