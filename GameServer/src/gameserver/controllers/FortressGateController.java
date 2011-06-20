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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.siege.FortressGate;
import gameserver.model.templates.siege.FortressGateTemplate;
import gameserver.model.templates.siege.SiegeSpawnLocationTemplate;
import gameserver.services.TeleportService;
import gameserver.utils.MathUtil;
import gameserver.world.WorldPosition;

/**
 * @author Xitanium, SuneC
 */
public class FortressGateController extends NpcController {

    @Override
    public void onDie(Creature lastAttacker) {
    	super.onDie(lastAttacker);
    }

    @Override
    public void onRespawn() {
        super.onRespawn();
    }

    @Override
    public FortressGate getOwner() {
        return (FortressGate) super.getOwner();
    }
    
    @Override
    public void onDialogRequest(Player player) {
    	if(getOwner().isEnemy(player))
    		return;
    	
    	FortressGateTemplate fgTemplate = getOwner().getTemplate();
    	SiegeSpawnLocationTemplate teleEnter = fgTemplate.getTeleEnter();
    	SiegeSpawnLocationTemplate teleExit = fgTemplate.getTeleExit();
    	
    	WorldPosition playerPos = player.getPosition();
    	
    	double fDistEnter = MathUtil.getDistance(playerPos.getX(), playerPos.getY(), playerPos.getZ(),
    		teleEnter.getX(), teleEnter.getY(), teleEnter.getZ());
    	double fDistExit = MathUtil.getDistance(playerPos.getX(), playerPos.getY(), playerPos.getZ(),
    		teleExit.getX(), teleExit.getY(), teleExit.getZ());
    	
    	if(fDistEnter < fDistExit) //Closer to entrance than exit -> port to Exit
    	{
    		TeleportService.teleportTo(player, playerPos.getMapId(), teleExit.getX(), teleExit.getY(), teleExit.getZ(), 0);
    	}
    	else //Closer to exit than entrance -> port to Entrance
    	{
    		TeleportService.teleportTo(player, playerPos.getMapId(), teleEnter.getX(), teleEnter.getY(), teleEnter.getZ(), 0);
    	}
    }
}
