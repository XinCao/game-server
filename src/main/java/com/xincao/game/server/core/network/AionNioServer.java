package com.xincao.game.server.core.network;

import com.xincao.common.util.tool.DeadLockDetector;

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