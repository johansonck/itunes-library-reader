package be.sonck.itunes.libreader.impl.service.sax;

import be.sonck.itunes.libreader.impl.service.event.NodeEncounteredEvent;
import be.sonck.itunes.libreader.impl.service.event.NodeEncounteredEventSource;
import be.sonck.itunes.libreader.impl.service.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by johansonck on 07/02/16.
 */
@Service
public class GenericContentHandler extends DefaultHandler {

    @Autowired
    private ApplicationEventPublisher publisher;

    private Node currentNode;
    private StringBuilder contentBuilder;


    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentNode = new Node(qName, currentNode);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        String content = null;

        if (contentBuilder != null) {
            content = contentBuilder.toString();
            contentBuilder = null;
        }

        publisher.publishEvent(new NodeEncounteredEvent(new NodeEncounteredEventSource(currentNode, content)));

        currentNode = currentNode.getParentNode();
    }

    public void characters(char characters[], int start, int length) throws SAXException {
        String content = new String(characters, start, length);

        if (contentBuilder == null) {
            contentBuilder = new StringBuilder(content);
        } else {
            contentBuilder.append(content);
        }
    }
}
