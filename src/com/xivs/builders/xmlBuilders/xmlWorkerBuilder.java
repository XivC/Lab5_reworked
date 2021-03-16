package com.xivs.builders.xmlBuilders;

import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.dataTransfer.Status;
import com.xivs.lab.Address;
import com.xivs.lab.Coordinates;
import com.xivs.lab.Organization;
import com.xivs.lab.Worker;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashMap;

public class xmlWorkerBuilder {
    XMLStreamReader reader;

    public xmlWorkerBuilder(XMLStreamReader reader) {
        this.reader = reader;
    }

    public Response build(String key) throws XMLStreamException {
        HashMap<String, String> rq_body = new HashMap<>();
        xmlCoordinatesBuilder coords_builder = new xmlCoordinatesBuilder(reader);
        xmlOrganizationBuilder org_builder = new xmlOrganizationBuilder(reader);
        Response coords_resp = Response.STANDART_ERROR();
        Response org_resp = Response.STANDART_ERROR();
        while (!(reader.isEndElement() && reader.getLocalName() == key)) {
            reader.next();

            if (reader.isStartElement()){

                switch(reader.getLocalName()) {
                    case "organization": org_resp = org_builder.build() ;break;
                    case "coordinates":  coords_resp = coords_builder.build(); break;
                    default: rq_body.put(reader.getLocalName(), reader.getElementText()); break;
                }
                //reader.next();
            }

        }
        if(org_resp.status.equals(Status.ERROR) | coords_resp.status.equals(Status.ERROR)) return Response.STANDART_ERROR();
        Organization org = (Organization)org_resp.attachments.get("organization").getObject();
        Coordinates coords = (Coordinates) coords_resp.attachments.get("coordinates").getObject();
        Response build_resp = Worker.create(new Request(rq_body, new HashMap<>(){{
            put("organization", org);
            put("coordinates", coords);
        }}));
        return build_resp;
    }
}

