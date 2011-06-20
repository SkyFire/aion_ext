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
import gameserver.dao.PlayerSkillListDAO;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.SkillList;
import gameserver.model.gameobjects.player.SkillListEntry;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on: 15.07.2009 19:33:07
 * Edited On:  13.09.2009 19:48:00
 *
 * @author IceReaper, orfeo087, Avol, AEJTester
 */
public class MySQL5PlayerSkillListDAO extends PlayerSkillListDAO {
    private static final Logger log = Logger.getLogger(MySQL5PlayerSkillListDAO.class);

    public static final String INSERT_QUERY = "INSERT INTO `player_skills` (`player_id`, `skillId`, `skillLevel`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE `skillLevel` = ?;";
    public static final String UPDATE_QUERY = "UPDATE `player_skills` set skillLevel=? where player_id=? AND skillId=?";
    public static final String DELETE_QUERY = "DELETE FROM `player_skills` WHERE `player_id`=? AND skillId=?";
    public static final String SELECT_QUERY = "SELECT `skillId`, `skillLevel` FROM `player_skills` WHERE `player_id`=?";


    /**
     * {@inheritDoc}
     */
    @Override
    public SkillList loadSkillList(final int playerId) {
        Map<Integer, SkillListEntry> skills = new HashMap<Integer, SkillListEntry>();
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
            stmt.setInt(1, playerId);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                int id = rset.getInt("skillId");
                int lv = rset.getInt("skillLevel");

                skills.put(id, new SkillListEntry(id, false, lv, PersistentState.UPDATED));
            }
            rset.close();
            stmt.close();
        }
        catch (Exception e) {
            log.fatal("Could not restore SkillList data for player: " + playerId + " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
        }
        return new SkillList(skills);
    }

    /**
     * Stores all player skills according to their persistence state
     */
    @Override
    public boolean storeSkills(Player player) {
        SkillListEntry[] skillsActive = player.getSkillList().getAllSkills();
        SkillListEntry[] skillsDeleted = player.getSkillList().getDeletedSkills();
        store(player, skillsActive);
        store(player, skillsDeleted);

        return true;
    }

    /**
     * @param player
     * @param skills
     */
    private void store(Player player, SkillListEntry[] skills) {
        for (int i = 0; i < skills.length; i++) {
            SkillListEntry skill = skills[i];
            switch (skill.getPersistentState()) {
                case NEW:
                    addSkill(player.getObjectId(), skill.getSkillId(), skill.getSkillLevel());
                    break;
                case UPDATE_REQUIRED:
                    updateSkill(player.getObjectId(), skill.getSkillId(), skill.getSkillLevel());
                    break;
                case DELETED:
                    deleteSkill(player.getObjectId(), skill.getSkillId());
                    break;
            }
            skill.setPersistentState(PersistentState.UPDATED);
        }
    }

    /**
     * Add a skill information into database
     *
     * @param playerId player object id
     * @param skill    skill contents.
     */
    private void addSkill(final int playerId, final int skillId, final int skillLevel) {
        DB.insertUpdate(INSERT_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, playerId);
                stmt.setInt(2, skillId);
                stmt.setInt(3, skillLevel);
                stmt.setInt(4, skillLevel);
                stmt.execute();
            }
        });
    }

    /**
     * Updates skill in database (after level change)
     *
     * @param playerId
     * @param skillId
     * @param skillLevel
     */
    private void updateSkill(final int playerId, final int skillId, final int skillLevel) {
        DB.insertUpdate(UPDATE_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, skillLevel);
                stmt.setInt(2, playerId);
                stmt.setInt(3, skillId);
                stmt.execute();
            }
        });
    }

    /**
     * Deletes skill from database
     *
     * @param playerId
     * @param skillId
     */
    private void deleteSkill(final int playerId, final int skillId) {
        DB.insertUpdate(DELETE_QUERY, new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
                stmt.setInt(1, playerId);
                stmt.setInt(2, skillId);
                stmt.execute();
            }
        });
    }

    @Override
    public boolean supports(String databaseName, int majorVersion, int minorVersion) {
        return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}