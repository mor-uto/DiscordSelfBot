package lol.moruto.discordselfbot;

import lol.moruto.discordselfbot.event.EventDispatcher;
import lol.moruto.discordselfbot.object.Message;
import lol.moruto.discordselfbot.object.Reaction;
import lol.moruto.discordselfbot.object.Member;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

public class SelfBot {
    private final String token;
    private static SelfBot instance;
    private WebSocket ws;
    private final EventDispatcher dispatcher = new EventDispatcher();
    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile Member selfMember;

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
        JSONObject identify = new JSONObject()
                .put("op", 2)
                .put("d", new JSONObject()
                        .put("token", token)
                        .put("intents", 262143)
                        .put("properties", new JSONObject()
                                .put("$os", "windows")
                                .put("$browser", "myself")
                                .put("$device", "myself")));

        webSocket.sendText(identify.toString(), true);
    }

    private void handleMessage(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String type = obj.optString("t");
            JSONObject data = obj.optJSONObject("d");

            if ("READY".equals(type) && data != null) {
                JSONObject userObj = data.optJSONObject("user");
                if (userObj != null) {
                    selfMember = new Member(
                            userObj.optString("id", "0"),
                            userObj.optString("username", "unknown"),
                            userObj.optString("discriminator", "0000"),
                            userObj.optString("avatar", null)
                    );
                }
            }


            if (type == null || data == null) return;

            switch (type) {
                case "MESSAGE_CREATE" -> {
                    String id = data.getString("id");
                    String channelId = data.getString("channel_id");
                    String content = data.optString("content", null);

                    if (content == null || content.isEmpty()) {
                        JSONArray embeds = data.optJSONArray("embeds");
                        JSONArray attachments = data.optJSONArray("attachments");
                        if (embeds != null && !embeds.isEmpty()) {
                            content = "[embed]";
                        } else if (attachments != null && !attachments.isEmpty()) {
                            content = "[attachment]";
                        } else {
                            content = "[unknown or empty message]";
                        }
                    }

                    JSONObject authorObj = data.getJSONObject("author");
                    Member author = new Member(
                            authorObj.optString("id", "0"),
                            authorObj.optString("username", "unknown"),
                            authorObj.optString("discriminator", "0000"),
                            authorObj.optString("avatar", null)
                    );

                    Message msg = Message.create(id, channelId, content, author, new ArrayList<>());
                    dispatcher.dispatchMessage(msg);
                }
                case "MESSAGE_REACTION_ADD" -> {
                    JSONObject emojiObj = data.getJSONObject("emoji");
                    Reaction reaction = new Reaction(
                            data.getString("message_id"),
                            data.getString("channel_id"),
                            emojiObj.getString("name"),
                            emojiObj.optString("id", null),
                            emojiObj.optBoolean("animated", false)
                    );
                    dispatcher.dispatchReactionAdd(reaction, data.getString("user_id"));
                }
                case "MESSAGE_REACTION_REMOVE" -> {
                    JSONObject emojiObj = data.getJSONObject("emoji");
                    Reaction reaction = new Reaction(
                            data.getString("message_id"),
                            data.getString("channel_id"),
                            emojiObj.getString("name"),
                            emojiObj.optString("id", null),
                            emojiObj.optBoolean("animated", false)
                    );
                    dispatcher.dispatchReactionRemove(reaction, data.getString("user_id"));
                }
                case "GUILD_MEMBER_ADD" -> {
                    JSONObject userObj = data.getJSONObject("user");
                    Member member = new Member(
                            userObj.getString("id"),
                            userObj.optString("username", "unknown"),
                            userObj.optString("discriminator", "0000"),
                            userObj.optString("avatar", null)
                    );
                    dispatcher.dispatchMemberJoin(member);
                }
                case "GUILD_MEMBER_REMOVE" -> {
                    JSONObject userObj = data.getJSONObject("user");
                    Member member = new Member(
                            userObj.getString("id"),
                            userObj.optString("username", "unknown"),
                            userObj.optString("discriminator", "0000"),
                            userObj.optString("avatar", null)
                    );
                    dispatcher.dispatchMemberLeave(member);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (ws != null) ws.sendClose(WebSocket.NORMAL_CLOSURE, "Shutdown").thenRun(latch::countDown);
        else latch.countDown();
    }

    public String getToken() { return token; }

    public Member getSelfMember() {
        return selfMember;
    }
}
