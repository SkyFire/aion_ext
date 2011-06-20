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
package gameserver.network;

import com.aionemu.commons.utils.Rnd;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;

/**
 * Crypt will encrypt server packet and decrypt client packet.
 *
 * @author hack99
 * @author -Nemesiss-
 * @author xavier
 */
public class Crypt {
    private final static Logger log = Logger.getLogger(Crypt.class);
    /**
     * Second byte of server packet must be equal to this
     */
    public final static byte staticServerPacketCode = 0x54;// 2.1.x
    /**
     * Crypt is enabled after first server packet was send.
     */
    private boolean isEnabled;

    private EncryptionKeyPair packetKey = null;

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * Enable crypt key - generate random key that will be used to encrypt second server packet [first one is
     * unencrypted] and decrypt client packets. This method is called from SM_KEY server packet, that packet sends key
     * to aion client.
     *
     * @return "false key" that should by used by aion client to encrypt/decrypt packets.
     */
    public final int enableKey() {
        if (packetKey != null)
            throw new KeyAlreadySetException();

        /** rnd key - this will be used to encrypt/decrypt packet */
        int key = Rnd.nextInt();

        packetKey = new EncryptionKeyPair(key);

        log.debug("using new encryption key " + packetKey);

        /** false key that will be sent to aion client in SM_KEY packet */
        return (key ^ 0xCD92E451) + 0x3FF2CC87;
    }

    /**
     * Decrypt client packet from this ByteBuffer.
     *
     * @param buf
     * @return true if decryption was successful.
     */
    public final boolean decrypt(ByteBuffer buf) {
        if (!isEnabled) {
            /* if encryption wasn't enabled, then maybe it's client reconnection, so skip packet */
            log.warn("encryption is not yet enabled, ignoring client packet [client reconnecting ?]");
            return false;
        }

        /* copying original buffer */
        int originalSize = buf.array().length;
        byte[] original = new byte[buf.array().length];

        for (int i = 0; i < originalSize; i++) {
            original[i] = buf.array()[i];
        }

        for (int i = 0; i < original.length; i++) {
            buf.array()[i] = original[i];
        }

        if (packetKey.decrypt(buf)) {
            packetKey.update();
            return true;
        } else {
            if (packetKey.isExpired()) {
                log.debug("expired key " + packetKey);
            }
        }

        return false;
    }

    /**
     * Encrypt server packet from this ByteBuffer.
     *
     * @param buf
     */
    public final void encrypt(ByteBuffer buf) {
        if (!isEnabled) {
            /** first packet is not encrypted */
            isEnabled = true;
            log.debug("encryption not enabled, should send SM_KEY");
            return;
        }

        packetKey.encrypt(buf);
    }

    /**
     * Server packet opcodec obfuscation.
     *
     * @param op
     * @return obfuscated opcodec
     */
    public static final byte encodeOpcodec(int op)
	{
		return (byte) ((op + 0xAE) ^ 0xEE);
	}
}
