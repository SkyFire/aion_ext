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
package gameserver.network.aion;

import gameserver.network.aion.serverpackets.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class is holding opcodes for all server packets. It's used only to have all opcodes in one place
 *
 * @author Luno
 * @author alexa026
 * @author ATracer
 * @author avol
 * @author orz
 * @author Ares/Kaipo (1.9-2.0-2.1)
 * @author Magenik (1.9-2.0-2.1)
 * @author poolsharky27 (1.9-2.0-2.1)
 * @author oni
 * @author PZIKO333 (2.1)
 * 
 */
public class ServerPacketsOpcodes {
    private static Map<Class<? extends AionServerPacket>, Integer> opcodes = new HashMap<Class<? extends AionServerPacket>, Integer>();

    static {
        Set<Integer> idSet = new HashSet<Integer>();

        addPacketOpcode(SM_STATUPDATE_HP.class, 0x00, idSet);// 2.1
        addPacketOpcode(SM_STATUPDATE_DP.class, 0x01, idSet);// 2.1
        addPacketOpcode(SM_CHANNEL_INFO.class, 0x02, idSet);// 2.1
        addPacketOpcode(SM_MACRO_LIST.class, 0x04, idSet);// 2.1
        addPacketOpcode(SM_CHAT_INIT.class, 0x05, idSet);// 2.1
        addPacketOpcode(SM_NICKNAME_CHECK_RESPONSE.class, 0x06, idSet);// 2.1
        addPacketOpcode(SM_MACRO_RESULT.class, 0x07, idSet);// 2.1
        addPacketOpcode(SM_SET_BIND_POINT.class, 0x08, idSet);// 2.1
        addPacketOpcode(SM_ABYSS_RANK.class, 0x0A, idSet);// 2.1
        addPacketOpcode(SM_RIFT_ANNOUNCE.class, 0x0B, idSet);// 2.1
        addPacketOpcode(SM_PETITION.class, 0x0C, idSet);// 2.1
        addPacketOpcode(SM_RECIPE_DELETE.class, 0x0D, idSet);// Testing
        addPacketOpcode(SM_LEARN_RECIPE.class, 0x0E, idSet);// 2.1
        addPacketOpcode(SM_FRIEND_UPDATE.class, 0x0F, idSet);// 2.1
        addPacketOpcode(SM_FORTRESS_INFO.class, 0x10, idSet);// 2.1
        addPacketOpcode(SM_PLAYER_MOVE.class, 0x12, idSet);// 2.1
        addPacketOpcode(SM_TELEPORT_LOC.class, 0x13, idSet);// 2.1
        addPacketOpcode(SM_LOGIN_QUEUE.class, 0x14, idSet);// 2.1
        addPacketOpcode(SM_DELETE.class, 0x15, idSet);// 2.1
        addPacketOpcode(SM_SYSTEM_MESSAGE.class, 0x16, idSet);// 2.1
        addPacketOpcode(SM_MESSAGE.class, 0x17, idSet);// 2.1
        addPacketOpcode(SM_INVENTORY_UPDATE.class, 0x18, idSet);// 2.1
        addPacketOpcode(SM_INVENTORY_INFO.class, 0x19, idSet);// 2.1
        addPacketOpcode(SM_UPDATE_ITEM.class, 0x1A, idSet);// 2.1
        addPacketOpcode(SM_DELETE_ITEM.class, 0x1B, idSet);// 2.1
        addPacketOpcode(SM_UI_SETTINGS.class, 0x1D, idSet);// 2.1
        addPacketOpcode(SM_CASTSPELL.class, 0x1E, idSet);// 2.1
        addPacketOpcode(SM_PLAYER_INFO.class, 0x1F, idSet);// 2.1
        addPacketOpcode(SM_GATHER_UPDATE.class, 0x20, idSet);// 2.1
        addPacketOpcode(SM_GATHER_STATUS.class, 0x21, idSet);// 2.1
        addPacketOpcode(SM_ATTACK_STATUS.class, 0x22, idSet);// 2.1
        addPacketOpcode(SM_STATUPDATE_MP.class, 0x23, idSet);// 2.1
        addPacketOpcode(SM_DP_INFO.class, 0x26, idSet);// 2.1
        addPacketOpcode(SM_LEGION_UPDATE_NICKNAME.class, 0x28, idSet);// 2.1
        addPacketOpcode(SM_STATUPDATE_EXP.class, 0x27, idSet);// 2.1
        addPacketOpcode(SM_ENTER_WORLD_CHECK.class, 0x2A, idSet);// 2.1
        addPacketOpcode(SM_LEGION_TABS.class, 0x2B, idSet);// 2.1
        addPacketOpcode(SM_PLAYER_SPAWN.class, 0x2C, idSet);// 2.1
        addPacketOpcode(SM_NPC_INFO.class, 0x2D, idSet);// 2.1
        addPacketOpcode(SM_GATHERABLE_INFO.class, 0x2E, idSet);// 2.1
        addPacketOpcode(SM_QUESTION_WINDOW.class, 0x33, idSet);// 2.1
        addPacketOpcode(SM_MOVE.class, 0x34, idSet);// 2.1
        addPacketOpcode(SM_ATTACK.class, 0x35, idSet);// 2.1
        // addPacketOpcode(SM_UNKNOW.class,0x36, idSet);// 2.1
        addPacketOpcode(SM_TRANSFORM.class, 0x39, idSet);// 2.1
        addPacketOpcode(SM_DIALOG_WINDOW.class, 0x3B, idSet);// 2.1
        addPacketOpcode(SM_SELL_ITEM.class, 0x3D, idSet);// 2.1
        addPacketOpcode(SM_VIEW_PLAYER_DETAILS.class, 0x3E, idSet);// 2.1
        addPacketOpcode(SM_WEATHER.class, 0x40, idSet);// 2.1
        addPacketOpcode(SM_EMOTION.class, 0x42, idSet);// 2.1
        addPacketOpcode(SM_UPDATE_PLAYER_APPEARANCE.class, 0x43, idSet);// 2.1
        addPacketOpcode(SM_TIME_CHECK.class, 0x44, idSet);// 2.1
        addPacketOpcode(SM_GAME_TIME.class, 0x45, idSet);// 2.1
        addPacketOpcode(SM_TARGET_SELECTED.class, 0x46, idSet);// 2.1
        addPacketOpcode(SM_LOOKATOBJECT.class, 0x47, idSet);// 2.1
        addPacketOpcode(SM_CASTSPELL_END.class, 0x48, idSet);// 2.1
        addPacketOpcode(SM_STIGMA_SKILL_REMOVE.class, 0x4A, idSet);// 2.1
        addPacketOpcode(SM_SKILL_ACTIVATION.class, 0x4D, idSet);// 2.1
        addPacketOpcode(SM_SKILL_LIST.class, 0x4B, idSet);// 2.1
        addPacketOpcode(SM_SKILL_CANCEL.class, 0x49, idSet);// 2.1?
        addPacketOpcode(SM_ABNORMAL_STATE.class, 0x4E, idSet);// 2.1
        addPacketOpcode(SM_SKILL_COOLDOWN.class, 0x4F, idSet);// 2.1
        addPacketOpcode(SM_ABNORMAL_EFFECT.class, 0x51, idSet);// 2.1
        addPacketOpcode(SM_INFLUENCE_RATIO.class, 0x52, idSet);// 2.1
        addPacketOpcode(SM_FORTRESS_STATUS.class, 0x55, idSet);// 2.1
        addPacketOpcode(SM_SHOW_NPC_ON_MAP.class, 0x56, idSet);// 2.1
        addPacketOpcode(SM_NAME_CHANGE.class, 0x57, idSet);// 2.1
        addPacketOpcode(SM_GROUP_MEMBER_INFO.class, 0x58, idSet);// 2.1
        addPacketOpcode(SM_GROUP_INFO.class, 0x59, idSet);// 2.1
        addPacketOpcode(SM_ABYSS_ARTIFACT_INFO.class, 0x5F, idSet);// 2.1
        addPacketOpcode(SM_QUIT_RESPONSE.class, 0x61, idSet);// 2.1
        addPacketOpcode(SM_PLAYER_STATE.class, 0x63, idSet);// 2.1
        addPacketOpcode(SM_STARTED_QUEST_LIST.class, 0x64, idSet);// 2.1
        addPacketOpcode(SM_LEVEL_UPDATE.class, 0x65, idSet);// 2.1
        addPacketOpcode(SM_SUMMON_PANEL_REMOVE.class, 0x66, idSet);// 2.1
        addPacketOpcode(SM_KEY.class, 0x67, idSet);// 2.1
        addPacketOpcode(SM_EXCHANGE_ADD_ITEM.class, 0x68, idSet);// 2.1
        addPacketOpcode(SM_EXCHANGE_REQUEST.class, 0x69, idSet);// 2.1
        addPacketOpcode(SM_EXCHANGE_ADD_KINAH.class, 0x6A, idSet);// 2.1
        addPacketOpcode(SM_EMOTION_LIST.class, 0x6C, idSet);// 2.1
        addPacketOpcode(SM_EXCHANGE_CONFIRMATION.class, 0x6D, idSet);// 2.1
        addPacketOpcode(SM_TARGET_UPDATE.class, 0x6E, idSet);// 2.1
        addPacketOpcode(SM_PLASTIC_SURGERY.class, 0x70, idSet);// 2.1
        addPacketOpcode(SM_LEGION_UPDATE_SELF_INTRO.class, 0x74, idSet);// 2.1
        addPacketOpcode(SM_QUEST_LIST.class, 0x78, idSet);// 2.1
        addPacketOpcode(SM_RIFT_STATUS.class, 0xAB, idSet);// 2.1
        addPacketOpcode(SM_QUEST_ACCEPTED.class, 0x7B, idSet);// 2.1
        addPacketOpcode(SM_NEARBY_QUESTS.class, 0x7C, idSet);// 2.1
        addPacketOpcode(SM_PING_RESPONSE.class, 0x7F, idSet);// 2.1
        addPacketOpcode(SM_CUBE_UPDATE.class, 0x81, idSet);// 2.1
        addPacketOpcode(SM_PET.class, 0x82, idSet);// 2.1
        addPacketOpcode(SM_ITEM_COOLDOWN.class, 0x84, idSet);// 2.1
        addPacketOpcode(SM_PLAY_MOVIE.class, 0x86, idSet);// 2.1
        addPacketOpcode(SM_UPDATE_NOTE.class, 0x87, idSet);// 2.1
        addPacketOpcode(SM_LEGION_ADD_MEMBER.class, 0x8C, idSet);// 2.1
        addPacketOpcode(SM_LEGION_INFO.class, 0x8D, idSet);// 2.1
        addPacketOpcode(SM_LEGION_UPDATE_TITLE.class, 0x90, idSet);// 2.1
        addPacketOpcode(SM_LEGION_UPDATE_MEMBER.class, 0x8E, idSet);// 2.1
        addPacketOpcode(SM_LEGION_LEAVE_MEMBER.class, 0x8F, idSet);// 2.1
        addPacketOpcode(SM_SUMMON_PANEL.class, 0x96, idSet);// 2.1
        addPacketOpcode(SM_SUMMON_UPDATE.class, 0x98, idSet);// 2.1
        addPacketOpcode(SM_SUMMON_OWNER_REMOVE.class, 0x99, idSet);// 2.1
        addPacketOpcode(SM_LEGION_MEMBERLIST.class, 0x9A, idSet);// 2.1
        addPacketOpcode(SM_LEGION_EDIT.class, 0x9D, idSet);// 2.1
        addPacketOpcode(SM_MAIL_SERVICE.class, 0x9E, idSet);// 2.1
        addPacketOpcode(SM_WINDSTREAM.class, 0xA0, idSet);// 2.1
        addPacketOpcode(SM_FIND_GROUP.class, 0xC5, idSet);// 2.1
        addPacketOpcode(SM_SUMMON_USESKILL.class, 0xA1, idSet);// 2.1
        addPacketOpcode(SM_PRIVATE_STORE.class, 0xA5, idSet); // 2.1
        addPacketOpcode(SM_FRIEND_LIST.class, 0xA3, idSet);// 2.1
        addPacketOpcode(SM_GROUP_LOOT.class, 0xA4, idSet); // 2.1
        addPacketOpcode(SM_MAY_LOGIN_INTO_GAME.class, 0xA6, idSet);// 2.1
        addPacketOpcode(SM_ABYSS_RANK_UPDATE.class, 0xA7, idSet);// 2.1
        addPacketOpcode(SM_ABYSS_RANKING_LEGIONS.class, 0xA8, idSet);// 2.1
        addPacketOpcode(SM_ABYSS_RANKING_PLAYERS.class, 0xA9, idSet);// 2.1
        addPacketOpcode(SM_PLAYER_ID.class, 0xAA, idSet);// 2.1
        addPacketOpcode(SM_KISK_UPDATE.class, 0xAF, idSet);// 2.1
        addPacketOpcode(SM_PONG.class, 0xAD, idSet);// 2.1
        addPacketOpcode(SM_PRIVATE_STORE_NAME.class, 0xAE, idSet);// 2.1
        addPacketOpcode(SM_BROKER_ITEMS.class, 0xB1, idSet);// 2.1
        addPacketOpcode(SM_CRAFT_UPDATE.class, 0xB2, idSet);// 2.1
        addPacketOpcode(SM_CRAFT_ANIMATION.class, 0xB3, idSet);// 2.1
        addPacketOpcode(SM_ITEM_USAGE_ANIMATION.class, 0xB4, idSet);// 2.1
        addPacketOpcode(SM_ASCENSION_MORPH.class, 0xB5, idSet);// 2.1
        addPacketOpcode(SM_DUEL.class, 0xB6, idSet);// 2.1
        addPacketOpcode(SM_CUSTOM_SETTINGS.class, 0xB7, idSet);// 2.1
        addPacketOpcode(SM_PET_MOVE.class, 0xB8, idSet);// 2.1
        addPacketOpcode(SM_QUESTIONNAIRE.class, 0xBC, idSet);// 2.1

        addPacketOpcode(SM_RESURRECT.class, 0xC1, idSet); // 2.1

        addPacketOpcode(SM_DIE.class, 0xBE, idSet);// 2.1
        addPacketOpcode(SM_FORCED_MOVE.class, 0xC0, idSet);// 2.1
        addPacketOpcode(SM_WINDSTREAM_ANNOUNCE.class, 0xC3, idSet);// 2.1
        addPacketOpcode(SM_REPURCHASE.class, 0xC4, idSet);// 2.1
        addPacketOpcode(SM_UPDATE_WAREHOUSE_ITEM.class, 0xC8, idSet);// 2.1
        addPacketOpcode(SM_WAREHOUSE_INFO.class, 0xC7, idSet);// 2.1
        addPacketOpcode(SM_WAREHOUSE_UPDATE.class, 0xC6, idSet); // 2.1
        addPacketOpcode(SM_DELETE_WAREHOUSE_ITEM.class, 0xC9, idSet);
        addPacketOpcode(SM_CHARACTER_SELECT.class, 0xCE, idSet);// 2.1
        addPacketOpcode(SM_TITLE_INFO.class, 0xCF, idSet);// 2.1
        addPacketOpcode(SM_LEGION_EMBLEM.class, 0xD2, idSet);// Testing
        addPacketOpcode(SM_LEGION_UPDATE_EMBLEM.class, 0xD4, idSet);// 2.1
        addPacketOpcode(SM_LEGION_EMBLEM_SEND.class, 0xD5, idSet);// Testing
        addPacketOpcode(SM_LEGION_EMBLEM_SEND.class, 0xBF, idSet);// Testing
        addPacketOpcode(SM_ABYSS_ARTIFACT_INFO2.class, 0xD9, idSet);// 2.1
        addPacketOpcode(SM_ABYSS_ARTIFACT_INFO3.class, 0xDB, idSet);// 2.1
        addPacketOpcode(SM_BLOCK_RESPONSE.class, 0xDD, idSet);// 1.9?
        addPacketOpcode(SM_FRIEND_RESPONSE.class, 0xDE, idSet);// 2.1
        addPacketOpcode(SM_BLOCK_LIST.class, 0xDF, idSet);// 2.1
        addPacketOpcode(SM_FRIEND_NOTIFY.class, 0xE0, idSet);// 2.1
        addPacketOpcode(SM_USE_OBJECT.class, 0xE2, idSet);// 2.1
        addPacketOpcode(SM_TELEPORT_MAP.class, 0xE3, idSet);// 2.1
        addPacketOpcode(SM_L2AUTH_LOGIN_CHECK.class, 0xE4, idSet);// 2.1
        addPacketOpcode(SM_CREATE_CHARACTER.class, 0xE6, idSet);// 2.1
        addPacketOpcode(SM_CHARACTER_LIST.class, 0xE7, idSet);// 2.1
        addPacketOpcode(SM_RESTORE_CHARACTER.class, 0xE8, idSet);// 2.1
        addPacketOpcode(SM_DELETE_CHARACTER.class, 0xE9, idSet);// 2.1
        addPacketOpcode(SM_LOOT_STATUS.class, 0xEA, idSet);// 2.1
        addPacketOpcode(SM_TARGET_IMMOBILIZE.class, 0xEB, idSet);// 2.1
        addPacketOpcode(SM_RECIPE_LIST.class, 0xEC, idSet);// 2.1
        addPacketOpcode(SM_LOOT_ITEMLIST.class, 0xED, idSet);// 2.1
        addPacketOpcode(SM_SIEGE_LOCATION_INFO.class, 0xEE, idSet);// 2.1
        addPacketOpcode(SM_MANTRA_EFFECT.class, 0xEF, idSet);// 2.1
        addPacketOpcode(SM_PLAYER_SEARCH.class, 0xF0, idSet);// 2.1
        addPacketOpcode(SM_ALLIANCE_INFO.class, 0xF2, idSet);// 2.1
        addPacketOpcode(SM_ALLIANCE_MEMBER_INFO.class, 0xF5, idSet);// testing
        addPacketOpcode(SM_FLY_TIME.class, 0xF3, idSet);// 2.1
        addPacketOpcode(SM_LEAVE_GROUP_MEMBER.class, 0xF4, idSet);// 2.1
        addPacketOpcode(SM_SHOW_BRAND.class, 0xF6, idSet);// 2.1
        addPacketOpcode(SM_ALLIANCE_READY_CHECK.class, 0xF9, idSet);// 2.1
        addPacketOpcode(SM_TRADELIST.class, 0xFA, idSet);// 2.1
        addPacketOpcode(SM_PRICES.class, 0xFB, idSet);// 2.1
        addPacketOpcode(SM_RECONNECT_KEY.class, 0xFC, idSet);// 2.1
        addPacketOpcode(SM_STATS_INFO.class, 0xFE, idSet);// 2.1
        addPacketOpcode(SM_VERSION_CHECK.class, 0xFF, idSet);// 2.1
        addPacketOpcode(SM_CUSTOM_PACKET.class, 99999, idSet);// fake packet
        addPacketOpcode(SM_TOLL_INFO.class, 0x9C, idSet);
        addPacketOpcode(SM_IN_GAME_SHOP_CATEGORY_LIST.class, 0xCB, idSet);
        addPacketOpcode(SM_IN_GAME_SHOP_LIST.class, 0xCA, idSet);
        addPacketOpcode(SM_IN_GAME_SHOP_ITEM.class, 0xCD, idSet);

        // Unrecognized Opcodes from 1.5.4:
        // addPacketOpcode(SM_BUY_LIST.class, 0x7E, idSet);

        // Unrecognized Opcodes from 1.5.0:
        // addPacketOpcode(SM_VIRTUAL_AUTH.class, 0xE4, idSet);
        // addPacketOpcode(SM_WAITING_LIST.class, 0x18, idSet);
    }

    static int getOpcode(Class<? extends AionServerPacket> packetClass) {
        Integer opcode = opcodes.get(packetClass);
        if (opcode == null) {
            throw new IllegalArgumentException("There is no opcode for " + packetClass + " defined.");
        }

        return opcode;
    }

    private static void addPacketOpcode(Class<? extends AionServerPacket> packetClass, int opcode, Set<Integer> idSet) {
        if (opcode < 0) {
            return;
        }

        if (idSet.contains(opcode)) {
            throw new IllegalArgumentException(String.format("There already exists another packet with id 0x%02X", opcode));
        }

        idSet.add(opcode);
        opcodes.put(packetClass, opcode);
    }
}
