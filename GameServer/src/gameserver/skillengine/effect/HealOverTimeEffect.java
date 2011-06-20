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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.PlayerLifeStats;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.HealType;
import gameserver.utils.ThreadPoolManager;
import gameserver.model.gameobjects.stats.CreatureLifeStats;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.concurrent.Future;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealOverTimeEffect")
public class HealOverTimeEffect extends EffectTemplate {

    @XmlAttribute(required = true)
    protected int checktime;
    @XmlAttribute
    protected int value;
    @XmlAttribute
    protected int delta;
    @XmlAttribute
    protected HealType type;
	@XmlAttribute
	protected boolean percent;

    private float finalRate;

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        effect.addSucessEffect(this);
    }

    @Override
    public void endEffect(Effect effect) {
        //nothing todo
    }

    @Override
    public void onPeriodicAction(Effect effect) {
        Creature effected = effect.getEffected();
        int valueWithDelta;

		final CreatureLifeStats<? extends Creature> cls = effect.getEffected().getLifeStats();
		if(percent)
			valueWithDelta = Math.round(cls.getMaxHp() * (value / 100f));
		else
			valueWithDelta = value + delta * effect.getSkillLevel();

        if (type == HealType.FP) {
            effected.getLifeStats().increaseFp(valueWithDelta);
            if (effected instanceof Player) {
                PlayerLifeStats stats = (PlayerLifeStats) effected.getLifeStats();
                stats.sendFpPacketUpdateImpl();
            }
        } else if (type == HealType.HP) {
            effected.getController().onRestore(type, Math.round(valueWithDelta * finalRate));
        } else
            effected.getController().onRestore(type, Math.round(valueWithDelta));

    }

    @Override
    public void startEffect(final Effect effect) {
        //boostheal a healrate should be counted only on start of effect
        //each player start with boost_heal 100, therefore -100
        float boostHeal = ((float) (effect.getEffector().getGameStats().getCurrentStat(StatEnum.BOOST_HEAL) - 100) / 1000f);
        float healRate = effect.getEffector().getController().getHealRate();
        finalRate = boostHeal + healRate;

        Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable() {

            @Override
            public void run() {
                onPeriodicAction(effect);
            }
        }, checktime, checktime);
        effect.setPeriodicTask(task, position);
    }
}
