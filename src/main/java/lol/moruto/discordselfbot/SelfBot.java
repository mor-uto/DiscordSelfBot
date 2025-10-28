package lol.moruto.discordselfbot;

public class SelfBot {
    private final String token;

    private static SelfBot instance;

    public SelfBot(String token) {
        instance = this;
        this.token = token;
    }

    public static SelfBot getInstance() {
        return instance;
    }

    public String getToken() {
        return token;
    }
}
