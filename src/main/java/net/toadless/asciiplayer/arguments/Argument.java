package net.toadless.asciiplayer.arguments;

import java.util.List;

public class Argument
{
    private boolean required;
    private String key;
    private List<String> inputs;
    private String defaultValue;

    public Argument(boolean required, String key, List<String> inputs, String defaultValue)
    {
        this.required = required;
        this.key = key;
        this.inputs = inputs;
        this.defaultValue = defaultValue;
    }

    public boolean isRequired()
    {
        return required;
    }

    public String getKey()
    {
        return key;
    }

    public List<String> getInputs()
    {
        return inputs;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }
}