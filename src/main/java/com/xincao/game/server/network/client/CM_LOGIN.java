package com.xincao.game.server.network.client;

import com.xincao.common.event.dispatcher.GameEventDispatcher;
import com.xincao.game.server.event.WorldEvents;
import com.xincao.game.server.log.LogService;
import com.xincao.game.server.log.LogServiceImplementWithLogback;
import com.xincao.game.server.model.Player;
import com.xincao.game.server.core.network.AionConnection;
import com.xincao.game.server.service.PlayerService;
import java.nio.ByteBuffer;

public class CM_LOGIN extends AionClientPacket {

    private String account;
    private static final PlayerService playerService = getBean(PlayerService.class);
    private static final LogService logService = getBean(LogServiceImplementWithLogback.class);
    private static final GameEventDispatcher gameEventDispatcher = getBean(GameEventDispatcher.class);

    public CM_LOGIN(ByteBuffer buf, AionConnection client, Integer opcode) {
        super(buf, client, opcode);
    }

    @Override
    protected void readImpl() {
        this.account = this.readS();
    }

    @Override
    protected void runImpl() {
        gameEventDispatcher.triggerEvent(WorldEvents.LOGIN, account);
        if (!playerService.haveLogin(account)) {
            Player player = playerService.load(account, this.getConnection());
            playerService.addPlayer(player);
            logService.user_login(account, "magic");
        } else {
            // 提示，已经登录
        }
    }
}