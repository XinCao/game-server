package com.xincao.game.server.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class AC implements ApplicationContextAware {

    private static ApplicationContext ac;

    @Override
    public void setApplicationContext(ApplicationContext ac) {
        AC.ac = ac;
    }

    public static ApplicationContext getApplicationContext() {
        return AC.ac;
    }
}
