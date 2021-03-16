package com.xivs.builders.lineBuilders;

import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.dataTransfer.Status;
import com.xivs.interpreter.commands.Command;
import com.xivs.io.InputManager;
import com.xivs.io.OutputManager;
import com.xivs.lab.Address;
import com.xivs.lab.Organization;
import com.xivs.lab.OrganizationType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class LineOrganizationBuilder {
    Organization organization;
    InputManager inputManager;
    OutputManager outputManager;
    public LineOrganizationBuilder(InputManager inputManager, OutputManager outputManager) {

        this.inputManager = inputManager;
        this.outputManager = outputManager;
    }

    public Response build(){
        try{
        Request rq;
        Response resp;
        Organization created = (Organization) Organization.create(new Request(new HashMap<>(){{put("annualTurnover", "1");put("type", "NONE");}}, new HashMap<>(){{put("address", Address.DEFAULT);}})).attachments.get("organization").getObject();
        do{
            outputManager.print("Введите годовой оборот: ");
            inputManager.nextLine();
            rq = new Request(new HashMap<>(){{put("annualTurnover", inputManager.getString());}}, new HashMap<>());
            resp = created.update(rq);
            if(resp.status.equals(com.xivs.dataTransfer.Status.ERROR)) outputManager.println(resp.messages.toString());

        }
        while(created.update(rq).status.equals(com.xivs.dataTransfer.Status.ERROR));
        do{
            outputManager.println("Доступные типы: " + Arrays.asList(OrganizationType.values()));
            outputManager.print("\nВведите тип организации: ");
            inputManager.nextLine();
            if(inputManager.getString().isEmpty()) break;
            rq = new Request(new HashMap<>(){{put("type", inputManager.getString());}}, new HashMap<>());
            resp = created.update(rq);
            if(resp.status.equals(com.xivs.dataTransfer.Status.ERROR)) outputManager.println(resp.messages.toString());
        }
        while(resp.status.equals(com.xivs.dataTransfer.Status.ERROR));
        boolean flg = false;
        while(!flg){
            outputManager.print("Вводить адрес? (y/n) ");
            inputManager.nextLine();
            switch(inputManager.getString().toLowerCase(Locale.ROOT)){
                case "y":
                    LineAddressBuilder builder = new LineAddressBuilder(inputManager, outputManager);
                    Response build_resp = builder.build();
                    if(build_resp.status.equals(com.xivs.dataTransfer.Status.ERROR)) return Response.STANDART_ERROR();
                    Address a = (Address)build_resp.attachments.get("address").getObject();
                    created.update(new Request(new HashMap<>(), build_resp.attachments));
                    flg = true;
                    break;
                case "n": flg = true; break;
            }
        }
        this.organization = created;
        Address a = (Address)created.get().attachments.get("address");
        return new Response(new ArrayList<>(), Status.SUCCESS, new HashMap<>(), new HashMap<>(){{put("organization", created);}});
    }
    catch(IOException ex){
            return Response.STANDART_ERROR();
        }
    }
}
