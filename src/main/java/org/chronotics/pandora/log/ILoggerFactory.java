package org.chronotics.pandora.log;

public interface ILoggerFactory {
    Logger createLogger(Class<?> classz);
}
