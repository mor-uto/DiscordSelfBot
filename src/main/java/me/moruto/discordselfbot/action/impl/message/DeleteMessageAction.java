package me.moruto.discordselfbot.action.impl.message;

import me.moruto.discordselfbot.GlobalConstants;
import me.moruto.discordselfbot.action.AbstractRestAction;
import me.moruto.discordselfbot.action.RestResponse;
import me.moruto.discordselfbot.util.HttpClient;

public class DeleteMessageAction extends AbstractRestAction {
    private final String channelId;

    public DeleteMessageAction(String channelId) {
        this.channelId = channelId;
    }

    @Override
    protected RestResponse perform(Object context) throws Exception {
        return HttpClient.delete(GlobalConstants.URL_BASE + "/channels/" + channelId + "/messages/" + context, TOKEN);
    }
}
