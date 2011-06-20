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

package quest;

import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;

public class _90000Dredgion extends QuestHandler {
    private static int questId = 90000;

    public _90000Dredgion() {
        super(questId);
    }

    @Override
    public void register() {
        qe.addOnDie(questId);
        qe.addOnEnterWorld(questId);
        qe.setNpcQuestData(214823).addOnKillEvent(questId);
    }

    @Override
    public boolean onDieEvent(QuestCookie env) {
        Player player = env.getPlayer();
        if (!player.isInDredgion())
            return false;
        player.getDredgion().onDieEvent(player);
        return true;
    }

    @Override
    public boolean onEnterWorldEvent(QuestCookie env) {
        Player player = env.getPlayer();
        if (player.getWorldId() != 300110000 && player.isInDredgion()) {
            player.getDredgion().onLeaveEvent(player);
            player.setDredgion(null);
        }
        return true;
    }

    @Override
    public boolean onKillEvent(QuestCookie env) {
        Player player = env.getPlayer();
        if (env.getTargetId() != 214823)
            return false;
        else if (!player.isInDredgion())
            return false;
        else player.getDredgion().onKillEvent(player);
        return true;
    }
}
