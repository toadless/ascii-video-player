package net.toadless.asciiplayer.sound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Sound
{
    private final Logger LOGGER = LoggerFactory.getLogger(Sound.class);

    private Clip clip;

    public Sound(String path)
    {
        try
        {
            AudioInputStream stream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(new FileInputStream(path))
            );

            this.clip = AudioSystem.getClip();
            this.clip.open(stream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            LOGGER.error("Unable to load sound... exiting!");
            System.exit(0);
        }
    }

    public void play()
    {
        this.clip.setFramePosition(0);
        this.clip.start();
    }
}