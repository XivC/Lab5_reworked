package com.xivs.lab;

import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.dataTransfer.DataTransference;

import java.util.ArrayList;
import java.util.HashMap;
import com.xivs.dataTransfer.Status;
public class Coordinates extends DataTransference<Coordinates> {
    private Long x; //Поле не может быть null
    private Double y; //Максимальное значение поля: 139, Поле не может быть null
    public static Coordinates DEFAULT = new Coordinates(0L, 0D);
    private Coordinates(Long x, Double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Проверить валидность запроса
     * @param rq
     * @return
     */
    public static boolean validate(Request rq){
        try{
            for(String c: rq.body.keySet()) {
                switch (c) {
                    case "x": Long xx = Long.valueOf(rq.body.get("x"));break;
                    case "y": Double yy = Double.valueOf(rq.body.get("y"));
                              if (yy > 139) return false;
                              break;
                }
            }

        }
        catch (NumberFormatException ex) {return false;}
        return true;
    }

    /**
     * Возвращает ссылку на себя
     * @return
     */
    public Coordinates getObject(){return this;}

    /**
     * Создать объект по запросу. Ссылка на созданный объект передаётся в attachments
     * @param rq
     * @return
     */
    public static Response create(Request rq){
        if (!validate(rq)) return Response.STANDART_ERROR();
        Long x = Long.valueOf(rq.body.get("x"));
        Double y = Double.valueOf(rq.body.get("y"));
        return new Response(new ArrayList<>(), Status.SUCCESS, new HashMap<>(), new HashMap<String, DataTransference>(){{put("coordinates",new Coordinates(x, y));}});
    }

    /**
     * Возвращает собственные поля в строковм представлении. Комплексные поля передаются в attachments
     * @return
     */
    public Response get(){
        String x = this.x.toString();
        String y = this.y.toString();
        return new Response(
                new ArrayList<>(),
                Status.SUCCESS,
                new HashMap<String, String>(){{
                    put("x", x);
                    put("y", y);
                }},
                new HashMap<String, DataTransference>()

        );
    }

    /**
     * Обновляет значение поля по ключу.
     * @param rq
     * @return
     */
    public Response update(Request rq){
        if (!validate(rq)){return Response.STANDART_ERROR();}
        for(String c: rq.body.keySet()){
            switch (c){
                case "x": this.x = Long.valueOf(rq.body.get("x"));break;
                case "y": this.y = Double.valueOf(rq.body.get("y"));break;
            }
        }
        return this.get();
    }
    }

