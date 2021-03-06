package com.xincao.game.server;

import org.springframework.context.ApplicationContext;

import com.xincao.game.server.core.network.AionNioServer;
import com.xincao.game.server.util.ShutdownHook;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 数据库服务器
 *
 */
public class GameServer {

    public static void main(String ... args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:config/app.xml");
        AionNioServer.start();
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }
}