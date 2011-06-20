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
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.Trap;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.siege.ArtifactProtector;
import gameserver.services.SiegeService;

/**
 * @author Xitanium
 */
public class ArtifactProtectorController extends NpcController {

    @Override
    public void onRespawn() {
        super.onRespawn();
    }

    @Override
    public void onStartMove() {
        super.onStartMove();
    }

    @Override
    public void onMove() {
        super.onMove();
    }

    @Override
    public void onStopMove() {
        super.onStopMove();
    }

    @Override
    public void onDie(Creature lastAttacker) {
        if (lastAttacker instanceof Player || lastAttacker instanceof Summon || lastAttacker instanceof Trap) {
            Player taker;
            if (lastAttacker instanceof Player)
                taker = (Player) lastAttacker;
            else if (lastAttacker instanceof Summon)
                taker = ((Summon) lastAttacker).getMaster();
            else if (lastAttacker instanceof Trap)
                taker = (Player) ((Trap) lastAttacker).getCreator();
            else
                taker = null;

            if (taker != null)
                SiegeService.getInstance().onArtifactCaptured(getOwner().getArtifact(), taker);
        } else {
            // Taken by Balaur
            if (lastAttacker != null)
                SiegeService.getInstance().onArtifactCaptured(getOwner().getArtifact());
        }
        super.onDie(lastAttacker);
    }

    @Override
    public ArtifactProtector getOwner() {
        return (ArtifactProtector) super.getOwner();
    }
}
