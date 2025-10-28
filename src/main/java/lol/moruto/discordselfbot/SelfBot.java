package lol.moruto.discordselfbot;

import lol.moruto.discordselfbot.event.EventDispatcher;
import lol.moruto.discordselfbot.object.Message;
import lol.moruto.discordselfbot.object.Reaction;
import lol.moruto.discordselfbot.object.Member;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;

public class SelfBot {
    private final String token;
    private static SelfBot instance;
    private WebSocket ws;
    private final EventDispatcher dispatcher = new EventDispatcher();
    private final CountDownLatch latch = new CountDownLatch(1);

    public SelfBot(String token) {
        instance = this;
        this.token = token;
    }

    public static SelfBot getInstance() { return instance; }
    public EventDispatcher getEventDispatcher() { return dispatcher; }

    public void connect() {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("wss://gateway.discord.gg/?v=10&encoding=json");

        ws = client.newWebSocketBuilder()
                .buildAsync(uri, new WebSocket.Listener() {

                    private final StringBuilder buffer = new StringBuilder();

                    @Override
                    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                        buffer.append(data);
                        if (last) {
                            handleMessage(buffer.toString());
                            buffer.setLength(0);
                        }
                        webSocket.request(1);
                        return null;
                    }

                    @Override
                    public void onOpen(WebSocket webSocket) {
                        System.out.println("Connected to Discord Gateway!");
                        sendIdentify(webSocket);
                        webSocket.request(1);
                    }

                    @Override
                    public void onError(WebSocket webSocket, Throwable error) {
                        System.err.println("WebSocket error: " + error.getMessage());
                        error.printStackTrace();
                    }

                    @Override
                    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                        System.out.println("WebSocket closed: " + statusCode + " - " + reason);
                        return null;
                    }
                }).join();

        try {
            latch.await();
        } catch (InterruptedException e) {
            System.err.println("Bot interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void sendIdentify(WebSocket webSocket) {
        String identify = new JSONObject()
                .put("op", 2)
                .put("d", new JSONObject()
                        .put("token", token)
                        .put("intents", 513)
                        .put("properties", new JSONObject()
                                .put("$os", "windows")
                                .put("$browser", "myself")
                                .put("$device", "myself")))
                .toString();

        webSocket.sendText(identify, true);
    }

    private void handleMessage(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String type = obj.optString("t");
            JSONObject data = obj.optJSONObject("d");

            if (type == null || data == null) return;

            switch (type) {
                case "MESSAGE_CREATE" -> {
                    String id = data.getString("id");
                    String channelId = data.getString("channel_id");
                    String content = data.optString("content", "");
                    JSONObject authorObj = data.getJSONObject("author");
                    String authorId = authorObj.getString("id");

                    Message msg = Message.create(id, channelId, content, authorId, new ArrayList<>());
                    dispatcher.dispatchMessage(msg);
                }
                case "MESSAGE_REACTION_ADD" -> {
                    JSONObject emojiObj = data.getJSONObject("emoji");
                    String emojiName = emojiObj.getString("name");
                    String emojiId = emojiObj.optString("id", null);
                    boolean animated = emojiObj.optBoolean("animated", false);

                    Reaction reaction = new Reaction(emojiName, emojiId, animated);
                    String channelId = data.getString("channel_id");
                    String messageId = data.getString("message_id");
                    String userId = data.getString("user_id");

                    dispatcher.dispatchReactionAdd(channelId, messageId, reaction, userId);
                }

                case "MESSAGE_REACTION_REMOVE" -> {
                    JSONObject emojiObj = data.getJSONObject("emoji");
                    String emojiName = emojiObj.getString("name");
                    String emojiId = emojiObj.optString("id", null);
                    boolean animated = emojiObj.optBoolean("animated", false);

                    Reaction reaction = new Reaction(emojiName, emojiId, animated);
                    String channelId = data.getString("channel_id");
                    String messageId = data.getString("message_id");
                    String userId = data.getString("user_id");

                    dispatcher.dispatchReactionRemove(channelId, messageId, reaction, userId);
                }
                case "GUILD_MEMBER_ADD" -> {
                    JSONObject userObj = data.getJSONObject("user");
                    String userId = userObj.getString("id");
                    String username = userObj.optString("username", "unknown");
                    String discriminator = userObj.optString("discriminator", "0000");
                    String avatarUrl = userObj.optString("avatar", null);

                    Member member = new Member(userId, username, discriminator, avatarUrl);
                    dispatcher.dispatchMemberJoin(member);
                }
                case "GUILD_MEMBER_REMOVE" -> {
                    JSONObject userObj = data.getJSONObject("user");
                    String userId = userObj.getString("id");
                    String username = userObj.optString("username", "unknown");
                    String discriminator = userObj.optString("discriminator", "0000");
                    String avatarUrl = userObj.optString("avatar", null);

                    Member member = new Member(userId, username, discriminator, avatarUrl);
                    dispatcher.dispatchMemberLeave(member);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (ws != null) {
            ws.sendClose(WebSocket.NORMAL_CLOSURE, "Shutdown").thenRun(latch::countDown);
        } else {
            latch.countDown();
        }
    }

    public String getToken() { return token; }
}
