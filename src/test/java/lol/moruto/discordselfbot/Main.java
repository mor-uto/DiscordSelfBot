package lol.moruto.discordselfbot;

import java.io.IOException;

public class Main {
    public static SelfBot selfBot;

    public static void main(String[] args) throws IOException {
        selfBot = new SelfBot((String) JSONLoader.loadObject("data.json").get("token"));

        selfBot.getEventDispatcher().addMessageListener(msg -> {
            System.out.println("New message received in channel: " + msg.getChannelId());
            System.out.println("Author ID: " + msg.getAuthor().getId());
            System.out.println("Content: " + msg.getContent());

            if (msg.getAuthor() == selfBot.getSelfMember()) return;

            if (msg.getContent().equals(">hi")) {
                System.out.println("YES");
                msg.delete();
            }
        });

        selfBot.connect();
    }
}
