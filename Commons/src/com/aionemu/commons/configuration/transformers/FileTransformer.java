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

package com.aionemu.commons.configuration.transformers;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Transforms string to file by creating new file instance. It's not checked if file exists.
 *
 * @author SoulKeeper
 */
public class FileTransformer implements PropertyTransformer<File> {
    /**
     * Shared instance of this transformer. It's thread-safe so no need of multiple instances
     */
    public static final FileTransformer SHARED_INSTANCE = new FileTransformer();

    /**
     * Transforms String to the file
     *
     * @param value value that will be transformed
     * @param field value will be assigned to this field
     * @return File object that represents string
     */
    @Override
    public File transform(String value, Field field) throws TransformationException {
        return new File(value);
    }
}
