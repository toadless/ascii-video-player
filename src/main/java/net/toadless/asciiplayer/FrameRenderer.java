package net.toadless.asciiplayer;

import net.toadless.asciiplayer.util.Time;

import java.util.List;

public class FrameRenderer
{
    private final AsciiVideoPlayer asciiVideoPlayer;

    private boolean rendering;
    private long lastRender;

    public FrameRenderer(AsciiVideoPlayer asciiVideoPlayer)
    {
        this.asciiVideoPlayer = asciiVideoPlayer;
        this.rendering = false;
    }

    public void renderFrames(List<String[]> frames)
    {
        this.rendering = true;

        int index = 0;

        while (this.rendering)
        {
            long now = Time.now();

            if (now - this.lastRender >= Time.NS_PER_SECOND /
                    this.asciiVideoPlayer.getArgumentParser().getInt("fps"))
            {
                lastRender = now;

                if (index >= (frames.size() - 1))
                {
                    this.rendering = false;
                    clearScreen(); // clear previous image now finished
                }

                clearScreen();
                renderFrame(frames.get(index));
                index++;
            }
        }
    }

    private void clearScreen()
    {
        System.out.print(System.lineSeparator().repeat(500));
    }

    private void renderFrame(String[] frame)
    {
        for (int i = 0; i < frame.length; i++)
        {
            final String row = frame[i];
            System.out.println(row);
        }
    }
}