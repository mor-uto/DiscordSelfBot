package me.moruto.discordselfbot.action;

public abstract class AbstractRestAction implements RestAction {
    protected abstract RestResponse perform(Object context) throws Exception;

    @Override
    public final RestResponse execute(Object context) {
        try {
            RestResponse response = perform(context);
            System.out.println(getClass().getSimpleName() + " → " + response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponse(0, "Exception in " + getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
