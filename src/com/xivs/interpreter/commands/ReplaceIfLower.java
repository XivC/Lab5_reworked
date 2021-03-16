package com.xivs.interpreter.commands;

import com.xivs.WorkersManager;
import com.xivs.builders.lineBuilders.LineWorkerBuilder;
import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.interpreter.Interpreter;
import com.xivs.io.InputManager;
import com.xivs.io.OutputManager;
import com.xivs.lab.Worker;

import java.util.HashMap;

public class ReplaceIfLower extends InterpreterCommand{
    public ReplaceIfLower(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        InputManager inputManager = this.interpreter.getInputManager();
        OutputManager outputManager = this.interpreter.getOutputManager();
        WorkersManager workersManager = this.interpreter.getWorkersManager();
        if(inputManager.getWords().size() < 4 || inputManager.getWords().get(1).isEmpty() ){ outputManager.println("Неверное количество аргументов"); return;}
        String key = inputManager.getWords().get(1);
        String salary = inputManager.getWords().get(3);
        if(!Worker.validate(new Request(new HashMap<String, String>(){{put("salary", salary);}}, new HashMap<String, com.xivs.dataTransfer.DataTransference>()))){ outputManager.println("Неверный формат данных"); return;}
        if(!this.interpreter.getWorkersManager().checkExistence(key)) {outputManager.println("Объект с данным ключом не существует"); return;}
        Worker ww = (Worker)workersManager.get(new Request(new HashMap<String, String>(){{put("key", key);}}, new HashMap<String, com.xivs.dataTransfer.DataTransference>())).attachments.get("worker").getObject();
        if(ww.compareTo(Float.parseFloat(salary)) <= 0) return;
        LineWorkerBuilder builder = new LineWorkerBuilder(inputManager, outputManager);
        Response build_resp = builder.build();
        if(build_resp.status.equals(com.xivs.dataTransfer.Status.ERROR)) return;


        Request create_request = new Request(new HashMap<String, String>(){{
            put("key", key);
        }}, build_resp.attachments);
        Response resp = this.interpreter.getWorkersManager().append(create_request);
        outputManager.println(resp.messages.toString());


    }
    @Override
    public String info(){
        return "Заменяет рабочего по ключу, если новое значение ЗП меньше старого";
    }
}
