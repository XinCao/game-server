package com.xincao.game.server.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincao.game.server.mapper.PlayerMapper;
import com.xincao.game.server.model.Player;
import com.xincao.game.server.network.core.AionConnection;
import com.xincao.game.server.network.core.AionPacketHandler.AionServerKind;
import com.xincao.game.server.network.packet.server.SM_HEART_BEAT;

@Service
public class PlayerService {

    private final Map<Integer, Player> players = new ConcurrentHashMap<Integer, Player>();

    @Autowired
    private PlayerMapper playerMapper;
    @Autowired
    private MailBoxService mailBoxService;

    public Player load(String account, AionConnection connection) {
        Player player = playerMapper.selectByAccount(account);
        player.setConnection(connection);
        connection.setObject(player);
        mailBoxService.loadMailBox(player);
        return player;
    }

    public void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    public void sendHeartBeat() {
        Set<Entry<Integer, Player>> s = players.entrySet();
        for (Entry<Integer, Player> e : s) {
            Player player = e.getValue();
            AionConnection connection = player.getConnection();
            connection.sendPacket(new SM_HEART_BEAT(AionServerKind.SM_HEART_BEAT.getOpcode()));
        }
    }

    public Player getPlayerByPlaterId (int playerId) {
        if (players.containsKey(playerId)) {
            return players.get(playerId);
        }
        return null;
    }

    public boolean haveLogin(String account) {
        Player player = playerMapper.selectByAccount(account);
        return players.containsKey(player.getId());
    }

    /**
     * 下线
     * 
     */
    public void logout(Player player) {
        players.remove(player.getId());
    }
}