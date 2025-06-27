package net.sf.l2j.gameserver.scripting.tasks;

import Custom.CustomConfig;
import net.sf.l2j.gameserver.data.sql.ClanTable;
import net.sf.l2j.gameserver.enums.skills.AbnormalEffect;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.scripting.ScheduledQuest;

public final class OpeningUnFreeze extends ScheduledQuest
{
	public OpeningUnFreeze()
	{
		super(-1, "tasks");
	}
	
	@Override
	public final void onStart()
	{
		if(CustomConfig.ENABLE_FREEZE){
			for(Player player : World.getInstance().getPlayers()){
				if(player.isFreezed()){
					player.setIsParalyzed(false);
					player.setIsFreezed(false);
					player.stopAbnormalEffect(AbnormalEffect.ROOT);
					player.sendPacket(new ExShowScreenMessage("Our server opening time arrived, enjoy our server!", 5000 , ExShowScreenMessage.SMPOS.TOP_CENTER, false));
				}
			}
		}
	}
	
	@Override
	public final void onEnd()
	{
	}
}