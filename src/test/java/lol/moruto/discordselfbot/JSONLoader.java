package lol.moruto.discordselfbot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JSONLoader {
    private final String json;
    private int position;

    private JSONLoader(String json) {
        this.json = json;
    }

    public static Object load(String file) throws IOException {
        String json = Files.readString(
                Path.of(file),
                StandardCharsets.UTF_8
        );

        return parse(json);
    }

    public static Object parse(String json) {
        if (json == null) {
            throw new IllegalArgumentException("JSON cannot be null");
        }

        JSONLoader parser = new JSONLoader(json);
        Object value = parser.parseValue();

        parser.skipWhitespace();

        if (!parser.isEnd()) {
            throw parser.error("Unexpected character '" + parser.peek() + "'");
        }

        return value;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadObject(String file) throws IOException {
        Object value = load(file);

        if (!(value instanceof Map)) {
            throw new IllegalArgumentException("JSON root is not an object");
        }

        return (Map<String, Object>) value;
    }

    @SuppressWarnings("unchecked")
    public static List<Object> loadArray(String file) throws IOException {
        Object value = load(file);

        if (!(value instanceof List)) {
            throw new IllegalArgumentException("JSON root is not an array");
        }

        return (List<Object>) value;
    }

    private Object parseValue() {
        skipWhitespace();

        if (isEnd()) {
            throw error("Unexpected end of JSON");
        }

        char c = peek();

        return switch (c) {
            case '{' -> parseObject();
            case '[' -> parseArray();
            case '"' -> parseString();
            case 't' -> parseLiteral("true", Boolean.TRUE);
            case 'f' -> parseLiteral("false", Boolean.FALSE);
            case 'n' -> parseLiteral("null", null);
            default -> {
                if (c == '-' || Character.isDigit(c)) {
                    yield parseNumber();
                }

                throw error("Unexpected character '" + c + "'");
            }
        };
    }

    private Map<String, Object> parseObject() {
        expect('{');

        Map<String, Object> object = new LinkedHashMap<>();

        skipWhitespace();

        if (consume('}')) {
            return object;
        }

        while (true) {
            skipWhitespace();

            if (peek() != '"') {
                throw error("Expected string key");
            }

            String key = parseString();

            skipWhitespace();
            expect(':');

            Object value = parseValue();
            object.put(key, value);

            skipWhitespace();

            if (consume('}')) {
                break;
            }

            expect(',');
        }

        return object;
    }

    private List<Object> parseArray() {
        expect('[');

        List<Object> array = new ArrayList<>();

        skipWhitespace();

        if (consume(']')) {
            return array;
        }

        while (true) {
            array.add(parseValue());

            skipWhitespace();

            if (consume(']')) {
                break;
            }

            expect(',');
        }

        return array;
    }

    private String parseString() {
        expect('"');

        StringBuilder result = new StringBuilder();

        while (!isEnd()) {
            char c = next();

            if (c == '"') {
                return result.toString();
            }

            if (c == '\\') {
                if (isEnd()) {
                    throw error("Unterminated escape sequence");
                }

                char escaped = next();

                switch (escaped) {
                    case '"' -> result.append('"');
                    case '\\' -> result.append('\\');
                    case '/' -> result.append('/');
                    case 'b' -> result.append('\b');
                    case 'f' -> result.append('\f');
                    case 'n' -> result.append('\n');
                    case 'r' -> result.append('\r');
                    case 't' -> result.append('\t');

                    case 'u' -> {
                        if (position + 4 > json.length()) {
                            throw error("Invalid Unicode escape");
                        }

                        String hex = json.substring(position, position + 4);

                        try {
                            result.append((char) Integer.parseInt(hex, 16));
                        } catch (NumberFormatException e) {
                            throw error("Invalid Unicode escape: \\u" + hex);
                        }

                        position += 4;
                    }

                    default -> throw error(
                            "Invalid escape sequence: \\" + escaped
                    );
                }
            } else {
                if (c < 0x20) {
                    throw error("Control character in string");
                }

                result.append(c);
            }
        }

        throw error("Unterminated string");
    }

    private Number parseNumber() {
        int start = position;

        if (consume('-')) {
            if (isEnd()) {
                throw error("Invalid number");
            }
        }

        if (consume('0')) {
            if (!isEnd() && Character.isDigit(peek())) {
                throw error("Leading zeroes are not allowed");
            }
        } else {
            if (isEnd() || !Character.isDigit(peek())) {
                throw error("Invalid number");
            }

            while (!isEnd() && Character.isDigit(peek())) {
                position++;
            }
        }

        boolean decimal = false;

        if (consume('.')) {
            decimal = true;

            if (isEnd() || !Character.isDigit(peek())) {
                throw error("Expected digit after decimal point");
            }

            while (!isEnd() && Character.isDigit(peek())) {
                position++;
            }
        }

        if (!isEnd() && (peek() == 'e' || peek() == 'E')) {
            decimal = true;
            position++;

            if (!isEnd() && (peek() == '+' || peek() == '-')) {
                position++;
            }

            if (isEnd() || !Character.isDigit(peek())) {
                throw error("Expected digit in exponent");
            }

            while (!isEnd() && Character.isDigit(peek())) {
                position++;
            }
        }

        String number = json.substring(start, position);

        try {
            if (decimal) {
                return Double.parseDouble(number);
            }

            try {
                return Long.parseLong(number);
            } catch (NumberFormatException ignored) {
                return Double.parseDouble(number);
            }

        } catch (NumberFormatException e) {
            throw error("Invalid number: " + number);
        }
    }

    private Object parseLiteral(String literal, Object value) {
        if (json.regionMatches(position, literal, 0, literal.length())) {
            position += literal.length();
            return value;
        }

        throw error("Expected '" + literal + "'");
    }

    private void skipWhitespace() {
        while (!isEnd()) {
            char c = peek();

            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                position++;
            } else {
                break;
            }
        }
    }

    private boolean consume(char expected) {
        if (!isEnd() && peek() == expected) {
            position++;
            return true;
        }

        return false;
    }

    private void expect(char expected) {
        if (!consume(expected)) {
            if (isEnd()) {
                throw error("Expected '" + expected + "', got end of JSON");
            }

            throw error("Expected '" + expected + "', got '" + peek() + "'");
        }
    }

    private char peek() {
        return json.charAt(position);
    }

    private char next() {
        return json.charAt(position++);
    }

    private boolean isEnd() {
        return position >= json.length();
    }

    private IllegalArgumentException error(String message) {
        return new IllegalArgumentException(message + " at position " + position);
    }
}