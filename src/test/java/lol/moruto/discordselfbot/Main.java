package lol.moruto.discordselfbot;

public class Main {
    public static void main(String[] args) {
        SelfBot selfBot = new SelfBot("MTI3NjYwNTYzMzU2MjY3NzM0MQ.Gn5udE.Dt3lqbRHtZw4po18jFg5sKeto8b46lg8cXGHMc");

        selfBot.getEventDispatcher().addMessageListener(msg -> {
            System.out.println("New message received in channel: " + msg.getChannelId());
            System.out.println("Author ID: " + msg.getAuthorId());
            System.out.println("Content: " + msg.getContent());
        });

        selfBot.connect();
    }
}
