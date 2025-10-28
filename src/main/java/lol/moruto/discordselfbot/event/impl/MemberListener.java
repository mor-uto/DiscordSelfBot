package lol.moruto.discordselfbot.event.impl;

import lol.moruto.discordselfbot.event.EventListener;
import lol.moruto.discordselfbot.object.Member;

public interface MemberListener extends EventListener {
    void onMemberJoin(Member member);
    void onMemberLeave(Member member);
}