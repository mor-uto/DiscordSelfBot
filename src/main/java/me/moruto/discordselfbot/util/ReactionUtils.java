package me.moruto.discordselfbot.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class ReactionUtils {
    private ReactionUtils() {}

    public static String encodeEmoji(String emoji) {
        if (emoji == null || emoji.isEmpty()) return "";

        if (emoji.matches(".+:[0-9]+$")) {
            return emoji.replace(":", "%3A");
        }

        return URLEncoder.encode(emoji, StandardCharsets.UTF_8);
    }
}
