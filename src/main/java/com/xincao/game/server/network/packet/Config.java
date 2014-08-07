package com.xincao.game.server.network.packet;

public class Config {

    private static final String STATUS = "debug";

    public static boolean isDebug() {
        return STATUS.equalsIgnoreCase("debug");
    }
}