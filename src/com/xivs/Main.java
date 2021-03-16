package com.xivs;

import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.interpreter.Interpreter;
import com.xivs.io.CommandLineInputManager;
import com.xivs.io.CommandLineOutManager;
import com.xivs.io.FileInputManager;
import com.xivs.lab.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.HashMap;

class test{
    public void tst(String s){
        System.out.println(s);
    }
}
public class Main {

    public static void main(String[] args) {
        WorkersManager manager = new WorkersManager();
        if(args.length == 1){
            Response r = manager.load(new Request(new HashMap<>(){{put("path", args[0]);}}, new HashMap<>()));
            System.out.println(r.messages);
        }
        Interpreter inter = new Interpreter(manager, new CommandLineInputManager(), new CommandLineOutManager(), Interpreter.DEFAULT_COMMANDS);
        inter.run();

    }

}
