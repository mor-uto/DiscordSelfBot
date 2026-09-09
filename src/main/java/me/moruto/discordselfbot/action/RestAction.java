package me.moruto.discordselfbot.action;

import me.moruto.discordselfbot.SelfBot;

public interface RestAction {
    String TOKEN = SelfBot.getInstance().getToken();
    RestResponse execute(Object context) throws Exception;
}
