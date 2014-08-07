package com.xincao.game.server.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author caoxin
 */
@Service
public class LogServiceImplementWithLogback implements LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    @Override
    public void user_login(String account, String role) {
        LogEntry entry = new LogEntry("login", account, role);
        logger.info(MarkerFactory.getMarker("USER.LOGIN"), entry.toString());
    }

}