package be.sonck.itunes.libreader.impl.service.event;

import be.sonck.itunes.libreader.impl.service.model.Node;

/**
 * Created by johansonck on 11/02/16.
 */
public class NodeEncounteredEventSource {

    private final Node node;
    private final String content;

    public NodeEncounteredEventSource(Node node, String content) {
        this.node = node;
        this.content = content;
    }

    public Node getNode() {
        return node;
    }

    public String getContent() {
        return content;
    }
}
