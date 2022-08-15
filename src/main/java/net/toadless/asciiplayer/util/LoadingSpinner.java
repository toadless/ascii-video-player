package net.toadless.asciiplayer.util;

public class LoadingSpinner implements Runnable
{
    private static final char[] CHARS = new char[]{ '/', '-', '\\', '|' };

    private static final int FPS = 10;

    private boolean running;

    private long lastUpdate;
    private String text;
    public int index;

    public LoadingSpinner()
    {
        this.running = false;
        this.text = "";
        this.index = 0;
    }

    public void start()
    {
        this.running = true;
        new Thread(this).start();
    }

    public void stop()
    {
        this.running = false;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    @Override
    public void run()
    {
        while (running)
        {
            long now = Time.now();

            if (now - lastUpdate >= Time.NS_PER_SECOND / FPS)
            {
                lastUpdate = now;

                if (this.index == (CHARS.length - 1)) this.index = 0;
                else this.index++;

                char spinnerPart = CHARS[this.index];
                System.out.printf("\r[%s] %s", spinnerPart, this.text);
            }
        }
    }
}