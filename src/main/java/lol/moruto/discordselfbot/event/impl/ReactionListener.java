package lol.moruto.discordselfbot.event.impl;

import lol.moruto.discordselfbot.object.Reaction;

public interface ReactionListener {
    void onReactionAdd(String channelId, String messageId, Reaction reaction, String userId);
    void onReactionRemove(String channelId, String messageId, Reaction reaction, String userId);
}
