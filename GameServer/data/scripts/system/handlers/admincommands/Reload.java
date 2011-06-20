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

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.dataholders.*;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.portal.PortalTemplate;
import gameserver.model.templates.spawn.SpawnGroup;
import gameserver.questEngine.QuestEngine;
import gameserver.skillengine.model.SkillTemplate;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.chathandlers.ChatHandlers;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.io.filefilter.FileFilterUtils.*;

/**
 * @author MrPoke
 */
public class Reload extends AdminCommand {
    private static final Logger log = Logger.getLogger(Reload.class);

    public Reload() {
        super("reload");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_RELOAD) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }

        if (params == null || params.length != 1) {
            PacketSendUtility.sendMessage(admin, "syntax //reload <quest | skill | portal | spawn | admcmd>");
            return;
        }
        if (params[0].equals("quest")) {
            File xml = new File("./data/static_data/quest_data/quest_data.xml");
            File dir = new File("./data/static_data/quest_script_data");
            try {
                QuestEngine.getInstance().shutdown();
                JAXBContext jc = JAXBContext.newInstance(StaticData.class);
                Unmarshaller un = jc.createUnmarshaller();
                un.setSchema(getSchema("./data/static_data/static_data.xsd"));
                QuestsData newQuestData = (QuestsData) un.unmarshal(xml);
                QuestsData questsData = DataManager.QUEST_DATA;
                questsData.setQuestsData(newQuestData.getQuestsData());
                QuestScriptsData questScriptsData = DataManager.QUEST_SCRIPTS_DATA;
                questScriptsData.getData().clear();
                for (File file : listFiles(dir, true)) {
                    QuestScriptsData data = ((QuestScriptsData) un.unmarshal(file));
                    if (data != null)
                        if (data.getData() != null)
                            questScriptsData.getData().addAll(data.getData());
                }
                QuestEngine.getInstance().load();
            }
            catch (Exception e) {
                PacketSendUtility.sendMessage(admin, "Quests reload failed!");
                log.error(e);
            }
            finally {
                PacketSendUtility.sendMessage(admin, "Quests reloaded successfuly!");
            }
        } else if (params[0].equals("skill")) {
            File dir = new File("./data/static_data/skills");
            try {
                JAXBContext jc = JAXBContext.newInstance(StaticData.class);
                Unmarshaller un = jc.createUnmarshaller();
                un.setSchema(getSchema("./data/static_data/static_data.xsd"));
                List<SkillTemplate> newTemplates = new ArrayList<SkillTemplate>();
                for (File file : listFiles(dir, true)) {
                    SkillData data = (SkillData) un.unmarshal(file);
                    if (data != null)
                        newTemplates.addAll(data.getSkillTemplates());
                }
                DataManager.SKILL_DATA.setSkillTemplates(newTemplates);
            }
            catch (Exception e) {
                PacketSendUtility.sendMessage(admin, "Skills reload failed!");
                log.error(e);
            }
            finally {
                PacketSendUtility.sendMessage(admin, "Skills reloaded successfuly!");
            }
        } else if (params[0].equals("portal")) {
            File dir = new File("./data/static_data/portals");
            try {
                JAXBContext jc = JAXBContext.newInstance(StaticData.class);
                Unmarshaller un = jc.createUnmarshaller();
                un.setSchema(getSchema("./data/static_data/static_data.xsd"));
                List<PortalTemplate> newTemplates = new ArrayList<PortalTemplate>();
                for (File file : listFiles(dir, true)) {
                    PortalData data = (PortalData) un.unmarshal(file);
                    if (data != null && data.getPortals() != null)
                        newTemplates.addAll(data.getPortals());
                }
                DataManager.PORTAL_DATA.setPortals(newTemplates);
            }
            catch (Exception e) {
                PacketSendUtility.sendMessage(admin, "Portals reload failed!");
                log.error(e);
            }
            finally {
                PacketSendUtility.sendMessage(admin, "Portals reloaded successfuly!");
            }
        } else if (params[0].equals("spawn")) {
            File dir = new File("./data/static_data/spawns");
            try {
                JAXBContext jc = JAXBContext.newInstance(StaticData.class);
                Unmarshaller un = jc.createUnmarshaller();
                un.setSchema(getSchema("./data/static_data/static_data.xsd"));
                List<SpawnGroup> newTemplates = new ArrayList<SpawnGroup>();
                for (File file : listFiles(dir, true)) {
                    SpawnsData data = (SpawnsData) un.unmarshal(file);
                    if (data != null && data.getSpawnGroups() != null)
                        newTemplates.addAll(data.getSpawnGroups());
                }
                DataManager.SPAWNS_DATA.setSpawns(newTemplates);
            }
            catch (Exception e) {
                PacketSendUtility.sendMessage(admin, "Spawns reload failed!");
                log.error(e);
            }
            finally {
                PacketSendUtility.sendMessage(admin, "Spawns reloaded successfuly!");
            }
        } else if (params[0].equals("admcmd")) {
            ChatHandlers.getInstance().reloadChatHandlers();
            PacketSendUtility.sendMessage(admin, "Admin commands reloaded successfully!");
        } else
            PacketSendUtility.sendMessage(admin, "syntax //reload <quest | skill | portal | spawn | admcmd>");
    }

    private Schema getSchema(String xml_schema) {
        Schema schema = null;
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try {
            schema = sf.newSchema(new File(xml_schema));
        }
        catch (SAXException saxe) {
            throw new Error("Error while getting schema", saxe);
        }

        return schema;
    }

    @SuppressWarnings("unchecked")
    private Collection<File> listFiles(File root, boolean recursive) {
        IOFileFilter dirFilter = recursive ? makeSVNAware(HiddenFileFilter.VISIBLE) : null;

        return FileUtils.listFiles(root, andFileFilter(andFileFilter(notFileFilter(prefixFileFilter("new")),
                suffixFileFilter(".xml")), HiddenFileFilter.VISIBLE), dirFilter);
    }
}