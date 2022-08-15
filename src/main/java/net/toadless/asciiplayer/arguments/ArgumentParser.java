package net.toadless.asciiplayer.arguments;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArgumentParser
{
    private final Logger LOGGER = LoggerFactory.getLogger(ArgumentParser.class);

    private final Argument[] arguments;
    private final List<ArgumentValue> argumentValues;

    public ArgumentParser(String[] args, Argument[] arguments)
    {
        this.arguments = arguments;
        this.argumentValues = loadArguments(args);
    }

    private List<ArgumentValue> loadArguments(String[] args)
    {
        List<ArgumentValue> values = new ArrayList<>();

        try
        {
            String input = String.join(" ", args);

            String[] arguments = input.split("-");

            for (String arg : arguments)
            {
                if (arg.equals(" ") || arg.equals("-"))
                {
                    continue;
                }

                String[] elements = arg.split("=| ");

                for (Argument argument : this.arguments)
                {
                    if (argument.getInputs().contains(elements[0]))
                    {
                        if (elements.length <= 1)
                        {
                            LOGGER.error("You must provide a value for argument {}!", argument.getKey());
                            System.exit(1);
                        }

                        String value = elements[1];

                        if (value.endsWith(" ")) // remove last space
                        {
                            int length = value.length();
                            value = value.substring(0, length - 1) + value.substring(length);
                        }

                        values.add(new ArgumentValue(argument.getKey(), value));
                    }
                }
            }

            return applyDefaults(values);
        }
        catch (Exception exception)
        {
            LOGGER.error("Unable to load arguments.", exception);
            System.exit(1);
            return Collections.emptyList();
        }
    }

    private List<ArgumentValue> applyDefaults(List<ArgumentValue> loadedValues)
    {
        for (Argument argument : this.arguments)
        {
            if (loadedValues.stream().map(ArgumentValue::getKey).noneMatch(key -> argument.getKey().equals(key)))
            {
                if (argument.isRequired())
                {
                    LOGGER.error("Required argument {} not specified.", argument.getKey());
                    System.exit(1);
                }

                loadedValues.add(new ArgumentValue(argument.getKey(), argument.getDefaultValue()));
            }
        }

        return Collections.unmodifiableList(loadedValues);
    }

    private Argument getArgument(String key)
    {
        for (Argument arg : this.arguments)
        {
            if (arg.getKey().equals(key))
            {
                return arg;
            }
        }

        return null;
    }

    public @NotNull String getString(String key)
    {
        final Argument argument = getArgument(key);

        if (argument == null) // unknown argument
        {
            return null;
        }

        synchronized (argumentValues)
        {
            for (ArgumentValue argumentValue : argumentValues)
            {
                if (key.equals(argumentValue.getKey()))
                {
                    return argumentValue.getValue();
                }
            }

            if (!argument.isRequired())
            {
                return argument.getDefaultValue();
            } else
            {
                LOGGER.error("Required argument {} not specified.", argument.getKey());
                System.exit(1);
                return null;
            }
        }
    }

    public @NotNull int getInt(String key)
    {
        final Argument argument = getArgument(key);

        if (argument == null) // unknown argument
        {
            return 0;
        }

        try
        {
            synchronized (argumentValues)
            {
                for (ArgumentValue argumentValue : argumentValues)
                {
                    if (key.equals(argumentValue.getKey()))
                    {
                        return Integer.parseInt(argumentValue.getValue());
                    }
                }

                if (!argument.isRequired())
                {
                    return Integer.parseInt(argument.getDefaultValue());
                } else
                {
                    LOGGER.error("Required argument {} not specified.", argument.getKey());
                    System.exit(1);
                    return 0;
                }
            }
        } catch (NumberFormatException exception)
        {
            LOGGER.error("Unable to parse argument {}, please make sure that its a valid integer.", argument.getKey());
            System.exit(1);
            return 0;
        }
    }

    public @NotNull double getDouble(String key)
    {
        final Argument argument = getArgument(key);

        if (argument == null) // unknown argument
        {
            return 0;
        }

        try
        {
            synchronized (argumentValues)
            {
                for (ArgumentValue argumentValue : argumentValues)
                {
                    if (key.equals(argumentValue.getKey()))
                    {
                        return Double.parseDouble(argumentValue.getValue());
                    }
                }

                if (!argument.isRequired())
                {
                    return Double.parseDouble(argument.getDefaultValue());
                } else
                {
                    LOGGER.error("Required argument {} not specified.", argument.getKey());
                    System.exit(1);
                    return 0;
                }
            }
        } catch (NumberFormatException exception)
        {
            LOGGER.error("Unable to parse argument {}, please make sure that its a valid double.", argument.getKey());
            System.exit(1);
            return 0;
        }
    }

    public @NotNull boolean getBoolean(String key)
    {
        final Argument argument = getArgument(key);

        if (argument == null) // unknown argument
        {
            return false;
        }

        try
        {
            synchronized (argumentValues)
            {
                for (ArgumentValue argumentValue : argumentValues)
                {
                    if (key.equals(argumentValue.getKey()))
                    {
                        return Boolean.parseBoolean(argumentValue.getValue());
                    }
                }

                if (!argument.isRequired())
                {
                    return Boolean.parseBoolean(argument.getDefaultValue());
                } else
                {
                    LOGGER.error("Required argument {} not specified.", argument.getKey());
                    System.exit(1);
                    return false;
                }
            }
        } catch (NumberFormatException exception)
        {
            LOGGER.error("Unable to parse argument {}, please make sure that its a valid boolean.", argument.getKey());
            System.exit(1);
            return false;
        }
    }

    public static class ArgumentValue
    {
        private String key;
        private String value;

        public ArgumentValue(String key, String value)
        {
            this.key = key;
            this.value = value;
        }

        public String getKey()
        {
            return key;
        }

        public String getValue()
        {
            return value;
        }
    }
}