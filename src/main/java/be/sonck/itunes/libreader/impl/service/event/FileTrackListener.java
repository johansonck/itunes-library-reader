package be.sonck.itunes.libreader.impl.service.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * Created by johansonck on 12/02/16.
 */
@Service
public class FileTrackListener implements ApplicationListener<FileTrackEvent> {

    @Override
    public void onApplicationEvent(FileTrackEvent fileTrackEvent) {
        System.out.println(fileTrackEvent.getFileTrack());
    }
}
