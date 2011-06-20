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

package usercommands;

import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.SkillLearnService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

/**
 *
 *
 */
public class GiveMissingSkills extends UserCommand {
    public GiveMissingSkills() {
        super("givemissingskills");
    }

    private int countSkills(Player player) {
        int count = 0;

        for (int i = 0; i < player.getSkillList().getAllSkills().length; i++) {
            if (!player.getSkillList().getAllSkills()[i].isStigma()) {
                count++;
            }
        }

        return count;
    }

    private int countStigmaSkills(Player player) {
        int count = 0;

        for (int i = 0; i < player.getSkillList().getAllSkills().length; i++) {
            if (player.getSkillList().getAllSkills()[i].isStigma()) {
                count++;
            }
        }

        return count;
    }

    @Override
    public void executeCommand(Player player, String params) {
        if (!CustomConfig.SKILL_AUTOLEARN) {
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_DISABLED));
            return;
        }

        int bStigmaCount = countStigmaSkills(player);
        int bSkillCount = countSkills(player);

        SkillLearnService.addMissingSkills(player);

        int aStigmaCount = countStigmaSkills(player);
        int aSkillCount = countSkills(player);

        if (CustomConfig.STIGMA_AUTOLEARN) {
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_MISSING_SKILLS_STIGMAS_ADDED, aSkillCount - bSkillCount, aStigmaCount - bStigmaCount));
        } else {
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_MISSING_SKILLS_ADDED, aSkillCount - bSkillCount));
        }
    }
}
