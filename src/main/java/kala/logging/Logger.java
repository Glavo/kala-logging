package kala.logging;

import java.util.function.Supplier;

public abstract class Logger {
    private int levelSeverity;

    public abstract String getName();

    public boolean isTraceEnabled() {
        return levelSeverity <= Level.TRACE_SEVERITY;
    }

    public abstract void trace(String msg);

    public abstract void trace(String format, Object arg0);

    public abstract void trace(String format, Object arg0, Object arg1);

    public abstract void trace(String format, Object... args);

    public abstract void trace(String format, Supplier<?> arg0);

    public abstract void trace(String format, Supplier<?> arg0, Supplier<?> arg1);

    public abstract void trace(String format, Supplier<?>... args);

    public abstract void trace(Supplier<String> msg);

    public abstract void trace(String msg, Throwable ex);

    public abstract void trace(Supplier<String> msg, Throwable ex);

    public boolean isDebugEnabled() {
        return levelSeverity <= Level.DEBUG_SEVERITY;
    }

    public abstract void debug(String msg);

    public abstract void debug(String format, Object arg0);

    public abstract void debug(String format, Object arg0, Object arg1);

    public abstract void debug(String format, Object... args);

    public abstract void debug(String format, Supplier<?> arg0);

    public abstract void debug(String format, Supplier<?> arg0, Supplier<?> arg1);

    public abstract void debug(String format, Supplier<?>... args);

    public abstract void debug(Supplier<String> msg);

    public abstract void debug(String msg, Throwable ex);

    public abstract void debug(Supplier<String> msg, Throwable ex);

    public boolean isInfoEnabled() {
        return levelSeverity <= Level.INFO_SEVERITY;
    }

    public abstract void info(String msg);

    public abstract void info(String format, Object arg0);

    public abstract void info(String format, Object arg0, Object arg1);

    public abstract void info(String format, Object... args);

    public abstract void info(String format, Supplier<?> arg0);

    public abstract void info(String format, Supplier<?> arg0, Supplier<?> arg1);

    public abstract void info(String format, Supplier<?>... args);

    public abstract void info(Supplier<String> msg);

    public abstract void info(String msg, Throwable ex);

    public abstract void info(Supplier<String> msg, Throwable ex);

    public boolean isWarnEnabled() {
        return levelSeverity <= Level.WARN_SEVERITY;
    }

    public abstract void warn(String msg);

    public abstract void warn(String format, Object arg0);

    public abstract void warn(String format, Object arg0, Object arg1);

    public abstract void warn(String format, Object... args);

    public abstract void warn(String format, Supplier<?> arg0);

    public abstract void warn(String format, Supplier<?> arg0, Supplier<?> arg1);

    public abstract void warn(String format, Supplier<?>... args);

    public abstract void warn(Supplier<String> msg);

    public abstract void warn(String msg, Throwable ex);

    public abstract void warn(Supplier<String> msg, Throwable ex);

    public boolean isErrorEnabled() {
        return levelSeverity <= Level.ERROR_SEVERITY;
    }

    public abstract void error(String msg);

    public abstract void error(String format, Object arg0);

    public abstract void error(String format, Object arg0, Object arg1);

    public abstract void error(String format, Object... args);

    public abstract void error(String format, Supplier<?> arg0);

    public abstract void error(String format, Supplier<?> arg0, Supplier<?> arg1);

    public abstract void error(String format, Supplier<?>... args);

    public abstract void error(Supplier<String> msg);

    public abstract void error(String msg, Throwable ex);

    public abstract void error(Supplier<String> msg, Throwable ex);
}
