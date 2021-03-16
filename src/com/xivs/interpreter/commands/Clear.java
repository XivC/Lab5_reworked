package com.xivs.interpreter.commands;

import com.xivs.WorkersManager;
import com.xivs.interpreter.Interpreter;

public class Clear extends InterpreterCommand{
    public Clear(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        WorkersManager workersManager = this.interpreter.getWorkersManager();
        workersManager.clear();
    }
}
