package com.xivs.lab;

import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.dataTransfer.DataTransference;
import com.xivs.dataTransfer.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class Organization extends DataTransference<Organization> {
    private Integer annualTurnover; //Поле не может быть null, Значение поля должно быть больше 0
    private OrganizationType type; //Поле может быть null
    private Address officialAddress; //Поле может быть null
    public static final Organization DEFAULT = new Organization(1, OrganizationType.NONE, Address.DEFAULT);
    private Organization(Integer annualTurnover, OrganizationType type, Address officialAddress){
        this.annualTurnover = annualTurnover;
        this.type = type;
        this.officialAddress = officialAddress;
    }

    /**
     * Обновить значение по ключу.
     * @param rq
     * @return
     */
    public Response update(Request rq){
        if(!validate(rq)) return Response.STANDART_ERROR();
        for(String c: rq.body.keySet()){
            switch(c){
                case "annualTurnover": this.annualTurnover = Integer.valueOf(rq.body.get("annualTurnover"));break;
                case "type": this.type = Enum.valueOf(OrganizationType.class, rq.body.get("type"));
            }
        }
        for(String c: rq.attachments.keySet()){
            switch(c){
                case "address": this.officialAddress = (Address)rq.attachments.get(c).getObject();
            }
        }
        return this.get();
    }

    /**
     * Возвращает собственные поля в строковм представлении. Комплексные поля передаются в attachments
     * @return
     */
    public Response get(){
        String annualTurnover = this.annualTurnover.toString();
        String type = this.type.toString();
        Address address = this.officialAddress;
        return new Response(
                new ArrayList<>(),
                Status.SUCCESS,
                new HashMap<>(){{put("annualTurnover", annualTurnover); put("type", type);}},
                new HashMap<>(){{put("address", address);}}
        );
    }

    /**
     * Вернуть ссылку на себя
     * @return
     */
    public Organization getObject(){return this;}

    /**
     * Создаёт объект по запросу.
     *Собранный объект передаётся в attachments;
     * @param rq
     * @return
     */
    public static Response create(Request rq){
        if(!validate(rq)) return Response.STANDART_ERROR();
        Integer annualTurnover = Integer.valueOf(rq.body.get("annualTurnover"));
        OrganizationType type = Enum.valueOf(OrganizationType.class, rq.body.get("type"));
        Address officialAddress = (Address) rq.attachments.get("address").getObject();
        HashMap<String, DataTransference> attach = new HashMap<>();
        attach.put("organization", new Organization(annualTurnover, type, officialAddress));
        return new Response(new ArrayList<>(), Status.SUCCESS, new HashMap<>(), attach);

    }

    /**
     * Валидация запроса
     * @param rq
     * @return
     */
    public static boolean validate(Request rq){
        try{
            for(String c: rq.body.keySet()) {
                switch(c) {
                    case "annualTurnover": if(Integer.parseInt(rq.body.get("annualTurnover")) < 0) return false; break;
                    case "type": Enum.valueOf(OrganizationType.class, rq.body.get("type"));break;

                }
            }
        }
        catch(IllegalArgumentException ex){
            return false;
        }
        return true;

    }




}
