package com.xivs;

import com.xivs.builders.xmlBuilders.xmlWorkerBuilder;
import com.xivs.dataTransfer.DataTransference;
import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.dataTransfer.Status;
import com.xivs.lab.*;

import javax.xml.stream.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author XivS
 * Класс менеджера рабочих. При инициализации время создания проставляется автоматически.
 */
public class WorkersManager {
    private HashMap<String, Worker> workers;
    public final LocalDateTime creationTime;
    public WorkersManager(){
        this.workers = new HashMap<String, Worker>();
        this.creationTime = LocalDateTime.now();
    }

    /**
     *Добавлет или изменяет значение ключа по переданному запросу.
     * @param rq
     * @return
     */
    public Response append(Request rq){
        if(!validate(rq)) return Response.STANDART_ERROR();
        String key = rq.body.get("key");
        String id = String.valueOf( 1 + (long) (Math.random() * (Long.MAX_VALUE-1)));
        Worker w = (Worker)rq.attachments.get("worker").getObject();
        w.update(new Request(
                new HashMap<String, String>(){{put("id", id);}},
                new HashMap<String, DataTransference>()
        ));
        this.workers.put(key, w);
        return new Response(
                new ArrayList<String>(){{add("Объект успешно добавлен/обновлён");}},
                Status.SUCCESS,
                new HashMap<String, String>(),
                new HashMap<String, DataTransference>()
        );
    }

    /**
     * Удаляет из коллекции значение по ключу. Если значение не найдено, статусу ответа присваивается ERROR
     * @param rq
     * @return
     */
    public Response remove(Request rq){
        if(!validate(rq)) return Response.STANDART_ERROR();
        Worker w = this.workers.remove(rq.body.get("key"));
        if(w == null) return new Response(new ArrayList<String>(){{add("Объект с данным ключом не существует");}}, Status.ERROR, new HashMap<String, String>(), new HashMap<String, DataTransference>());
        return new Response(new ArrayList<String>(){{add("Объект {" + w.get().body.get("id") + "} успешно удалён");}}, Status.SUCCESS, new HashMap<String, String>(), new HashMap<String, DataTransference>());
    }

    /**
     * Возвращает коллекцию рабочих с поисковым фильтром.
     * Для запроса всей коллекции нужно вызвать метод без фильтров.
     * Доступны фильтры: id, position
     * @param rq
     * @return
     */
    public Response search(Request rq){
        if(!validate(rq)) return Response.STANDART_ERROR();
        HashMap<String, Worker> w = (HashMap<String, Worker>) this.workers.clone();
        for(String c: rq.body.keySet()){
           switch (c){
               case "id": for(String q: this.workers.keySet()) if (!w.get(q).get().body.get("id").equals(rq.body.get("id"))) w.remove(q); break;
               case "position": for(String q: this.workers.keySet()) if(!(Enum.valueOf(Position.class, w.get(q).get().body.get("position")).equals(Enum.valueOf(Position.class, rq.body.get("position"))))){ w.remove(q);} break;
           }
        }
        return new Response(new ArrayList<String>(), Status.SUCCESS, new HashMap<String, String>(), new HashMap<String, DataTransference>(){{put("workers", new DataTransference() {
            @Override
            public HashMap<String, Worker> getObject() {
                return w;
            }
        });}});
    }

    /**
     * Возвращает рабочего по ключу
     * @param rq
     * @return
     */
    public Response get(Request rq){
        if(!validate(rq)) return Response.STANDART_ERROR();
        String key = rq.body.get("key");
        if (this.workers.get(key) == null) return new Response(new ArrayList<String>(){{add("Объект с данным ключом не существует");}}, Status.ERROR, new HashMap<String, String>(), new HashMap<String, DataTransference>());
        Worker w = this.workers.get(key);
        return new Response(new ArrayList<String>(), Status.SUCCESS, new HashMap<String, String>(), new HashMap<String, DataTransference>(){{put("worker", w);}});
    }

    /**
     * Проверить наличие ключа в коллекции
     * @param key
     * @return
     */
    public boolean checkExistence(String key){
        if(this.workers.get(key) == null) return false;
        return true;

    }

    /**
     * Вернуть информацию о коллекции
     * @return
     */
    public Response info(){
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("creationTime", creationTime.toString());
        body.put("count", String.valueOf(this.workers.size()));
        body.put("type", "HashMap");
        return new Response(new ArrayList<String>(), Status.SUCCESS, body, new HashMap<String, DataTransference>());
    }

