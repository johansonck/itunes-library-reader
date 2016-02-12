package be.sonck.itunes.libreader.impl.service.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by johansonck on 11/02/16.
 */
public class NodeEncounteredEvent extends ApplicationEvent {

    public NodeEncounteredEvent(NodeEncounteredEventSource source) {
        super(source);
    }

    public NodeEncounteredEventSource getNodeEncounteredEventSource() {
        return (NodeEncounteredEventSource) getSource();
    }
}
