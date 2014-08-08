package com.xincao.game.server.core.network;

import com.xincao.game.server.core.ThreadPoolManager;
import com.xincao.common.nio.NioServer;
import com.xincao.common.nio.ServerCfg;
import com.xincao.game.server.constant.Config;

public class IOServer {

    private final static NioServer instance;

    static {
        ServerCfg aionCfg = new ServerCfg(Config.ip, Config.port, Config.hostName, new AionConnectionFactoryImpl());
        instance = new NioServer(5, ThreadPoolManager.getInstance(), aionCfg);
    }

    public static NioServer getInstance() {
        return instance;
    }
}