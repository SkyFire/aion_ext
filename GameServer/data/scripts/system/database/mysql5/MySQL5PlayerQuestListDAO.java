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

package mysql5;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.IUStH;
import gameserver.dao.PlayerQuestListDAO;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.QuestStateList;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author MrPoke
 */
public class MySQL5PlayerQuestListDAO extends PlayerQuestListDAO {
    private static final Logger log = Logger.getLogger(MySQL5PlayerQuestListDAO.class);

    public static final String SELECT_QUERY = "SELECT `quest_id`, `status`, `quest_vars`, `complete_count` FROM `player_quests` WHERE `player_id`=?";
    public static final String UPDATE_QUERY = "UPDATE `player_quests` SET `status`=?, `quest_vars`=?, `complete_count`=? where `player_id`=? AND `quest_id`=?";
    public static final String DELETE_QUERY = "DELETE FROM `player_quests` WHERE `player_id`=? AND `quest_id`=?";
    public static final String INSERT_QUERY = "INSERT INTO `player_quests` (`player_id`, `quest_id`, `status`, `quest_vars`, `complete_count`) VALUES (?,?,?,?,?)";

    /*
      * (non-Javadoc)
      * @see com.aionemu.gameserver.dao.PlayerQuestListDAO#load(com.aionemu.gameserver.model.gameobjects.player.Player)
      */

    @Override
    public QuestStateList load(final Player player) {
        QuestStateList questStateList = new QuestStateList();

        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
            stmt.setInt(1, player.getObjectId());
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                int questId = rset.getInt("quest_id");
                int questVars = rset.getInt("quest_vars");
                int completeCount = rset.getInt("complete_count");
                QuestStatus status = QuestStatus.valueOf(rset.getString("status"));
                QuestState questState = new QuestState(questId, status, questVars, completeCount);
                questState.setPersistentState(PersistentState.UPDATED);
                questStateList.addQuest(questId, questState);
            }
            rset.close();
            stmt.close();
        }
        catch (Exception e) {
            log.fatal("Could not restore QuestStateList data for player: " + player.getObjectId() + " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
        }
        return questStateList;
    }

    /*
      * (non-Javadoc)
      * @see com.aionemu.gameserver.dao.PlayerQuestListDAO#store(com.aionemu.gameserver.model.gameobjects.player.Player)
      */

    @Override
    public void store(final Player player) {
        for (QuestState qs : player.getQuestStateList().getAllQuestState()) {
            switch (qs.getPersistentState()) {
                case NEW:
                    addQuest(player.getObjectId(), qs);
                    break;
                case UPDATE_REQUIRED:
                    updateQuest(player.getObjectId(), qs);
                    break;
            }
            qs.setPersistentState(PersistentState.UPDATED);
        }
    }

    /**
     * @param playerId
     * @param QuestState
     */
    private void addQuest(final int playerId, final QuestState qs) {
        DB.insertUpdate(INSERT_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, playerId);
                stmt.setInt(2, qs.getQuestId());
                stmt.setString(3, qs.getStatus().toString());
                stmt.setInt(4, qs.getQuestVars().getQuestVars());
                stmt.setInt(5, qs.getCompliteCount());
                stmt.execute();
            }
        });
    }

    /**
     * @param playerId
     * @param qs
     */
    private void updateQuest(final int playerId, final QuestState qs) {
        DB.insertUpdate(UPDATE_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setString(1, qs.getStatus().toString());
                stmt.setInt(2, qs.getQuestVars().getQuestVars());
                stmt.setInt(3, qs.getCompliteCount());
                stmt.setInt(4, playerId);
                stmt.setInt(5, qs.getQuestId());
                stmt.execute();
            }
        });
    }

    /**
     * @param playerId The playerObjectId of the player who's quest needs to be deleted
     * @param questId  The questId that needs to be deleted
     */
    public void deleteQuest(final int playerId, final int questId) {
        DB.insertUpdate(DELETE_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, playerId);
                stmt.setInt(2, questId);
                stmt.execute();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(String s, int i, int i1) {
        return MySQL5DAOUtils.supports(s, i, i1);
    }

}