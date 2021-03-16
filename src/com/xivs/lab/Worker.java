package com.xivs.lab;

import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.dataTransfer.DataTransference;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Worker extends DataTransference<Worker> {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private float salary; //Значение поля должно быть больше 0
    private java.time.LocalDate endDate; //Поле может быть null
    private Position position; //Поле не может быть null
    private Status status; //Поле может быть null
    private Organization organization; //Поле может быть null
    public Worker(String name, float salary, java.time.LocalDate creationDate, java.time.LocalDate endDate, Status status, Position position, Organization organization, Coordinates coordinates){
        this.name = name;
        this.salary = salary;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.organization = organization;
        this.coordinates = coordinates;
        this.status = status;
        this.position = position;
        this.id = 0L;

    }

    /**
     * Возвращает ссылку на себя.
     * @return
     */
    public Worker getObject(){return this;}

    /**
     * Валидация запроса
     * @param rq
     * @return
     */
    public static boolean validate(Request rq) {
        try {
            for (String c : rq.body.keySet()) {
                switch (c) {
                    case "id":  Long id = Long.valueOf(rq.body.get("id")); if(id <= 0L) return false; break;
                    case "name": if(rq.body.get("name").isEmpty()) return false; break;
                    case "salary": float salary = Float.valueOf(rq.body.get("salary")); if(salary <= 0) return false; break;
                    case "creationDate": LocalDate.parse(rq.body.get("creationDate")); break;
                    case "endDate": LocalDate.parse(rq.body.get("endDate"));break;
                    case "position": Enum.valueOf(Position.class, rq.body.get("position")); break;
                    case "status": Enum.valueOf(Status.class, rq.body.get("status")); break;


                }
            }
        }
        catch (IllegalArgumentException | DateTimeException ex){
            return false;
        }
        return true;
    }

    /**
     * Создаёт объект по запросу. Собранный объект хранится в attachments.
     * @param rq
     * @return
     */
    public static Response create(Request rq){
        if(!validate(rq)) return  Response.STANDART_ERROR();
        String name = rq.body.get("name");
        LocalDate creationDate = LocalDate.parse(rq.body.get("creationDate"));
        float salary = Float.parseFloat(rq.body.get("salary"));
        LocalDate endDate = LocalDate.parse(rq.body.get("endDate"));
        Status status = Enum.valueOf(Status.class, rq.body.get("status"));
        Position position = Enum.valueOf(Position.class, rq.body.get("position"));
        Organization organization = (Organization)rq.attachments.get("organization").getObject();
        Coordinates coordinates = (Coordinates)rq.attachments.get("coordinates").getObject();
        Worker w = new Worker(name, salary, creationDate, endDate, status, position, organization, coordinates);
        if(rq.body.get("id") != null) w.update(new Request(new HashMap<String, String>(){{put("id", rq.body.get("id") );}}, new HashMap<String, DataTransference>()));
        return new Response(
                new ArrayList<String>(),
                com.xivs.dataTransfer.Status.SUCCESS,
                new HashMap<String, String>(),
                new HashMap<String, DataTransference>(){{
                    put("worker", w);
                }}

        );
    }

    /**
     * Возвращает собственные поля в строковм представлении. Комплексные поля передаются в attachments
     * @return
     */
    public Response get(){
        Worker w = this;
        return new Response(
                new ArrayList<String>(),
                com.xivs.dataTransfer.Status.SUCCESS,
                new HashMap<String, String>(){{
                    put("id", String.valueOf(w.id));
                    put("name", String.valueOf(w.name));
                    put("salary", String.valueOf(w.salary));
                    put("creationDate", String.valueOf(w.creationDate));
                    put("endDate", String.valueOf(w.endDate));
                    put("position", String.valueOf(w.position));
                    put("status", String.valueOf(w.status));

                }},
               new HashMap<String, DataTransference>(){{
                   put("organization", w.organization);
                   put("coordinates", w.coordinates);
               }}
        );
    }

    /**
     * Обновить значение поля по запросу
     * @param rq
     * @return
     */
    public Response update(Request rq){
        if(!validate(rq)) return Response.STANDART_ERROR();
        for (String c: rq.body.keySet()){
            switch (c){
                case "id":  this.id = Long.valueOf(rq.body.get("id")); break;
                case "name": this.name = rq.body.get("name"); break;
                case "salary": this.salary = Float.valueOf(rq.body.get("salary")); break;
                case "creationDate": this. creationDate = LocalDate.parse(rq.body.get("creationDate")); break;
                case "endDate": this.endDate = LocalDate.parse(rq.body.get("endDate"));break;
                case "position": this.position = Enum.valueOf(Position.class, rq.body.get("position")); break;
                case "status": this.status = Enum.valueOf(Status.class, rq.body.get("status")); break;
            }
        }
        for (String c: rq.attachments.keySet()){
            switch(c){
                case "coordinates": this.coordinates = (Coordinates)rq.attachments.get("coordinates").getObject();break;
                case "organization": this.organization = (Organization)rq.attachments.get("organization").getObject();break;
            }
        }
        return this.get();

    }

    /**
     * Сравнивает собственную salary с переданной
     * @param salary
     * @return
     */
    public int compareTo(float salary){
        Double dv = Math.floor(this.salary - salary);
        return dv.intValue();
    }


}