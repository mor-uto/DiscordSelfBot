package lol.moruto.discordselfbot.action;

public class RestResponse {
    private final int statusCode;
    private final String body;
    private final boolean success;

    public RestResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
        this.success = statusCode >= 200 && statusCode < 300;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "[HTTP " + statusCode + "] " + (success ? "✅ Success" : "❌ Error") + " → " + body;
    }
}
