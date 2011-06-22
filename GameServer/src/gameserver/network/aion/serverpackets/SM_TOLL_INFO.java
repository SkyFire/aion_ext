package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

/**
 * @author PZIKO333
 */

public class SM_TOLL_INFO extends AionServerPacket {
    private int tollCount;

    public SM_TOLL_INFO(int tollCount) {
        this.tollCount = tollCount;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, tollCount);
        writeD(buf, 0);
    }
}