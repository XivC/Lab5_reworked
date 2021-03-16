package com.xivs.interpreter.commands;

import com.xivs.WorkersManager;
import com.xivs.builders.lineBuilders.LineWorkerBuilder;
import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.dataTransfer.Status;
import com.xivs.interpreter.Interpreter;
import com.xivs.io.InputManager;
import com.xivs.io.OutputManager;
import com.xivs.lab.Worker;

import java.util.HashMap;

public class Update extends InterpreterCommand{
    public Update(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        InputManager inputManager = this.interpreter.getInputManager();
        OutputManager outputManager = this.interpreter.getOutputManager();
        WorkersManager workersManager = this.interpreter.getWorkersManager();

        if(inputManager.getWords().size() < 4 || inputManager.getWords().get(1).isEmpty() ){ outputManager.println("Неверное количество аргументов"); return;}

        String id = inputManager.getWords().get(1);
        Response r = workersManager.search(new Request(new HashMap<>(){{
            put("id", id);
        }}, new HashMap<>()));
        if(r.status.equals(Status.ERROR)) {outputManager.println(r.messages.toString()); return;}
        HashMap<String, Worker> workers = (HashMap<String, Worker>) r.attachments.get("workers").getObject();
        if(workers.isEmpty()){ outputManager.println("Объект с данным id не найден"); return;}
        String key = "";
        for(String c: workers.keySet()) key = c;
        if(!this.interpreter.getWorkersManager().checkExistence(key)) {outputManager.println("Объект с данным ключом не существует"); return;}
        LineWorkerBuilder builder = new LineWorkerBuilder(inputManager, outputManager);
        Response build_resp = builder.build();
        if(build_resp.status.equals(com.xivs.dataTransfer.Status.ERROR)) return;

        String finalKey = key;
        Request create_request = new Request(new HashMap<>(){{
            put("key", finalKey);
        }}, build_resp.attachments);
        Response resp = this.interpreter.getWorkersManager().append(create_request);
        outputManager.println(resp.messages.toString());
    }
    @Override
    public String info(){
        return "Обновляет объект по его id. update <id> <name> <salary>";
    }
}
