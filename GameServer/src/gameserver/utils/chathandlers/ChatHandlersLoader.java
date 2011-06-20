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
package gameserver.utils.chathandlers;

import com.aionemu.commons.scripting.classlistener.ClassListener;
import com.aionemu.commons.scripting.classlistener.DefaultClassListener;
import com.aionemu.commons.utils.ClassUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Modifier;

/**
 * Created on: 12.09.2009 14:13:24
 *
 * @author Aquanox
 */
class ChatHandlersLoader
        extends DefaultClassListener
        implements ClassListener {
    private static final Logger logger = Logger.getLogger(ChatHandlersLoader.class);

    private final CommandChatHandler adminCCH;

    public ChatHandlersLoader(CommandChatHandler handler) {
        this.adminCCH = handler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postLoad(Class<?>[] classes) {
        for (Class<?> c : classes) {
            if (logger.isDebugEnabled())
                logger.debug("Load class " + c.getName());

            if (!isValidClass(c))
                continue;

            if (ClassUtils.isSubclass(c, AdminCommand.class)) {
                Class<? extends AdminCommand> tmp = (Class<? extends AdminCommand>) c;
                if (tmp != null)
                    try {
                        adminCCH.registerAdminCommand(tmp.newInstance());
                    }
                    catch (InstantiationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            }
            if (ClassUtils.isSubclass(c, UserCommand.class)) {
                Class<? extends UserCommand> tmp = (Class<? extends UserCommand>) c;
                if (tmp != null) {
                    try {
                        adminCCH.registerUserCommand(tmp.newInstance());
                    }
                    catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // call onClassLoad()
        super.postLoad(classes);

        logger.info("Loaded " + adminCCH.getAdminCommandsCount() + " admin command and " + adminCCH.getUserCommandsCount() + " user command handlers.");
    }

    @Override
    public void preUnload(Class<?>[] classes) {
        if (logger.isDebugEnabled())
            for (Class<?> c : classes)// debug messages
                logger.debug("Unload class " + c.getName());

        // call onClassUnload()
        super.preUnload(classes);

        adminCCH.clearHandlers();// unload all admin handlers.
    }

    public boolean isValidClass(Class<?> clazz) {
        final int modifiers = clazz.getModifiers();

        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers))
            return false;

        if (!Modifier.isPublic(modifiers))
            return false;

        return true;
    }
}
