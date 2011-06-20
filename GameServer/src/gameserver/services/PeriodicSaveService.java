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
import gameserver.configs.main.PeriodicSaveConfig;
import gameserver.dao.InventoryDAO;
import gameserver.dao.ItemStoneListDAO;
import gameserver.model.gameobjects.Item;
import gameserver.model.items.GodStone;
import gameserver.model.items.ManaStone;
import gameserver.model.legion.Legion;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * @author ATracer
 */
public class PeriodicSaveService {
    private static final Logger log = Logger.getLogger(PeriodicSaveService.class);

    private Future<?> legionWhUpdateTask;

    public static final PeriodicSaveService getInstance() {
        return SingletonHolder.instance;
    }

    private PeriodicSaveService() {

        int DELAY_LEGION_ITEM = PeriodicSaveConfig.LEGION_ITEMS * 1000;

        legionWhUpdateTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new LegionWhUpdateTask(),
                DELAY_LEGION_ITEM, DELAY_LEGION_ITEM);
    }

    private class LegionWhUpdateTask implements Runnable {
        @Override
        public void run() {
            log.info("Legion WH update task started.");
            long startTime = System.currentTimeMillis();
            Iterator<Legion> legionsIterator = LegionService.getInstance().getCachedLegionIterator();
            int legionWhUpdated = 0;
            while (legionsIterator.hasNext()) {
                Legion legion = legionsIterator.next();
                List<Item> allItems = legion.getLegionWarehouse().getAllItems();
                try {
                    /**
                     * 1. save items first
                     */
                    for (Item item : allItems) {
                        DAOManager.getDAO(InventoryDAO.class).store(item, legion.getLegionId());
                    }

                    /**
                     * 2. save item stones
                     */
                    for (Item item : allItems) {
                        if (item.hasManaStones()) {
                            Set<ManaStone> manaStones = item.getItemStones();
                            DAOManager.getDAO(ItemStoneListDAO.class).storeManaStone(manaStones);
                        }
                        GodStone godStone = item.getGodStone();
                        if (godStone != null) {
                            DAOManager.getDAO(ItemStoneListDAO.class).store(godStone);
                        }
                    }
                }
                catch (Exception ex) {
                    log.error("Exception during periodic saving of legion WH " + ex.getCause() != null ? ex.getCause()
                            .getMessage() : "null");
                }

                legionWhUpdated++;
            }
            long workTime = System.currentTimeMillis() - startTime;
            log.info("Legion WH update: " + workTime + " ms, legions: " + legionWhUpdated + ".");
        }
    }

    /**
     * Save data on shutdown
     */
    public void onShutdown() {
        log.info("Starting data save on shutdown.");
        //save legion warehouse
        legionWhUpdateTask.cancel(false);
        new LegionWhUpdateTask().run();
        log.info("Data successfully saved.");
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final PeriodicSaveService instance = new PeriodicSaveService();
    }
}
