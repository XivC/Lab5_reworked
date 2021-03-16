package com.xivs.interpreter.commands;

import com.xivs.WorkersManager;
import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.dataTransfer.Status;
import com.xivs.interpreter.Interpreter;
import com.xivs.io.InputManager;
import com.xivs.io.OutputManager;

import java.util.HashMap;

public class Save extends InterpreterCommand{
    public Save(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        InputManager inputManager = this.interpreter.getInputManager();
        OutputManager outputManager = this.interpreter.getOutputManager();
        WorkersManager workersManager = this.interpreter.getWorkersManager();
        if(inputManager.getWords().size() < 2 ){ outputManager.println("Неверное количество аргументов");return;}
        String path = inputManager.getWords().get(1);
        Response r = workersManager.dump(new Request(new HashMap<>(){{put("path", path);}}, new HashMap<>()));
        outputManager.println(r.messages.toString());


    }
    @Override
    public String info(){
        return "сохранить коллекцию в файл. save <path>";
    }
}
