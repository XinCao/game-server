package com.xincao.game.server.constant;

public enum EnhanceItemType {
    SAMPLE (1, "sample"),
    
    ;
    private final int no;
    private final String name;

    private EnhanceItemType (int no, String name) {
        this.no = no;
        this.name = name;
    }

    public static String getNameFormNo (int no) {
        for (EnhanceItemType enhanceItemType : values()) {
            if (enhanceItemType.getNo() == no) {
                return enhanceItemType.getName();
            }
        }
        return null;
    }

    public int getNo () {
        return this.no;
    }

    public String getName () {
        return this.name;
    }
}