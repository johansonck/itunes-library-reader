package be.sonck.itunes.libreader.impl.service.mapper;

import be.sonck.itunes.api.model.FileTrack;
import be.sonck.itunes.api.model.FileTrackBuilder;
import be.sonck.itunes.api.model.RatingKind;
import be.sonck.itunes.libreader.impl.service.model.Dictionary;
import org.springframework.stereotype.Service;

/**
 * Created by johansonck on 08/02/16.
 */
@Service
public class FileTrackMapper implements DictionaryMapper<FileTrack> {

    @Override
    public FileTrack mapDictionary(Dictionary dictionary) {
        return new FileTrackBuilder()
                .album(dictionary.getString("Album"))
                .artist(dictionary.getString("Artist"))
                .name(dictionary.getString("Name"))
                .persistentId(dictionary.getString("Persistent ID"))
                .albumRating(dictionary.getInteger("Album Rating"))
                .albumRatingKind(ratingKind(dictionary.getBoolean("Album Rating Computed")))
                .discNumber(dictionary.getInteger("Disc Number"))
                .location(dictionary.getFile("Location"))
                .rating(dictionary.getInteger("Rating"))
                .trackNumber(dictionary.getInteger("Track Number"))
                .build();
    }

    private RatingKind ratingKind(Boolean value) {
        return value == Boolean.TRUE ? RatingKind.COMPUTED : RatingKind.USER;
    }
}
