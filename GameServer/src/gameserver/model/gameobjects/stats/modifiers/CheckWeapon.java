package gameserver.model.gameobjects.stats.modifiers;

import gameserver.model.templates.item.WeaponType;
import gameserver.model.gameobjects.player.Player;


public class CheckWeapon
{	
	public static final CheckWeapon getInstance()
	{
		return SingletonHolder.instance;
	}
	public int [] temp = new int [1];

	public void WeaponCheck(Player player)
	{	
		if (player.getEquipment().getMainHandWeaponType()!=null)
		{
			if (player.getEquipment().getMainHandWeaponType().getRequiredSlots() ==2)
			{
				if( player.getEquipment().getMainHandWeaponType().equals(WeaponType.BOOK_2H))
				temp[0]= 1090;
				if(	player.getEquipment().getMainHandWeaponType().equals(WeaponType.ORB_2H))
				temp[0]= 1090;
				if(	player.getEquipment().getMainHandWeaponType().equals(WeaponType.POLEARM_2H))
				temp[0]= 1390;		
				if( player.getEquipment().getMainHandWeaponType().equals(WeaponType.BOW))
				temp[0]= 1190;
				if(	player.getEquipment().getMainHandWeaponType().equals(WeaponType.SWORD_2H))
				temp[0]= 1190;
				if(	player.getEquipment().getMainHandWeaponType().equals(WeaponType.TOOLROD_2H))
				temp[0]= 1190;
				if( player.getEquipment().getMainHandWeaponType().equals(WeaponType.TOOLPICK_2H))
				temp[0]= 990;
				if(	player.getEquipment().getMainHandWeaponType().equals(WeaponType.STAFF_2H))
				temp[0]= 990;		
			}
			else
			temp[0]= 790;
		}
	}
	public int getValue()
	{
		return temp[0];
	}
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final  CheckWeapon instance = new  CheckWeapon();
	}
}
