/*
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is pryvate software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.ai.state.handler;

import gameserver.ai.AI;
import gameserver.ai.desires.impl.AttackDesire;
import gameserver.ai.desires.impl.MoveToTargetDesire;
import gameserver.ai.desires.impl.SkillUseDesire;
import gameserver.ai.state.AIState;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class AttackingStateHandler extends StateHandler {
    @Override
    public AIState getState() {
        return AIState.ATTACKING;
    }

    /**
     * State ATTACKING
     * AI MonsterAi
     * AI AggressiveAi
     */
    @Override
    public void handleState(AIState state, AI<?> ai) {
        ai.clearDesires();

        Creature target = ((Npc) ai.getOwner()).getAggroList().getMostHated();
        if (target == null)
            return;

        Npc owner = (Npc) ai.getOwner();
        owner.setTarget(target);
        PacketSendUtility.broadcastPacket(owner, new SM_LOOKATOBJECT(owner));

        owner.setState(CreatureState.WEAPON_EQUIPPED);
        PacketSendUtility.broadcastPacket(owner,
                new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, target.getObjectId()));
        PacketSendUtility.broadcastPacket(owner,
                new SM_EMOTION(owner, EmotionType.ATTACKMODE, 0, target.getObjectId()));

        owner.getMoveController().setSpeed(owner.getGameStats().getCurrentStat(StatEnum.SPEED) / 1000f);
        owner.getMoveController().setDistance(owner.getGameStats().getCurrentStat(StatEnum.ATTACK_RANGE) / 1000f);

        if (owner.getNpcSkillList() != null)
            ai.addDesire(new SkillUseDesire(owner, AIState.USESKILL.getPriority()));
        ai.addDesire(new AttackDesire(owner, target, AIState.ATTACKING.getPriority()));
        if (owner.getGameStats().getCurrentStat(StatEnum.SPEED) != 0)
            ai.addDesire(new MoveToTargetDesire(owner, target, AIState.ATTACKING.getPriority()));

        ai.schedule();
    }
}
