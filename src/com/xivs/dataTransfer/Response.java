package com.xivs.dataTransfer;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Класс стандартного ответа.
 * Сообщения передаются в messages
 * Статус запроса присваивается SUCCESS или ERROR
 * body хранит строковые поля
 * attachments хранит любые dataTransference
 */
public class Response{
    public final ArrayList<String> messages;
    public final Status status;
    public final HashMap<String, String> body;
    public final HashMap<String, DataTransference> attachments;

    public Response (ArrayList<String> messages, Status status, HashMap<String,String> body, HashMap<String, DataTransference> attachments) {
        this.messages = messages;
        this.status = status;
        this.body = body;
        this.attachments = attachments;
    }
    public static Response STANDART_ERROR(){
        return new Response(
                new ArrayList<String>(){{add("Неверный формат данных");}},
                Status.ERROR,
                new HashMap<>(),
                new HashMap<>()
        );
    }

}