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

import gameserver.ai.events.Event;
import gameserver.controllers.NpcController;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.NpcTemplate;
import gameserver.model.templates.WorldMapTemplate;
import gameserver.model.templates.portal.ExitPoint;
import gameserver.model.templates.portal.PortalTemplate;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.InstanceService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.WorldMapInstance;

/**
 * @author PZIKO333
 *
 */
public class KromedesTrialController extends NpcController {

	@Override
	public void onDialogRequest(final Player player) {
		getOwner().getAi().handleEvent(Event.TALK);

		NpcTemplate npctemplate = DataManager.NPC_DATA.getNpcTemplate(getOwner().getNpcId());
		if (npctemplate.getTitleId() == 370315) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
			return;
		}
	}

	@Override
	public void onDialogSelect(int dialogId, final Player player, int questId) {
		Npc npc = getOwner();
		int targetObjectId = npc.getObjectId();

		if (dialogId == 10000 && (npc.getNpcId() == 205229 || npc.getNpcId() == 205234)) {

			if (player.getLevel() < 37 || player.getLevel() > 44) {
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1055));
				return;
			}
			if (player.getPlayerGroup() != null) {
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1013));
				return;
			}

			WorldMapTemplate world = DataManager.WORLD_MAPS_DATA.getTemplate(300230000);
			if (!InstanceService.onRegisterRequest(player, world.getInstanceId(), world.getCooldown())) {
				int timeinMinutes = InstanceService.getTimeInfo(player).get(world.getInstanceId()) / 60;
				if (timeinMinutes >= 60)
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_HOUR(401255, timeinMinutes / 60));
				else
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_MIN(401255, timeinMinutes));

				return;
			}

			WorldMapInstance instance = InstanceService.getRegisteredInstance(300230000, player.getObjectId());
			if (instance == null) {
				instance = InstanceService.getNextAvailableInstance(300230000);
			}

			PortalTemplate portalTemplate = DataManager.PORTAL_DATA.getPortalTemplate(getOwner().getNpcId());
			ExitPoint exit = null;
			for (ExitPoint point : portalTemplate.getExitPoint()) {
				if (point.getRace() == null || point.getRace().equals(player.getCommonData().getRace()))
					exit = point;
			}
			TeleportService.teleportTo(player, exit.getMapId(), instance.getInstanceId(), exit.getX(), exit.getY(), exit.getZ(), 0);
			return;
		}
	}
}
