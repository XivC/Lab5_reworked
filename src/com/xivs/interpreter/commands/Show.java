package com.xivs.interpreter.commands;

import com.xivs.WorkersManager;
import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.interpreter.Interpreter;
import com.xivs.io.InputManager;
import com.xivs.io.OutputManager;
import com.xivs.lab.Address;
import com.xivs.lab.Coordinates;
import com.xivs.lab.Organization;
import com.xivs.lab.Worker;

import java.util.HashMap;

public class Show extends InterpreterCommand{
    public Show(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        InputManager inputManager = this.interpreter.getInputManager();
        OutputManager outputManager = this.interpreter.getOutputManager();
        WorkersManager workersManager = this.interpreter.getWorkersManager();
        HashMap<String, Worker> workers = (HashMap<String, Worker>) workersManager.search(new Request(new HashMap<>(), new HashMap<>())).attachments.get("workers").getObject();
        for(String c: workers.keySet()){
            outputManager.println("------------" + c + "------------");
            Response response = workers.get(c).get();
            outputManager.println(response.body.toString());
            Organization o = (Organization)response.attachments.get("organization").getObject();
            Coordinates co = (Coordinates) response.attachments.get("coordinates").getObject();
            Address a = (Address)o.get().attachments.get("address").getObject();
            outputManager.println("Организация: " + o.get().body.toString());
            outputManager.println("Адрес организации: " + a.get().body.toString());
            outputManager.println("Координаты: " + co.get().body.toString());
        }
    }
    @Override
    public String info() {
        return "показывает список элементов коллекции";
    }
}
