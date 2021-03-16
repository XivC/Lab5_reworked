package com.xivs.dataTransfer;

import java.util.HashMap;
/**
 * Класс стандартного ответа.
 * body хранит строковые поля
 * attachments хранит любые dataTransference
 */
public class Request {

    public final HashMap<String, String> body;
    public final HashMap<String, DataTransference> attachments;

    public Request( HashMap<String, String> body, HashMap<String, DataTransference> attachments)  {

        this.body = body;
        this.attachments = attachments;
    }


}
