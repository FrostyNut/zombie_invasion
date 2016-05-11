package gamemanager;

import javafx.scene.media.AudioClip;

/**
 *
 *
 * @author Jackie Chan
 * May 9, 2016
 */
public class SoundManager {

    public SoundManager() {}
    
    public void playSound() {
        AudioClip ac = new AudioClip(SoundManager.class.getResource("/theme.mp3").toString());
        ac.play();
    }
}
