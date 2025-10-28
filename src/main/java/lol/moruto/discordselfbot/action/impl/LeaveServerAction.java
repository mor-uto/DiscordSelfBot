package lol.moruto.discordselfbot.action.impl;

import lol.moruto.discordselfbot.GlobalConstants;
import lol.moruto.discordselfbot.action.AbstractRestAction;
import lol.moruto.discordselfbot.action.RestResponse;
import lol.moruto.discordselfbot.util.HttpClient;

public class LeaveServerAction extends AbstractRestAction {
    @Override
    protected RestResponse perform(Object context) throws Exception {
        return HttpClient.delete(GlobalConstants.URL_BASE + "/users/@me/guilds/" + context, TOKEN);
    }
}
