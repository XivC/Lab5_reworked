package com.xivs.interpreter.commands;

import com.xivs.WorkersManager;
import com.xivs.dataTransfer.Request;
import com.xivs.interpreter.Interpreter;
import com.xivs.io.OutputManager;
import com.xivs.lab.Worker;

import java.util.HashMap;

public class MaxBySalary extends InterpreterCommand {
    public MaxBySalary(Interpreter interpreter){
        super(interpreter);
    }
    public void execute(){
        OutputManager outputManager = this.interpreter.getOutputManager();
        WorkersManager workersManager = this.interpreter.getWorkersManager();
        HashMap<String, Worker> workers = (HashMap<String, Worker>) workersManager.search(new Request(new HashMap<>(), new HashMap<>())).attachments.get("workers").getObject();
        String max = "";
        float max_salary = 0;
        for(String c: workers.keySet()){
            Worker w = workers.get(c);
            if(w.compareTo(max_salary) > 0){
                max = c;
                max_salary = Float.parseFloat(w.get().body.get("salary"));
            }


        }
        outputManager.println(max + " - " + Float.toString(max_salary));
    }
    @Override
    public String info(){
        return "Показывает рабочего с максимальной ЗП в формате ключ - максимум";
    }

}
