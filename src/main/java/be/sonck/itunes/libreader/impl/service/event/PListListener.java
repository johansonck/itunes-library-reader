package be.sonck.itunes.libreader.impl.service.event;

import be.sonck.itunes.libreader.impl.service.event.DictionaryEvent.Type;
import be.sonck.itunes.libreader.impl.service.model.Dictionary;
import be.sonck.itunes.libreader.impl.service.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by johansonck on 11/02/16.
 */
@Service
public class PListListener implements ApplicationListener<NodeEncounteredEvent> {

    @Autowired
    private ApplicationEventPublisher publisher;

    private Dictionary currentDictionary;

    private String currentKey;
    private String previousDictionaryNodePath;


    @Override
    public void onApplicationEvent(NodeEncounteredEvent nodeEncounteredEvent) {
        NodeEncounteredEventSource source = nodeEncounteredEvent.getNodeEncounteredEventSource();

        Node node = source.getNode();
        String content = source.getContent();

        String nodeName = node.toString();

        String currentDictionaryNodePath = getDictionaryNodePath(nodeName);
        if (!Objects.equals(currentDictionaryNodePath, previousDictionaryNodePath)) {
            handleNewDictionary(currentDictionaryNodePath);
        }

        if (nodeName.endsWith(":key")) {
            currentKey = content;

        } else {
            Object value = getValue(node, content);
            if (value != null) {
                currentDictionary.put(currentKey, value);
            }
        }

        previousDictionaryNodePath = currentDictionaryNodePath;
    }

    private String getDictionaryNodePath(String nodeName) {
        int index = nodeName.lastIndexOf(":dict:");
        return index < 0 ? nodeName : nodeName.substring(0, index + ":dict".length());
    }

    private Object getValue(Node node, String content) {
        switch (node.getName()) {
            case "integer":
                return Long.parseLong(content);
            case "true":
                return Boolean.TRUE;
            case "false":
                return Boolean.FALSE;
            case "string":
            case "data":
                return content;
            case "real":
                return new BigDecimal(content);
            default:
                return null;
        }
    }

    private void handleNewDictionary(String currentDictionaryNodePath) {
        if (getLength(currentDictionaryNodePath) > getLength(previousDictionaryNodePath)) {
            currentDictionary = new Dictionary(currentKey, currentDictionary);
            publisher.publishEvent(new DictionaryEvent(currentDictionary, Type.START));
        } else {
            publisher.publishEvent(new DictionaryEvent(currentDictionary, Type.END));
            currentDictionary = currentDictionary.getParent();
        }
    }

    private int getLength(String string) {
        return string == null ? 0 : string.length();
    }
}

