package com.xincao.game.server.mapper;

import com.xincao.game.server.model.Player;

public interface PlayerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Player record);

    int insertSelective(Player record);

    Player selectByPrimaryKey(Integer id);

    Player selectByAccount(String account);

    int updateByPrimaryKeySelective(Player record);

    int updateByPrimaryKey(Player record);
}