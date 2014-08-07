package com.xincao.game.server.mapper;

import com.xincao.game.server.model.EnhanceItem;
import java.util.List;

public interface EnhanceItemMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(EnhanceItem record);

    int insertSelective(EnhanceItem record);

    EnhanceItem selectByPrimaryKey(Integer id);

    List<EnhanceItem> selectAll();

    int updateByPrimaryKeySelective(EnhanceItem record);

    int updateByPrimaryKey(EnhanceItem record);
}