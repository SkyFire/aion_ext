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

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.skillengine.model.SkillTemplate;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

/**
 * @author iopiop, ggadv2
 */
public class Dispel extends AdminCommand {
    public Dispel() {
        super("dispel");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_DISPEL) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }

        if(params.length < 1) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_DISPEL_SYNTAX));
            return;
        }

        VisibleObject target = admin.getTarget();
        Player effected = admin;

        if (target instanceof Player)
            effected = (Player) target;

        SkillTemplate sTemplate = null;

        if (params[0].equals("all")) {
            effected.getEffectController().removeAllEffects();
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_DISPEL_EFFECT_ALL, effected.getName()));
        }
        else {
            try {
                sTemplate = DataManager.SKILL_DATA.getSkillTemplate(Integer.parseInt(params[0]));
            }
            catch (Exception e) {
                PacketSendUtility.sendMessage(admin,
                    LanguageHandler.translate(CustomMessageId.COMMAND_WRONG_SKILL_ID));
                return;
            }
            effected.getEffectController().removeEffect(sTemplate.getSkillId());
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_DISPEL_EFFECT, sTemplate.getName(), sTemplate.getName()));
            return;
        }
    }
}