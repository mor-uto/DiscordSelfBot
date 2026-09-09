package me.moruto.discordselfbot.event.impl;

import me.moruto.discordselfbot.event.EventListener;
import me.moruto.discordselfbot.object.Member;

public interface MemberListener extends EventListener {
    void onMemberJoin(Member member);
    void onMemberLeave(Member member);
}