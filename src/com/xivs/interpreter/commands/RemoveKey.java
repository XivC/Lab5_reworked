package com.xivs.interpreter.commands;

import com.xivs.WorkersManager;
import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.dataTransfer.Status;
import com.xivs.interpreter.Interpreter;
import com.xivs.io.InputManager;
import com.xivs.io.OutputManager;

import java.util.HashMap;

public class RemoveKey extends InterpreterCommand {
    public RemoveKey (Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        InputManager inputManager = this.interpreter.getInputManager();
        OutputManager outputManager = this.interpreter.getOutputManager();
        WorkersManager workersManager = this.interpreter.getWorkersManager();
        if(inputManager.getWords().size() < 2 || inputManager.getWords().get(1).isEmpty() ){ outputManager.println("Неверное количество аргументов");return;}
        String key = inputManager.getWords().get(1);
        Response r = workersManager.remove(new Request(new HashMap<>(){{put("key", key);}}, new HashMap<>()));
        if(r.status.equals(Status.ERROR)) {outputManager.println(r.messages.toString()); return;}
        outputManager.println("Объект успешно удалён");


    }
    @Override
    public String info(){
        return "Удаляя объект по ключу. remove <key>";
    }

}
