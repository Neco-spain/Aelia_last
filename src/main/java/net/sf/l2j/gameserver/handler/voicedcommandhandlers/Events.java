/*
 * Copyright (C) 2004-2014 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import Customs.Events.CTF.CTFConfig;
import Customs.Events.CTF.CTFEvent;
import Customs.Events.DM.DMConfig;
import Customs.Events.DM.DMEvent;
import Customs.Events.TvT.TvTConfig;
import Customs.Events.TvT.TvTEvent;

public class Events implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"ctfinfo",
		"ctfjoin",
		"ctfleave",
		"dminfo",
		"dmjoin",
		"dmleave",
		/*"lminfo",
		"lmjoin",
		"lmleave",*/
		"tvtinfo",
		"tvtjoin",
		"tvtleave"
	};

	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		if (command.equals("ctfinfo"))
		{
			if (CTFEvent.isStarting() || CTFEvent.isStarted())
			{
				showCTFStatuPage(activeChar);
				return true;
			}
			activeChar.sendMessage("Capture the Flag event is not in progress.");
			return false;
		}
		else if (command.equals("ctfjoin"))
		{
			if (!CTFEvent.isPlayerParticipant(activeChar.getObjectId()))
			{
				CTFEvent.onBypass("ctf_event_participation", activeChar);
			}
			else
			{
				activeChar.sendMessage("You are already registered.");
				return false;
			}
		}
		else if (command.equals("ctfleave"))
		{
			if (CTFEvent.isPlayerParticipant(activeChar.getObjectId()))
			{
				CTFEvent.onBypass("ctf_event_remove_participation", activeChar);
			}
			else
			{
				activeChar.sendMessage("You are not registered.");
				return false;
			}
		}
		else if (command.equals("dminfo"))
		{
			if (DMEvent.isStarted() ) //DMEvent.isStarting() || 
			{
				showDMStatuPage(activeChar);
				return true;
			}
			activeChar.sendMessage("Deathmatch fight is not in progress.");
			return false;
		}
		else if (command.equalsIgnoreCase("dmjoin"))
		{
			if (!DMEvent.isPlayerParticipant(activeChar))
			{
				DMEvent.onBypass("dm_event_participation", activeChar);
			}
			else
			{
				activeChar.sendMessage("You are already registered.");
				return false;
			}
		}
		else if (command.equalsIgnoreCase("dmleave"))
		{
			if (DMEvent.isPlayerParticipant(activeChar))
			{
				DMEvent.onBypass("dm_event_remove_participation", activeChar);
			}
			else
			{
				activeChar.sendMessage("You are not registered.");
				return false;
			}
		}
		/*else if (command.equals("lminfo"))
		{
			if (LMEvent.isStarting() || LMEvent.isStarted())
			{
				showLMStatuPage(activeChar);
				return true;
			}
			else
			{
				activeChar.sendMessage("Last Man fight is not in progress.");
				return false;
			}
		}
		else if (command.equalsIgnoreCase("lmjoin"))
		{
			if (!LMEvent.isPlayerParticipant(activeChar))
			{
				LMEvent.onBypass("lm_event_participation", activeChar);
			}
			else
			{
				activeChar.sendMessage("You are already registered.");
				return false;
			}
		}
		else if (command.equalsIgnoreCase("lmleave"))
		{
			if (LMEvent.isPlayerParticipant(activeChar))
			{
				LMEvent.onBypass("lm_event_remove_participation", activeChar);
			}
			else
			{
				activeChar.sendMessage("You are not registered.");
				return false;
			}
		}*/
		 if (command.equals("tvtinfo"))
		{
			if (TvTEvent.isStarting() || TvTEvent.isStarted())
			{
				showTvTStatuPage(activeChar);
				return true;
			}
			activeChar.sendMessage("Team vs Team fight is not in progress.");
			return false;
		}
		else if (command.equals("tvtjoin"))
		{
			if (!TvTEvent.isPlayerParticipant(activeChar.getObjectId()))
			{
				TvTEvent.onBypass("tvt_event_participation", activeChar);
			}
			else
			{
				activeChar.sendMessage("You are already registered.");
				return false;
			}
		}
		else if (command.equals("tvtleave"))
		{
			if (TvTEvent.isPlayerParticipant(activeChar.getObjectId()))
			{
				TvTEvent.onBypass("tvt_event_remove_participation", activeChar);
			}
			else
			{
				activeChar.sendMessage("You are not registered.");
				return false;
			}
		}
		return true;
	}

