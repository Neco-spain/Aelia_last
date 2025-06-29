package net.sf.l2j.gameserver.model.actor;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.manager.ZoneManager;
import net.sf.l2j.gameserver.enums.AiEventType;
import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.enums.skills.L2EffectFlag;
import net.sf.l2j.gameserver.enums.skills.L2EffectType;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.stat.PlayableStat;
import net.sf.l2j.gameserver.model.actor.status.PlayableStatus;
import net.sf.l2j.gameserver.model.actor.template.CreatureTemplate;
import net.sf.l2j.gameserver.model.zone.type.RandomZone;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.Revive;
import net.sf.l2j.gameserver.scripting.QuestState;

import Customs.Events.CTF.CTFEvent;
import Customs.Events.DM.DMEvent;
import Customs.Events.TvT.TvTEvent;
import Customs.mods.PvpColor;

/**
 * This class represents all Playable characters in the world.<BR>
 * <BR>
 * L2Playable :<BR>
 * <BR>
 * <li>Player</li>
 * <li>L2Summon</li><BR>
 * <BR>
 */
public abstract class Playable extends Creature
{
	
	//custom
	static L2Skill noblesse = SkillTable.getInstance().getInfo(1323, 1);
	
	
	/**
	 * Constructor of L2Playable (use Creature constructor).<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Call the Creature constructor to create an empty _skills slot and link copy basic Calculator set to this L2Playable</li><BR>
	 * <BR>
	 * @param objectId Identifier of the object to initialized
	 * @param template The L2CharTemplate to apply to the L2Playable
	 */
	public Playable(int objectId, CreatureTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void initCharStat()
	{
		setStat(new PlayableStat(this));
	}
	
	@Override
	public PlayableStat getStat()
	{
		return (PlayableStat) super.getStat();
	}
	
	@Override
	public void initCharStatus()
	{
		setStatus(new PlayableStatus(this));
	}
	
	@Override
	public PlayableStatus getStatus()
	{
		return (PlayableStatus) super.getStatus();
	}
	
	@Override
	public void onActionShift(Player player)
	{
		//custom events
        if (!CTFEvent.onAction(player, this.getObjectId()) || !DMEvent.onAction(player, this.getObjectId()) /*|| !LMEvent.onAction(player, this.getObjectId())*/ || !TvTEvent.onAction(player, this.getObjectId())) {
            return;
        }
        
        
		if (player.getTarget() != this)
			player.setTarget(this);
		else
		{
			if (isAutoAttackable(player) && player.isInsideRadius(this, player.getPhysicalAttackRange(), false, false) && GeoEngine.getInstance().canSeeTarget(player, this))
				player.getAI().setIntention(IntentionType.ATTACK, this);
			else
				player.sendPacket(ActionFailed.STATIC_PACKET);
		}
	}
	
	@Override
	public boolean doDie(Creature killer)
	{
		// killing is only possible one time
		synchronized (this)
		{
			if (isDead())
				return false;
			
			// now reset currentHp to zero
			setCurrentHp(0);
			
			setIsDead(true);
		}
		
		
		//Customs
		if(killer instanceof Player)
			PvpColor.checkColorPlayer(killer.getActingPlayer());
		
		
		// Set target to null and cancel Attack or Cast
		setTarget(null);
		
		// Stop movement
		stopMove(null);
		
		// Stop HP/MP/CP Regeneration task
		getStatus().stopHpMpRegeneration();
		
		// Stop all active skills effects in progress
		if (isPhoenixBlessed())
		{
			// remove Lucky Charm if player has SoulOfThePhoenix/Salvation buff
			if (getCharmOfLuck())
				stopCharmOfLuck(null);
			
			//custom TODO:test functionality
			if (isNoblesseBlessed() ) // && killer.getActingPlayer().isInsideZone(ZoneId.CHANGE_PVP_ZONE)
				stopNoblesseBlessing(null);
		}
		// Same thing if the Character isn't a Noblesse Blessed L2Playable
		else if (isNoblesseBlessed() ) // && killer.getActingPlayer().isInsideZone(ZoneId.CHANGE_PVP_ZONE)
		{
			//custom TODO:test functionality
			stopNoblesseBlessing(null);
			
			// remove Lucky Charm if player have Nobless blessing buff
			if (getCharmOfLuck())
				stopCharmOfLuck(null);
		}
		else
			stopAllEffectsExceptThoseThatLastThroughDeath();
		
		// Send the Server->Client packet StatusUpdate with current HP and MP to all other Player to inform
		broadcastStatusUpdate();
		
		// Notify Creature AI
		getAI().notifyEvent(AiEventType.DEAD);
		
		// Notify Quest of L2Playable's death
		final Player actingPlayer = getActingPlayer();
		for (QuestState qs : actingPlayer.getNotifyQuestOfDeath())
			qs.getQuest().notifyDeath((killer == null ? this : killer), actingPlayer);
		
		if (killer != null)
		{
			final Player player = killer.getActingPlayer();
			if (player != null)
				player.onKillUpdatePvPKarma(this);
		}
		
		return true;
	}
	
	@Override
	public void doRevive()
	{
		if (!isDead() || isTeleporting())
			return;
		
		//custom  ,always give nobless if is in pvp zone

		Player p  = getActingPlayer();
		if(p != null) {
			if (!isNoblesseBlessed() && p.isInsideZone(ZoneId.CHANGE_PVP_ZONE))
				noblesse.getEffects(p, p);
		}
		 
		setIsDead(false);
		
		if (isPhoenixBlessed())
		{
			stopPhoenixBlessing(null);
			
			getStatus().setCurrentHp(getMaxHp());
			getStatus().setCurrentMp(getMaxMp());
		}
		else
			getStatus().setCurrentHp(getMaxHp() * Config.RESPAWN_RESTORE_HP);
		
		
		//custom on res protection
		if(p != null && !p.isSpawnProtected() && !p.isReviveProtected())
			p.setReviveProtection(true);
		
		// Start broadcast status
		broadcastPacket(new Revive(this));
		
		//custom
	//	final RandomZone zone = ZoneManager.getInstance().getZone(this, RandomZone.class);
	//	if (zone != null)
	//		zone.respawnCharacter(this);
	}

	
	public boolean checkIfPvP(Playable target)
	{
		if (target == null || target == this)
			return false;
		
		final Player player = getActingPlayer();
		if (player == null || player.getKarma() != 0)
			return false;
		
		final Player targetPlayer = target.getActingPlayer();
		if (targetPlayer == null || targetPlayer == this)
			return false;
		
		if (targetPlayer.getKarma() != 0 || targetPlayer.getPvpFlag() == 0)
			return false;
		
		return true;
	}
	
	/**
	 * Return True.
	 */
	@Override
	public boolean isAttackable()
	{
		return true;
	}
	
	/**
	 * <B><U> Overridden in </U> :</B>
	 * <ul>
	 * <li>L2Summon</li>
	 * <li>Player</li>
	 * </ul>
	 * @param id The system message to send to player.
	 */
	public void sendPacket(SystemMessageId id)
	{
		// default implementation
	}
	
	// Support for Noblesse Blessing skill, where buffs are retained after resurrect
	public final boolean isNoblesseBlessed()
	{
		return _effects.isAffected(L2EffectFlag.NOBLESS_BLESSING);
	}
	
	public final void stopNoblesseBlessing(L2Effect effect)
	{
		if (effect == null)
			stopEffects(L2EffectType.NOBLESSE_BLESSING);
		else
			removeEffect(effect);
		updateAbnormalEffect();
	}
	
	// Support for Soul of the Phoenix and Salvation skills
	public final boolean isPhoenixBlessed()
	{
		return _effects.isAffected(L2EffectFlag.PHOENIX_BLESSING);
	}
	
	public final void stopPhoenixBlessing(L2Effect effect)
	{
		if (effect == null)
			stopEffects(L2EffectType.PHOENIX_BLESSING);
		else
			removeEffect(effect);
		
		updateAbnormalEffect();
	}
	
	/**
	 * @return True if the Silent Moving mode is active.
	 */
	public boolean isSilentMoving()
	{
		return _effects.isAffected(L2EffectFlag.SILENT_MOVE);
	}
	
	// for Newbie Protection Blessing skill, keeps you safe from an attack by a chaotic character >= 10 levels apart from you
	public final boolean getProtectionBlessing()
	{
		return _effects.isAffected(L2EffectFlag.PROTECTION_BLESSING);
	}
	
	public void stopProtectionBlessing(L2Effect effect)
	{
		if (effect == null)
			stopEffects(L2EffectType.PROTECTION_BLESSING);
		else
			removeEffect(effect);
		
		updateAbnormalEffect();
	}
	
	// Charm of Luck - During a Raid/Boss war, decreased chance for death penalty
	public final boolean getCharmOfLuck()
	{
		return _effects.isAffected(L2EffectFlag.CHARM_OF_LUCK);
	}
	
	public final void stopCharmOfLuck(L2Effect effect)
	{
		if (effect == null)
			stopEffects(L2EffectType.CHARM_OF_LUCK);
		else
			removeEffect(effect);
		
		updateAbnormalEffect();
	}
	
	@Override
	public void updateEffectIcons(boolean partyOnly)
	{
		_effects.updateEffectIcons(partyOnly);
	}
	
	/**
	 * This method allows to easily send relations. Overridden in L2Summon and Player.
	 */
	public void broadcastRelationsChanges()
	{
	}
	
	@Override
	public boolean isInArena()
	{
		return isInsideZone(ZoneId.PVP) && !isInsideZone(ZoneId.SIEGE);
	}
	
	public abstract void doPickupItem(WorldObject object);
	
	public abstract int getKarma();
	
	public abstract byte getPvpFlag();
	
	public abstract boolean useMagic(L2Skill skill, boolean forceUse, boolean dontMove);
}