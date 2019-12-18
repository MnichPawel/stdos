package stdos.Interface;

import javax.sound.sampled.*;
import java.io.File;


public class Sound {

    static File START_UP_FILE = new File("resources/startup.wav");
    static File EXIT_FILE = new File("resources/logoff.wav");
    static File ERROR_FILE = new File("resources/error.wav");

    public static void startSystem() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(START_UP_FILE));
            clip.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void exitSystem() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(EXIT_FILE));
            clip.start();
            Thread.sleep(1500);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
     public static void errorSound() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(ERROR_FILE));
            clip.start();
            Thread.sleep(1000);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


