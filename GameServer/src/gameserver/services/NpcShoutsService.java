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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import gameserver.dao.NpcShoutsDAO;
import gameserver.model.NpcShout;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.Executor;
import gameserver.world.World;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

/**
 * This class is handling NPC automatic shouts
 *
 * @author zdead
 */
public class NpcShoutsService {
    private static final Logger log = Logger.getLogger(NpcShoutsService.class);

    private FastMap<Integer, NpcShout> shoutsCache;

    public NpcShoutsService() {
        log.info("Starting loading NPC shouts from database ...");
        shoutsCache = DAOManager.getDAO(NpcShoutsDAO.class).getShouts();
        log.info("Successfully loaded " + shoutsCache.size() + " NPC shouts");
        World.getInstance().doOnAllNpcs(new Executor<Npc>() {
            @Override
            public boolean run(Npc obj) {
                final int npcId = ((Npc) obj).getNpcId();
                final int objectId = obj.getObjectId();

                if (shoutsCache.containsKey(npcId)) {
                    NpcShout shout = shoutsCache.get(npcId);
                    final int messageId = shout.getMessageId();
                    final int interval = Rnd.get(shout.getInterval(), shout.getInterval() * 2);

                    ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            AionObject npcObj = World.getInstance().findAionObject(objectId);
                            if (npcObj != null && npcObj instanceof Npc) {
                                Npc npc2 = (Npc) npcObj;
                                final int npc2Oid = npc2.getObjectId();

                                npc2.getKnownList().doOnAllPlayers(new Executor<Player>() {
                                    @Override
                                    public boolean run(Player kObj) {
                                        PacketSendUtility.sendPacket((Player) kObj, new SM_SYSTEM_MESSAGE(messageId, true, npc2Oid));
                                        return true;
                                    }
                                });
                            }
                        }
                    }, 0, interval * 1000);
                }
                return true;
            }
        });
    }

    public static final NpcShoutsService getInstance() {
        return SingletonHolder.instance;
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final NpcShoutsService instance = new NpcShoutsService();
    }

}
