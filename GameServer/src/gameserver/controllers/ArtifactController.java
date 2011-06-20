/**
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
package gameserver.controllers;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.siege.Artifact;
import gameserver.model.siege.SiegeLocation;
import gameserver.model.siege.SiegeType;
import gameserver.network.aion.serverpackets.SM_ABNORMAL_STATE;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.services.SiegeService;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.SkillTemplate;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import org.apache.log4j.Logger;

import java.util.Collections;

/**
 * @author Xitanium
 */
public class ArtifactController extends NpcController {

    @Override
    public void onDialogRequest(final Player player) {
        if (getOwner().getObjectTemplate().getRace() == player.getCommonData().getRace()) {
            PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(160028, getOwner().getObjectId()));
        }
    }

    public void onActivate(Player player) {

        SiegeLocation loc = SiegeService.getInstance().getSiegeLocation(getOwner().getLocationId());

        if (loc.getRemainingEffectSeconds() > 0)
            return;

        if (loc.getLegionId() != 0) {
            if (player.getLegion() == null)
                return;
            if (player.getLegion().getLegionId() != loc.getLegionId())
                return;
            if (!player.getLegionMember().hasRights(5))
                return;
        }

        if (player.getInventory().getItemCountByItemId(188020000) == 0)
            return;

        player.getInventory().removeFromBagByItemId(188020000, 1);

        Logger.getLogger(ArtifactController.class).debug("Artifact " + getOwner().getLocationId() + " activated by " + player.getName());
        SkillTemplate sTemplate = DataManager.SKILL_DATA.getSkillTemplate(getOwner().getTemplate().getEffectTemplate().getSkillId());
        if (sTemplate == null) {
            Logger.getLogger(ArtifactController.class).error("No skill template for artifact effect id : " + getOwner().getTemplate().getEffectTemplate().getSkillId());
            return;
        }

        if (loc.getSiegeType() == SiegeType.FORTRESS) {
            loc.setLastArtifactActivation(System.currentTimeMillis());

            for (Player p : World.getInstance().getPlayers()) {
                if (p.getCommonData().getRace() == player.getCommonData().getRace()) {
                    // apply effect to player
                    if (p.getEffectController() != null && p.getEffectController().hasAbnormalEffect(sTemplate.getSkillId()))
                        continue;
                    final Effect effect = new Effect(player, p, sTemplate, 1, loc.getRemainingEffectSeconds() * 1000);
                    p.getEffectController().addEffect(effect);
                    effect.addAllEffectToSucess();
                    effect.startEffect(true);
                    PacketSendUtility.sendPacket(p, new SM_ABNORMAL_STATE(Collections.singletonList(effect), p.getEffectController().getAbnormals()));
                }
            }
        }

    }

    @Override
    public void onRespawn() {
        super.onRespawn();
    }

    @Override
    public Artifact getOwner() {
        return (Artifact) super.getOwner();
    }
}
