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

package gameserver.network.aion.clientpackets;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.spawnengine.SpawnEngine;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;

/**
 * @author xitanium
 */
public class CM_MAIL_SUMMON_ZEPHYR extends AionClientPacket {

    private int value;

    public CM_MAIL_SUMMON_ZEPHYR(int opcode) {
        super(opcode);
    }


    @Override
    protected void readImpl() {
        value = readC();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        final Player player = getConnection().getActivePlayer();
        if (player != null && value == 1) {
            int zephyrNpcId = 0;

            switch (player.getCommonData().getRace()) {
                case ELYOS:
                    zephyrNpcId = 798044;
                    break;
                case ASMODIANS:
                    zephyrNpcId = 798101;
                    break;
            }

            if (zephyrNpcId == 0)
                return;

            // POSTMAN_ALREADY_SUMMONED
            if (player.getZephyrObjectId() != 0) {
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300877));
                return;
            }

            // POSTMAN_COOLDOWN (30mn)
            if (player.getLastZephyrInvokationSeconds() > (System.currentTimeMillis() / 1000) - 1800) {
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300878));
                return;
            }

            if (player.isInState(CreatureState.FLYING) || player.isInState(CreatureState.FLIGHT_TELEPORT) || player.isInState(CreatureState.GLIDING)) {
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300879));
                return;
            }

            SpawnTemplate zst = SpawnEngine.getInstance().addNewSpawn(player.getWorldId(), player.getInstanceId(), zephyrNpcId, player.getX(), player.getY(), player.getZ(), player.getHeading(), 0, 0, true, true);
            VisibleObject zvo = SpawnEngine.getInstance().spawnObject(zst, player.getInstanceId());

            if (zvo != null && zvo instanceof Creature) {
                final Creature zc = (Creature) zvo;
                player.setZephyrObjectId(zc.getObjectId());
                player.setLastZephyrInvokationSeconds(System.currentTimeMillis() / 1000);
                zc.setTarget(player);
                zc.getMoveController().setFollowTarget(true);
                zc.getMoveController().schedule();

                // Despawn Zephyr after 5 minutes if not already despawned
                ThreadPoolManager.getInstance().schedule(new Runnable() {

                    @Override
                    public void run() {
                        int zid = zc.getObjectId();
                        AionObject obj = World.getInstance().findAionObject(zid);
                        if (obj != null && obj instanceof Creature) {
                            Creature zephyr = (Creature) obj;
                            DataManager.SPAWNS_DATA.removeSpawn(zephyr.getSpawn());
                            zephyr.getController().delete();
                        }
                        player.setZephyrObjectId(0);
                    }
                }, 300000);
            }
        }
    }
}