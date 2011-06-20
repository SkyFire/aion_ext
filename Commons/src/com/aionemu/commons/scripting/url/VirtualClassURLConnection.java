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

package com.aionemu.commons.scripting.url;

import com.aionemu.commons.scripting.ScriptClassLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class represents URL Connection that is used to "connect" to scripts binary data that was loaded by specified
 * {@link com.aionemu.commons.scripting.impl.javacompiler.ScriptCompilerImpl}.<br>
 * <br>
 * TODO: Implement all methods of {@link URLConnection} to ensure valid behaviour
 *
 * @author SoulKeeper
 */
public class VirtualClassURLConnection extends URLConnection {
    /**
     * Input stream, is assigned from class
     */
    private InputStream is;

    /**
     * Creates URL connections that "connects" to class binary data
     *
     * @param url class name
     * @param cl  classloader
     */
    protected VirtualClassURLConnection(URL url, ScriptClassLoader cl) {
        super(url);
        is = new ByteArrayInputStream(cl.getByteCode(url.getHost()));
    }

    /**
     * This method is ignored
     */
    @Override
    public void connect() throws IOException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return is;
    }
}
