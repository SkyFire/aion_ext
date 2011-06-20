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

import java.lang.reflect.Field;

/**
 * Transforms enum string representation to enum. String must match case definition of enum, for instance:
 * <p/>
 * <pre>
 * enum{
 *  FILE,
 *  URL
 * }
 * </pre>
 * <p/>
 * will be parsed with string "FILE" but not "file".
 *
 * @author SoulKeeper
 */
public class EnumTransformer implements PropertyTransformer<Enum<?>> {
    /**
     * Shared instance of this transformer. It's thread-safe so no need of multiple instances
     */
    public static final EnumTransformer SHARED_INSTANCE = new EnumTransformer();

    /**
     * Trnasforms string to enum
     *
     * @param value value that will be transformed
     * @param field value will be assigned to this field
     * @return Enum object representing the value
     * @throws TransformationException if somehting went wrong
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Enum<?> transform(String value, Field field) throws TransformationException {
        Class<? extends Enum> clazz = (Class<? extends Enum>) field.getType();

        try {
            return Enum.valueOf(clazz, value);
        }
        catch (Exception e) {
            throw new TransformationException(e);
        }
    }
}
