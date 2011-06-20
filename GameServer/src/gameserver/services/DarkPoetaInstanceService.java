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

import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.Monster;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.PlayerGroup;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.spawnengine.SpawnEngine;
import gameserver.utils.PacketSendUtility;

/**
 * @author kosyachok
 */
public class DarkPoetaInstanceService {
    public void onGroupReward(Monster monster, PlayerGroup group) {
        int pointsReward = calculatePointsReward(monster, group.getGroupLeader());

        group.setGroupInstancePoints(group.getGroupInstancePoints() + pointsReward);

        for (Player member : group.getMembers())
            PacketSendUtility.sendMessage(member, "Raid gets " + pointsReward + " points. Total score is: " + group.getGroupInstancePoints());

        //extra boss spawn after killing final boss
        if (monster.getObjectTemplate().getTemplateId() == 214904) {
            int totalPoints = group.getGroupInstancePoints();
            long timeRemain = (group.getInstanceStartTime() + 14400000) - System.currentTimeMillis();

            SpawnTemplate spawn;

            if (timeRemain > (CustomConfig.DARKPOETA_GRADE_S_TIME * 1000) && totalPoints > CustomConfig.DARKPOETA_GRADE_S_POINTS) {
                for (Player member : group.getMembers()) {
                    PacketSendUtility.sendMessage(member, "Raid grade is <S>");
                }
                spawn = SpawnEngine.getInstance().addNewSpawn(300040000, group.getGroupLeader().getInstanceId(), 215280, 1176f, 1227f, 145f, (byte) 14, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
            }
            else if (timeRemain > (CustomConfig.DARKPOETA_GRADE_A_TIME * 1000) && totalPoints > CustomConfig.DARKPOETA_GRADE_A_POINTS) {
                for (Player member : group.getMembers()) {
                    PacketSendUtility.sendMessage(member, "Raid grade is <A>");
                }
                spawn = SpawnEngine.getInstance().addNewSpawn(300040000, group.getGroupLeader().getInstanceId(), 215281, 1176f, 1227f, 145f, (byte) 14, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
            }
            else if (timeRemain > (CustomConfig.DARKPOETA_GRADE_B_TIME * 1000) && totalPoints > CustomConfig.DARKPOETA_GRADE_B_POINTS) {
                for (Player member : group.getMembers()) {
                    PacketSendUtility.sendMessage(member, "Raid grade is <B>");
                }
                spawn = SpawnEngine.getInstance().addNewSpawn(300040000, group.getGroupLeader().getInstanceId(), 215282, 1176f, 1227f, 145f, (byte) 14, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
            }
            else if (timeRemain > (CustomConfig.DARKPOETA_GRADE_C_TIME * 1000) && totalPoints > CustomConfig.DARKPOETA_GRADE_C_POINTS) {
                for (Player member : group.getMembers()) {
                    PacketSendUtility.sendMessage(member, "Raid grade is <C>");
                }
                spawn = SpawnEngine.getInstance().addNewSpawn(300040000, group.getGroupLeader().getInstanceId(), 215283, 1176f, 1227f, 145f, (byte) 14, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(spawn, group.getGroupLeader().getInstanceId());
            }
            else {
                for (Player member : group.getMembers()) {
                    PacketSendUtility.sendMessage(member, "Raid grade is <D>. You failed.");
                }
            }

            group.setGroupInstancePoints(0);
        }
    }

    private int calculatePointsReward(Monster monster, Player leader) {
        int pointsReward = 0;

        switch (monster.getObjectTemplate().getRank()) {
            case HERO:
                switch (monster.getObjectTemplate().getHpGauge()) {
                    case 21:
                        pointsReward = 786;
                        break;

                    default:
                        pointsReward = 300;
                }
                break;
            default:
                if (monster.getObjectTemplate().getRace() == null)
                    break;

                switch (monster.getObjectTemplate().getRace().getRaceId()) {
                    case 22:  //UNDEAD
                        pointsReward = 12;
                        break;
                    case 9:   //BROWNIE
                        pointsReward = 18;
                        break;
                    case 6:   //LIZARDMAN
                        pointsReward = 24;
                        break;
                    case 8:   //NAGA
                    case 18:  //DRAGON
                    case 24: //MAGICALMONSTER
                        pointsReward = 30;
                        break;
                    default:
                        pointsReward = 11;
                        break;
                }
        }

        //Drana
        if (monster.getObjectTemplate().getTemplateId() == 700520)
            pointsReward = 48;
        //Walls
        else if (monster.getObjectTemplate().getTemplateId() == 700518
              || monster.getObjectTemplate().getTemplateId() == 700558)
            pointsReward = 156;
        //Named1
        else if (monster.getObjectTemplate().getTemplateId() == 214841
              || monster.getObjectTemplate().getTemplateId() == 215431)
            pointsReward = 162;
        //Named2
        else if (monster.getObjectTemplate().getTemplateId() == 214842
              || monster.getObjectTemplate().getTemplateId() == 215429
              || monster.getObjectTemplate().getTemplateId() == 215430
              || monster.getObjectTemplate().getTemplateId() == 215432)
            pointsReward = 186;
        //Named3
        else if (monster.getObjectTemplate().getTemplateId() == 214871
              || monster.getObjectTemplate().getTemplateId() == 215386
              || monster.getObjectTemplate().getTemplateId() == 215428)
            pointsReward = 204;
        //Marabata
        else if (monster.getObjectTemplate().getTemplateId() == 214849
              || monster.getObjectTemplate().getTemplateId() == 214850
              || monster.getObjectTemplate().getTemplateId() == 214851)
            pointsReward = 318;
        //Generators
        else if (monster.getObjectTemplate().getTemplateId() == 214895
              || monster.getObjectTemplate().getTemplateId() == 214896
              || monster.getObjectTemplate().getTemplateId() == 214897)
            pointsReward = 372;
        //Atmach
        else if (monster.getObjectTemplate().getTemplateId() == 214843)
            pointsReward = 456;
        //Boss
        else if (monster.getObjectTemplate().getTemplateId() == 214864
              || monster.getObjectTemplate().getTemplateId() == 214880
              || monster.getObjectTemplate().getTemplateId() == 214894
              || monster.getObjectTemplate().getTemplateId() == 215387
              || monster.getObjectTemplate().getTemplateId() == 215388
              || monster.getObjectTemplate().getTemplateId() == 215389)
            pointsReward = 786;
        else if (monster.getObjectTemplate().getTemplateId() == 214904)
            pointsReward = 954;

        if (leader.getAbyssRank().getRank().getId() >= 10)
            pointsReward = Math.round(pointsReward * 1.1f);

        pointsReward = Math.round(pointsReward * CustomConfig.DARKPOETA_REWARD_POINT_RATE);

        return pointsReward;

    }

    public static DarkPoetaInstanceService getInstance() {
        return SingletonHolder.instance;
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final DarkPoetaInstanceService instance = new DarkPoetaInstanceService();
    }
}
