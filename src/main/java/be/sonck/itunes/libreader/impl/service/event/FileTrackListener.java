package be.sonck.itunes.libreader.impl.service.event;

import be.sonck.itunes.api.model.FileTrack;
import be.sonck.itunes.api.model.RatingKind;
import be.sonck.itunes.api.service.ITunesBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * Created by johansonck on 12/02/16.
 */
@Service
public class FileTrackListener implements ApplicationListener<FileTrackEvent> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ITunesBridge iTunesBridge;


    @Override
    public void onApplicationEvent(FileTrackEvent fileTrackEvent) {
        FileTrack fileTrack = fileTrackEvent.getFileTrack();
        if (logger.isDebugEnabled()) {
            logger.debug("handling track " + toString(fileTrack));
        }

        if (!isRated(fileTrack)) return;

        FileTrack iTunesTrack = getTrackFromITunes(fileTrack);

        if (isSongRatingUpdateRequired(fileTrack, iTunesTrack)) {
            if (logger.isInfoEnabled()) {
                logger.info("updating song rating for " + fileTrack);
            }
//            iTunesBridge.setTrackRating(iTunesTrack.getPersistentId(), fileTrack.getRating());
        }

        if (isAlbumRatingUpdateRequired(fileTrack, iTunesTrack)) {
            if (logger.isInfoEnabled()) {
                logger.info("updating album rating for " + fileTrack);
            }
//            iTunesBridge.setAlbumRating(iTunesTrack.getPersistentId(), fileTrack.getAlbumRating());
        }
    }

    private boolean isAlbumRatingUpdateRequired(FileTrack libraryFileTrack, FileTrack iTunesTrack) {
        if (libraryFileTrack.getAlbumRatingKind() == RatingKind.COMPUTED) return false;

        return !libraryFileTrack.getAlbumRating().equals(iTunesTrack.getAlbumRating());
    }

    private boolean isSongRatingUpdateRequired(FileTrack libraryFileTrack, FileTrack iTunesTrack) {
        return !libraryFileTrack.getRating().equals(iTunesTrack.getRating());
    }

    private FileTrack getTrackFromITunes(FileTrack fileTrack) {
        return iTunesBridge.getTrack(fileTrack.getName(), fileTrack.getAlbum(), fileTrack.getArtist());
    }

    private boolean isRated(FileTrack fileTrack) {
        Integer rating = fileTrack.getRating();
        if (rating > 0) return true;

        Integer albumRating = fileTrack.getAlbumRating();
        if (albumRating == 0) return false;

        RatingKind albumRatingKind = fileTrack.getAlbumRatingKind();
        return albumRatingKind == RatingKind.USER;
    }

    private String toString(FileTrack fileTrack) {
        return new StringBuilder()
                .append("[")
                .append(fileTrack.getName())
                .append(",")
                .append(fileTrack.getArtist())
                .append(",")
                .append(fileTrack.getAlbum())
                .append("]")
                .toString();
    }
}
