package quest.kromedes_trial;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;

import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.skillengine.SkillEngine;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.Skill;
import gameserver.skillengine.model.SkillTemplate;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;


public class _18604MeetingWithRotan extends QuestHandler
{
	private final static int	questId	= 18604;

	public _18604MeetingWithRotan()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setQuestEnterZone(ZoneName.GRAND_CAVERN_300230000).add(questId);
		qe.setNpcQuestData(700961).addOnTalkEvent(questId); // Grave Robber's Corpse
		qe.setNpcQuestData(700961).addOnActionItemEvent(questId);

		qe.setQuestItemIds(164000141).add(questId); // Silver Blade Rotan
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		if(env.getTargetId() == 700961)
		{
			env.setQuestId(0);
			if(env.getDialogId() == -1)
			{
				PacketSendUtility
				.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 1011));
				return true;
			}
			else if(env.getDialogId() == 1012)
			{
				// Dialog ID 1097 is for failure (not handled)
				return checkScrollAddFailure(env, player);
			}
		}

		return false;
	}

	private boolean checkScrollAddFailure(QuestCookie env, Player player)
	{
		if(player.getInventory().getItemCountByItemId(164000141) > 0)
		{
			PacketSendUtility
			.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 27));
			return true;
		}
		else
		{
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs.getStatus() == QuestStatus.START)
			{
				env.setQuestId(questId);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				QuestService.questFinish(env);
			}
			else
			{
				defaultQuestGiveItem(env, 164000141, 1);
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName)
	{
		if(zoneName != ZoneName.GRAND_CAVERN_300230000)
			return false;

		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(qs != null)
			return false;

		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			env.setQuestId(questId);
			return QuestService.startQuest(env, QuestStatus.START);
		}

		return false;
	}

	@Override
	public boolean onItemUseEvent(final QuestCookie env, final Item item)
	{
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if (id != 164000141)
			return true;

		if(player.getWorldId() != 300230000)
			return false;

		VisibleObject target = player.getTarget();
		
		if (target == null)
			return false;
		
		if (player.isEnemy(target))
		{
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
					useSkill(player, item);
				}
			}, 3000);
		}

		return false; // don't remove from inventory
	}

	private void useSkill(Player player, Item item)
	{
		if (player.isItemUseDisabled(item.getItemTemplate().getDelayId()))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANT_USE_UNTIL_DELAY_TIME);
			return;
		}

		int useDelay = item.getItemTemplate().getDelayTime();
		player.addItemCoolDown(item.getItemTemplate().getDelayId(), System.currentTimeMillis() + useDelay, useDelay / 1000);

		Skill skill = SkillEngine.getInstance().getSkill(player, 9836, 1, player);
		if(skill != null)
		{
			skill.setFirstTargetRangeCheck(false);
			if (skill.canUseSkill())
			{
				SkillTemplate template = skill.getSkillTemplate();
				Effect ef = new Effect(player, (Creature)player.getTarget(), template, skill.getSkillLevel(), template
					.getEffectsDuration(), item.getItemTemplate());
				ef.initialize();
				ef.applyEffect();
				ef = null;
			}
		}
	}

	@Override
	public boolean onActionItemEvent(QuestCookie env)
	{
		return env.getTargetId() == 700961;
	}

}
