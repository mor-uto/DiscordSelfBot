package me.moruto.discordselfbot.action.impl.message.reaction;

import me.moruto.discordselfbot.GlobalConstants;
import me.moruto.discordselfbot.action.AbstractRestAction;
import me.moruto.discordselfbot.action.RestResponse;
import me.moruto.discordselfbot.other.ReactionContext;
import me.moruto.discordselfbot.util.HttpClient;
import me.moruto.discordselfbot.util.ReactionUtils;

public class RemoveUserReactionAction extends AbstractRestAction {
    private final String channelId;

    public RemoveUserReactionAction(String channelId) {
        this.channelId = channelId;
    }

    @Override
    protected RestResponse perform(Object context) throws Exception {
        ReactionContext rc = (ReactionContext) context;
        String userId = rc.getUserId();
        if (userId == null) {
            return new RestResponse(400, "{\"message\":\"userId required for RemoveUserReactionAction\"}");
        }

        String encodedEmoji = ReactionUtils.encodeEmoji(rc.getEmoji());

        String url = GlobalConstants.URL_BASE + "/channels/" + channelId + "/messages/"
                + rc.getMessageId() + "/reactions/" + encodedEmoji + "/" + userId;

        return HttpClient.delete(url, TOKEN);
    }
}
