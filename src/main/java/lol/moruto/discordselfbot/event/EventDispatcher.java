package lol.moruto.discordselfbot.event;

import lol.moruto.discordselfbot.event.impl.MemberListener;
import lol.moruto.discordselfbot.event.impl.MessageListener;
import lol.moruto.discordselfbot.event.impl.ReactionListener;
import lol.moruto.discordselfbot.object.Member;
import lol.moruto.discordselfbot.object.Message;
import lol.moruto.discordselfbot.object.Reaction;

import java.util.ArrayList;
import java.util.List;

public class EventDispatcher {
    private final List<MessageListener> messageListeners = new ArrayList<>();
    private final List<ReactionListener> reactionListeners = new ArrayList<>();
    private final List<MemberListener> memberListeners = new ArrayList<>();

    public void addMessageListener(MessageListener l) { messageListeners.add(l); }
    public void addReactionListener(ReactionListener l) { reactionListeners.add(l); }
    public void addMemberListener(MemberListener l) { memberListeners.add(l); }

    public void dispatchMessage(Message msg) {
        for (MessageListener l : messageListeners) l.onMessage(msg);
    }

    public void dispatchReactionAdd(Reaction reaction, String userId) {
        for (ReactionListener l : reactionListeners) l.onReactionAdd(reaction, userId);
    }

    public void dispatchReactionRemove(Reaction reaction, String userId) {
        for (ReactionListener l : reactionListeners) l.onReactionRemove(reaction, userId);
    }

    public void dispatchMemberJoin(Member member) {
        for (MemberListener l : memberListeners) l.onMemberJoin(member);
    }

    public void dispatchMemberLeave(Member member) {
        for (MemberListener l : memberListeners) l.onMemberLeave(member);
    }
}
