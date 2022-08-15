package net.toadless.asciiplayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AsciiEncoder
{
    private final Logger LOGGER = LoggerFactory.getLogger(AsciiEncoder.class);

    private final char[] CHARS = new char[]{ '.', ',', ':', ';', '+', '*', '?', '%', 'S', '#', '@' };

    private final double characterAspectRatio;
    private final int width;

    public AsciiEncoder(AsciiVideoPlayer asciiVideoPlayer)
    {
        this.characterAspectRatio = asciiVideoPlayer.getArgumentParser().getDouble("char");
        this.width = asciiVideoPlayer.getArgumentParser().getInt("width");
    }

    public String[] getEncodedAsciiImage(File file)
    {
        return encodeImageAsAscii(createScaledImage(file));
    }

    private String[] encodeImageAsAscii(BufferedImage image)
    {
        final int w = image.getWidth();
        final int h = image.getHeight();

        final String[] asciiImage = new String[h];

        for (int y = 0; y < h; y++)
        {
            final StringBuilder row = new StringBuilder();

            for (int x = 0; x < w; x++)
            {
                final Color color = new Color(image.getRGB(x, y));
                final int pixel = (color.getRed() + color.getGreen() + color.getBlue()) / 3; // create grayscale pixel

                char asciiChar = CHARS[pixel * CHARS.length / 256];
                row.append(asciiChar);
            }

            asciiImage[y] = row.toString();
        }

        return asciiImage;
    }

    private BufferedImage createScaledImage(File file)
    {
        try
        {
            final BufferedImage originalImage = ImageIO.read(file);
            final Dimension requiredImageSize = calculateImageSize(originalImage);

            final BufferedImage image = new BufferedImage(
                    requiredImageSize.width, requiredImageSize.height, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2 = image.createGraphics();
            g2.drawImage(originalImage.getScaledInstance(
                    requiredImageSize.width, requiredImageSize.height, Image.SCALE_SMOOTH), 0, 0, null);
            g2.dispose();

            return image;
        } catch (IOException e)
        {
            LOGGER.error("Unable scale image, abort.");
            System.exit(1);
            return null; // will not get called
        }
    }

    private Dimension calculateImageSize(BufferedImage image)
    {
        int w = this.width;
        int h = this.width;

        int ratio = image.getWidth() / image.getHeight();
        h *= ratio;

        // we use a char aspect ratio because characters are taller
        // than they are wider...
        h /= this.characterAspectRatio;

        return new Dimension(w, h);
    }
}