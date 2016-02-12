package be.sonck.itunes.libreader.impl.service.event;

import be.sonck.itunes.libreader.impl.service.model.Dictionary;
import org.springframework.context.ApplicationEvent;

/**
 * Created by johansonck on 12/02/16.
 */
public class DictionaryEvent extends ApplicationEvent {

    public enum Type { START, END }

    private final Type type;

    public DictionaryEvent(Dictionary source, Type type) {
        super(source);
        this.type = type;
    }

    public Dictionary getDictionary() {
        return (Dictionary) getSource();
    }

    public Type getType() {
        return type;
    }
}
