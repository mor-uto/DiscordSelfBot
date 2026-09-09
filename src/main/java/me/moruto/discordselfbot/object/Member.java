package me.moruto.discordselfbot.object;

public final class Member {
    private final String id;
    private final String username;
    private final String discriminator;
    private final String avatarUrl;

    public Member(String id, String username, String discriminator, String avatarUrl) {
        this.id = id;
        this.username = username;
        this.discriminator = discriminator;
        this.avatarUrl = avatarUrl;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public String toString() {
        return username + "#" + discriminator;
    }
}
