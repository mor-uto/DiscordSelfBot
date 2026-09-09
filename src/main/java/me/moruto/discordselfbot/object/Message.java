package me.moruto.discordselfbot.object;

import me.moruto.discordselfbot.action.RestResponse;
import me.moruto.discordselfbot.action.impl.message.DeleteMessageAction;
import me.moruto.discordselfbot.action.impl.message.EditMessageAction;
import lol.moruto.discordselfbot.action.impl.message.reaction.*;
import me.moruto.discordselfbot.action.impl.message.reaction.AddReactionAction;
import me.moruto.discordselfbot.action.impl.message.reaction.GetReactionsAction;
import me.moruto.discordselfbot.action.impl.message.reaction.RemoveOwnReactionAction;
import me.moruto.discordselfbot.action.impl.message.reaction.RemoveUserReactionAction;
import me.moruto.discordselfbot.other.ReactionContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Message {
    private final String id;
    private final String channelId;
    private String content;
    private final Member author;
    private final List<String> reactions;

    Message(String id, String channelId, String content, Member author, List<String> reactions) {
        this.id = id;
        this.channelId = channelId;
        this.content = content;
        this.author = author;
        this.reactions = new ArrayList<>(reactions != null ? reactions : Collections.emptyList());
    }

    public static Message create(String id, String channelId, String content, Member author, List<String> reactions) {
        return new Message(id, channelId, content, author, reactions);
    }

    public String getId() {
        return id;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getContent() {
        return content;
    }

    public Member getAuthor() {
        return author;
    }

    public List<String> getReactions() {
        return Collections.unmodifiableList(reactions);
    }

    public RestResponse delete() {
        return new DeleteMessageAction(channelId).execute(id);
    }

    public RestResponse edit(String newContent) {
        EditMessageAction action = new EditMessageAction(channelId);
        RestResponse response = action.execute(new Object[]{newContent, id});
        if (response.isSuccess()) this.content = newContent;
        return response;
    }

    public RestResponse addReaction(String emoji) {
        AddReactionAction action = new AddReactionAction(channelId);
        RestResponse response = action.execute(new ReactionContext(id, emoji));
        if (response.isSuccess() && !reactions.contains(emoji)) reactions.add(emoji);
        return response;
    }

    public RestResponse removeReaction(String emoji) {
        RemoveOwnReactionAction action = new RemoveOwnReactionAction(channelId);
        RestResponse response = action.execute(new ReactionContext(id, emoji));
        if (response.isSuccess()) reactions.remove(emoji);
        return response;
    }

    public RestResponse removeUserReaction(String emoji, String userId) {
        RemoveUserReactionAction action = new RemoveUserReactionAction(channelId);
        RestResponse response = action.execute(new ReactionContext(id, emoji, userId));
        if (response.isSuccess()) reactions.remove(emoji);
        return response;
    }

    public RestResponse getReactions(String emoji) {
        GetReactionsAction action = new GetReactionsAction(channelId);
        return action.execute(new ReactionContext(id, emoji));
    }
}
