package com.xivs.interpreter;


import com.xivs.WorkersManager;
import com.xivs.interpreter.commands.InterpreterCommand;
import com.xivs.interpreter.commands.*;
import com.xivs.io.InputManager;
import com.xivs.io.OutputManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Interpreter {
    private LinkedList<String> history;
    private HashMap<String, Class> commands;
    private HashMap<String, InterpreterCommand> executors;
    private InputManager inputManager;
    private OutputManager outputManager;
    private WorkersManager workersManager;
    private boolean endFlag;
    public static final HashMap<String, Class> DEFAULT_COMMANDS = new HashMap<String, Class>(){{
        put("help", Help.class);
        put("info", Info.class);
        put("show", Show.class);
        put("insert", Insert.class);
        put("update", Update.class);
        put("remove_key", RemoveKey.class);
        put("clear", Clear.class);
        put("save", Save.class);
        put("execute_script", ExecuteScript.class);
        put("exit", Exit.class);
        put("remove_greater", RemoveGreater.class);
        put("history", History.class);
        put("replace_if_lower", ReplaceIfLower.class);
        put("remove_all_by_position", RemoveAllByPosition.class);
        put("max_by_salary", MaxBySalary.class);
        put("print_field_ascending_position", PrintFieldAscendingPosition.class);


    }};


    public Interpreter(WorkersManager manager, InputManager inputManager, OutputManager outputManager, HashMap<String, Class> commands){
        this.workersManager = manager;
        this.commands = commands;
        this.inputManager = inputManager;
        this.outputManager = outputManager;
        this.endFlag = false;
        this.history = new LinkedList<String>();
        for(int i = 0; i < 6; i++) this.history.add("");
        this.build();
    }

    /**
     * запуск интерпретатора.
     */
    public void run(){
        try{
        while(!endFlag && this.inputManager.hasNext()){
            this.outputManager.print(">");
            this.inputManager.nextLine();
            if(this.inputManager.getString().isEmpty()) continue;
            String cmd = this.inputManager.getWords().get(0);
            history.removeLast();
            history.addFirst(cmd);
            if(this.executors.get(cmd) == null) {this.outputManager.println("Неизвестная команда"); continue;}
            this.executors.get(cmd).execute();
        }

        }
        catch(IOException ex){
            this.endFlag = true;
        }
    }

    /**
     * Удалить коммадну из интерпретатора
     * @param key
     */
    public void removeCommand(String key){
        this.commands.remove(key);
        this.build();
    }

    /**
     * добавить команду в интерпретатор
     * @param key
     * @param clazz
     */
    public void addCommand(String key, Class<InterpreterCommand> clazz){
        this.commands.put(key, clazz);
        this.build();
    }

    /**
     * Перессборка интрепретатора. Пересоздаются экземпляры команд.
     */
    public void build(){
        HashMap<String, InterpreterCommand> executors = new HashMap<String, InterpreterCommand>();
        for(String c: this.commands.keySet()){
            try {
                
                InterpreterCommand command = (InterpreterCommand) this.commands.get(c).getDeclaredConstructors()[0].newInstance(this);
                executors.put(c, command);
            }
            catch(ReflectiveOperationException ex){ex.printStackTrace(); continue; }
        }
        this.executors = executors;
    }

    /**
     * Прерывание работы интерпретатора.
     */
    public void exit(){
        this.endFlag = true;
    }
    public HashMap<String, InterpreterCommand> getExecutors(){
        return this.executors;
    }
    public InputManager getInputManager() {return this.inputManager;}
    public OutputManager getOutputManager() {return this.outputManager;}
    public WorkersManager getWorkersManager() {return this.workersManager;}
    public LinkedList<String> getHistory() {return this.history;}


}
