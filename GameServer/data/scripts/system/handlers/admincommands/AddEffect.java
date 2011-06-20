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
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.SkillTemplate;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

/**
 * @author ggadv2
 */
public class AddEffect extends AdminCommand {
    public AddEffect() {
            super("addeffect");
    }

    @Override
    public void executeCommand(final Player admin, String[] params) {
        if(admin.getAccessLevel() < AdminConfig.COMMAND_ADDEFFECT) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }

        if(params.length < 1) {
            PacketSendUtility.sendMessage(admin,
                LanguageHandler.translate(CustomMessageId.COMMAND_ADDEFFECT_SYNTAX));
            return;
        }

        VisibleObject target = admin.getTarget();
        Player effected = admin;
        int duration = 0;

        if (target instanceof Player)
                effected = (Player) target;

        SkillTemplate sTemplate = null;

        try {
                sTemplate = DataManager.SKILL_DATA.getSkillTemplate(Integer.parseInt(params[0]));
        }
        catch (Exception e) {
                PacketSendUtility.sendMessage(admin,
                    LanguageHandler.translate(CustomMessageId.COMMAND_WRONG_SKILL_ID));
                return;
        }

        if (params.length == 2) {
                duration = Integer.parseInt(params[1]) * 1000;
        }
        else {
                duration = sTemplate.getDuration();
                if (duration < 1)
                        duration = sTemplate.getEffects().getEffectsDuration();
        }

        Effect effect = new Effect(admin, effected, sTemplate, 1, duration);
        effected.getEffectController().addEffect(effect);
        effect.addAllEffectToSucess();
        effect.startEffect(true);
        PacketSendUtility.sendMessage(admin,
            LanguageHandler.translate(CustomMessageId.COMMAND_ADDEFFECT_SUCCESS,
                sTemplate.getName(), effected.getName(), (duration / 1000)));
    }
}
