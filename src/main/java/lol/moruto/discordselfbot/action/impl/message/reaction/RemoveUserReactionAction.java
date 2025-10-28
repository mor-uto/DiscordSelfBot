package lol.moruto.discordselfbot.action.impl.message.reaction;

import lol.moruto.discordselfbot.GlobalConstants;
import lol.moruto.discordselfbot.action.AbstractRestAction;
import lol.moruto.discordselfbot.action.RestResponse;
import lol.moruto.discordselfbot.other.ReactionContext;
import lol.moruto.discordselfbot.util.HttpClient;
import lol.moruto.discordselfbot.util.ReactionUtils;

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
