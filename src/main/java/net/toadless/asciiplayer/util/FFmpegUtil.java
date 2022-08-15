package net.toadless.asciiplayer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class FFmpegUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FFmpegUtil.class);

    public static boolean executeFFmpeg(String... args)
    {
        try
        {
            // Copy array and append the ffmpeg command to the start of it
            String[] command = Arrays.copyOf(args, args.length + 1);
            command[0] = "ffmpeg";

            System.arraycopy(args, 0, command, 1, args.length);

            int result = Runtime.getRuntime().exec(command).waitFor();

            if (result == 1)
            {
                LOGGER.error("A problem occurred whilst running ffmpeg... :(");
                System.exit(1);
            }
        } catch (InterruptedException | IOException e)
        {
            LOGGER.error("Unable to run ffmpeg, please make sure that it is installed!");
            System.exit(1);
        }

        return true;
    }

    public static boolean checkIfFFmpegPresent()
    {
        try
        {
            int result = Runtime.getRuntime().exec(new String[]{ "ffmpeg" }).waitFor();

            if (result == 127) // 127 means ffmpeg is not present
            {
                LOGGER.error("FFmpeg not present on system, please make sure that it is installed!");
                System.exit(1);
            }
        } catch (InterruptedException | IOException e)
        {
            LOGGER.error("Unable to check ffmpeg presence on system, please make sure that it is installed!");
            System.exit(1);
        }

        return true;
    }
}