/*	private static void showCTFStatuPage(Player activeChar)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(activeChar.getObjectId());
		html.setFile("data/html/mods/events/ctf/Status.htm");
		html.replace("%team1name%", CTFConfig.CTF_EVENT_TEAM_1_NAME);
		html.replace("%team1playercount%", String.valueOf(CTFEvent.getTeamsPlayerCounts()[0]));
		html.replace("%team1points%", String.valueOf(CTFEvent.getTeamsPoints()[0]));
		html.replace("%team2name%", CTFConfig.CTF_EVENT_TEAM_2_NAME);
		html.replace("%team2playercount%", String.valueOf(CTFEvent.getTeamsPlayerCounts()[1]));
		html.replace("%team2points%", String.valueOf(CTFEvent.getTeamsPoints()[1]));
		activeChar.sendPacket(html);	
	}

	private static void showDMStatuPage(Player activeChar)
	{
		String[] firstPositions = DMEvent.getFirstPosition(DMConfig.DM_REWARD_FIRST_PLAYERS);
		NpcHtmlMessage html = new NpcHtmlMessage(activeChar.getObjectId());
		html.setFile("data/html/mods/events/dm/Status.htm");

		String htmltext = "";
		htmltext += "<table width=\"250\">";
		htmltext += "<tr><td width=\"150\">Name</td><td width=\"100\" align=\"center\">kill's</td></tr>";
		if (firstPositions != null)
		{
			for (int i = 0; i < firstPositions.length; i++)
			{
				String[] row = firstPositions[i].split("\\,");
				htmltext += "<tr><td>" + row[0] + "</td><td align=\"center\">" + row[1] + "</td></tr>";
			}
		}
		htmltext += "</table>";

		html.replace("%positions%", htmltext);
		activeChar.sendPacket(html);
	}

	private static void showLMStatuPage(Player activeChar)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(activeChar.getObjectId());
		html.setFile("data/html/mods/events/lm/Status.htm");
		String htmltext = "";
		htmltext = String.valueOf(LMEvent.getPlayerCounts());
		html.replace("%countplayer%", htmltext);
		activeChar.sendPacket(html);
	}
*/
	
    
    private static void showCTFStatuPage(final Player activeChar) {
        final NpcHtmlMessage html = new NpcHtmlMessage(activeChar.getObjectId());
        html.setFile("data/html/mods/events/ctf/Status.htm");
        html.replace("%team1name%", CTFConfig.CTF_EVENT_TEAM_1_NAME);
        html.replace("%team1playercount%", String.valueOf(CTFEvent.getTeamsPlayerCounts()[0]));
        html.replace("%team1points%", String.valueOf(CTFEvent.getTeamsPoints()[0]));
        html.replace("%team2name%", CTFConfig.CTF_EVENT_TEAM_2_NAME);
        html.replace("%team2playercount%", String.valueOf(CTFEvent.getTeamsPlayerCounts()[1]));
        html.replace("%team2points%", String.valueOf(CTFEvent.getTeamsPoints()[1]));
        activeChar.sendPacket(html);
    }
    
/*    private static void showDMStatuPage(final Player activeChar) {
        final String[] firstPositions = DMEvent.getFirstPosition(DMConfig.DM_REWARD_FIRST_PLAYERS);
        final NpcHtmlMessage html = new NpcHtmlMessage(activeChar.getObjectId());
        html.setFile("data/html/mods/events/dm/Status.htm");
        String htmltext = "";
        htmltext += "<table width=\"250\">";
        htmltext += "<tr><td width=\"150\">Name</td><td width=\"100\" align=\"center\">kill's</td></tr>";
        if (firstPositions != null) {
            for (int i = 0; i < firstPositions.length; ++i) {
                final String[] row = firstPositions[i].split("\\,");
                htmltext = htmltext + "<tr><td>" + row[0] + "</td><td align=\"center\">" + row[1] + "</td></tr>";
            }
        }
        htmltext += "</table>";
        html.replace("%positions%", htmltext);
        activeChar.sendPacket(html);
    }
   */ 
 
    private static void showDMStatuPage(final Player activeChar) {
    	StringBuilder sb = new StringBuilder();
        final String[] firstPositions = DMEvent.getFirstPosition(DMConfig.DM_REWARD_FIRST_PLAYERS);
        final NpcHtmlMessage html = new NpcHtmlMessage(activeChar.getObjectId());
        html.setFile("data/html/mods/events/dm/Status.htm");
      
        sb.append("<table width=\"250\">");
        sb.append("<tr><td width=\"150\">Name</td><td width=\"100\" align=\"center\">kill's</td></tr>");
        if (firstPositions != null) {
            for (int i = 0; i < firstPositions.length; ++i) {
                final String[] row = firstPositions[i].split("\\,");
                sb.append("<tr><td>" + row[0] + "</td><td align=\"center\">" + row[1] + "</td></tr>");
            }
        }
        sb.append("</table>");
        html.replace("%positions%", sb.toString());
        activeChar.sendPacket(html);
    }
    
	private static void showTvTStatuPage(Player activeChar)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(activeChar.getObjectId());
		html.setFile("data/html/mods/events/tvt/Status.htm");
		html.replace("%team1name%", TvTConfig.TVT_EVENT_TEAM_1_NAME);
		html.replace("%team1playercount%", String.valueOf(TvTEvent.getTeamsPlayerCounts()[0]));
		html.replace("%team1points%", String.valueOf(TvTEvent.getTeamsPoints()[0]));
		html.replace("%team2name%", TvTConfig.TVT_EVENT_TEAM_2_NAME);
		html.replace("%team2playercount%", String.valueOf(TvTEvent.getTeamsPlayerCounts()[1]));
		html.replace("%team2points%", String.valueOf(TvTEvent.getTeamsPoints()[1]));
		activeChar.sendPacket(html);	
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}