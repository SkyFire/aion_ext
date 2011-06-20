/**
 * This file is part of Aion X Emu <aionxemu.com>
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.network.aion.clientpackets;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.TradeListTemplate;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.trade.TradeItem;
import gameserver.model.trade.TradeList;
import gameserver.network.aion.AionClientPacket;
import gameserver.questEngine.QuestEngine;
import gameserver.questEngine.model.QuestCookie;
import gameserver.services.PrivateStoreService;
import gameserver.services.TradeService;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import gameserver.world.World;

import org.apache.log4j.Logger;

/**
 * @author orz modified by ATracer
 *         modified by Simple
 */
public class CM_BUY_ITEM extends AionClientPacket {
    private static final Logger log = Logger.getLogger(CM_BUY_ITEM.class);

    private int sellerObjId;
    private int unk1;
    private int amount;
    private int itemId = 0;
    private int itemSlot = 0;
    private int count;

    public int unk2;

    public CM_BUY_ITEM(int opcode) {
        super(opcode);
    }

    private TradeList tradeList;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        sellerObjId = readD();
        unk1 = readH();
        amount = readH(); // total no of items

        tradeList = new TradeList();
        tradeList.setSellerObjId(sellerObjId);

        for (int i = 0; i < amount; i++) {
            int tmpInt1 = readD();
            if (unk1 == 0)
                itemSlot = tmpInt1;
            else
                itemId = tmpInt1;

            count = readD();
            unk2 = readD();

            // prevent exploit packets
            if (count < 1)
                continue;

            if (unk1 == 13 || unk1 == 14 || unk1 == 15) {
                tradeList.addBuyItem(itemId, count);
            }
            else if (unk1 == 1) {
                tradeList.addSellItem(itemId, count);
            }
            else if (unk1 == 0) {
                tradeList.addSellItem(itemSlot, count);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        AionObject obj = World.getInstance().findAionObject(sellerObjId);
		TradeListTemplate tlist = null;
		Npc npc = null;

        switch (unk1) {
            case 0:
                Player targetPlayer = (Player) obj;
                tradeList = PrivateStoreService.sellStoreItem(targetPlayer, player, tradeList);
                break;

            case 1:
                TradeService.performSellToShop(player, tradeList);
                break;

            case 13:
                TradeService.performBuyFromShop(player, tradeList);
                break;

            case 14:
                npc = (Npc) World.getInstance().findAionObject(sellerObjId);
                tlist = DataManager.TRADE_LIST_DATA.getTradeListTemplate(npc.getNpcId());
                if (tlist.getCategory() == 1)
                    TradeService.performBuyFromAbyssShop(player, tradeList);
                break;

            case 15:
            	npc = (Npc) World.getInstance().findAionObject(sellerObjId);
            	tlist = DataManager.TRADE_LIST_DATA.getTradeListTemplate(npc.getNpcId());
            	if (tlist.getCategory() == 3)
            		TradeService.performBuyFromSpecialShop(player, tradeList);
            	break;

            default:
                log.info(String.format("Unhandle shop action unk1: %d", unk1));
                break;
        }

        if (tradeList == null)
            return;

        VisibleObject visibleObject = null;
        if (obj instanceof VisibleObject)
            visibleObject = (VisibleObject) obj;

        for (TradeItem item : tradeList.getTradeItems()) {
            ItemTemplate itemTemplate = item.getItemTemplate();
            if (itemTemplate == null) {
                log.warn(LanguageHandler.translate(CustomMessageId.ERROR_TRADEITEM_TEMPATE_MISSING,
                    player.getObjectId(), item.getItemId()));
                continue;
            }
            QuestEngine.getInstance().onItemSellBuyEvent(new QuestCookie(visibleObject, player, itemTemplate.getItemQuestId(), 0),
                    itemTemplate.getTemplateId());
        }

    }

}
