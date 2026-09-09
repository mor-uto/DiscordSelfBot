
# DiscordSelfBot
- I dont recommend using this as its WIP ⚠️

### Installation
- download it in ur project folder ig or smth

### Features
- listen for events ig? 

### Usage

```java
public class Main {
    public static final SelfBot selfBot = new SelfBot("TOKEN HERE :)");
    
    public static void main(String[] args) {
        selfBot.getEventDispatcher().addMessageListener(msg -> {
            System.out.println("New message received in channel: " + msg.getChannelId());
            System.out.println("Author ID: " + msg.getAuthor().getId());
            System.out.println("Content: " + msg.getContent());

            if (msg.getAuthor() != selfBot.getSelfMember()) return;

            if (msg.getContent().equals(">hi")) {
                msg.delete();
                executeHiCommand(msg.getChannelId());

                System.out.println("command executed.");
            }
        });

        selfBot.connect();
    }
    
    private static void executeHiCommand(String channelId) {
        SendMessageAction sendMessageAction = new SendMessageAction(channelId);
        sendMessageAction.execute("hello back!");
    }
}

```