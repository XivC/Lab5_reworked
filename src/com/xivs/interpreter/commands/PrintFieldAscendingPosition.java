package com.xivs.interpreter.commands;

import com.xivs.WorkersManager;
import com.xivs.dataTransfer.Request;
import com.xivs.interpreter.Interpreter;
import com.xivs.io.InputManager;
import com.xivs.io.OutputManager;
import com.xivs.lab.Position;
import com.xivs.lab.Worker;

import java.util.HashMap;

public class PrintFieldAscendingPosition extends InterpreterCommand{
    public PrintFieldAscendingPosition(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){

        OutputManager outputManager = this.interpreter.getOutputManager();
        WorkersManager workersManager = this.interpreter.getWorkersManager();
        HashMap<String, Worker> workers;
        Position[] range = {Position.CLEANER, Position.LABORER, Position.BAKER, Position.HEAD_OF_DEPARTMENT};
        for(Position p: range){
            workers = (HashMap<String, Worker>) workersManager.search(new Request(new HashMap<String, String>(){{put("position", p.toString());}}, new HashMap<String, com.xivs.dataTransfer.DataTransference>())).attachments.get("workers").getObject();
            for(String c: workers.keySet()){
                outputManager.println(c + " - " + p.toString());
            }

        }
    }
    @Override
    public String info(){
        return "вывести список рабочих, отсортированный по должности в формате key - position";
    }
}
