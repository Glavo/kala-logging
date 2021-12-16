package kala.logging;

import java.util.Arrays;

// @ValueBased
public final class MessageFormatter {
    private final String format;
    private final long[] markers;

    private MessageFormatter(String format, long[] markers) {
        this.format = format;
        this.markers = markers;
    }

    public static MessageFormatter parse(String format) {
        final int formatLength = format.length();
        final StringBuilder builder = new StringBuilder();
        final LongArrayBuilder markersBuilder = new LongArrayBuilder();

        int count = 0;

        for (int i = 0; i < formatLength; i++) {
            final char ch = format.charAt(i);
            if (ch == '{' && i + 1 < formatLength) {
                if (format.charAt(i + 1) == '}') {
                    markersBuilder.append(combination(count++, builder.length()));
                    builder.append('\0');
                    i++;
                    continue;
                }

                int end = format.indexOf('}', i + 2);
                if (end < 0) {
                    builder.append('{');
                    continue;
                }

                int n = parseInt(format, i + 1, end);
                if (n < 0) {
                    builder.append('{');
                    continue;
                }
                markersBuilder.append(combination(n, builder.length()));
                builder.append('\0');
                i = end;
                continue;
            } else if (ch == '\\') {
                if (i + 1 < formatLength) {
                    final char ch1 = format.charAt(i + 1);
                    if (ch1 == '{') {
                        builder.append('{');
                        i++;
                        continue;
                    } else if (ch1 == '\\' && i + 2 < formatLength && format.charAt(i + 2) == '{') {
                        builder.append('\\');
                        i++;
                        continue;
                    }
                }
            }

            builder.append(ch);
        }
        return new MessageFormatter(builder.toString(), markersBuilder.toArray());
    }

    public String format(Object... args) {
        return format(new StringBuilder(), args).toString();
    }

    public StringBuilder format(StringBuilder res, Object... args) {
        int markersLength = markers.length;
        if (markersLength == 0) {
            res.append(format);
            return res;
        }

        int previousOffset = getOffset(markers[0]);

        res.append(format, 0, previousOffset);
        append(res, args, getNumber(markers[0]));

        for (int i = 1; i < markersLength; i++) {
            long marker = markers[i];
            int number = getNumber(marker);
            int offset = getOffset(marker);

            res.append(format, previousOffset + 1, offset);
            append(res, args, number);

            previousOffset = offset;
        }

        res.append(format, previousOffset + 1, format.length());
        return res;
    }

    public String toDebugString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MessageFormatter[\"");
        escapes(builder, format, 0, format.length());
        builder.append('"');
        if (markers.length != 0) {
            builder.append(", [");
            builder.append("(").append(getNumber(markers[0])).append(", ").append(getOffset(markers[0])).append(')');
            for (int i = 1; i < markers.length; i++) {
                long marker = markers[i];
                builder.append(", ");
                builder.append("(").append(getNumber(marker)).append(", ").append(getOffset(marker)).append(')');
            }
            builder.append(']');
        }

        builder.append(']');
        return builder.toString();
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("MessageFormatter[");
        if (markers.length == 0) {
            builder.append('"');
            escapes(builder, format, 0, format.length());
            builder.append('"').append(']');
            return builder.toString();
        }

        int previousOffset = getOffset(markers[0]);

        if (previousOffset != 0) {
            builder.append('"');
            escapes(builder, format, 0, previousOffset);
            builder.append('"');
            builder.append(", ");
        }

        builder.append('{').append(getNumber(markers[0])).append('}');

        for (int i = 1; i < markers.length; i++) {
            long marker = markers[i];
            int number = getNumber(marker);
            int offset = getOffset(marker);

            builder.append(", ");

            if (previousOffset + 1 < offset) {
                builder.append('"');
                escapes(builder, format, previousOffset + 1, offset);
                builder.append('"');
                builder.append(", ");
            }

            builder.append('{').append(number).append('}');
            previousOffset = offset;
        }

        if (previousOffset + 1 < format.length()) {
            builder.append(", ");
            builder.append('"');
            escapes(builder, format, previousOffset + 1, format.length());
            builder.append('"');
        }
        builder.append(']');
        return builder.toString();
    }

    private static int parseInt(String source, int beginIndex, int endIndex) {
        int res = 0;
        for (int i = beginIndex; i < endIndex; i++) {
            int n = source.charAt(i) - '0';
            if (n < 0 || n > 9) {
                return -1;
            }

            res *= 10;
            res += n;
        }
        return res;
    }

    private static long combination(int number, int offset) {
        return (((long) number) << 32) | (offset & 0xffffffffL);
    }

    private static int getNumber(long combined) {
        return (int) (combined >> 32);
    }

    private static int getOffset(long combined) {
        return (int) combined;
    }

    private static void escapes(StringBuilder builder, String value, int beginIndex, int endIndex) {
        for (int i = beginIndex; i < endIndex; i++) {
            char ch = value.charAt(i);
            switch (ch) {
                case '\0':
                    builder.append("\\0");
                    break;
                case '\b':
                    builder.append("\\b");
                    break;
                case '\f':
                    builder.append("\\f");
                    break;
                case '\n':
                    builder.append("\\n");
                    break;
                case '\r':
                    builder.append("\\r");
                    break;
                case '\t':
                    builder.append("\\t");
                    break;
                case '"':
                    builder.append("\\\"");
                    break;
                case '\\':
                    builder.append("\\\\");
                    break;
                default:
                    if ((ch >= 32 && ch <= 126) || Character.isJavaIdentifierPart(ch)) {
                        builder.append(ch);
                    } else {
                        builder.append("\\u").append(String.format("%04x", (int) ch));
                    }
                    break;
            }
        }
    }

    private static void append(StringBuilder builder, Object[] array, int index) {
        if (index >= array.length) {
            builder.append('{').append(index).append('}');
        } else {
            builder.append(array[index]);
        }
    }

    private static final class LongArrayBuilder {
        private static final long[] EMPTY_ARRAY = new long[0];
        private static final int DEFAULT_CAPACITY = 16;

        private long[] elements = EMPTY_ARRAY;
        private int size = 0;

        private void grow() {
            int oldCapacity = elements.length;

            long[] newArray;
            if (oldCapacity == 0) {
                newArray = new long[Math.max(DEFAULT_CAPACITY, size + 1)];
            } else {
                int newCapacity = Math.max(Math.max(oldCapacity, size + 1), oldCapacity + (oldCapacity >> 1));
                newArray = new long[newCapacity];
            }

            if (elements.length != 0) {
                System.arraycopy(elements, 0, newArray, 0, size);
            }
            elements = newArray;
        }

        public void append(long value) {
            if (size == elements.length) {
                grow();
            }
            elements[size++] = value;
        }

        public long[] toArray() {
            return elements.length == size ? elements : Arrays.copyOf(elements, size);
        }
    }

}
