package lol.moruto.discordselfbot.util;

import lol.moruto.discordselfbot.action.RestResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpClient {
    private static String readResponse(HttpURLConnection connection) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getResponseCode() < 400 ? connection.getInputStream() : connection.getErrorStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private static RestResponse handleResponse(HttpURLConnection connection) throws Exception {
        int status = connection.getResponseCode();
        String body = readResponse(connection);
        return new RestResponse(status, body);
    }

    private static void applyCommonHeaders(HttpURLConnection connection, String authToken) {
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        if (authToken != null && !authToken.isEmpty()) {
            connection.setRequestProperty("Authorization", authToken);
        }
    }

    public static RestResponse post(String url, String jsonPayload, String authToken) throws Exception {
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

        connection.setRequestMethod("POST");
        applyCommonHeaders(connection, authToken);
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        return handleResponse(connection);
    }

    public static RestResponse patch(String url, String jsonPayload, String authToken) throws Exception {
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

        connection.setRequestMethod("PATCH");
        applyCommonHeaders(connection, authToken);
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        return handleResponse(connection);
    }

    public static RestResponse get(String url, String authToken) throws Exception {
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

        connection.setRequestMethod("GET");
        applyCommonHeaders(connection, authToken);

        return handleResponse(connection);
    }

    public static RestResponse put(String url, String jsonPayload, String authToken) throws Exception {
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

        connection.setRequestMethod("PUT");
        applyCommonHeaders(connection, authToken);
        if (jsonPayload != null && !jsonPayload.isEmpty()) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }
        }

        return handleResponse(connection);
    }

    public static RestResponse delete(String url, String authToken) throws Exception {
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

        connection.setRequestMethod("DELETE");
        applyCommonHeaders(connection, authToken);

        return handleResponse(connection);
    }
}
