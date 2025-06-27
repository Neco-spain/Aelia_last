package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.manager.CursedWeaponManager;
import net.sf.l2j.gameserver.enums.TeamType;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.enums.skills.AbnormalEffect;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;

import Customs.Events.CTF.CTFEvent;
import Customs.Events.DM.DMEvent;
import Customs.Events.TvT.TvTEvent;
import Customs.data.SkinTable;

public class CharInfo extends L2GameServerPacket
{
	private final Player _player;
	private final Inventory _inv;
	
	//custom  events
	private boolean _inSpecialEvent;
	private boolean _inSpecialEventNoDm;
	
	public CharInfo(Player player)
	{
		_player = player;
		_inv = _player.getInventory();
	}
	
	@Override
	protected final void writeImpl()
	{
		boolean canSeeInvis = false;
		if (_player.getAppearance().getInvisible())
		{
			final Player tmp = getClient().getPlayer();
			if (tmp != null && tmp.isGM())
				canSeeInvis = true;
		}

		writeC(0x03);
		writeD(_player.getX());
		writeD(_player.getY());
		writeD(_player.getZ());
		writeD((_player.getBoat() == null) ? 0 : _player.getBoat().getObjectId());
		writeD(_player.getObjectId());
		
		//custom Events
		_inSpecialEvent = CTFEvent.isPlayerParticipantCustom1(_player.getObjectId()) || DMEvent.isPlayerParticipantCustom1(_player.getObjectId()) || TvTEvent.isPlayerParticipantCustom1(_player.getObjectId());
		_inSpecialEventNoDm = CTFEvent.isPlayerParticipantCustom1(_player.getObjectId()) || TvTEvent.isPlayerParticipantCustom1(_player.getObjectId());

		if(_inSpecialEvent) {
			writeS("Player");
			writeD(ClassRace.DWARF.ordinal());
			writeD(1);
		}
		else {
			writeS(_player.getName());
			writeD(_player.getRace().ordinal());
			writeD(_player.getAppearance().getSex().ordinal());
		}
		
		writeD((_player.getClassIndex() == 0) ? _player.getClassId().getId() : _player.getBaseClass());
		
	/*	Default one
		writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_HAIRALL));
		writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
		writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
		writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
		writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
		writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
		writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
		writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_FEET));
		writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_BACK));
		writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
		writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
		writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_FACE));*/
		
		//custom events
		if (_inSpecialEvent)
		{
			writeD(0);
			writeD(0);
			writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
			writeD(0);
			writeD(6408);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeD(0);
			writeD(0);
		}
		else{
			writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_HAIRALL));
			writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
			writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_LHAND));

			writeD(_player.getVisualGloves() > 0 ? _player.getVisualGloves() : _inv.getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
			writeD(_player.getVisualChest() > 0 ? _player.getVisualChest() : _inv.getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
			writeD(_player.getVisualLegs() > 0 ? _player.getVisualLegs() : _inv.getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
			writeD(_player.getVisualBoots() > 0 ? _player.getVisualBoots() : _inv.getPaperdollItemId(Inventory.PAPERDOLL_FEET));

			writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_BACK));
			writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
			writeD(_inv.getPaperdollItemId(Inventory.PAPERDOLL_FACE));
		}

		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeD(_inv.getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeD(_inv.getPaperdollAugmentationId(Inventory.PAPERDOLL_LHAND));
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		
		writeD(_player.getPvpFlag());
		writeD(_player.getKarma());
		writeD(_player.getMAtkSpd());
		writeD(_player.getPAtkSpd());
		writeD(_player.getPvpFlag());
		writeD(_player.getKarma());
		
		final int runSpd = _player.getStat().getBaseRunSpeed();
		final int walkSpd = _player.getStat().getBaseWalkSpeed();
		final int swimSpd = _player.getStat().getBaseSwimSpeed();
		
		writeD(runSpd);
		writeD(walkSpd);
		writeD(swimSpd);
		writeD(swimSpd);
		writeD(runSpd);
		writeD(walkSpd);
		writeD((_player.isFlying()) ? runSpd : 0);
		writeD((_player.isFlying()) ? walkSpd : 0);
		
		writeF(_player.getStat().getMovementSpeedMultiplier());
		writeF(_player.getStat().getAttackSpeedMultiplier());
		
		final Summon summon = _player.getSummon();
		if (_player.getMountType() != 0 && summon != null)
		{
			writeF(summon.getCollisionRadius());
			writeF(summon.getCollisionHeight());
		}
		//custom fix event dward height and radius
		else if (_inSpecialEvent) {
			writeF(_player.getCollisionRadiusEvents());
			writeF(_player.getCollisionHeightEvents());
		}
		else
		{
			writeF(_player.getCollisionRadius());
			writeF(_player.getCollisionHeight());
		}
		
		writeD(_player.getAppearance().getHairStyle());
		writeD(_player.getAppearance().getHairColor());
		writeD(_player.getAppearance().getFace());
		
		writeS((canSeeInvis) ? "Invisible" : _player.getTitle());
		
		writeD(_player.getClanId());
		writeD(_player.getClanCrestId());
		writeD(_player.getAllyId());
		writeD(_player.getAllyCrestId());
		
		writeD(0);
		
		writeC((_player.isSitting()) ? 0 : 1);
		writeC((_player.isRunning()) ? 1 : 0);
		writeC((_player.isInCombat()) ? 1 : 0);
		writeC((_player.isAlikeDead()) ? 1 : 0);
		writeC((!canSeeInvis && _player.getAppearance().getInvisible()) ? 1 : 0);
		
		writeC(_player.getMountType());
		writeC(_player.getStoreType().getId());
		
		writeH(_player.getCubics().size());
		for (int id : _player.getCubics().keySet())
			writeH(id);
		
		writeC((_player.isInPartyMatchRoom()) ? 1 : 0);
		writeD((canSeeInvis) ? (_player.getAbnormalEffect() | AbnormalEffect.STEALTH.getMask()) : _player.getAbnormalEffect());
		writeC(_player.getRecomLeft());
		writeH(_player.getRecomHave());
		writeD(_player.getClassId().getId());
		writeD(_player.getMaxCp());
		writeD((int) _player.getCurrentCp());
		writeC((_player.isMounted()) ? 0 : _player.getEnchantEffect());
		writeC((!_inSpecialEventNoDm && ((Config.PLAYER_SPAWN_PROTECTION > 0 && _player.isSpawnProtected()) || _player.isReviveProtected())) ? TeamType.BLUE.getId() : _player.getTeam().getId());
		writeD(_player.getClanCrestLargeId());
		writeC((_player.isNoble()) ? 1 : 0);
		writeC((_player.isHero() || (_player.isGM() && Config.GM_HERO_AURA)) ? 1 : 0);
		writeC((_player.isFishing()) ? 1 : 0);
		writeLoc(_player.getFishingStance().getLoc());
		writeD(_player.getAppearance().getNameColor());
		writeD(_player.getHeading());
		writeD(_player.getPledgeClass());
		writeD(_player.getPledgeType());
		writeD(_player.getAppearance().getTitleColor());
		writeD(CursedWeaponManager.getInstance().getCurrentStage(_player.getCursedWeaponEquippedId()));
	}
}