package com.xivs.builders.xmlBuilders;

import com.xivs.dataTransfer.Request;
import com.xivs.dataTransfer.Response;
import com.xivs.lab.Address;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashMap;

public class xmlAddressBuilder {
    XMLStreamReader reader;

    public xmlAddressBuilder(XMLStreamReader reader) {
        this.reader = reader;
    }

    public Response build() throws XMLStreamException {
        HashMap<String, String> rq_body = new HashMap<>();
        while (!(reader.isEndElement() && reader.getLocalName() == "address")) {
            reader.next();
            if (reader.isStartElement()) rq_body.put(reader.getLocalName(), reader.getElementText());
        }
        Response build_resp = Address.create(new Request(rq_body, new HashMap<>()));
        return build_resp;
    }
}

