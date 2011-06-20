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
package gameserver.skillengine.effect;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_SUMMON_USESKILL;
import gameserver.skillengine.model.Effect;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PetOrderUseUltraSkillEffect")
public class PetOrderUseUltraSkillEffect extends EffectTemplate {
    @Override
    public void applyEffect(Effect effect) {
        Player effector = (Player) effect.getEffector();
        Summon summon = effector.getSummon();

        if (summon == null)
            return;

        int effectorId = summon.getObjectId();

        int npcId = summon.getNpcId();
        int orderSkillId = effect.getSkillId();

        int petUseSkillId = DataManager.PET_SKILL_DATA.getPetOrderSkill(orderSkillId, npcId);
        int targetId = effect.getEffected().getObjectId();

        PacketSendUtility.sendPacket(effector, new SM_SUMMON_USESKILL(effectorId, petUseSkillId,
                1, targetId));
    }

    @Override
    public void calculate(Effect effect) {
        if (effect.getEffector() instanceof Player && effect.getEffected() != null)
            effect.addSucessEffect(this);
    }
}
