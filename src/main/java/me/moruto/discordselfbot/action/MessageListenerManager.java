package me.moruto.discordselfbot.action;

import me.moruto.discordselfbot.event.impl.MessageListener;
import me.moruto.discordselfbot.object.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageListenerManager {
    private final List<MessageListener> listeners = new ArrayList<>();

    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MessageListener listener) {
        listeners.remove(listener);
    }

    public void trigger(Message message) {
        for (MessageListener l : listeners) {
            l.onMessage(message);
        }
    }
}
