package net.toadless.asciiplayer;

import net.toadless.asciiplayer.arguments.Argument;
import net.toadless.asciiplayer.arguments.ArgumentParser;
import net.toadless.asciiplayer.sound.Sound;
import net.toadless.asciiplayer.util.FFmpegUtil;
import net.toadless.asciiplayer.util.LoadingSpinner;
import net.toadless.asciiplayer.util.TempGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AsciiVideoPlayer
{
    private final Logger logger;
    private final ArgumentParser argumentParser;
    private final LoadingSpinner loadingSpinner;
    private final TempGenerator tempGenerator;
    private final FrameGenerator frameGenerator;
    private final FrameRenderer frameRenderer;

    public AsciiVideoPlayer(String[] args)
    {
        this.logger = LoggerFactory.getLogger(AsciiVideoPlayer.class);
        this.argumentParser = new ArgumentParser(args, new Argument[]{
                new Argument(true, "input", List.of("input", "i"), ""),
                new Argument(true, "char", List.of("char", "c"), ""),
                new Argument(true, "width", List.of("width", "w"), ""),
                new Argument(false, "audio", List.of("audio", "a"), "true"),
                new Argument(false, "fps", List.of("fps"), "60")
        });
        this.loadingSpinner = new LoadingSpinner();
        this.tempGenerator = new TempGenerator(this);
        this.frameGenerator = new FrameGenerator(this);
        this.frameRenderer = new FrameRenderer(this);
    }

    public void build()
    {
        this.logger.info("Starting AsciiVideoPlayer by Toadless.");

        FFmpegUtil.checkIfFFmpegPresent(); // will exit if ffmpeg is not present

        this.loadingSpinner.start();

        // generate frames (possibly audio)
        this.tempGenerator.generateTempContents();

        List<String[]> frames = this.frameGenerator.generateAsciiEncodedFrames();

        this.loadingSpinner.stop();

        if (this.argumentParser.getBoolean("audio"))
        {
            Sound sound = new Sound(String.format("%s/%s",
                    TempGenerator.TEMP_FOLDER.getAbsolutePath(), Constants.AUDIO_NAME));
            sound.play();
        }

        this.frameRenderer.renderFrames(frames);

        // dispose of temp resources
        this.tempGenerator.deleteTempDirectory();

        this.logger.info("Successfully rendered :)");
        System.exit(0);
    }

    public ArgumentParser getArgumentParser()
    {
        return this.argumentParser;
    }

    public LoadingSpinner getLoadingSpinner()
    {
        return this.loadingSpinner;
    }
}