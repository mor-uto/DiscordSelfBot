package lol.moruto.discordselfbot.object;

public final class Reaction {
    private final String emojiName;
    private final String emojiId;
    private final boolean animated;

    public Reaction(String emojiName, String emojiId, boolean animated) {
        this.emojiName = emojiName;
        this.emojiId = emojiId;
        this.animated = animated;
    }

    public String getEmojiName() {
        return emojiName;
    }

    public String getEmojiId() {
        return emojiId;
    }

    public boolean isAnimated() {
        return animated;
    }

    @Override
    public String toString() {
        return (animated ? "<a:" : "<:") + emojiName + (emojiId != null ? ":" + emojiId : "") + ">";
    }
}