    /**
     * Складывает коллекцию в файл. Файл передаёюся в запросе в параметре path;
     * @param rq
     * @return
     */
    public Response dump(Request rq){
        String path = rq.body.get("path");
        try {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = factory.createXMLStreamWriter(new FileWriter(new File(path)));
            writer.writeStartElement("Workers");
            writer.writeCharacters("\n\t");
            for (String key : this.workers.keySet()) {
                Worker w = workers.get(key);
                Organization org = (Organization) w.get().attachments.get("organization").getObject();
                Coordinates coords = (Coordinates) w.get().attachments.get("coordinates").getObject();
                Address addr = (Address) org.get().attachments.get("address").getObject();
                writer.writeStartElement(key);
                writer.writeCharacters("\n\t");
                for(String param: w.get().body.keySet()){
                    writer.writeStartElement(param);
                    writer.writeCharacters(w.get().body.get(param));
                    writer.writeEndElement();
                    writer.writeCharacters("\n\t");

                }
                writer.writeStartElement("organization");
                writer.writeCharacters("\n\t");
                for(String param: org.get().body.keySet()){
                    writer.writeStartElement(param);
                    writer.writeCharacters(org.get().body.get(param));
                    writer.writeEndElement();
                    writer.writeCharacters("\n\t");
                }
                writer.writeStartElement("address");
                writer.writeCharacters("\n\t");
                for(String param1: addr.get().body.keySet()) {
                    writer.writeStartElement(param1);
                    writer.writeCharacters(addr.get().body.get(param1));
                    writer.writeEndElement();
                    writer.writeCharacters("\n\t");
                }
                writer.writeEndElement();
                writer.writeEndElement();
                writer.writeStartElement("coordinates");
                writer.writeCharacters("\n\t");
                for(String param: coords.get().body.keySet()){
                    writer.writeStartElement(param);
                    writer.writeCharacters(coords.get().body.get(param));
                    writer.writeEndElement();
                    writer.writeCharacters("\n\t");
                }
                writer.writeEndElement();
                writer.writeCharacters("\n\t");
                writer.writeEndElement();
                writer.writeCharacters("\n\t");
            }
            writer.writeEndElement();
            writer.flush();
            writer.close();

        }
        catch(IOException | XMLStreamException ex){return new Response(new ArrayList<String>(){{add("Ошибка при работе с файлом");}}, Status.ERROR, new HashMap<String, String>(), new HashMap<String, DataTransference>());}
        return new Response(new ArrayList<String>(){{add("Коллекия собрана в файл " + path);}}, Status.SUCCESS, new HashMap<String, String>(), new HashMap<String, DataTransference>());
    }

    /**
     * Очистить коллекцию
     * @return
     */
    public Response clear(){
        this.workers.clear();
        return new Response(new ArrayList<String>(), Status.SUCCESS, new HashMap<String, String>(), new HashMap<String, DataTransference>());
    }

    /**
     * загрузить коллекцию изх файла. Файл передаётся в запросе в параметре path
     * @param rq
     * @return
     */
    public Response load(Request rq){
        String path = rq.body.get("path");

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new BufferedInputStream(new FileInputStream(path)));
            xmlWorkerBuilder builder = new xmlWorkerBuilder(reader);
            Response worker_resp = Response.STANDART_ERROR();
            while (reader.hasNext()) {
                reader.next();

                if (reader.isStartElement() && reader.getLocalName() == "Workers") {
                    while (!(reader.isEndElement() && reader.getLocalName() == "Workers")) {
                        String key = "";
                        reader.next();
                        if (reader.isStartElement()){
                            key = reader.getLocalName();
                            reader.next();
                            worker_resp = builder.build(key);
                            if(worker_resp.status.equals(Status.ERROR)) {System.out.println();continue;}
                            Worker w = (Worker)worker_resp.attachments.get("worker").getObject();
                            this.workers.put(key, w);
                        }

                    }
                }
            }



        }
        catch(IOException | XMLStreamException ex){ex.printStackTrace();return new Response(new ArrayList<String>(){{add("Ошибка при работе с файлом");}}, Status.ERROR, new HashMap<String, String>(), new HashMap<String, DataTransference>());}
        return new Response(new ArrayList<String>(){{add("Коллекия собрана из файла " + path);}}, Status.SUCCESS, new HashMap<String, String>(), new HashMap<String, DataTransference>());

    }

    /**
     * Проверить запрос на валидность
     * @param rq
     * @return
     */
    public static boolean validate(Request rq){
        if(!Worker.validate(rq)) return false;
        try {
            for (String c : rq.body.keySet()) {
                switch (c) {
                    case "key":
                        if (rq.body.get("key").isEmpty()) return false;

                }
            }
            return true;
        }
        catch (IllegalArgumentException ex){
            return false;
        }
    }

}
