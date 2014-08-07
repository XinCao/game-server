package com.xincao.game.server.mapper;

import com.xincao.game.server.model.MailBox;

public interface MailBoxMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(MailBox record);

    int insertSelective(MailBox record);

    MailBox selectByPrimaryKey(Integer id);
    
    MailBox selectByPlayerId(Integer playerId);

    int updateByPrimaryKeySelective(MailBox record);

    int updateByPrimaryKey(MailBox record);

}