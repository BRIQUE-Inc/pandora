package org.chronotics.pandora.log;

public class LogbackFactory implements ILoggerFactory {
    @Override
    public Logger createLogger(Class<?> clazz) {
        return new LogbackLogger(clazz);
    }

    @Override
    public String toString() {
        return "Logback";
    }
}
