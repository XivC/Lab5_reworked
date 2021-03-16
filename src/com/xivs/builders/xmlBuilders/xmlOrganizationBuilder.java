package com.xivs.builders.xmlBuilders;

import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.dataTransfer.Status;
import com.xivs.lab.Address;
import com.xivs.lab.Organization;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashMap;

public class xmlOrganizationBuilder {
    XMLStreamReader reader;

    public xmlOrganizationBuilder(XMLStreamReader reader) {
        this.reader = reader;
    }

    public Response build() throws XMLStreamException {
        HashMap<String, String> rq_body = new HashMap<>();
        xmlAddressBuilder builder = new xmlAddressBuilder(reader);
        Response addr_resp = Response.STANDART_ERROR();
        while (!(reader.isEndElement() && reader.getLocalName() == "organization")) {
            reader.next();
            if (reader.isStartElement()){
                switch(reader.getLocalName()) {
                    case "address": addr_resp = builder.build() ;break;
                    default: rq_body.put(reader.getLocalName(), reader.getElementText());break;
                }
            };
        }

        if (addr_resp.status.equals(Status.ERROR)) return Response.STANDART_ERROR();
        Response build_resp = Organization.create(new Request(rq_body, addr_resp.attachments));
        return build_resp;
    }
}
