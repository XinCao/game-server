package com.xincao.game.server.action;

import com.xincao.action_container.AbstractAction;
import com.xincao.action_container.Action;
import com.xincao.game.server.event.WorldEvents;

@Action(option = WorldEvents.LOGIN)
public class LoginAction extends AbstractAction {

    @Override
    public void setArguments(Object... args) {
    }

    @Override
    public boolean canPerform(Object... args) {
        return true;
    }

    @Override
    public void perform(Object... args) {
    }
}
