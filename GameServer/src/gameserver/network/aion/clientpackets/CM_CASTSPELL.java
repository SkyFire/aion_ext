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
import gameserver.model.gameobjects.player.Player;
import gameserver.model.siege.FortressGeneral;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SKILL_CANCEL;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.skillengine.model.Skill;
import gameserver.skillengine.model.SkillTemplate;
import gameserver.utils.PacketSendUtility;

/**
 * @author alexa026
 * @author rhys2002
 */
public class CM_CASTSPELL extends AionClientPacket {
    private int spellid;
    private int targetType; // 0 - obj id, 1 - point location
    private float x, y, z;

    @SuppressWarnings("unused")
    private int targetObjectId;
    @SuppressWarnings("unused")
    private int time;
    @SuppressWarnings("unused")
    private int level;


    /**
     * Constructs new instance of <tt>CM_CM_REQUEST_DIALOG </tt> packet
     *
     * @param opcode
     */
    public CM_CASTSPELL(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        spellid = readH();
        level = readC();

        targetType = readC();

        switch (targetType) {
            case 0:
                targetObjectId = readD();
                break;
            case 1:
                x = readF();
                y = readF();
                z = readF();
                break;
            default:
                break;
        }

        time = readH();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();

        if (player == null)
            return;

        if (player.isProtectionActive()) {
            player.getController().stopProtectionActiveTask();
        }

        if (player.getTarget() instanceof FortressGeneral) {
            if (player.getCommonData().getRace() == ((FortressGeneral) player.getTarget()).getObjectTemplate().getRace())
                return;
        }

        if (!player.getLifeStats().isAlreadyDead()) {
            SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(spellid);
            if (template == null || template.isPassive())
                return;

            if (!player.getSkillList().isSkillPresent(spellid))
                return;

            //Custom Skill Cancellation Helper (Implemented by Untamed/TimeBomb)
            Skill castingSkill = player.getCastingSkill();
            if (castingSkill != null) {
                int skillId = castingSkill.getSkillTemplate().getSkillId();
                castingSkill.cancelCast();
                player.removeSkillCoolDown(skillId);
                player.setCasting(null);
                PacketSendUtility.sendPacket(player, new SM_SKILL_CANCEL(player, skillId));
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_CANCELED());
            }

            player.getController().useSkill(spellid, targetType, x, y, z);
		}
	}
}
