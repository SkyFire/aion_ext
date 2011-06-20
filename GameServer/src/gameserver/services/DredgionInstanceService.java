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

package gameserver.services;

import gameserver.controllers.instances.DredgionController;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.PlayerGroup;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.Executor;
import gameserver.world.World;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ArkShadow
 */

public class DredgionInstanceService {
    private static boolean isStarted = false;
    private static boolean autorun = false;

    Set<Player> regPlayers = new HashSet<Player>();
    Set<PlayerGroup> regGroups = new HashSet<PlayerGroup>();
    Timer timer;
    int remainingTime;

    public DredgionInstanceService() {
        remainingTime = 10;
        timer = initTimer();
    }

    public int getPlayersSize() {
        return regPlayers.size();
    }

    public boolean loadDredgion() {
        if (isStarted)
            return false;
        isStarted = true;
        if (remainingTime <= 0)
            remainingTime = 10;

        World.getInstance().doOnAllPlayers(new Executor<Player>() {
            @Override
            public boolean run(Player p) {
                if (p.getCommonData().getLevel() > 45)
                    PacketSendUtility.sendSysMessage(p, "A new Dredgion just appeared. Type .dredgion to register for the instance. \n Remaining time : " + remainingTime + " minutes");
                return true;
            }
        });
        timer.start();
        return true;
    }

    public void setAuto(boolean b) {
        autorun = b;
        if (b)
            this.loadDredgion();
    }

    public void setTimer(int time) {
        remainingTime = time;
    }

    public int getTimer() {
        if (isStarted)
            return remainingTime;
        else
            return -1;
    }

    private Timer initTimer() {
        ActionListener action = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                remainingTime--;
                DredgionInstanceService.getInstance().sendRemainTime();
                if (remainingTime == 0) {
                    DredgionInstanceService.getInstance().onTimerEnd();
                    timer.stop();
                }
            }

        };

        return new Timer(60 * 1000, action);
    }

    public void sendMsg(Player p, String message) {
        PacketSendUtility.sendMessage(p, message);
    }

    public void registerPlayer(Player player) {
        if (isStarted && player.getCommonData().getLevel() > 45) {
            if (player.isInGroup()) {
                if (player.getPlayerGroup().getGroupLeader() == player && player.getPlayerGroup().size() == 6) {
                    if (regGroups.add(player.getPlayerGroup())) {
                        for (Player p : player.getPlayerGroup().getMembers())
                            this.sendMsg(p, "You are now registered for the next dredgion");
                    } else
                        this.sendMsg(player, "Your group is already registered");
                } else
                    this.sendMsg(player, "You are not allowed to register your group");

            } else {
                if (regPlayers.add(player))
                    this.sendMsg(player, "You are now registered for the next dredgion");
                else
                    this.sendMsg(player, "You are already registered for the next dredgion");
            }
        } else
            this.sendMsg(player, "No dredgion available");
    }

    public void unregisterPlayer(Player player) {
        if (regPlayers.contains(player)) {
            if (regPlayers.remove(player))
                this.sendMsg(player, "Successfully unregistred");
            else
                this.sendMsg(player, "Unknow error. Please contact administrator");
        } else
            this.sendMsg(player, "You are not registered into dredgion");
    }

    public synchronized void makeOneDredgion() {
        DredgionController dredgion = new DredgionController();
        Set<Player> toRegister = getOneGroup();
        for (Player player : toRegister) {
            dredgion.registerPlayer(player);
        }
        dredgion.start();
        this.scheduleStop(dredgion);

    }

    private void scheduleStop(final DredgionController dredgion) {
        ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                dredgion.stop();
            }

        }, 60 * 60 * 1000);
    }

    private synchronized Set<Player> getOneGroup() {
        Set<Player> players = new HashSet<Player>();
        if (regGroups.isEmpty()) {
            for (Player p : regPlayers) {
                int asmos = 0;
                int elyos = 0;
                if (p.getCommonData().getRace() == Race.ASMODIANS && asmos < 6) {
                    asmos++;
                    players.add(p);
                } else if (p.getCommonData().getRace() == Race.ELYOS && elyos < 6) {
                    elyos++;
                    players.add(p);
                }
            }

            for (Player p : players) {
                regPlayers.remove(p);
            }
        } else {
            boolean asmos = false;
            boolean elyos = false;
            Set<PlayerGroup> toRemove = new HashSet<PlayerGroup>();
            for (PlayerGroup p : regGroups) {
                if (p.getGroupLeader().getCommonData().getRace() == Race.ASMODIANS && !asmos) {
                    asmos = true;
                    for (Player player : p.getMembers())
                        players.add(player);
                    toRemove.add(p);
                } else if (p.getGroupLeader().getCommonData().getRace() == Race.ELYOS && !elyos) {
                    elyos = true;
                    for (Player player : p.getMembers())
                        players.add(player);
                    toRemove.add(p);
                }
            }
            for (PlayerGroup pg : toRemove) {
                regGroups.remove(pg);
            }
        }
        return players;
    }

    public void sendRemainTime() {
        if (remainingTime == 5 || remainingTime == 2 || remainingTime == 10) {
            World.getInstance().doOnAllPlayers(new Executor<Player>() {
                @Override
                public boolean run(Player pl) {
                    PacketSendUtility.sendSysMessage(pl, "Remaining time before dredgion depart : " + remainingTime + " minutes");
                    return true;
                }
            });
        }
    }

    public void onTimerEnd() {
        isStarted = false;
        while (!regPlayers.isEmpty()) {
            this.makeOneDredgion();
        }
        if (autorun) {
            remainingTime = 120;
            isStarted = true;
            timer.start();
        }
    }

    public void reset() {
        isStarted = false;
        timer.stop();
        regPlayers.clear();
        regGroups.clear();
        remainingTime = 10;
        autorun = false;
    }

    public static DredgionInstanceService getInstance() {
        return SingletonHolder.dr;
    }

    private static class SingletonHolder {
        public static DredgionInstanceService dr = new DredgionInstanceService();
    }
}
