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
package gameserver.model.siege;

import gameserver.ai.npcai.AggressiveAi;
import gameserver.ai.npcai.MonsterAi;
import gameserver.configs.main.CustomConfig;
import gameserver.controllers.FortressGeneralController;
import gameserver.model.alliance.PlayerAllianceGroup;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.PlayerGroup;
import gameserver.model.siege.SiegeRace;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.services.SiegeService;
import javolution.util.FastList;

public class FortressGeneral extends Npc {
    /**
     * @param objId
     * @param controller
     * @param spawn
     * @param objectTemplate
     */

    private int linkedFortressId;
    private SiegeRace siegeRace;
    private FastList<PlayerGroup> rewardGroups;
    private FastList<PlayerAllianceGroup> rewardAlliances;

    public FortressGeneral(int objId, FortressGeneralController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate, int fortressId, SiegeRace siegeRace) {
        super(objId, controller, spawn, objectTemplate);
        this.linkedFortressId = fortressId;
        this.siegeRace = siegeRace;

        this.rewardGroups = new FastList<PlayerGroup>();
        this.rewardAlliances = new FastList<PlayerAllianceGroup>();
    }

    public void registerGroup(PlayerGroup group) {
        if (!rewardGroups.contains(group))
            rewardGroups.add(group);
    }

    public void registerAllianceGroup(PlayerAllianceGroup group) {
        if (!rewardAlliances.contains(group))
            rewardAlliances.add(group);
    }

    public FastList<PlayerGroup> getRewardGroups() {
        return rewardGroups;
    }

    public FastList<PlayerAllianceGroup> getRewardAlliances() {
        return rewardAlliances;
    }

    public int getFortressId() {
        return this.linkedFortressId;
    }

    @Override
    public FortressGeneralController getController() {
        return (FortressGeneralController) super.getController();
    }

    @Override
    public void initializeAi() {
        if (isAggressive() && !CustomConfig.DISABLE_MOB_AGGRO)
            this.ai = new AggressiveAi();
        else
            this.ai = new MonsterAi();

        ai.setOwner(this);
    }

    @Override
    public boolean isEnemyPlayer(Player player) {
        SiegeRace siegeRace = SiegeService.getSiegeRaceFromRace(player.getCommonData().getRace());
        return siegeRace != this.siegeRace;
    }
}
