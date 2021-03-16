package com.xivs.builders.lineBuilders;

import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.dataTransfer.Status;
import com.xivs.interpreter.Interpreter;
import com.xivs.interpreter.commands.Command;
import com.xivs.interpreter.commands.InterpreterCommand;
import com.xivs.io.InputManager;
import com.xivs.io.OutputManager;
import com.xivs.lab.Address;
import com.xivs.lab.Coordinates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LineCoordinatesBuilder {
    Coordinates coordinates;
    InputManager inputManager;
    OutputManager outputManager;
    public LineCoordinatesBuilder (InputManager inputManager, OutputManager outputManager){

        this.inputManager = inputManager;
        this.outputManager = outputManager;
    }
    public Response build() {

        Coordinates created = (Coordinates) Coordinates.create(new Request(new HashMap<>() {{
            put("x", "100");
            put("y", "1");
        }}, new HashMap<>())).attachments.get("coordinates").getObject();
        Request rq;
        Response resp;
        try {
            outputManager.println("Ввод координат: ");
            do {

                outputManager.print("x: ");
                inputManager.nextLine();
                rq = new Request(new HashMap<>() {{
                    put("x", inputManager.getString());
                }}, new HashMap<>());
                resp = created.update(rq);
                if (resp.status.equals(com.xivs.dataTransfer.Status.ERROR))
                    outputManager.println(resp.messages.toString());

            }
            while (created.update(rq).status.equals(com.xivs.dataTransfer.Status.ERROR));
            do {
                outputManager.print("y: ");
                inputManager.nextLine();
                rq = new Request(new HashMap<>() {{
                    put("y", inputManager.getString());
                }}, new HashMap<>());
                resp = created.update(rq);
                if (resp.status.equals(com.xivs.dataTransfer.Status.ERROR))
                    outputManager.println(resp.messages.toString());

            }
            while (created.update(rq).status.equals(com.xivs.dataTransfer.Status.ERROR));
            this.coordinates = created;
            return new Response(new ArrayList<>(), Status.SUCCESS, new HashMap<>(), new HashMap<>(){{put("coordinates", created);}});
        }
        catch(IOException ex) {return Response.STANDART_ERROR();}
    }



}
