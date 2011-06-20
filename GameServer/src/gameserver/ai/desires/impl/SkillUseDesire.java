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
package gameserver.ai.desires.impl;

import com.aionemu.commons.utils.Rnd;
import gameserver.ai.AI;
import gameserver.ai.desires.AbstractDesire;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.templates.npcskill.NpcSkillList;
import gameserver.model.templates.npcskill.NpcSkillTemplate;
import gameserver.skillengine.SkillEngine;
import gameserver.skillengine.model.Skill;

import java.util.List;

/**
 * @author ATracer
 */
public class SkillUseDesire extends AbstractDesire {

    protected Creature owner;
    private NpcSkillList skillList;

    /**
     * @param owner
     * @param desirePower
     */
    public SkillUseDesire(Creature owner, int desirePower) {
        super(desirePower);
        this.owner = owner;
        this.skillList = ((Npc) owner).getNpcSkillList();
    }

    @Override
    public boolean handleDesire(AI<?> ai) {
        if (owner.isCasting())
            return true;

        /**
         * Demo mode - take random skill
         */
        List<NpcSkillTemplate> skills = skillList.getNpcSkills();
        NpcSkillTemplate npcSkill = skills.get(Rnd.get(0, skillList.getCount() - 1));

        /**
         * Demo mode - use probability from template
         */

        int skillProbability = npcSkill.getProbability();
        if (Rnd.get(0, 100) < skillProbability) {
            Skill skill = SkillEngine.getInstance().getSkill(owner, npcSkill.getSkillid(), npcSkill.getSkillLevel(), owner.getTarget());

            if (skill != null)
                skill.useSkill();
        }

        return true;
    }

    @Override
    public void onClear() {
        // TODO Auto-generated method stub
    }

    @Override
    public int getExecutionInterval() {
        return 1;
    }
}
