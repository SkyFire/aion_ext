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

import gameserver.controllers.MoveController;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Servant;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.skillengine.model.Effect;
import gameserver.spawnengine.SpawnEngine;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import java.util.concurrent.Future;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SummonServantEffect")
public class SummonServantEffect extends SummonEffect {

    @XmlAttribute(name = "skill_id", required = true)
    protected int skillId;
    @XmlAttribute(name = "hp_ratio", required = true)
    protected int hpRatio;
    @XmlAttribute(name = "count", required = false)
    protected int count;

    @Override
    public void applyEffect(Effect effect) {
        Creature effector = effect.getEffector();
        SpawnEngine spawnEngine = SpawnEngine.getInstance();
        float x = effector.getX();
        float y = effector.getY();
        float z = effector.getZ();
        byte heading = effector.getHeading();
        int worldId = effector.getWorldId();
        int instanceId = effector.getInstanceId();

        final Creature target = (Creature) effector.getTarget();

        // Energy servant (no skill)
        if (skillId == 0) {
            for (int i = 0; i < count; i++) {
                final SpawnTemplate spawn = spawnEngine.addNewSpawn(worldId, instanceId, npcId, x, y, z, heading, 0, 0, true, true);
                final Servant servant = spawnEngine.spawnServant(spawn, instanceId, effector, skillId, hpRatio);
                ThreadPoolManager.getInstance().schedule(new Runnable() {

                    @Override
                    public void run() {
                        servant.getController().cancelAllTasks();
                        DataManager.SPAWNS_DATA.removeSpawn(spawn);
                        servant.getController().onDelete();
                    }
                }, 15000);
                servant.setTarget(target);

                MoveController moveController = servant.getMoveController();
                if (moveController == null)
                    return;
                moveController.setNewDirection(target.getX(), target.getY(), target.getZ());
                moveController.setFollowTarget(true);
                moveController.schedule();

                servant.getController().attackTarget(target);
                target.getAggroList().addHate(effector, 25);
            }
        }
        // Healing servant
        else if (skillId == 18886 || skillId == 18887) {
            final SpawnTemplate spawn = spawnEngine.addNewSpawn(worldId, instanceId, npcId, x, y, z, heading, 0, 0, true, true);
            final Servant servant = spawnEngine.spawnServant(spawn, instanceId, effector, skillId, hpRatio);
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    servant.getController().cancelAllTasks();
                    DataManager.SPAWNS_DATA.removeSpawn(spawn);
                    servant.getController().onDelete();
                }
            }, 70000); //1 min 10 secs
        }
        // Skill-enabled servant
        else {
            final SpawnTemplate spawn = spawnEngine.addNewSpawn(worldId, instanceId, npcId, x, y, z, heading, 0, 0, true, true);
            final Servant servant = spawnEngine.spawnServant(spawn, instanceId, effector, skillId, hpRatio);
            ThreadPoolManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    servant.getController().cancelAllTasks();
                    DataManager.SPAWNS_DATA.removeSpawn(spawn);
                    servant.getController().onDelete();
                }
            }, 30000);
            servant.getController().useSkill(servant.getSkillId());
            ThreadPoolManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    servant.getController().useSkill(servant.getSkillId());
                }
            }, 12000);
            ThreadPoolManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    servant.getController().useSkill(servant.getSkillId());
                }
            }, 24000);
            target.getAggroList().addHate(effector, 50);
        }
    }

    @Override
    public void calculate(Effect effect) {
        effect.addSucessEffect(this);
    }
}
