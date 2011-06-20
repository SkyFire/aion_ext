/*
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is pryvate software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class CustomConfig {
    /**
     * Enable or Disable Character Passkey
     */
    @Property(key = "passkey.enable", defaultValue = "true")
    public static boolean PASSKEY_ENABLE;

    /**
     * Enter the maximum number of incorrect password set
     */
    @Property(key = "passkey.wrong.maxcount", defaultValue = "5")
    public static int PASSKEY_WRONG_MAXCOUNT;
    /**
     * Factions speaking mode
     */
    @Property(key = "gameserver.factions.speaking.mode", defaultValue = "0")
    public static int FACTIONS_SPEAKING_MODE;

    /*
     * Factions search mode
     */
    @Property(key = "gameserver.factions.search.mode", defaultValue = "false")
    public static boolean FACTIONS_SEARCH_MODE;

    /**
     * Skill autolearn
     */
    @Property(key = "gameserver.skill.autolearn", defaultValue = "false")
    public static boolean SKILL_AUTOLEARN;

    /**
     * Stigma autolearn
     */
    @Property(key = "gameserver.stigma.autolearn", defaultValue = "false")
    public static boolean STIGMA_AUTOLEARN;

     /**
     * Stigma level antihack
     */
    @Property(key = "gameserver.stigma.antihack", defaultValue = "level")
    public static String STIGMA_ANTIHACK;

    /**
     * Advanced Stigma level antihack
     */
    @Property(key = "gameserver.advstigma.antihack", defaultValue = "level")
    public static String ADVANCED_STIGMA_ANTIHACK;

    /**
     * Retail like char deletion
     */
    @Property(key = "gameserver.character.delete.retail", defaultValue = "true")
    public static boolean RETAIL_CHAR_DELETION;

    /**
     * Disable monsters aggressive behave
     */
    @Property(key = "gameserver.disable.mob.aggro", defaultValue = "false")
    public static boolean DISABLE_MOB_AGGRO;

    /**
     * Enable 2nd class change simple mode
     */
    @Property(key = "gameserver.enable.simple.2ndclass", defaultValue = "false")
    public static boolean ENABLE_SIMPLE_2NDCLASS;

    /**
     * Unstuck delay
     */
    @Property(key = "gameserver.unstuck.delay", defaultValue = "3600")
    public static int UNSTUCK_DELAY;

    /**
     * Enable instances
     */
    @Property(key = "gameserver.instances.enable", defaultValue = "true")
    public static boolean ENABLE_INSTANCES;

    /**
     * Base Fly Time
     */
    @Property(key = "gameserver.base.flytime", defaultValue = "60")
    public static int BASE_FLYTIME;

    /**
     * Allows players of opposite factions to bind in enemy territory
     */
    @Property(key = "gameserver.cross.faction.binding", defaultValue = "false")
    public static boolean ENABLE_CROSS_FACTION_BINDING;

    /**
     * Disable drop rate reduction based on level diference between players and mobs
     */
    @Property(key = "gameserver.disable.drop.reduction", defaultValue = "false")
    public static boolean DISABLE_DROP_REDUCTION;

    /**
     * Allowed Kills in time period for full AP. Move to separate config when more pvp options.
     */
    @Property(key = "gameserver.pvp.maxkills", defaultValue = "5")
    public static int MAX_DAILY_PVP_KILLS;

    /**
     * Time period for max daily kills in pvp
     */
    @Property(key = "gameserver.pvp.period", defaultValue = "24")
    public static int DAILY_PVP_PERIOD;

    /**
     * Enable customs channels
     */
    @Property(key = "gameserver.channels.all.enabled", defaultValue = "false")
    public static boolean CHANNEL_ALL_ENABLED;

    /**
     * Enable custom channel .world
     */
    @Property(key = "gameserver.channels.world.enabled", defaultValue = "false")
    public static boolean CHANNEL_WORLD_ENABLED;

    /**
     * Enable V-research showing all connected players from both faction for GMs
     */
    @Property(key = "gameserver.search.listall", defaultValue = "false")
    public static boolean SEARCH_LIST_ALL;

    /**
     * Enable or disable gm tags
     */
    @Property(key = "gameserver.gmtag.display", defaultValue = "false")
    public static boolean GMTAG_DISPLAY;

    @Property(key = "gameserver.gmtag.level1", defaultValue = "<HELPER>")
    public static String GM_LEVEL1;

    @Property(key = "gameserver.gmtag.level2", defaultValue = "<GM>")
    public static String GM_LEVEL2;

    @Property(key = "gameserver.gmtag.level3", defaultValue = "<HEADGM>")
    public static String GM_LEVEL3;

    @Property(key = "gameserver.gmtag.level4", defaultValue = "<ADMIN>")
    public static String GM_LEVEL4;

    @Property(key = "gameserver.gmtag.level5", defaultValue = "<HEADADMIN>")
    public static String GM_LEVEL5;

    /**
     * Announce on GM connection
     */
    @Property(key = "gameserver.announce.gm.connection", defaultValue = "false")
    public static boolean ANNOUNCE_GM_CONNECTION;

    /**
     * Invis on GM connection
     */
    @Property(key = "gameserver.invis.gm.connection", defaultValue = "false")
    public static boolean INVIS_GM_CONNECTION;

    /**
     * Invul on GM connection
     */
    @Property(key = "gameserver.invul.gm.connection", defaultValue = "false")
    public static boolean INVUL_GM_CONNECTION;

    /**
     * Silence on GM connection
     */
    @Property(key = "gameserver.silence.gm.connection", defaultValue = "false")
    public static boolean SILENCE_GM_CONNECTION;

    /**
     * Speed on GM connection
     */
    @Property(key = "gameserver.speed.gm.connection", defaultValue = "0")
    public static int SPEED_GM_CONNECTION;

    /**
     * Enable or disable instance cooldown
     */
    @Property(key = "gameserver.instance.cooldown", defaultValue = "true")
    public static boolean INSTANCE_COOLDOWN;


    /**
     * Enable or disable Global announce for rare drops
     */
    @Property(key = "gameserver.announce.raredrops", defaultValue = "false")
    public static boolean ANNOUNCE_RAREDROPS;

    /**
     * Enable or disable Kick players using speed hack
     */
    @Property(key = "gameserver.kick.speedhack.enable", defaultValue = "true")
    public static boolean KICK_SPEEDHACK;

    /**
     * Ping minimun Interval to consider hack
     */
    @Property(key = "gameserver.kick.speedhack.pinginterval", defaultValue = "100000")
    public static long KICK_PINGINTERVAL;

    /**
     * Chain trigger rate. If false all Chain are 100% success.
     */
    @Property(key = "gameserver.skill.chain.triggerrate", defaultValue = "true")
    public static boolean SKILL_CHAIN_TRIGGERRATE;

    /**
     * Add a reward to player for pvp kills
     */
    @Property(key = "gameserver.pvpreward.enable", defaultValue = "false")
    public static boolean PVPREWARD_ENABLE;

    /**
     * Kills needed for item reward
     */
    @Property(key = "gameserver.pvpreward.kills.needed1", defaultValue = "5")
    public static int PVPREWARD_KILLS_NEEDED1;

    @Property(key = "gameserver.pvpreward.kills.needed2", defaultValue = "10")
    public static int PVPREWARD_KILLS_NEEDED2;

    @Property(key = "gameserver.pvpreward.kills.needed3", defaultValue = "15")
    public static int PVPREWARD_KILLS_NEEDED3;

    /**
     * Item Rewards
     */
    @Property(key = "gameserver.pvpreward.item.reward1", defaultValue = "186000031")
    public static int PVPREWARD_ITEM_REWARD1;

    @Property(key = "gameserver.pvpreward.item.reward2", defaultValue = "186000030")
    public static int PVPREWARD_ITEM_REWARD2;

    @Property(key = "gameserver.pvpreward.item.reward3", defaultValue = "186000096")
    public static int PVPREWARD_ITEM_REWARD3;

    /**
     * Send to the prison (artmoney hack)
     */
    @Property(key = "gameserver.artmoney.hack", defaultValue = "true")
    public static boolean ARTMONEY_HACK;

    @Property(key = "gameserver.artmoney.hackbuy.time", defaultValue = "120")
    public static int ARTMONEY_HACKBUY_TIME;

    /**
     * Player Search Level Restriction (Level 10)
     */
    @Property(key = "search.level.restriction", defaultValue = "10")
    public static int LEVEL_TO_SEARCH;

    /**
     * Whisper Level Restriction (Level 10)
     */
    @Property(key = "whisper.level.restriction", defaultValue = "10")
    public static int LEVEL_TO_WHISPER;

    @Property(key = "gameserver.player.experience.control", defaultValue = "false")
    public static boolean PLAYER_EXPERIENCE_CONTROL;

    /**
     * Time in seconds which character stays online after closing client window
     */
    @Property(key = "gameserver.disconnect.time", defaultValue = "10")
    public static int DISCONNECT_DELAY;

    /**
     * Enable Surveys
     */
    @Property(key = "gameserver.enable.surveys", defaultValue = "true")
    public static boolean ENABLE_SURVEYS;

    /**
     * Enable the HTML Welcome Message Window on Player Login
     */
    @Property(key = "enable.html.welcome", defaultValue = "false")
    public static boolean ENABLE_HTML_WELCOME;

    /**
     * Minimum level for item remodelisation
     */
    @Property(key = "gameserver.itemremodel.minlevel", defaultValue = "20")
    public static int ITEM_REMODEL_MINLEVEL;

    /**
     * Transport price multiplier
     */
    @Property(key = "gameserver.transport.price.multiplier", defaultValue = "0.8")
    public static float TRANSPORT_COST_MULTIPLIER;

    /**
     * Soul healing price multiplier
     */
    @Property(key = "gameserver.soulhealing.price.multiplier", defaultValue = "1")
    public static float SOULHEALING_PRICE_MULTIPLIER;

    /**
     * Minimum level rift
     */
    @Property(key = "gameserver.rift.minimum.level", defaultValue = "25")
    public static int RIFT_MIN_LEVEL;

    /**
     * Remove title restriction for portals
     */
    @Property(key = "gameserver.portal.requirement.title", defaultValue = "false")
    public static boolean PORTAL_REQUIREMENT_TITLE;

    /**
     * Remove race restriction for portals
     */
    @Property(key = "gameserver.portal.requirement.race", defaultValue = "false")
    public static boolean PORTAL_REQUIREMENT_RACE;

    /**
     * Remove level restriction for portals
     */
    @Property(key = "gameserver.portal.requirement.level", defaultValue = "false")
    public static boolean PORTAL_REQUIREMENT_LEVEL;

    /**
     * Dark Poeta configuration
     */
    @Property(key = "gameserver.darkpoeta.reward.point.rate", defaultValue = "1.0")
    public static float DARKPOETA_REWARD_POINT_RATE;

    @Property(key = "gameserver.darkpoeta.grade.S.time", defaultValue = "7200000")
    public static int DARKPOETA_GRADE_S_TIME;

    @Property(key = "gameserver.darkpoeta.grade.S.points", defaultValue = "20000")
    public static int DARKPOETA_GRADE_S_POINTS;

    @Property(key = "gameserver.darkpoeta.grade.A.time", defaultValue = "5400000")
    public static int DARKPOETA_GRADE_A_TIME;

    @Property(key = "gameserver.darkpoeta.grade.A.points", defaultValue = "17100")
    public static int DARKPOETA_GRADE_A_POINTS;

    @Property(key = "gameserver.darkpoeta.grade.B.time", defaultValue = "3600000")
    public static int DARKPOETA_GRADE_B_TIME;

    @Property(key = "gameserver.darkpoeta.grade.B.points", defaultValue = "13100")
    public static int DARKPOETA_GRADE_B_POINTS;

    @Property(key = "gameserver.darkpoeta.grade.C.time", defaultValue = "1800000")
    public static int DARKPOETA_GRADE_C_TIME;

    @Property(key = "gameserver.darkpoeta.grade.C.points", defaultValue = "11000")
    public static int DARKPOETA_GRADE_C_POINTS;

    /**
     * Time when top ranking is updated
     */
    @Property(key = "gameserver.topranking.time", defaultValue = "0:00:00")
    public static String TOP_RANKING_TIME;

    /**
     * Time between updates of top ranking
     */
    @Property(key = "gameserver.topranking.delay", defaultValue = "24")
    public static int TOP_RANKING_DELAY;

    /**
     * Time between using worldchat
     */
    @Property(key = "gameserver.chat.talkdelay", defaultValue = "10")
    public static int TALK_DELAY;

    /**
     * Time Length of Duel Battles.
     */
    @Property(key = "gameserver.duel.length", defaultValue = "300")
    public static int DUEL_LENGTH;

    /**
     * Size of Text Messages per packet.
     */
    @Property(key = "gameserver.message.length", defaultValue = "512")
    public static int MESSAGE_LENGTH;

    /**
     *
     */
    @Property(key = "gameserver.drops.scoring_enable", defaultValue = "true")
    public static boolean SCORING_DROP_ENABLE;

    /**
     *
     */
    @Property(key = "gameserver.drops.npc.normal", defaultValue = "65")
    public static int DROP_POINTS_NPC_NORMAL;

    /**
     *
     */
    @Property(key = "gameserver.drops.npc.elite", defaultValue = "175")
    public static int DROP_POINTS_NPC_ELITE;

    /**
     *
     */
    @Property(key = "gameserver.drops.npc.junk", defaultValue = "225")
    public static int DROP_POINTS_NPC_JUNK;

	/**
     *
     */
    @Property(key = "gameserver.drops.npc.hero", defaultValue = "350")
    public static int DROP_POINTS_NPC_HERO;

    /**
     *
     */
    @Property(key = "gameserver.drops.npc.champion", defaultValue = "600")
    public static int DROP_POINTS_NPC_CHAMPION;

    /**
     *
     */
    @Property(key = "gameserver.drops.item.limit.mythic", defaultValue = "1")
    public static int DROP_LIMIT_ITEM_MYTHIC;

    /**
     *
     */
    @Property(key = "gameserver.drops.item.limit.epic", defaultValue = "1")
    public static int DROP_LIMIT_ITEM_EPIC;

    /**
     *
     */
    @Property(key = "gameserver.drops.item.limit.unique", defaultValue = "1")
    public static int DROP_LIMIT_ITEM_UNIQUE;

    /**
     *
     */
    @Property(key = "gameserver.drops.item.limit.legend", defaultValue = "1")
    public static int DROP_LIMIT_ITEM_LEGEND;

    /**
     *
     */
    @Property(key = "gameserver.drops.item.limit.rare", defaultValue = "2")
    public static int DROP_LIMIT_ITEM_RARE;

    /**
     *
     */
    @Property(key = "gameserver.drops.item.limit.common", defaultValue = "3")
    public static int DROP_LIMIT_ITEM_COMMON;

    /**
     *
     */
    @Property(key = "gameserver.drops.item.limit.junk", defaultValue = "5")
    public static int DROP_LIMIT_ITEM_JUNK;

    /**
     *
     */
    @Property(key = "gameserver.drops.item.points.mythic", defaultValue = "400")
    public static int DROP_POINTS_ITEM_MYTHIC;

    /**
     *
     */
    @Property(key = "gameserver.drops.item.points.epic", defaultValue = "200")
    public static int DROP_POINTS_ITEM_EPIC;

    /**
     *
     */
    @Property(key = "gameserver.drops.item.points.unique", defaultValue = "100")
    public static int DROP_POINTS_ITEM_UNIQUE;

    /**
     *
     */
    @Property(key = "gameserver.drops.item.points.legend", defaultValue = "50")
    public static int DROP_POINTS_ITEM_LEGEND;

    /**
     *
     */
    @Property(key = "gameserver.drops.item.points.rare", defaultValue = "25")
    public static int DROP_POINTS_ITEM_RARE;

    /**
     *
     */
    @Property(key = "gameserver.drops.item.points.common", defaultValue = "10")
    public static int DROP_POINTS_ITEM_COMMON;

    /**
     *
     */
    @Property(key = "gameserver.drops.item.points.junk", defaultValue = "1")
    public static int DROP_POINTS_ITEM_JUNK;
}
