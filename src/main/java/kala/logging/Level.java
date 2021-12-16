package kala.logging;

import java.io.Serializable;

public final class Level implements Comparable<Level>, Serializable {
    public static final int ALL_SEVERITY = Integer.MIN_VALUE;
    public static final int TRACE_SEVERITY = 400;
    public static final int DEBUG_SEVERITY = 500;
    public static final int INFO_SEVERITY = 800;
    public static final int WARNING_SEVERITY = 900;
    public static final int ERROR_SEVERITY = 1000;
    public static final int OFF_SEVERITY = Integer.MAX_VALUE;

    public static final Level ALL = new Level("ALL", ALL_SEVERITY);
    public static final Level TRACE = new Level("TRACE", TRACE_SEVERITY);
    public static final Level DEBUG = new Level("DEBUG", DEBUG_SEVERITY);
    public static final Level INFO = new Level("INFO", INFO_SEVERITY);
    public static final Level WARNING = new Level("WARNING", WARNING_SEVERITY);
    public static final Level ERROR = new Level("ERROR", ERROR_SEVERITY);
    public static final Level OFF = new Level("OFF", OFF_SEVERITY);

    private final String name;
    private final int severity;

    private Level(String name, int severity) {
        this.name = name;
        this.severity = severity;
    }

    public String name() {
        return name;
    }

    public int severity() {
        return severity;
    }

    public static Level forName(String name) {
        return forName(name, null);
    }

    public static Level forName(String name, Level defaultLevel) {
        if ("ALL".equalsIgnoreCase(name)) {
            return ALL;
        }
        if ("TRACE".equalsIgnoreCase(name)) {
            return TRACE;
        }
        if ("DEBUG".equalsIgnoreCase(name)) {
            return DEBUG;
        }
        if ("INFO".equalsIgnoreCase(name)) {
            return INFO;
        }
        if ("WARNING".equalsIgnoreCase(name)) {
            return WARNING;
        }
        if ("ERROR".equalsIgnoreCase(name)) {
            return ERROR;
        }
        if ("OFF".equalsIgnoreCase(name)) {
            return OFF;
        }
        return defaultLevel;
    }

    public static Level forSeverity(int severity) {
        return forSeverity(severity, null);
    }

    public static Level forSeverity(int severity, Level defaultLevel) {
        switch (severity) {
            case ALL_SEVERITY:
                return ALL;
            case TRACE_SEVERITY:
                return TRACE;
            case DEBUG_SEVERITY:
                return DEBUG;
            case INFO_SEVERITY:
                return INFO;
            case WARNING_SEVERITY:
                return WARNING;
            case ERROR_SEVERITY:
                return ERROR;
            case OFF_SEVERITY:
                return OFF;
            default:
                return defaultLevel;
        }
    }

    @Override
    public int compareTo(Level other) {
        return Integer.compare(this.severity, other.severity);
    }

    @Override
    public String toString() {
        return "Level." + name;
    }

    private Object readResolve() {
        return forSeverity(severity);
    }
}
