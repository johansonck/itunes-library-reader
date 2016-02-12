package be.sonck.itunes.libreader.impl.service.event;

import be.sonck.itunes.api.model.FileTrack;
import org.springframework.context.ApplicationEvent;

/**
 * Created by johansonck on 12/02/16.
 */
public class FileTrackEvent extends ApplicationEvent {

    public FileTrackEvent(FileTrack source) {
        super(source);
    }

    public FileTrack getFileTrack() {
        return (FileTrack) getSource();

    }
}
