package net.toadless.asciiplayer.util;

public class Time
{
    public static final long NS_PER_SECOND = 1000000000;

    private Time()
    {
        //Overrides the default, public, constructor
    }

    public static long now()
    {
        return System.nanoTime();
    }
}