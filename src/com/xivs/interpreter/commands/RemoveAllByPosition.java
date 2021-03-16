package com.xivs.interpreter.commands;

import com.xivs.WorkersManager;
import com.xivs.dataTransfer.Request;
import com.xivs.interpreter.Interpreter;
import com.xivs.io.InputManager;
import com.xivs.io.OutputManager;
import com.xivs.lab.Worker;

import java.util.HashMap;

public class RemoveAllByPosition extends InterpreterCommand{

    public RemoveAllByPosition(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        InputManager inputManager = this.interpreter.getInputManager();
        OutputManager outputManager = this.interpreter.getOutputManager();
        WorkersManager workersManager = this.interpreter.getWorkersManager();
        if(inputManager.getWords().size() < 2){ outputManager.println("Неверное количество аргументов"); return;}
        if(!Worker.validate(new Request(new HashMap<>(){{put("position", inputManager.getWords().get(1));}}, new HashMap<>()))){ outputManager.println("Неверный формат данных"); return;}
        String position = inputManager.getWords().get(1);
        HashMap<String, Worker> workers = (HashMap<String, Worker>) workersManager.search(new Request(new HashMap<>(), new HashMap<>())).attachments.get("workers").getObject();
        for(String c: workers.keySet()){
            if(workers.get(c).get().body.get("position").equals(position)){
                workersManager.remove(new Request(new HashMap<>(){{put("key", c);}}, new HashMap<>()));
            }
        }

    }
    @Override
    public String info(){
        return "Удаляет из коллекции рабочих, должность которых совпадает с заданной. remove_all_by_position <position>";
    }
}
