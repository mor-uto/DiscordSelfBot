package me.moruto.discordselfbot.event.impl;

import me.moruto.discordselfbot.event.EventListener;
import me.moruto.discordselfbot.object.Message;

public interface MessageListener extends EventListener {
    void onMessage(Message message);
}