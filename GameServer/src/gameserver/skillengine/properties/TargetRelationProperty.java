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
package gameserver.skillengine.properties;

import com.aionemu.commons.utils.Rnd;
import gameserver.model.alliance.PlayerAllianceGroup;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Servant;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.skillengine.model.Skill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.Iterator;
import java.util.List;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetRelationProperty")
public class TargetRelationProperty extends Property {

    @XmlAttribute(required = true)
    protected TargetRelationAttribute value;

    /**
     * Gets the value of the value property.
     *
     * @return possible object is {@link TargetRelationAttribute }
     */
    public TargetRelationAttribute getValue() {
        return value;
    }

    @Override
    public boolean set(Skill skill) {
        List<Creature> effectedList = skill.getEffectedList();
        Creature effector = skill.getEffector();

        skill.setTargetRelationAttribute(value);

        switch (value) {
            case ALL:
                break;
            case ENEMY:
                for (Iterator<Creature> iter = effectedList.iterator(); iter.hasNext();) {
                    Creature nextEffected = iter.next();

                    if (effector.isEnemy(nextEffected))
                        continue;

                    iter.remove();
                }
                break;
            case FRIEND:
                for (Iterator<Creature> iter = effectedList.iterator(); iter.hasNext();) {
                    Creature nextEffected = iter.next();

                    if (!effector.isEnemy(nextEffected))
                        continue;

                    iter.remove();
                }

                if (effectedList.size() == 0) {
                    skill.setFirstTarget(skill.getEffector());
                    effectedList.add(skill.getEffector());
                } else if (skill.getFirstTarget() != skill.getEffector()) {
                    skill.setFirstTarget(effectedList.get(0));
                }
                //party has higher priority
                int counter = 0;
                while (effectedList.size() > skill.getMaxEffected()) {
                    if (counter >= effectedList.size())
                        break;
                    Creature nextEffected = effectedList.get(counter);
                    counter++;
                    if (nextEffected == skill.getFirstTarget())
                        continue;
                    if (nextEffected instanceof Player) {
                        if (((Player) effector).isInAlliance()) {
                            PlayerAllianceGroup pag = ((Player) effector).getPlayerAlliance().getPlayerAllianceGroupForMember(effector.getObjectId());
                            if (pag != null && pag.isInSamePlayerAllianceGroup(effector.getObjectId(), nextEffected.getObjectId()))
                                continue;
                        } else if (((Player) effector).isInGroup()) {
                            if (((Player) effector).getPlayerGroup() != null && ((Player) nextEffected).getPlayerGroup() != null) {
                                if (((Player) effector).getPlayerGroup().getGroupId() == ((Player) nextEffected).getPlayerGroup().getGroupId())
                                    continue;
                            }
                        }
                    }
                    effectedList.remove(nextEffected);
                }
                break;
            case ALLY:
                //remove enemies and effector from the list
                for (Iterator<Creature> iter = effectedList.iterator(); iter.hasNext();) {
                    Creature nextEffected = iter.next();

                    if (!effector.isEnemy(nextEffected) && nextEffected != effector)
                        continue;

                    iter.remove();
                }

                if (effectedList.isEmpty())
                    break;

                //remove from effected list if not an ally
                for (int i = 0; i < skill.getMaxEffected(); i++) {
                    Creature nextEffected = effectedList.get(i);
                    if (nextEffected instanceof Player) {
                        if (((Player) effector).isInAlliance()) {
                            PlayerAllianceGroup pag = ((Player) effector).getPlayerAlliance().getPlayerAllianceGroupForMember(effector.getObjectId());
                            if (pag != null && pag.isInSamePlayerAllianceGroup(effector.getObjectId(), nextEffected.getObjectId()))
                                continue;
                        } else if (((Player) effector).isInGroup()) {
                            if (((Player) effector).getPlayerGroup() != null && ((Player) nextEffected).getPlayerGroup() != null) {
                                if (((Player) effector).getPlayerGroup().getGroupId() == ((Player) nextEffected).getPlayerGroup().getGroupId())
                                    continue;
                            }
                        }
                    }
                    effectedList.remove(nextEffected);
                }
                break;
            case MYPARTY:
                Player master = null;
                if (effector instanceof Servant) {
                    effector = skill.getEffector().getMaster();
                    master = (Player) effector;
                }
                for (Iterator<Creature> iter = effectedList.iterator(); iter.hasNext();) {
                    Creature nextEffected = iter.next();

                    Player player = null;
                    if (nextEffected instanceof Player) {
                        player = (Player) nextEffected;
                    } else if (nextEffected instanceof Summon) {
                        if (((Summon) nextEffected).getMaster() != null)
                            player = (Player) ((Summon) nextEffected).getMaster();
                    }
                    if (player != null) {
                        if (((Player) effector).isInAlliance()) {
                            PlayerAllianceGroup pag = ((Player) effector).getPlayerAlliance().getPlayerAllianceGroupForMember(effector.getObjectId());
                            if (pag != null && pag.isInSamePlayerAllianceGroup(effector.getObjectId(), player.getObjectId()))
                                continue;
                        } else if (((Player) effector).isInGroup()) {
                            if (((Player) effector).getPlayerGroup() != null && player.getPlayerGroup() != null) {
                                if (((Player) effector).getPlayerGroup().getGroupId() == player.getPlayerGroup().getGroupId())
                                    continue;
                            }
                        } else if (master != null) {
                            if (player == master)
                                continue;
                        }
                    }


                    iter.remove();
                }

                if (effectedList.size() == 0) {
                    skill.setFirstTarget(skill.getEffector());
                    effectedList.add(skill.getEffector());
                } else if (skill.getFirstTarget() != skill.getEffector()) {
                    skill.setFirstTarget(effectedList.get(0));
                }
                break;
        }

        while (effectedList.size() > skill.getMaxEffected()) {
            effectedList.remove(Rnd.get(effectedList.size()));
        }

        return true;
	}
}
