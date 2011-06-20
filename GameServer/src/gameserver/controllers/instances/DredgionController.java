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

package gameserver.controllers.instances;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.instances.Dredgion;
import gameserver.services.InstanceService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.WorldMapInstance;
import org.apache.log4j.Logger;

/**
 * @author ArkShadow
 */

public class DredgionController {

    private static final Logger log = Logger.getLogger(DredgionController.class);
    private WorldMapInstance newinstance;
    private Dredgion dred = new Dredgion();
    private boolean isStopped = false;

    public DredgionController() {
        newinstance = InstanceService.getNextAvailableInstance(300110000);
    }

    public synchronized void registerPlayer(Player player) {
        if (dred.isAlreadyRegister(player)) {
            log.warn("Exception in DredgionInstanceService : Player shouldn't be registered twice");
            return;
        }
        if (dred.addPlayer(player)) {
            InstanceService.registerPlayerWithInstance(newinstance, player);
        } else {
            log.warn("Cannot register player : " + player.getName());
            return;
        }
    }

    public void teleportSpec(Player p) {
        TeleportService.teleportTo(p, 300110000, newinstance.getInstanceId(), 558, 190, 432, 3000);
    }

    public void stop() {
        if (isStopped)
            return;
        isStopped = true;
        dred.teleportOut();
        log.info("Dredgion is now stopped");
    }

    public void start() {
        if (dred.canStart()) {
            log.info("New Dredgion started");
            dred.teleportIn(newinstance, this);
            dred.sendBeginMessage();
        } else {
            log.info("Not enought player to start dredgion. Aborting.");
            dred.sendSorryMessage();
        }
    }

    public void onDieEvent(Player p) {
    }

    public void updateScore(Player p, int value) {
        dred.addScore(p, value);
    }

    public void onKillEvent(Player p) {
        dred.setWinnerRace(p.getCommonData().getRace());
        this.stop();
    }

    public void onLeaveEvent(Player p) {
        PacketSendUtility.sendMessage(p, "You leave the dredgion, you will not receive any reward");
        dred.removePlayer(p);
    }
}
