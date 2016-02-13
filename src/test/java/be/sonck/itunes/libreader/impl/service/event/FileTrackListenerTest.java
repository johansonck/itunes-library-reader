package be.sonck.itunes.libreader.impl.service.event;

import be.sonck.itunes.api.model.FileTrack;
import be.sonck.itunes.api.model.FileTrackBuilder;
import be.sonck.itunes.api.model.RatingKind;
import be.sonck.itunes.api.service.ITunesBridge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by johansonck on 12/02/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class FileTrackListenerTest {

    @Mock
    private ITunesBridge iTunesBridge;

    @InjectMocks
    private FileTrackListener bean;


    @Test
    public void noRating() {
        FileTrack fileTrack = fileTrackBuilder()
                .rating(0)
                .albumRating(0)
                .build();

        onApplicationEvent(fileTrack);

        verify(iTunesBridge, never()).getTrack(anyString(), anyString(), anyString());
    }

    @Test
    public void noSongRatingAndComputedAlbumRating() {
        FileTrack fileTrack = fileTrackBuilder()
                .rating(0)
                .albumRating(10)
                .albumRatingKind(RatingKind.COMPUTED)
                .build();

        onApplicationEvent(fileTrack);

        verify(iTunesBridge, never()).getTrack(anyString(), anyString(), anyString());
    }

    @Test
    public void sameSongRatingInItunesNoAlbumRating() {
        FileTrack fileTrack = fileTrackBuilder()
                .rating(20)
                .build();

        when(getTrack(fileTrack)).thenReturn(fileTrack);

        onApplicationEvent(fileTrack);

        verifyNoUpdates(fileTrack);
    }

    @Test
    public void differentSongRatingNoAlbumRating() {
        int songRating = 20;

        FileTrack fileTrack = fileTrackBuilder().rating(songRating).build();
        FileTrack iTunesTrack = fileTrackBuilder(fileTrack).rating(songRating + 1).build();

        when(getTrack(fileTrack)).thenReturn(iTunesTrack);

        onApplicationEvent(fileTrack);

        verifyUpdateSongRating(fileTrack, iTunesTrack, songRating);
    }

    @Test
    public void differentSongRatingDifferentComputedAlbumRating() {
        int songRating = 20;

        FileTrack fileTrack = fileTrackBuilder()
                .rating(songRating)
                .albumRating(40)
                .albumRatingKind(RatingKind.COMPUTED)
                .build();

        FileTrack iTunesTrack = fileTrackBuilder(fileTrack)
                .rating(songRating + 1)
                .albumRating(fileTrack.getAlbumRating() + 1)
                .build();

        when(getTrack(fileTrack)).thenReturn(iTunesTrack);

        onApplicationEvent(fileTrack);

        verifyUpdateSongRating(fileTrack, iTunesTrack, songRating);
    }

    @Test
    public void sameSongRatingDifferentComputedAlbumRating() {
        FileTrack fileTrack = fileTrackBuilder()
                .rating(20)
                .albumRating(20)
                .albumRatingKind(RatingKind.COMPUTED)
                .build();

        FileTrack iTunesTrack = fileTrackBuilder(fileTrack)
                .albumRating(fileTrack.getAlbumRating() + 1)
                .albumRatingKind(RatingKind.USER)
                .build();

        when(getTrack(fileTrack)).thenReturn(iTunesTrack);

        onApplicationEvent(fileTrack);

        verifyNoUpdates(fileTrack);
    }

    @Test
    public void sameSongRatingDifferentUserAlbumRating() {
        int albumRating = 20;

        FileTrack fileTrack = fileTrackBuilder()
                .rating(20).albumRating(albumRating).albumRatingKind(RatingKind.USER)
                .build();

        FileTrack iTunesTrack = fileTrackBuilder(fileTrack)
                .albumRating(albumRating + 1)
                .build();

        when(getTrack(fileTrack)).thenReturn(iTunesTrack);

        onApplicationEvent(fileTrack);

        verifyUpdateAlbumRating(fileTrack, iTunesTrack, albumRating);
    }

    @Test
    public void differentSongRatingDifferentUserAlbumRating() {
        int songRating = 30;
        int albumRating = 20;

        FileTrack fileTrack = fileTrackBuilder()
                .rating(songRating).albumRating(albumRating).albumRatingKind(RatingKind.USER)
                .build();

        FileTrack iTunesTrack = fileTrackBuilder(fileTrack)
                .rating(songRating + 1).albumRating(albumRating + 1)
                .build();

        when(getTrack(fileTrack)).thenReturn(iTunesTrack);

        onApplicationEvent(fileTrack);

        verifyUpdateSongRatingAndAlbumRating(fileTrack, iTunesTrack, songRating, albumRating);
    }

    private void verifyUpdateSongRatingAndAlbumRating(FileTrack fileTrack, FileTrack iTunesTrack, int songRating,
            int albumRating) {
        verifyGetTrack(fileTrack);
        verify(iTunesBridge).setTrackRating(iTunesTrack.getPersistentId(), songRating);
        verify(iTunesBridge).setAlbumRating(iTunesTrack.getPersistentId(), albumRating);
    }

    private void verifyUpdateSongRating(FileTrack fileTrack, FileTrack iTunesTrack, int songRating) {
        verifyGetTrack(fileTrack);
        verify(iTunesBridge).setTrackRating(iTunesTrack.getPersistentId(), songRating);
        verifyAlbumRatingNotSet();
    }

    private void verifyUpdateAlbumRating(FileTrack fileTrack, FileTrack iTunesTrack, int albumRating) {
        verifyGetTrack(fileTrack);
        verifyTrackRatingNotSet();
        verify(iTunesBridge).setAlbumRating(iTunesTrack.getPersistentId(), albumRating);
    }

    private void onApplicationEvent(FileTrack fileTrack) {
        bean.onApplicationEvent(new FileTrackEvent(fileTrack));
    }

    private FileTrackBuilder fileTrackBuilder() {
        return new FileTrackBuilder().persistentId(randomAlphabetic(10))
                .name(randomAlphabetic(10))
                .artist(randomAlphabetic(10))
                .album(randomAlphabetic(10));
    }

    private FileTrackBuilder fileTrackBuilder(FileTrack fileTrack) {
        return new FileTrackBuilder(fileTrack);
    }

    private void verifyTrackRatingNotSet() {
        verify(iTunesBridge, never()).setTrackRating(anyString(), anyInt());
    }

    private void verifyGetTrack(FileTrack fileTrack) {
        verify(iTunesBridge).getTrack(fileTrack.getName(), fileTrack.getAlbum(), fileTrack.getArtist());
    }

    private FileTrack getTrack(FileTrack fileTrack) {
        return iTunesBridge.getTrack(fileTrack.getName(), fileTrack.getAlbum(), fileTrack.getArtist());
    }

    private void verifyAlbumRatingNotSet() {
        verify(iTunesBridge, never()).setAlbumRating(anyString(), anyInt());
    }

    private void verifyNoUpdates(FileTrack fileTrack) {
        verifyGetTrack(fileTrack);
        verifyTrackRatingNotSet();
        verifyAlbumRatingNotSet();
    }
}