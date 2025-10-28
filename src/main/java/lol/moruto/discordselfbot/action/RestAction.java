package lol.moruto.discordselfbot.action;

import lol.moruto.discordselfbot.SelfBot;

public interface RestAction {
    String TOKEN = SelfBot.getInstance().getToken();
    RestResponse execute(Object context) throws Exception;
}
