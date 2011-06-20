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

package gameserver.network.aion.serverpackets;

import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author Mr. Poke
 */
public class SM_CRAFT_UPDATE extends AionServerPacket {
    private int skillId;
    private int itemId;
    private int action;
    private int success;
    private int failure;
    private int nameId;

    /**
     * @param skillId
     * @param item
     * @param success
     * @param failure
     * @param action
     */
    public SM_CRAFT_UPDATE(int skillId, ItemTemplate item, int success, int failure, int action) {
        this.action = action;
        this.skillId = skillId;
        this.itemId = item.getTemplateId();
        this.success = success;
        this.failure = failure;
        this.nameId = item.getNameId();
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeH(buf, skillId);
        writeC(buf, action);
        writeD(buf, itemId);

        switch (action) {
            case 0: //init
            {
                writeD(buf, success);
                writeD(buf, failure);
                writeD(buf, 0);
                writeD(buf, 1200);        //timer??
                writeD(buf, 1330048);
                writeH(buf, 0x24); //0x24
                writeD(buf, nameId);
                writeH(buf, 0);
                break;
            }
            case 1: //update
            {
                writeD(buf, success);
                writeD(buf, failure);
                writeD(buf, 700);        //unk timer??
                writeD(buf, 1200);      //unk timer??
                writeD(buf, 0); //unk timer??writeD(buf, 700);
                writeH(buf, 0);
                break;
            }
            case 2: //crit
            {
                writeD(buf, success);
                writeD(buf, failure);
                writeD(buf, 700);//unk timer??
                writeD(buf, 1200); //unk timer??
                writeD(buf, 0); //unk timer??writeD(buf, 700);
                writeH(buf, 0);
                break;
            }
            case 3: //crit
            {
                writeD(buf, success);
                writeD(buf, failure);
                writeD(buf, 0);
                writeD(buf, 1200);        //timer??
                writeD(buf, 1330048);
                writeH(buf, 0x24); //0x24
                writeD(buf, nameId);
                writeH(buf, 0);
                break;
            }
            case 5: //sucess
            {
                writeD(buf, success);
                writeD(buf, failure);
                writeD(buf, 700);//unk timer??
                writeD(buf, 1200); //unk timer??
                writeD(buf, 0); //unk timer??writeD(buf, 700);
                writeH(buf, 0);
                break;
            }
            case 6: //failed
            {
                writeD(buf, success);
                writeD(buf, failure);
                writeD(buf, 700); //unk timer??
                writeD(buf, 1200); //unk timer??
                writeD(buf, 0); //unk timer??writeD(buf, 700);
                writeH(buf, 0);
                break;
            }
            case 7: {
                writeD(buf, success);
                writeD(buf, failure);
                writeD(buf, 0);
                writeD(buf, 1200); //timer??
                writeD(buf, 1330050); //??text??skill??
                writeH(buf, 0x24); //0x24
                writeD(buf, nameId);
                writeH(buf, 0); //0x24
                break;
            }
        }
    }
}