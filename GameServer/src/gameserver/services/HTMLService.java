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
import gameserver.configs.main.GSConfig;
import gameserver.dao.SurveyDAO;
import gameserver.model.gameobjects.Survey;
import gameserver.model.gameobjects.SurveyOption;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_QUESTIONNAIRE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.idfactory.IDFactory;
import gameserver.world.Executor;
import gameserver.world.World;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Use this service to send raw html to the client
 * Absolute alpha phase. Not yet tested what is allowed
 *
 * @author lhw, ginho1, ZeroSignal
 */
public class HTMLService {
    private static final Logger log = Logger.getLogger(HTMLService.class);

    private static String HTMLTemplate(Survey survey) {
        int itemId = survey.getItemId();
        int itemCount = survey.getItemCount();

        if (survey.getSurveyOptions() == null)
            return null;

        StringBuilder sb = new StringBuilder();

        sb.append("<poll>\n");
        sb.append("<poll_introduction>\n");
        sb.append("	<![CDATA[<font color='4CB1E5'>" + survey.getTitle() + "</font>]]>\n");
        sb.append("</poll_introduction>\n");
        sb.append("<poll_title>\n");
        sb.append("	<font color='ffc519'></font>\n");
        sb.append("</poll_title>\n");
        sb.append("<start_date>2010-08-08 00:00</start_date>\n");
        sb.append("<end_date>2010-09-14 01:00</end_date>\n");
        sb.append("<servers></servers>\n");
        sb.append("<order_num></order_num>\n");
        sb.append("<race></race>\n");
        sb.append("<main_class></main_class>\n");
        sb.append("<world_id></world_id>\n");

        sb.append("<item_id>");
        if (itemId > 0 && itemCount > 0) {
            sb.append(itemId);
        }
        if (itemId == 0 && itemCount == 0 && survey.getSurveyOptionsSize() == 1) {
            SurveyOption surveyOption = survey.getSurveyOption(0);
            if (surveyOption.getItemId() > 0) {
                sb.append(surveyOption.getItemId());
            }
        }
        else {
            for (SurveyOption surveyOption : survey.getSurveyOptions()) {
                if (surveyOption.getItemId() > 0) {
                    sb.append("," + surveyOption.getItemId());
                }
            }
        }
        sb.append("</item_id>\n");

        sb.append("<item_cnt>");
        if (itemId > 0 && itemCount > 0) {
            sb.append(itemCount);
        }
        if (itemId == 0 && itemCount == 0 && survey.getSurveyOptionsSize() == 1) {
            SurveyOption surveyOption = survey.getSurveyOption(0);
            if (surveyOption.getItemId() > 0) {
                sb.append(surveyOption.getItemCount());
            }
        }
        else {
            for (SurveyOption surveyOption : survey.getSurveyOptions()) {
                if (surveyOption.getItemId() > 0) {
                    sb.append("," + surveyOption.getItemCount());
                }
            }
        }
        sb.append("</item_cnt>\n");

        if (survey.getPlayerLevelMin() > 0 && survey.getPlayerLevelMax() > 0)
            sb.append("<level>" + survey.getPlayerLevelMin() + "~" + survey.getPlayerLevelMax() + "</level>\n");
        else
            sb.append("<level>1~55</level>\n");
        sb.append("<questions>\n");
        sb.append("	<question>\n");
        sb.append("		<title>\n");
        sb.append("			<![CDATA[\n");
        sb.append("<br><br>");
        sb.append(survey.getMessage());
        sb.append("<br><br><br>\n");
        sb.append("			]]>\n");
        sb.append("		</title>\n");
        sb.append("		<select>\n");
        for (SurveyOption surveyOption : survey.getSurveyOptions()) {
            sb.append("<input type='radio' value='" + surveyOption.getOptionId() + "'>");
            sb.append(surveyOption.getOptionText());
            sb.append("</input>\n");
        }
        sb.append("		</select>\n");
        sb.append("	</question>\n");
        sb.append("</questions>\n");
        sb.append("</poll>\n");

        return sb.toString();
    }

    public static void checkSurveys(Player player) {
        if (player == null)
            return;

        List<Survey> surveys = DAOManager.getDAO(SurveyDAO.class).loadSurveys(player);

        int playerId = player.getObjectId();
        for (Survey survey : surveys) {
            int surveyId = survey.getSurveyId();
            String html = HTMLTemplate(survey);
            sendData(player, surveyId, html);
            int optionId = DAOManager.getDAO(SurveyDAO.class).loadPlayerSurvey(surveyId, playerId);
            if (optionId < 0)
                DAOManager.getDAO(SurveyDAO.class).insertPlayerSurvey(surveyId, playerId, 0);
        }
    }

    public static void getMessage(Player player, int messageId, int choiceId) {
        if (player == null)
            return;

        if (messageId < 1)
            return;

        int playerId = player.getObjectId();
        Survey survey = DAOManager.getDAO(SurveyDAO.class).loadSurvey(messageId, playerId);

        if (survey != null) {
            // TODO: Add better check to see if all Items will properly fit in player Inventory.
            if (player.getInventory().isFull()) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_INVEN_ERROR);
                return;
            }

            if (choiceId <= 0)
                return;

            DAOManager.getDAO(SurveyDAO.class).updatePlayerSurvey(messageId, playerId, choiceId);

            if (survey.getItemId() > 0 && survey.getItemCount() > 0) {
                ItemService.addItem(player, survey.getItemId(), survey.getItemCount());
                if (GSConfig.LOG_ITEM)
                    log.info(String.format("[ITEM] Item Survey ID/Count - %d/%d to player %s.", survey.getItemId(), survey.getItemCount(), player.getName()));
            }

            SurveyOption surveyOption = survey.getSurveyOption((choiceId - 1));
            if (surveyOption == null)
                return;

            if (surveyOption.getItemId() > 0 && surveyOption.getItemCount() > 0) {
                ItemService.addItem(player, surveyOption.getItemId(), surveyOption.getItemCount());
                if (GSConfig.LOG_ITEM)
                    log.info(String.format("[ITEM] Item Survey ID/Count - %d/%d to player %s.", surveyOption.getItemId(), surveyOption.getItemCount(), player.getName()));
            }
        }
    }

    public static void pushSurvey(final String html) {
        final int messageId = IDFactory.getInstance().nextId();
        World.getInstance().doOnAllPlayers(new Executor<Player>() {
            @Override
            public boolean run(Player ply) {
                sendData(ply, messageId, html);
                return true;
            }
        });
    }

    public static void showHTML(Player player, String html) {
        if (html == null) {
            log.error("showHTML no html found for player: " + player.getName());
            return;
        }
        sendData(player, IDFactory.getInstance().nextId(), html);
    }

    private static void sendData(Player player, int messageId, String html) {
        byte packet_count = (byte) Math.ceil(html.length() / (Short.MAX_VALUE - 8) + 1);
        if (packet_count < 256) {
            for (byte i = 0; i < packet_count; i++) {
                try {
                    int from = i * (Short.MAX_VALUE - 8), to = (i + 1) * (Short.MAX_VALUE - 8);
                    if (from < 0)
                        from = 0;
                    if (to > html.length())
                        to = html.length();
                    String sub = html.substring(from, to);
                    player.getClientConnection().sendPacket(new SM_QUESTIONNAIRE(messageId, i, packet_count, sub));
                }
                catch (Exception e) {
                    log.error(e);
                }
            }
        }
    }
}
