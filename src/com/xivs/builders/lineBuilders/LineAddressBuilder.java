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
import com.xivs.lab.OrganizationType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LineAddressBuilder {
    Address address;
    InputManager inputManager;
    OutputManager outputManager;

    public LineAddressBuilder(InputManager inputManager, OutputManager outputManager) {

        this.inputManager = inputManager;
        this.outputManager = outputManager;
    }

    public Response build() {
        try {
            Address created = (Address) Address.create(new Request(new HashMap<String, String>() {{
                put("zipCode", "0000000000");
                put("street", "no street");
            }}, new HashMap<>())).attachments.get("address").getObject();
            Request rq;
            Response resp;
            do {

                outputManager.print("Введите почтовый индекс: ");
                inputManager.nextLine();
                rq = new Request(new HashMap<String, String>() {{
                    put("zipCode", inputManager.getString());
                }}, new HashMap<>());
                resp = created.update(rq);
                if (resp.status.equals(com.xivs.dataTransfer.Status.ERROR))
                    outputManager.println(resp.messages.toString());
            }
            while (created.update(rq).status.equals(com.xivs.dataTransfer.Status.ERROR));
            do {

                outputManager.print("Введите улицу: ");
                inputManager.nextLine();
                if (inputManager.getString().isEmpty()) break;
                rq = new Request(new HashMap<String, String>() {{
                    put("street", inputManager.getString());
                }}, new HashMap<>());
                resp = created.update(rq);
                if (resp.status.equals(com.xivs.dataTransfer.Status.ERROR))
                    outputManager.println(resp.messages.toString());

            }
            while (created.update(rq).status.equals(com.xivs.dataTransfer.Status.ERROR));
            this.address = created;

            return new Response(new ArrayList<>(), Status.SUCCESS, new HashMap<>(), new HashMap<String, com.xivs.dataTransfer.DataTransference>() {{
                put("address", created);
            }});
        } catch (IOException ex) {
            return Response.STANDART_ERROR();
        }


    }
}
