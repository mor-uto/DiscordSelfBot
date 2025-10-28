package lol.moruto.discordselfbot.action.impl.message;

import lol.moruto.discordselfbot.GlobalConstants;
import lol.moruto.discordselfbot.action.AbstractRestAction;
import lol.moruto.discordselfbot.action.RestResponse;
import lol.moruto.discordselfbot.util.HttpClient;

public class EditMessageAction extends AbstractRestAction {
    private final String channelId;

    public EditMessageAction(String channelId) {
        this.channelId = channelId;
    }

    @Override
    protected RestResponse perform(Object context) throws Exception {
        Object[] arr = (Object[]) context;
        String newContent = (String) arr[0];
        String messageId = (String) arr[1];

        String url = GlobalConstants.URL_BASE + "/channels/" + channelId + "/messages/" + messageId;
        String payload = "{\"content\": \"" + newContent + "\"}";

        return HttpClient.patch(url, payload, TOKEN);
    }
}
