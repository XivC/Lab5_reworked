package com.xivs.interpreter.commands;

import com.xivs.WorkersManager;
import com.xivs.dataTransfer.Request;
import com.xivs.interpreter.Interpreter;
import com.xivs.io.InputManager;
import com.xivs.io.OutputManager;
import com.xivs.lab.Worker;

import java.util.HashMap;

public class RemoveGreater extends InterpreterCommand{
    public RemoveGreater(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        InputManager inputManager = this.interpreter.getInputManager();
        OutputManager outputManager = this.interpreter.getOutputManager();
        WorkersManager workersManager = this.interpreter.getWorkersManager();
        if(inputManager.getWords().size() < 2){ outputManager.println("Неверное количество аргументов"); return;}
        if(!Worker.validate(new Request(new HashMap<String, String>(){{put("salary", inputManager.getWords().get(1));}}, new HashMap<String, com.xivs.dataTransfer.DataTransference>()))){ outputManager.println("Неверный формат данных"); return;}
        float salary = Float.parseFloat(inputManager.getWords().get(1));
        HashMap<String, Worker> workers = (HashMap<String, Worker>) workersManager.search(new Request(new HashMap<String, String>(), new HashMap<String, com.xivs.dataTransfer.DataTransference>())).attachments.get("workers").getObject();
        for(String c: workers.keySet()){
            if(workers.get(c).compareTo(salary) >= 0){
                workersManager.remove(new Request(new HashMap<String, String>(){{put("key", c);}}, new HashMap<String, com.xivs.dataTransfer.DataTransference>()));
            }
        }

    }
    @Override
    public String info(){
        return "Удаляет всех рабочих, ЗП которых превышает заданную. remove_greater <salary>";
    }
}
