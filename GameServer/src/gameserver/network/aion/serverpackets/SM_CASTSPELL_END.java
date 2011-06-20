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

import gameserver.model.gameobjects.Creature;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.skillengine.model.Effect;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author alexa026, Sweetkr
 */
public class SM_CASTSPELL_END extends AionServerPacket {
    private Creature attacker;
    private Creature target;
    private int spellid;
    private int level;
    private int cooldown;
    private List<Effect> effects;
    private int spellStatus;
    private float x;
    private float y;
    private float z;
    private int targetType;
    private boolean chainSuccess;

    public SM_CASTSPELL_END(Creature attacker, Creature target, List<Effect> effects,
                            int spellid, int level, int cooldown, boolean chainSuccess, int spellStatus) {
        // TODO: Pass Skill type instead?
        this.attacker = attacker;
        this.target = target;
        this.spellid = spellid;// empty
        this.level = level;
        this.effects = effects;
        this.cooldown = cooldown;
        this.spellStatus = spellStatus;
        this.chainSuccess = chainSuccess;
        this.targetType = 0;
    }

    public SM_CASTSPELL_END(Creature attacker, Creature target, List<Effect> effects,
                            int spellid, int level, int cooldown, boolean chainSuccess, int spellStatus, float x, float y, float z) {
        this(attacker, target, effects, spellid, level, cooldown, chainSuccess, spellStatus);
        this.x = x;
        this.y = y;
        this.z = z;
        this.targetType = 1;
    }

    /**
     * {@inheritDoc}
     */

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, attacker.getObjectId());
        writeC(buf, targetType);
        switch (targetType) {
            case 0:
                writeD(buf, target.getObjectId());
                break;
            case 1:
                writeF(buf, x);
                writeF(buf, y);
                writeF(buf, z + 0.4f);
                break;
			case 3:
				writeD(buf, 0);
				break;
        }
        writeH(buf, spellid);
        writeC(buf, level);
        writeD(buf, cooldown);
        writeH(buf, 560); // time?
        writeC(buf, 0); // unk

        /**
         * 0 : chain skill (counter too)
         * 16 : no damage to all target like dodge, resist or effect size is 0
         * 32 : regular
         */
        if (chainSuccess)
            writeH(buf, 32);
        else
            writeH(buf, 0);

        /**
         * Dash Type
         *
         * 1 : teleport to back (1463)
         * 2 : dash (816)
         * 4 : assault (803)
         */
        writeC(buf, 0);

        // TODO refactor skill engine
        /*	switch(attacker.getDashType().getId())
          {
              case 1:
              case 2:
              case 4:
                  writeC(buf, heading);
                  writeF(buf, x);
                  writeF(buf, y);
                  writeF(buf, z);
                  break;
              default:
                  break;
          }*/

        writeH(buf, effects.size());
        for (Effect effect : effects) {
            writeD(buf, effect.getEffected().getObjectId());
            writeC(buf, 0); // unk

            int attackerMaxHp = attacker.getLifeStats().getMaxHp();
            int attackerCurrHp = attacker.getLifeStats().getCurrentHp();
            int targetMaxHp = target.getLifeStats().getMaxHp();
            int targetCurrHp = target.getLifeStats().getCurrentHp();

            writeC(buf, 100 * targetCurrHp / targetMaxHp); // target %hp
            writeC(buf, 100 * attackerCurrHp / attackerMaxHp); // attacker %hp


            /**
             * Spell Status
             *
             * 1 : stumble
             * 2 : knockback
             * 4 : open aerial
             * 8 : close aerial
             * 16 : spin
             * 32 : block
             * 64 : parry
             * 128 : dodge
             * 256 : resist
             */
            writeC(buf, this.spellStatus);

            switch (this.spellStatus) {
                case 1:
                case 2:
                case 4:
                case 8:
                    writeF(buf, target.getX());
                    writeF(buf, target.getY());
                    writeF(buf, target.getZ() + 0.4f);
                    break;
                case 16:
                    writeC(buf, target.getHeading());
                    break;
                default:
                    break;
            }

            writeC(buf, 16); // unk
            writeC(buf, 0); // current carve signet count

            writeC(buf, 1); // unk always 1
            writeC(buf, (effect.isMpheal() == true ? 1 : 0));
            writeD(buf, effect.getReserved1()); // damage
            writeC(buf, effect.getAttackStatus().getId());
            writeC(buf, effect.getShieldDefense());

            switch (effect.getShieldDefense()) {
                case 1: // reflect shield
                    writeD(buf, 0x00);
                    writeD(buf, 0x00);
                    writeD(buf, 0x00);
                    writeD(buf, 0x00); // reflect damage
                    writeD(buf, 0x00); // skill id
                    break;
                case 2: // normal shield
                default:
                    break;
            }
        }
    }
}
