package com.xivs.interpreter.commands;

import com.xivs.WorkersManager;
import com.xivs.interpreter.Interpreter;
import com.xivs.io.FileInputManager;
import com.xivs.io.InputManager;
import com.xivs.io.OutputManager;
import com.xivs.io.VoidOutputManager;

import java.io.File;
import java.io.IOException;

public class ExecuteScript extends InterpreterCommand{
    public ExecuteScript(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        InputManager inputManager = this.interpreter.getInputManager();
        OutputManager outputManager = this.interpreter.getOutputManager();
        WorkersManager workersManager = this.interpreter.getWorkersManager();
        if(inputManager.getWords().size() < 2 ){ outputManager.println("Неверное количество аргументов");return;}
        String path = inputManager.getWords().get(1);
        try {
            Interpreter exe_inter = new Interpreter(workersManager, new FileInputManager(new File(path)), new VoidOutputManager(), Interpreter.DEFAULT_COMMANDS);
            exe_inter.run();
        }
        catch(IOException ex){
            outputManager.println("Скрипт или файл сломался :(");
        }
    }
    public String info(){
        return "исполнить скрипт. script_execute <path>";
    }
}
