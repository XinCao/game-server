package com.xincao.game.server.service;

import com.xincao.action_container.ActionService;
import com.xincao.action_container.IAction;
import com.xincao.game.server.constant.EnhanceItemType;
import com.xincao.game.server.mapper.EnhanceItemMapper;
import com.xincao.game.server.model.EnhanceItem;
import com.xincao.game.server.model.Player;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 1.可用于功能开启道具的实现。（可以通过背包中的道具引导玩家新功能的开启）
 * 
 */
public class EnhanceItemService {

    private static final Logger logger = LoggerFactory.getLogger(EnhanceItemService.class);
    @Autowired
    private EnhanceItemMapper enhanceItemMapper;
    @Autowired
    private ActionService actionService;

    public void init () {
        List<EnhanceItem> enhanceItems = enhanceItemMapper.selectAll();
        for (EnhanceItem enhanceItem : enhanceItems) {
            IAction iaction = actionService.getAction(enhanceItem.getActionName());
            iaction.setArguments(enhanceItem.getCondition());
        }
    }

    public void open (Player player, int no ) {
        IAction iaction = actionService.getAction(EnhanceItemType.getNameFormNo(no));
        if (iaction == null) {
            logger.error("this EnhanceItemType is not exist where no = {}", no);
            return;
        }
        if (iaction.canPerform(player)) {
            iaction.perform(player);
        }
    }
}