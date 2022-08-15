package net.toadless.asciiplayer;

import net.toadless.asciiplayer.util.TempGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FrameGenerator
{
    private final AsciiVideoPlayer asciiVideoPlayer;
    private final AsciiEncoder asciiEncoder;

    public FrameGenerator(AsciiVideoPlayer asciiVideoPlayer)
    {
        this.asciiVideoPlayer = asciiVideoPlayer;
        this.asciiEncoder = new AsciiEncoder(asciiVideoPlayer);
    }

    public List<String[]> generateAsciiEncodedFrames()
    {
        final int frames = TempGenerator.FRAMES_FOLDER.list().length;

        final List<String[]> encodedFrames = new ArrayList<>();

        for (int i = 0; i < frames; i++)
        {
            this.asciiVideoPlayer.getLoadingSpinner().setText(
                    String.format("Encoding frame %s out of %s total frames.", i, frames));

            final File image = new File(
                    String.format("%s/%s", TempGenerator.FRAMES_FOLDER.getAbsolutePath(),
                            Constants.FRAME_NAME.replace("{}", String.valueOf(i))));

            if (!image.exists()) continue;

            String[] frame = this.asciiEncoder.getEncodedAsciiImage(image);
            encodedFrames.add(frame);
        }

        return encodedFrames;
    }
}