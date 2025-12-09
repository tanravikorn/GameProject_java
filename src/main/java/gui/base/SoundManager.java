package gui.base;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundManager {

    private static MediaPlayer bgmPlayer;

    public static void playBGM(String filename) {
        try {
            if (bgmPlayer != null) {
                bgmPlayer.stop();
            }

            URL resource = SoundManager.class.getResource("/music/" + filename);
            if (resource == null) {
                return;
            }

            Media media = new Media(resource.toExternalForm());
            bgmPlayer = new MediaPlayer(media);

            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bgmPlayer.setVolume(0.3);
            bgmPlayer.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }
    public static void playSFX(String filename) {
        try {
            URL resource = SoundManager.class.getResource("/soundEffect/" + filename);
            if (resource == null) {
                return;
            }
            Media media = new Media(resource.toExternalForm());

            MediaPlayer sfxPlayer = new MediaPlayer(media);
            sfxPlayer.setVolume(0.55);
            sfxPlayer.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
