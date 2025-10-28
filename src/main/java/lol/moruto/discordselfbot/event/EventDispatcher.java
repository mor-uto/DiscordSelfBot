package lol.moruto.discordselfbot.event;

import lol.moruto.discordselfbot.event.impl.*;
import lol.moruto.discordselfbot.object.Member;
import lol.moruto.discordselfbot.object.Reaction;
import lol.moruto.discordselfbot.object.Message;

import java.util.ArrayList;
import java.util.List;

public class EventDispatcher {

    private final List<MessageListener> messageListeners = new ArrayList<>();
    private final List<ReactionListener> reactionListeners = new ArrayList<>();
    private final List<MemberListener> memberListeners = new ArrayList<>();

    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    public void addReactionListener(ReactionListener listener) {
        reactionListeners.add(listener);
    }

    public void addMemberListener(MemberListener listener) {
        memberListeners.add(listener);
    }

    public void dispatchMessage(Message msg) {
        for (MessageListener l : messageListeners) {
            l.onMessage(msg);
        }
    }

    public void dispatchReactionAdd(String channelId, String messageId, Reaction reaction, String userId) {
        for (ReactionListener l : reactionListeners) {
            l.onReactionAdd(channelId, messageId, reaction, userId);
        }
    }

    public void dispatchReactionRemove(String channelId, String messageId, Reaction reaction, String userId) {
        for (ReactionListener l : reactionListeners) {
            l.onReactionRemove(channelId, messageId, reaction, userId);
        }
    }

    public void dispatchMemberJoin(Member member) {
        for (MemberListener l : memberListeners) {
            l.onMemberJoin(member);
        }
    }

    public void dispatchMemberLeave(Member member) {
        for (MemberListener l : memberListeners) {
            l.onMemberLeave(member);
        }
    }
}
