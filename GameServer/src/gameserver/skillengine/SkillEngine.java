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
package gameserver.skillengine;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.skillengine.model.ActivationAttribute;
import gameserver.skillengine.model.Skill;
import gameserver.skillengine.model.SkillTemplate;

/**
 * @author ATracer
 */
public class SkillEngine {
    public static final SkillEngine skillEngine = new SkillEngine();

    /**
     * should not be instantiated directly
     */
    private SkillEngine() {

    }

    /**
     * This method is used for skills that were learned by player
     *
     * @param player
     * @param skillId
     * @return Skill
     */
    public Skill getSkillFor(Player player, int skillId, VisibleObject firstTarget) {
        SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skillId);

        if (template == null)
            return null;

        // player doesn't have such skill and ist not provoked
        if (template.getActivationAttribute() != ActivationAttribute.PROVOKED) {
            if (!player.getSkillList().isSkillPresent(skillId))
                return null;
        }


        Creature target = null;
        if (firstTarget instanceof Creature)
            target = (Creature) firstTarget;

        return new Skill(template, player, target);
    }

    /**
     * This method is used for not learned skills (item skills etc)
     *
     * @param creature
     * @param skillId
     * @param skillLevel
     * @return Skill
     */
    public Skill getSkill(Creature creature, int skillId, int skillLevel, VisibleObject firstTarget) {
        SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skillId);

        if (template == null)
            return null;

        Creature target = null;
        if (firstTarget instanceof Creature)
            target = (Creature) firstTarget;
        return new Skill(template, creature, skillLevel, target);
    }

    public static SkillEngine getInstance()
	{
		return skillEngine;
	}
}
