package gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class InGameShopConfig {

    @Property(key = "gameserver.ingameshop.enable", defaultValue = "false")
    public static boolean ENABLE_IN_GAME_SHOP;

    @Property(key = "gameserver.ingameshop.gift", defaultValue = "false")
    public static boolean ENABLE_GIFT_OTHER_RACE;

    @Property(key = "gameserver.ingameshop.itemlog", defaultValue = "false")
    public static boolean ENABLE_ITEM_LOG;
}