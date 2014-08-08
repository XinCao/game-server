package com.xincao.game.server.event.listener;

import com.xincao.action_container.ActionService;
import com.xincao.action_container.IAction;
import com.xincao.common.event.core.Event;
import com.xincao.common.event.listener.AbstractGameEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class WorldEventListener extends AbstractGameEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WorldEventListener.class);

    @Autowired
    private ActionService actionService;

    @Override
    public void handleEvent(Event e) throws Exception {
        String eventName = e.getName();
        Object context = e.getContext();
        IAction iaction = actionService.getAction(eventName);
        if (iaction == null) {
            logger.error("action name={} not found", eventName);
            return;
        }
        if (iaction.canPerform(context)) {
            iaction.perform(context);
        }
    }
}