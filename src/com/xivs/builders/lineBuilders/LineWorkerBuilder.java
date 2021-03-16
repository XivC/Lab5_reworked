package com.xivs.builders.lineBuilders;


import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.interpreter.commands.Command;
import com.xivs.io.InputManager;
import com.xivs.io.OutputManager;
import com.xivs.lab.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class LineWorkerBuilder{
    private Worker worker;
    InputManager inputManager;
    OutputManager outputManager;
    public LineWorkerBuilder (InputManager inputManager, OutputManager outputManager){

        this.inputManager = inputManager;
        this.outputManager = outputManager;
    }


    public Response build() {
        try {
            ArrayList<String> params = inputManager.getWords();
            String name = params.get(2);
            String salary = params.get(3);
            Worker created;

            Response resp = Worker.create(new Request(
                    new HashMap<String, String>() {{
                        put("name", name);
                        put("salary", salary);
                        put("creationDate", LocalDate.now().toString());
                        put("endDate", LocalDate.MAX.toString());
                        put("position", Position.NONE.toString());
                        put("status", Status.NONE.toString());

                    }},
                    new HashMap<String, com.xivs.dataTransfer.DataTransference>() {{
                        put("organization", Organization.DEFAULT);
                        put("coordinates", Coordinates.DEFAULT);
                    }}
            ));
            if (resp.status == com.xivs.dataTransfer.Status.ERROR) {
                outputManager.println(resp.messages.toString());
                return Response.STANDART_ERROR();
            }
            created = (Worker) resp.attachments.get("worker").getObject();
            resp = created.update(new Request(new HashMap<String, String>() {{
                put("endDate", "");
            }}, new HashMap<>()));
            Request rq;
            while (resp.status.equals(com.xivs.dataTransfer.Status.ERROR)) {
                outputManager.print("Дата окончания контракта: ");
                inputManager.nextLine();
                String endDate = inputManager.getString();
                if (endDate.isEmpty()) break;
                resp = created.update(new Request(new HashMap<String, String>() {{
                    put("endDate", endDate);
                }}, new HashMap<>()));
                if (resp.status.equals(com.xivs.dataTransfer.Status.ERROR))
                    outputManager.println(resp.messages.toString());
            }
            do {
                outputManager.print("Доступные позиции: " + Arrays.asList(Position.values()));
                outputManager.print("\nВведите позицию: ");
                inputManager.nextLine();
                rq = new Request(new HashMap<String, String>() {{
                    put("position", inputManager.getString());
                }}, new HashMap<>());
                resp = created.update(rq);
                if (resp.status.equals(com.xivs.dataTransfer.Status.ERROR))
                    outputManager.println(resp.messages.toString());
            }
            while (resp.status.equals(com.xivs.dataTransfer.Status.ERROR));
            do {
                outputManager.println("Доступные статусы: " + Arrays.asList(Status.values()));
                outputManager.print("\nВведите статус: ");
                inputManager.nextLine();
                if (inputManager.getString().isEmpty()) break;
                rq = new Request(new HashMap<String, String>() {{
                    put("status", inputManager.getString());
                }}, new HashMap<>());
                resp = created.update(rq);
                if (resp.status.equals(com.xivs.dataTransfer.Status.ERROR))
                    outputManager.println(resp.messages.toString());
            }
            while (resp.status.equals(com.xivs.dataTransfer.Status.ERROR));
            LineCoordinatesBuilder builderr = new LineCoordinatesBuilder(inputManager, outputManager);
            Response build_resp = builderr.build();
            if(build_resp.status.equals(com.xivs.dataTransfer.Status.ERROR)) return Response.STANDART_ERROR();
            created.update(new Request(new HashMap<>(), build_resp.attachments));


            boolean flg = false;
            while (!flg) {
                outputManager.print("Вводить организацию? (y/n)");
                inputManager.nextLine();
                switch (inputManager.getString().toLowerCase(Locale.ROOT)) {
                    case "y":
                        LineOrganizationBuilder builder = new LineOrganizationBuilder(inputManager, outputManager);
                        build_resp = builder.build();
                        if(build_resp.status.equals(com.xivs.dataTransfer.Status.ERROR)) return Response.STANDART_ERROR();
                        created.update(new Request(new HashMap<>(), build_resp.attachments));
                        flg = true;
                        break;
                    case "n":
                        flg = true;
                        break;
                }
            }


            this.worker = created;
            return new Response(new ArrayList<>(), com.xivs.dataTransfer.Status.SUCCESS, new HashMap<>(), new HashMap<String, com.xivs.dataTransfer.DataTransference>(){{put("worker", created);}});

        }
        catch(IOException ex){
            return Response.STANDART_ERROR();
        }
    }





    }


