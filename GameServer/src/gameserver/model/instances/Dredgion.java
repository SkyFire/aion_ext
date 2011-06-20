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

package gameserver.model.instances;

import gameserver.controllers.instances.DredgionController;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.WorldMapInstance;
import javolution.util.FastMap;

import java.util.Map;

/**
 * @author ArkShadow
 */

public class Dredgion {
    private int elyos = 0;
    private int asmos = 0;
    private Race winnerRace = null;
    private Map<Player, Integer> participants = new FastMap<Player, Integer>();

    public synchronized boolean addPlayer(Player player) {
        if (player == null) {
            return false;
        }
        Race playerRace = player.getCommonData().getRace();
        if (playerRace == Race.ASMODIANS) {
            if (asmos == 6)
                return false;
            else
                asmos++;
        } else if (playerRace == Race.ELYOS) {
            if (elyos == 6)
                return false;
            else
                elyos++;
        }
        participants.put(player, 0);
        return true;
    }

    public void setWinnerRace(Race race) {
        this.winnerRace = race;
    }

    public Race getWinnerRace() {
        if (winnerRace != null)
            return this.winnerRace;
        else {
            int asmosPoints = 0;
            int elyosPoints = 0;
            for (Player p : participants.keySet()) {
                if (p.getCommonData().getRace() == Race.ASMODIANS)
                    asmosPoints += participants.get(p);
                else elyosPoints += participants.get(p);
            }
            if (asmosPoints > elyosPoints)
                return Race.ASMODIANS;
            else
                return Race.ELYOS;
        }
    }

    public boolean isAlreadyRegister(Player player) {
        return participants.containsKey(player);
    }

    public void teleportIn(WorldMapInstance newinstance, DredgionController dred) {
        for (Player p : participants.keySet()) {
            if (p == null) {
                return;
            } else if (p.getCommonData().getRace() == Race.ELYOS)
                TeleportService.teleportTo(p, 300110000, newinstance.getInstanceId(), 558, 190, 432, 3000);
            else
                TeleportService.teleportTo(p, 300110000, newinstance.getInstanceId(), 414, 193, 431, 3000);
            p.setDredgion(dred);
        }
    }

    public void teleportOut() {
        for (Player p : participants.keySet()) {
            if (p == null) {
                return;
            }
            p.setDredgion(null);
            if (p.getWorldId() == 300110000)
                TeleportService.moveToBindLocation(p, true, 15000);
        }
        this.sendEndMessage();
        doReward();
        for (Player p : participants.keySet()) {
            p.setDredgion(null);
        }
        participants.clear();
    }

    public boolean canStart() {
        return (participants.size() == 12);
        //return true;
    }

    public void sendSorryMessage() {
        for (Player p : participants.keySet())
            PacketSendUtility.sendMessage(p, "Not enough players to start.");
    }

    public void sendBeginMessage() {
        for (Player p : participants.keySet()) {
            PacketSendUtility.sendSysMessage(p, "Welcome to the Dredgion. Let the battle begin!");
        }
    }

    private void doReward() {
        for (Player p : participants.keySet()) {
            if (p.getCommonData().getRace() == getWinnerRace())
                p.getCommonData().addAp(3000);
            else
                p.getCommonData().addAp(1500);
            if (participants.get(p) > 0)
                p.getCommonData().addAp(participants.get(p));
        }
    }

    public synchronized void addScore(Player p, int value) {
        int newValue = participants.get(p) + value;
        participants.remove(p);
        participants.put(p, newValue);
    }

    public void sendEndMessage() {
        String message = "Scoreboard : \n\n";
        message += "Felicitations " + getWinnerRace().toString() + " ! \n\n";
        for (Player p : participants.keySet()) {
            int score = participants.get(p);
            if (score < 0) score = 0;
            if (p.getCommonData().getRace() == getWinnerRace())
                score += 3000;
            else score += 1500;
            message += ("player : " + p.getName() + " a fait : " + String.valueOf(score) + " Points.\n");
        }
        message += "You will be teleported in 15s";
        for (Player p : participants.keySet()) {
            PacketSendUtility.sendSysMessage(p, message);
        }
    }

    public void removePlayer(Player p) {
        participants.remove(p);
    }
}
