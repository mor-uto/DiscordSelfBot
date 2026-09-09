package me.moruto.discordselfbot.other;

public class ReactionContext {
    private final String messageId;
    private final String emoji;
    private final String userId;

    public ReactionContext(String messageId, String emoji) {
        this(messageId, emoji, null);
    }

    public ReactionContext(String messageId, String emoji, String userId) {
        this.messageId = messageId;
        this.emoji = emoji;
        this.userId = userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getUserId() {
        return userId;
    }
}
