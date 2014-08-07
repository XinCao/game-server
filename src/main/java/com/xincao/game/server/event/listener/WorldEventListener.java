package com.xincao.game.server.event.listener;

import com.xincao.common.event.core.Event;
import com.xincao.common.event.listener.AbstractGameEventListener;
import com.xincao.game.server.event.WorldEvents;

public class WorldEventListener extends AbstractGameEventListener {

    @Override
    public void handleEvent(Event e) throws Exception {
        String eventName = e.getName();
        String name = (String) e.getContext();
        if (WorldEvents.LOGIN.equals(eventName)) {
            this.login(name);
        }
    }

    private void login(String name) {
        System.out.println(name + "登录游戏世界");
    }
}