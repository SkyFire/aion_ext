package gameserver.network.aion.serverpackets;

import com.aionemu.commons.database.dao.DAOManager;
import gameserver.dao.InGameShopDAO;
import gameserver.model.inGameShop.InGameShop;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

/**
 * @author PZIKO333
 */

public class SM_IN_GAME_SHOP_ITEM extends AionServerPacket {
    private int objectId;

    public SM_IN_GAME_SHOP_ITEM(int objectId) {
        this.objectId = objectId;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        InGameShop item = null;
        item = (DAOManager.getDAO(InGameShopDAO.class)).loadInGameShopItem(objectId);

        writeD(buf, item.getObjectId());
        writeD(buf, item.getItemPrice());
        writeD(buf, 0);
        writeH(buf, 0);
        writeD(buf, item.getItemId());
        writeD(buf, item.getItemCount());
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeH(buf, 0);
        writeC(buf, 0);
        writeS(buf, item.getDescription());
        writeH(buf, 0);
    }
}