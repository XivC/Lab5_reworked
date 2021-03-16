package com.xivs.lab;

import com.xivs.dataTransfer.*;
import com.xivs.dataTransfer.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class Address extends DataTransference<Address> {
    private String street; //Поле может быть null
    private String zipCode; //Длина строки должна быть не меньше 9, Поле не может быть null
    public static final Address DEFAULT = new Address("", "000000000");
    private Address(String street, String zipCode){
        this.street = street;
        this.zipCode = zipCode;
    }

    /**
     * Возвращает ссылку на объект
     * @return
     */
    public Address getObject(){
        return this;
    }

    /**
     * Обновить значение по ключу.
     * @param rq
     * @return
     */
    public Response update(Request rq){
        if (!validate(rq)){return Response.STANDART_ERROR();}
        for(String c: rq.body.keySet()){
            switch (c){
                case "street": this.street = rq.body.get("street");break;
                case "zipCode": this.zipCode = rq.body.get("zipCode");break;
            }
        }
        return this.get();
    }

    /**
     * Возвращает собственные поля в строковм представлении. Комплексные поля передаются в attachments
     * @return
     */
    public Response get(){
        HashMap<String, String> resp_body = new HashMap<>();
        resp_body.put("street", this.street);
        resp_body.put("zipCode", this.zipCode);
        HashMap<String, DataTransference> attach = new HashMap<>();


        return new Response(new ArrayList<>(), Status.SUCCESS, resp_body, attach);

    }

    /**
     * Создаёт объект по запросу. Ссылка на созданный объект передаёстя в attachments
     * @param rq
     * @return
     */
    public static Response create(Request rq){
        if (!validate(rq)){return Response.STANDART_ERROR();}
        String street = rq.body.get("street");
        String zipCode = rq.body.get("zipCode");

        HashMap<String, DataTransference> attach = new HashMap<>();
        attach.put("address", new Address(street, zipCode));
        return new Response(new ArrayList<String>(), Status.SUCCESS, new HashMap<String,String>(), attach);


        }

    /**
     * Проверить валидность запроса
     * @param rq
     * @return
     */
    public static boolean validate(Request rq){

        for(String c: rq.body.keySet()){
            switch (c){
                case "zipCode":
                    if(rq.body.get(c).length() < 9 || rq.body.get(c).equals("")) return false;
            }
        }
        return true;
    }

    }
