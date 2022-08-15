package net.toadless.asciiplayer.util;

import net.toadless.asciiplayer.AsciiVideoPlayer;
import net.toadless.asciiplayer.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class TempGenerator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TempGenerator.class);

    public static final File TEMP_FOLDER = new File("temp");
    public static final File FRAMES_FOLDER = new File("temp/frames");

    private final AsciiVideoPlayer asciiVideoPlayer;

    public TempGenerator(AsciiVideoPlayer asciiVideoPlayer)
    {
        this.asciiVideoPlayer = asciiVideoPlayer;
        initTempFile();
        initFramesFile();
    }

    private void initTempFile()
    {
        try
        {
            if (TEMP_FOLDER.mkdir())
            {
                LOGGER.debug("Created new temp folder.");
            }
            else
            {
                LOGGER.debug("Temp folder exists, remaking.");

                deleteTempDirectory();
                initTempFile();
            }
        }
        catch (Exception exception)
        {
            LOGGER.error("An exception occurred while creating the temp folder, abort.", exception);
            System.exit(1);
        }
    }

    private void initFramesFile()
    {
        try
        {
            if (FRAMES_FOLDER.mkdir())
            {
                LOGGER.debug("Created new frames folder.");
            }
            else
            {
                LOGGER.debug("Frames folder exists.");
            }
        }
        catch (Exception exception)
        {
            LOGGER.error("An exception occurred while creating the frames folder, abort.", exception);
            System.exit(1);
        }
    }

    public void deleteTempDirectory()
    {
      try (var stream = Files.walk(Paths.get(TEMP_FOLDER.getPath())))
      {
          stream.map(Path::toFile).sorted(Comparator.reverseOrder()).forEach(File::delete);
      } catch (IOException e)
      {
          LOGGER.error("Unable to delete directory :(");
          System.exit(1);
      }
    }

    public void generateTempContents()
    {
        final File video = new File(this.asciiVideoPlayer.getArgumentParser().getString("file"));

        if (!video.exists())
        {
            LOGGER.error("The provided video file doesn't exist, abort.");
            System.exit(1);
            return;
        }

        final int fps = this.asciiVideoPlayer.getArgumentParser().getInt("fps");

        this.asciiVideoPlayer.getLoadingSpinner().setText("Generating individual frames...");

        // generate individual frames with ffmpeg
        FFmpegUtil.executeFFmpeg(
                "-i", video.getAbsolutePath(), "-vf", "fps=" + fps,
                FRAMES_FOLDER.getAbsolutePath() + "/" + Constants.FRAME_NAME.replace("{}", "%d"));

        // generate standalone audio file with ffmpeg
        if (this.asciiVideoPlayer.getArgumentParser().getBoolean("audio"))
        {
            this.asciiVideoPlayer.getLoadingSpinner().setText("Generating audio...");

            FFmpegUtil.executeFFmpeg("-i", video.getAbsolutePath(),
                    TEMP_FOLDER.getAbsolutePath() + "/" + Constants.AUDIO_NAME);
        }
    }
}