package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.manager.CursedWeaponManager;
import net.sf.l2j.gameserver.enums.PolyType;
import net.sf.l2j.gameserver.enums.TeamType;
import net.sf.l2j.gameserver.enums.skills.AbnormalEffect;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;

import Customs.Events.CTF.CTFEvent;
import Customs.Events.TvT.TvTEvent;
import Customs.data.SkinTable;

public class UserInfo extends L2GameServerPacket
{
	private final Player _player;
	private int _relation;

	//custom  events
	private boolean _inSpecialEventNoDm;
	
	public UserInfo(Player player)
	{
		_player = player;
		
		_relation = _player.isClanLeader() ? 0x40 : 0;
		
		if (_player.getSiegeState() == 1)
			_relation |= 0x180;
		if (_player.getSiegeState() == 2)
			_relation |= 0x80;
	}
	
	@Override
	protected final void writeImpl()
	{
		_inSpecialEventNoDm = CTFEvent.isPlayerParticipantCustom1(_player.getObjectId()) || TvTEvent.isPlayerParticipantCustom1(_player.getObjectId());
		
		writeC(0x04);
		writeD(_player.getX());
		writeD(_player.getY());
		writeD(_player.getZ());
		writeD(_player.getHeading());
		writeD(_player.getObjectId());
		writeS((_player.getPolyTemplate() != null) ? _player.getPolyTemplate().getName() : _player.getName());
		writeD(_player.getRace().ordinal());
		writeD(_player.getAppearance().getSex().ordinal());
		writeD((_player.getClassIndex() == 0) ? _player.getClassId().getId() : _player.getBaseClass());
		writeD(_player.getLevel());
		writeQ(_player.getExp());
		writeD(_player.getSTR());
		writeD(_player.getDEX());
		writeD(_player.getCON());
		writeD(_player.getINT());
		writeD(_player.getWIT());
		writeD(_player.getMEN());
		writeD(_player.getMaxHp());
		writeD((int) _player.getCurrentHp());
		writeD(_player.getMaxMp());
		writeD((int) _player.getCurrentMp());
		writeD(_player.getSp());
		writeD(_player.getCurrentLoad());
		writeD(_player.getMaxLoad());
		writeD(_player.getActiveWeaponItem() != null ? 40 : 20);
		
	/*	Default one
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIRALL));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_REAR));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEAR));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_NECK));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HEAD));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_CHEST));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEGS));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FEET));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_BACK));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIR));
		writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FACE));
		
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIRALL));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_REAR));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEAR));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_NECK));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RFINGER));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LFINGER));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_BACK));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
		writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FACE));
	*/
		//custom
		if (_player.getTrySkin() == 0){
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIRALL));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_REAR));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEAR));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_NECK));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HEAD));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND));

			writeD(_player.getVisualGloves() > 0 ? _player.getVisualGloves() : _player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES));
			writeD(_player.getVisualChest() > 0 ? _player.getVisualChest() : _player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_CHEST));
			writeD(_player.getVisualLegs() > 0 ? _player.getVisualLegs() : _player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEGS));
			writeD(_player.getVisualBoots() > 0 ? _player.getVisualBoots() : _player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FEET));

			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_BACK));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIR));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FACE));

			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIRALL));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_REAR));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEAR));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_NECK));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RFINGER));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LFINGER));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND));

			writeD(_player.getVisualGloves() > 0 ? _player.getVisualGloves() : _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
			writeD(_player.getVisualChest() > 0 ? _player.getVisualChest() : _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
			writeD(_player.getVisualLegs() > 0 ? _player.getVisualLegs() : _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
			writeD(_player.getVisualBoots() > 0 ? _player.getVisualBoots() : _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET));

		 	writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_BACK));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FACE));
		}
		else if(_player.getTrySkin() != 0) {
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIRALL));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_REAR));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEAR));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_NECK));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HEAD));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND));

			writeD(SkinTable.getInstance().getSkin(_player.getTrySkin()) != null ? ((SkinTable.getInstance().getSkin(_player.getTrySkin()).getGlovesId() != 0) ? SkinTable.getInstance().getSkin(_player.getTrySkin()).getGlovesId() : _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES)) :_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
			writeD(SkinTable.getInstance().getSkin(_player.getTrySkin()) != null ? ((SkinTable.getInstance().getSkin(_player.getTrySkin()).getChestId() != 0) ? SkinTable.getInstance().getSkin(_player.getTrySkin()).getChestId() : _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST)) :_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
			writeD(SkinTable.getInstance().getSkin(_player.getTrySkin()) != null ? ((SkinTable.getInstance().getSkin(_player.getTrySkin()).getLegsId() != 0) ? SkinTable.getInstance().getSkin(_player.getTrySkin()).getLegsId() : _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS)) :_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
			writeD(SkinTable.getInstance().getSkin(_player.getTrySkin()) != null ? ((SkinTable.getInstance().getSkin(_player.getTrySkin()).getBootsId() != 0) ? SkinTable.getInstance().getSkin(_player.getTrySkin()).getBootsId() : _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET)) :_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET));

			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_BACK));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
			writeD(SkinTable.getInstance().getSkin(_player.getTrySkin()) == null ? _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR) : SkinTable.getInstance().getSkin(_player.getTrySkin()).getHairId() != 0 ? SkinTable.getInstance().getSkin(_player.getTrySkin()).getHairId() : _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR));

			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FACE));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIRALL));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_REAR));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEAR));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_NECK));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RFINGER));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LFINGER));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HEAD));

			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND));

			writeD(SkinTable.getInstance().getSkin(_player.getTrySkin()) != null ? ((SkinTable.getInstance().getSkin(_player.getTrySkin()).getGlovesId() != 0) ? SkinTable.getInstance().getSkin(_player.getTrySkin()).getGlovesId() : _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES)) :_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
			writeD(SkinTable.getInstance().getSkin(_player.getTrySkin()) != null ? ((SkinTable.getInstance().getSkin(_player.getTrySkin()).getChestId() != 0) ? SkinTable.getInstance().getSkin(_player.getTrySkin()).getChestId() : _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST)) :_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
			writeD(SkinTable.getInstance().getSkin(_player.getTrySkin()) != null ? ((SkinTable.getInstance().getSkin(_player.getTrySkin()).getLegsId() != 0) ? SkinTable.getInstance().getSkin(_player.getTrySkin()).getLegsId() : _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS)) :_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
			writeD(SkinTable.getInstance().getSkin(_player.getTrySkin()) != null ? ((SkinTable.getInstance().getSkin(_player.getTrySkin()).getBootsId() != 0) ? SkinTable.getInstance().getSkin(_player.getTrySkin()).getBootsId() : _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET)) :_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET));

			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_BACK));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeD(SkinTable.getInstance().getSkin(_player.getTrySkin()) == null ? _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR) : SkinTable.getInstance().getSkin(_player.getTrySkin()).getHairId() != 0 ? SkinTable.getInstance().getSkin(_player.getTrySkin()).getHairId() : _player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FACE));
		}
		else {
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIRALL));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_REAR));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEAR));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_NECK));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HEAD));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_CHEST));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEGS));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FEET));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_BACK));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIR));
			writeD(_player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FACE));

			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIRALL));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_REAR));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEAR));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_NECK));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RFINGER));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LFINGER));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_BACK));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
			writeD(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FACE));
		}
			
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
		writeH(0x00);
		writeH(0x00);
		writeD(_player.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
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
		writeD(_player.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LHAND));
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		
		writeD(_player.getPAtk(null));
		writeD(_player.getPAtkSpd());
		writeD(_player.getPDef(null));
		writeD(_player.getEvasionRate(null));
		writeD(_player.getAccuracy());
		writeD(_player.getCriticalHit(null, null));
		writeD(_player.getMAtk(null, null));
		writeD(_player.getMAtkSpd());
		writeD(_player.getPAtkSpd());
		writeD(_player.getMDef(null, null));
		writeD(_player.getPvpFlag());
		writeD(_player.getKarma());
		
		final int runSpd = _player.getStat().getBaseRunSpeed();
		final int walkSpd = _player.getStat().getBaseWalkSpeed();
		final int swimSpd = _player.getStat().getBaseSwimSpeed();
		
		writeD(runSpd);
		writeD(walkSpd);
		writeD(swimSpd);
		writeD(swimSpd);
		writeD(0);
		writeD(0);
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
		else
		{
			writeF(_player.getCollisionRadius());
			writeF(_player.getCollisionHeight());
		}
		
		writeD(_player.getAppearance().getHairStyle());
		writeD(_player.getAppearance().getHairColor());
		writeD(_player.getAppearance().getFace());
		writeD((_player.isGM()) ? 1 : 0);
		
		writeS((_player.getPolyType() != PolyType.DEFAULT) ? "Morphed" : _player.getTitle());
		
		writeD(_player.getClanId());
		writeD(_player.getClanCrestId());
		writeD(_player.getAllyId());
		writeD(_player.getAllyCrestId());
		writeD(_relation);
		writeC(_player.getMountType());
		writeC(_player.getStoreType().getId());
		writeC((_player.hasDwarvenCraft()) ? 1 : 0);
		writeD(_player.getPkKills());
		writeD(_player.getPvpKills());
		
		writeH(_player.getCubics().size());
		for (int id : _player.getCubics().keySet())
			writeH(id);
		
		writeC((_player.isInPartyMatchRoom()) ? 1 : 0);
		writeD((_player.getAppearance().getInvisible() && _player.isGM()) ? (_player.getAbnormalEffect() | AbnormalEffect.STEALTH.getMask()) : _player.getAbnormalEffect());
		writeC(0x00);
		writeD(_player.getClanPrivileges());
		writeH(_player.getRecomLeft());
		writeH(_player.getRecomHave());
		writeD((_player.getMountNpcId() > 0) ? _player.getMountNpcId() + 1000000 : 0);
		writeH(_player.getInventoryLimit());
		writeD(_player.getClassId().getId());
		writeD(0x00);
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
		writeC((_player.isRunning()) ? 0x01 : 0x00);
		writeD(_player.getPledgeClass());
		writeD(_player.getPledgeType());
		writeD(_player.getAppearance().getTitleColor());
		writeD(CursedWeaponManager.getInstance().getCurrentStage(_player.getCursedWeaponEquippedId()));
	}
}