package com.xivs.interpreter.commands;

import com.xivs.builders.lineBuilders.LineWorkerBuilder;
import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.interpreter.Interpreter;
import com.xivs.io.InputManager;
import com.xivs.io.OutputManager;
import com.xivs.lab.Worker;

import java.util.HashMap;

public class Insert extends InterpreterCommand{
    public Insert(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        InputManager inputManager = this.interpreter.getInputManager();
        OutputManager outputManager = this.interpreter.getOutputManager();
        if(inputManager.getWords().size() < 4 || inputManager.getWords().get(1).isEmpty() ){ outputManager.println("Неверное количество аргументов"); return;}
        String key = inputManager.getWords().get(1);
        if(this.interpreter.getWorkersManager().checkExistence(key)) {outputManager.println("Объект с данным ключом уже существует"); return;}
        LineWorkerBuilder builder = new LineWorkerBuilder(this.interpreter.getInputManager(), this.interpreter.getOutputManager());
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
        return "создать новый элемент коллекции с заданным ключом. insert <key> <name> <salary>";
    }
}
