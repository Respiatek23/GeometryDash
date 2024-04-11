package Util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * Clase para poder usar imagenes
 */
public class Music  {
    private String filepath;
    AudioInputStream audio = null;
    Clip audioClip = null;
    public Music(String filename) {
        filepath = new File(filename).getAbsolutePath();
        try {
            audio = AudioSystem.getAudioInputStream(new File(filepath));
            audioClip = AudioSystem.getClip();
            audioClip.open(audio);
            restartClip();
            audioClip.loop(audioClip.LOOP_CONTINUOUSLY);
            Thread.sleep(1000);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        audioClip.stop();
    }
    public void continuar(){
        audioClip.start();
    }
    public void restartClip() {
        try {
            audioClip.stop();
            audioClip.setFramePosition(0);
            audioClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

