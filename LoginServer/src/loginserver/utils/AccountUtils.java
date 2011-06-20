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

package loginserver.utils;

import com.aionemu.commons.utils.Base64;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class with usefull methods to use with accounts
 *
 * @author SoulKeeper
 */
public class AccountUtils {
    /**
     * Logger :)
     */
    private static final Logger log = Logger.getLogger(AccountUtils.class);

    /**
     * Encodes password. SHA-1 is used to encode password bytes, Base64 wraps SHA1-hash to string.
     *
     * @param password password to encode
     * @return retunrs encoded password.
     */
    public static String encodePassword(String password) {
        try {
            MessageDigest messageDiegest = MessageDigest.getInstance("SHA-1");
            messageDiegest.update(password.getBytes("UTF-8"));
            return Base64.encodeToString(messageDiegest.digest(), false);
        }
        catch (NoSuchAlgorithmException e) {
            log.error("Exception while encoding password");
            throw new Error(e);
        }
        catch (UnsupportedEncodingException e) {
            log.error("Exception while encoding password");
            throw new Error(e);
        }
    }
}
