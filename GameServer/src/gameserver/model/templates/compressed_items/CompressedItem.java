/* 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,  
 * MA  02110-1301, USA. 
 * 
 * http://www.gnu.org/copyleft/gpl.html 
 */ 
 
package gameserver.model.templates.compressed_items; 
 
import com.aionemu.commons.utils.Rnd; 
import gameserver.itemengine.actions.AbstractItemAction; 
import gameserver.model.ChatType;
import gameserver.model.TaskId; 
import gameserver.model.gameobjects.Item; 
import gameserver.model.gameobjects.player.Player; 
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION; 
import gameserver.network.aion.serverpackets.SM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE; 
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility; 
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

import java.util.Collections;
import java.util.List; 
import javax.xml.bind.annotation.XmlAccessType; 
import javax.xml.bind.annotation.XmlAccessorType; 
import javax.xml.bind.annotation.XmlAttribute; 
import javax.xml.bind.annotation.XmlElement; 
import javax.xml.bind.annotation.XmlType; 

/** 
 * @author Mr. Poke, ZeroSignal, Jefe
 * 
 */ 
@XmlAccessorType(XmlAccessType.FIELD) 
@XmlType(name = "CompressedItem", propOrder = 
{ "production" }) 
public class CompressedItem extends AbstractItemAction {
    private static String showAnnounce;
    @XmlElement(required = true) 
    protected List<Production>      production; 
    @XmlAttribute(required = true) 
    protected int id;
    @XmlAttribute(required = false)
    protected int maxproduction;
    @XmlAttribute(required = false)
    protected int level;
    

    public List<Production> getProduction() { 
        return this.production; 
    } 

    /** 
     * Gets the value of the id property. 
     *  
     */ 
    public int getId() { 
        return id; 
    }
    /** 
     * Get the value, Maximum production. 
     *  
     */ 
    public int getMaxproduction() { 
        return maxproduction; 
    }
    
    /** 
     * Max Min Count. 
     *  
     */
    public static int Countmaxmin(int max,int min) {
        return (int)(Math.random()*(max-min))+min;
    }

    @Override 
    public boolean canAct(Player player, Item parentItem, Item targetItem) { 
        if (production == null) 
            return false; 
        if (parentItem.getItemTemplate().getTemplateId() != id) { 
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_ERROR); 
            return false; 
        } 
        if (player.getInventory().isFull()) { 
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DECOMPRESS_INVENTORY_IS_FULL); 
            return false; 
        } 
        if(player.getLevel() < level)
        {
            String message = LanguageHandler.translate(CustomMessageId.ERROR_COMPRESS_MIN_LEVEL, level);
            PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null,message ,ChatType.ANNOUNCEMENTS));
            return false;
        }
        return true; 
    } 

    @Override 
    public void act(final Player player, final Item parentItem, Item targetItem) { 
        PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate() 
                        .getTemplateId(), 3000, 0, 0)); 
        player.getController().cancelTask(TaskId.ITEM_USE); 
        player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() 
        { 
            @Override 
            public void run() {
                PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate() 
                                .getTemplateId(), 0, 1, 0));

                if (!player.getInventory().removeFromBagByObjectId(parentItem.getObjectId(), 1))
                    return;

                int max = 0 ;
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_UNCOMPRESS_COMPRESSED_ITEM_SUCCEEDED(parentItem.getNameID()));
                    Collections.shuffle(production);
                    for (Production product : production){ 
                    int rand = Rnd.get(0, 100);
                    int chance = -1;
                    if (product.getChance() >= rand || chance == product.getChance()) { 
                        if (rand != -1) { 
                            rand = -1; 
                            chance = product.getChance(); 
                        }                        
                        if (product.getMin() > 0 && product.getMax() > 0) {
                            if (maxproduction > 0 && maxproduction == ++max) {
                                ItemService.addItem(player, product.getItemId(), Countmaxmin(product.getMax(), product.getMin()));
                                return;
                            }
                            ItemService.addItem(player, product.getItemId(), Countmaxmin(product.getMax(), product.getMin()));
                        }
                        else if (product.getCount() > 0){
                            if (maxproduction > 0 && maxproduction == ++max){
                                    ItemService.addItem(player, product.getItemId(), product.getCount());
                                    return;
                            }
                            ItemService.addItem(player, product.getItemId(), product.getCount());
                        }
                        else{
                            String serverMessageerror = LanguageHandler.translate(CustomMessageId.ERROR_ITEM_COMPRESSED);
                            showAnnounce = serverMessageerror;
                            serverMessageerror = null;
                            PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, showAnnounce,
                                ChatType.ANNOUNCEMENTS));
                        }
                    }
                }
            }
        }, 3000)); 
    } 
}
