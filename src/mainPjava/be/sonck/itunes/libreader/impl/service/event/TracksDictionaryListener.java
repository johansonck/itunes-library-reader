package be.sonck.itunes.libreader.impl.service.event;


import be.sonck.itunes.api.model.FileTrack;
import be.sonck.itunes.libreader.impl.service.model.Dictionary;
import be.sonck.itunes.libreader.impl.service.mapper.FileTrackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * Created by johansonck on 12/02/16.
 */
@Service
public class TracksDictionaryListener implements ApplicationListener<DictionaryEvent> {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private FileTrackMapper fileTrackMapper;


    @Override
    public void onApplicationEvent(DictionaryEvent dictionaryEvent) {
        if (DictionaryEvent.Type.START == dictionaryEvent.getType()) return;

        Dictionary dictionary = dictionaryEvent.getDictionary();

        String parentKey = getParentKey(dictionary);
        if (!"Tracks".equals(parentKey)) return;

        FileTrack fileTrack = fileTrackMapper.mapDictionary(dictionary);
        publisher.publishEvent(new FileTrackEvent(fileTrack));
    }

    private String getParentKey(Dictionary dictionary) {
        if (dictionary == null) return null;

        Dictionary parent = dictionary.getParent();
        if (parent == null) return null;

        return parent.getKey();
    }
}
