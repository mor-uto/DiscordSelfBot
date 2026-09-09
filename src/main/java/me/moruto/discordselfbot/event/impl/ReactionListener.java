package me.moruto.discordselfbot.event.impl;

import me.moruto.discordselfbot.object.Reaction;

public interface ReactionListener {
    void onReactionAdd(Reaction reaction, String userId);
    void onReactionRemove(Reaction reaction, String userId);
}
