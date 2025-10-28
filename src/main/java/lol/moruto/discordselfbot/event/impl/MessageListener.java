package lol.moruto.discordselfbot.event.impl;

import lol.moruto.discordselfbot.event.EventListener;
import lol.moruto.discordselfbot.object.Message;

public interface MessageListener extends EventListener {
    void onMessage(Message message);
}