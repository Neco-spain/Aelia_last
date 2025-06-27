/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.sf.l2j.gameserver.communitybbs.Manager.custom;

import Custom.CustomConfig;
import net.sf.l2j.Config;
import net.sf.l2j.commons.util.StatsSet;
import net.sf.l2j.gameserver.communitybbs.Manager.BaseBBSManager;
import net.sf.l2j.gameserver.data.cache.HtmCache;
import net.sf.l2j.gameserver.data.manager.GrandBossManager;
import net.sf.l2j.gameserver.data.manager.RaidBossManager;
import net.sf.l2j.gameserver.data.sql.ClanTable;
import net.sf.l2j.gameserver.data.xml.NpcData;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.VipShop;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.spawn.BossSpawn;
import net.sf.l2j.gameserver.network.serverpackets.ShowBoard;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;


public class IndexCBSManager extends BaseBBSManager
{

	@Override
	public void parseCmd(String command, Player player)
	{
		if (command.equals("_cbshome"))
		{
			final DecimalFormat df = new DecimalFormat("0");

			String content = HtmCache.getInstance().getHtm("data/html/CommunityBoard/custom/index.htm");
			content = content.replaceAll("%name%", String.valueOf(player.getName()));
			content = content.replaceAll("%Accontname%", player.getAccountName());
			content = content.replaceAll("%HwidIp%", player.getIP());
			content = content.replace("%class%", player.getTemplate().getClassName());
			content = content.replaceAll("%ServerName%", CustomConfig.SERVER_NAME);
			content = content.replace("%max_players%", String.valueOf(World.getInstance().getPlayers().size()));
			content = content.replace("%max_sub%", String.valueOf(CustomConfig.ALLOWED_SUBCLASS));
			content = content.replace("%pvpkills%", String.valueOf(player.getPvpKills()));
			content = content.replace("%pkkills%", String.valueOf(player.getPkKills()));

			Clan playerClan = ClanTable.getInstance().getClan(player.getClanId());
			if (playerClan != null) {
				content = content.replaceAll("%clan%", playerClan.getName());
			} else {
				content = content.replaceAll("%clan%", "");
			}
			if (player.isVip()) {
				content = content.replace("%rate_xp%", String.valueOf(df.format(Config.RATE_XP * CustomConfig.VIP_XP_SP_RATE)));
				content = content.replace("%rate_sp%", String.valueOf(CustomConfig.VIP_XP_SP_RATE));
				content = content.replace("%rate_adena%", String.valueOf(CustomConfig.VIP_ADENA_RATE));
				content = content.replace("%rate_items%", String.valueOf(CustomConfig.VIP_DROP_RATE));
				content = content.replace("%rate_spoil%", String.valueOf(CustomConfig.VIP_SPOIL_RATE));
			} else {
				content = content.replace("%rate_xp%", String.valueOf(Config.RATE_XP));
				content = content.replace("%rate_sp%", String.valueOf(Config.RATE_SP));
				content = content.replace("%rate_adena%", String.valueOf(Config.RATE_DROP_ADENA));
				content = content.replace("%rate_items%", String.valueOf(Config.RATE_DROP_ITEMS));
				content = content.replace("%rate_spoil%", String.valueOf(Config.RATE_DROP_SPOIL));
			}
			content = content.replace("%Premium%", player.isVip() ? "<font color=00FF00>ON</font>" : "<font color=FF0000>OFF</font>");

			long delay = player.getMemos().getLong("vip", 0L);
			if(delay > 0) {
				long _daysleft = (delay - Calendar.getInstance().getTimeInMillis()) / 86400000L;

				if(_daysleft > 120L)
					content = content.replace("%PremiumEnd%", "Never expires.");
				else
					content = content.replace("%PremiumEnd%", (new SimpleDateFormat("dd-MM-yyyy HH:mm")).format(Long.valueOf(delay)));
			}
			else
				content = content.replace("%PremiumEnd%", "no VIP status");

			separateAndSend(content, player);
		}
		else if(command.startsWith("_bbsLink;"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();

			String idp = String.valueOf(st.nextToken());

			String content = null;

			if(st.hasMoreTokens()) {
				content = HtmCache.getInstance().getHtm("data/html/CommunityBoard/custom/" + idp + "/" + st.nextToken() + ".htm");
			}
			else
			 	content = HtmCache.getInstance().getHtm("data/html/CommunityBoard/custom/" + idp + ".htm");

			if(content == null)
			{
				content = "<html><body><br><br><center>404 :File Not foud: 'data/html/CommunityBoard/custom/" + idp + ".htm' </center></body></html>";
			}
			separateAndSend(content, player);
			st = null;
			content = null;
		}
		else if (command.startsWith("bbs_buffer"))
			BufferCBSManager.getInstance().showMainWindow(player);

		else if(command.startsWith("bbs_customs_vip"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			st.nextToken();

			if (st.countTokens() != 3) // Days-Item-Count
				return;

			int days = Integer.parseInt(st.nextToken());
			int item = Integer.parseInt(st.nextToken());
			int count = Integer.parseInt(st.nextToken());

			VipCBSManager.getInstance().doVIP(player, days, item, count);
			SendHome(player);
		}

		else
		{
			ShowBoard sb = new ShowBoard("<html><body><br><br><center>The command: " + command + " is not implemented yet!</center><br><br></body></html>", "101");
			player.sendPacket(sb);
			sb = null;
			player.sendPacket(new ShowBoard(null, "102"));
			player.sendPacket(new ShowBoard(null, "103"));
		}
	}

	public void SendHome(Player player) {
		parseCmd("_cbshome", player);
	}

	@Override
	protected String getFolder()
	{
		return "custom/";
	}

	private static IndexCBSManager _instance = new IndexCBSManager();

	/**
	 * @return
	 */
	public static IndexCBSManager getInstance()
	{
		return _instance;
	}
}
