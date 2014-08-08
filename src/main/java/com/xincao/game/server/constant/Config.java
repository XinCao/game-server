package com.xincao.game.server.constant;

public class Config {

    private static final String STATUS = "debug";
    public  static final String ip = "127.0.0.1";
    public static final int port = 8888;
    public static final String hostName = "Aion Connections";

    public static boolean isDebug() {
        return STATUS.equalsIgnoreCase("debug");
    }
}