package com.xincao.game.server.network;

import com.xincao.common.util.tool.DeadLockDetector;
import com.xincao.game.server.network.core.IOServer;

/**
 *
 * @author caoxin
 */
public class AionNioServer {

    public static void start() {
        IOServer.getInstance().connect();
        DeadLockDetector.detector(60, DeadLockDetector.Dealt.RESTART);
    }
}