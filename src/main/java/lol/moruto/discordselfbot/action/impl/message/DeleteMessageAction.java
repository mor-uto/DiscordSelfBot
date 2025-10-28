package lol.moruto.discordselfbot.action.impl.message;

import lol.moruto.discordselfbot.GlobalConstants;
import lol.moruto.discordselfbot.action.AbstractRestAction;
import lol.moruto.discordselfbot.action.RestResponse;
import lol.moruto.discordselfbot.util.HttpClient;

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
