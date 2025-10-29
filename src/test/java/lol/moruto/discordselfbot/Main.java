package lol.moruto.discordselfbot;

import lol.moruto.discordselfbot.action.impl.message.SendMessageAction;

public class Main {
    public static final SelfBot selfBot = new SelfBot("MTI3NjYwNTYzMzU2MjY3NzM0MQ.GFQ-mQ.vVDPMRkFkLvHaZKktfp7UH_7Oe-Xmrf72CZLYw");

    public static void main(String[] args) {
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
