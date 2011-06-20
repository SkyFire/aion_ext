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

package loginserver;

import loginserver.network.ncrypt.EncryptedRSAKeyPair;
import loginserver.network.ncrypt.KeyGen;
import org.junit.Test;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;

/**
 * This test is for KeyGen initialization and performance checking
 */
public class KeyGenTest {
    /**
     * A test for keygen init
     */
    @Test
    public void testKeyGenInit() {
        try {
            KeyGen.init();
        }
        catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void testRSAPreInit() throws Exception {
        EncryptedRSAKeyPair[] RSAKeyPairs = new EncryptedRSAKeyPair[10];

        KeyPairGenerator rsaKeyPairGenerator = KeyPairGenerator.getInstance("RSA");

        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4);

        rsaKeyPairGenerator.initialize(spec);


        for (int i = 0; i < 10; i++) {
            RSAKeyPairs[i] = new EncryptedRSAKeyPair(rsaKeyPairGenerator.generateKeyPair());
        }

        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            Cipher pRsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
            pRsaCipher.init(Cipher.DECRYPT_MODE, RSAKeyPairs[i].getRSAKeyPair().getPrivate());
        }
        long t2 = System.currentTimeMillis();
        System.out.println("RSA init time: " + (t2 - t1));

        byte[] data = new byte[128];

        t1 = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
            rsaCipher.init(Cipher.DECRYPT_MODE, RSAKeyPairs[0].getRSAKeyPair().getPrivate());
            rsaCipher.doFinal(data, 0, 128);
        }
        t2 = System.currentTimeMillis();
        System.out.println("RSA decryption time: " + (t2 - t1));

    }

}
