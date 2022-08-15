package net.toadless.asciiplayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)
    {
        try
        {
            AsciiVideoPlayer player = new AsciiVideoPlayer(args);
            player.build();
        } catch (Exception e)
        {
            LOGGER.error("An exception occurred whilst running... ", e);
            System.exit(1);
        }
    }
}