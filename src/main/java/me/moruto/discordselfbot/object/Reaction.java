package me.moruto.discordselfbot.object;

public record Reaction(String messageId, String channelId, String emojiName, String emojiId, boolean animated) {

    @Override
    public String toString() {
        return (animated ? "<a:" : "<:") + emojiName + (emojiId != null ? ":" + emojiId : "") + ">";
    }
}
