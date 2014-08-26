package com.xincao.game.server.util;

import com.xincao.common.util.tool.RunnableStatsManager;

/**
 * 
 * @author xincao 
 */
public class ShutdownHook extends Thread {

    @Override
    public void run () {
        RunnableStatsManager.dumpClassStats();
    }
}
