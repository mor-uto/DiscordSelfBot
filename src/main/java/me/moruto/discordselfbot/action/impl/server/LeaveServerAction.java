package me.moruto.discordselfbot.action.impl.server;

import me.moruto.discordselfbot.GlobalConstants;
import me.moruto.discordselfbot.action.AbstractRestAction;
import me.moruto.discordselfbot.action.RestResponse;
import me.moruto.discordselfbot.util.HttpClient;

public class LeaveServerAction extends AbstractRestAction {
    @Override
    protected RestResponse perform(Object context) throws Exception {
        return HttpClient.delete(GlobalConstants.URL_BASE + "/users/@me/guilds/" + context, TOKEN);
    }
}
