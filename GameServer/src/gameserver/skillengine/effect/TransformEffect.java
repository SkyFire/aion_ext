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
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_TRANSFORM;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.TransformType;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sweetkr
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransformEffect")
public class TransformEffect extends EffectTemplate {
    @XmlAttribute
    protected int model;

    @XmlAttribute
    protected TransformType type;


    @Override
    public void applyEffect(Effect effect) {
        final Creature effected = effect.getEffected();
        boolean transformed = false;
        if (effected instanceof Npc) {
            transformed = effected.getTransformedModelId() == effected.getObjectTemplate().getTemplateId();
        } else if (effected instanceof Player) {
            transformed = effected.getTransformedModelId() != 0;
        }
        if (transformed) {
            for (Effect tmp : effected.getEffectController().getAbnormalEffects()) {
                if (effect.getSkillId() == tmp.getSkillId())
                    continue;
                boolean abort = false;
                for (EffectTemplate template : tmp.getEffectTemplates()) {
                    if (template instanceof TransformEffect) {
                        abort = true;
                        break;
                    }
                }
                if (abort)
                    tmp.endEffect();
            }
        }
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        //TODO calc probability
        effect.addSucessEffect(this);
    }

    @Override
    public void endEffect(Effect effect) {
        final Creature effected = effect.getEffected();
        effected.getEffectController().unsetAbnormal(EffectId.SHAPECHANGE.getEffectId());

        if (effected instanceof Npc) {
            effected.setTransformedModelId(effected.getObjectTemplate().getTemplateId());
        } else if (effected instanceof Player) {
            effected.setTransformedModelId(0);
        }
        PacketSendUtility.broadcastPacketAndReceive(effected, new SM_TRANSFORM(effected));
    }

    @Override
    public void startEffect(final Effect effect) {
        final Creature effected = effect.getEffected();
        switch (effect.getSkillId())    //check if is an allowed fly transformation instead of disable all flying
        {
            case (689):        //MAU transform effect
            case (690):
            case (10265):   // transform candy
            case (10266):
            case (10281):
            case (10282):
            case (10260):
            case (10261):
            case (780):
            case (781):
            case (782):
            case (789):
            case (790):
            case (791):
            case (11885):    //Abyss transform effect
            case (11890):
            case (11886):
            case (11891):
            case (11887):
            case (11892):
            case (11888):
            case (11893):
            case (11889):
            case (11894):
                break;
            default:
                effect.setAbnormal(EffectId.SHAPECHANGE.getEffectId());
                effected.getEffectController().setAbnormal(EffectId.SHAPECHANGE.getEffectId());
        }
        effected.setTransformedModelId(model);
        PacketSendUtility.broadcastPacketAndReceive(effected, new SM_TRANSFORM(effected));
    }

    public TransformType getTransformType() {
        return type;
    }

}
