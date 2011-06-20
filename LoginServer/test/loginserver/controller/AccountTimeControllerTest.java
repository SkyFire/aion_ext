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

package loginserver.controller;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.dao.DAOManager;
import loginserver.configs.Config;
import loginserver.dao.AccountDAO;
import loginserver.dao.AccountTimeDAO;
import loginserver.model.Account;
import loginserver.model.AccountTime;
import org.junit.Before;
import org.junit.Test;

/**
 * A controller made for testing
 */
public class AccountTimeControllerTest {
    /**
     * Initialise
     */
    @Before
    public void init() {
        Config.load();
        DatabaseFactory.init();
    }

    /**
     * A test controller made for account time
     */
    @Test
    @SuppressWarnings("unused")
    public void testAccountTimeController() {
        Account account = DAOManager.getDAO(AccountDAO.class).getAccount("df");
        AccountTime accountTime = DAOManager.getDAO(AccountTimeDAO.class).getAccountTime(account.getId());
    }
}
