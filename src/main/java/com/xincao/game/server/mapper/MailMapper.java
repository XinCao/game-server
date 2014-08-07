package com.xincao.game.server.mapper;

import com.xincao.game.server.model.IntPair;
import com.xincao.game.server.model.Mail;
import java.util.List;

public interface MailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Mail record);

    int insertSelective(Mail record);

    Mail selectByPrimaryKey(Integer id);

    List<Mail> selectByMailBoxId(IntPair intPair);

    int updateByPrimaryKeySelective(Mail record);

    int updateByPrimaryKey(Mail record);
}