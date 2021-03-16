package com.xivs.interpreter.commands;

import com.xivs.interpreter.Interpreter;

public abstract class InterpreterCommand extends Command{
    Interpreter interpreter;
    public InterpreterCommand(Interpreter interpreter){
        this.interpreter = interpreter;
    }
    public String info(){
        return "";
    };
}
