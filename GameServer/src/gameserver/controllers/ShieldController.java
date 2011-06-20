/**
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is private software: you can redistribute it and/or modify
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

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.shield.Shield;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.apache.log4j.Logger;

/**
 * @author xavier
 */
public class ShieldController extends CreatureController<Shield> {
    @Override
    public void see(VisibleObject object) {
        if (object instanceof Player) {
            Player p = (Player) object;
            Shield owner = (Shield) getOwner();
            if (p.getCommonData().getRace() != owner.getTemplate().getRace() && p.getAccessLevel() <= AdminConfig.GM_SHIELD_VULNERABLE) {
                Logger.getLogger(this.getClass()).info("Schield " + owner.getName() + " killing " + p.getName());
                p.getController().setCanAutoRevive(false);
                p.getController().onAttack(owner, owner.getTemplate().getSkill(), TYPE.DAMAGE, p.getLifeStats().getCurrentHp() + 1, 0x5B, true);
                p.getReviveController().bindRevive();
                p.getController().setCanAutoRevive(true);
            }
        }
    }

}
