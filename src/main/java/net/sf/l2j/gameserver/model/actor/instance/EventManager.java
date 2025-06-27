/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.data.cache.HtmCache;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import Customs.Events.CTF.CTFConfig;
import Customs.Events.CTF.CTFEvent;
import Customs.Events.DM.DMConfig;
import Customs.Events.DM.DMEvent;
import Customs.Events.TvT.TvTConfig;
import Customs.Events.TvT.TvTEvent;

public class EventManager extends Npc
{
	private static final String ctfhtmlPath = "data/html/mods/events/ctf/";
	private static final String TvthtmlPath = "data/html/mods/events/tvt/";
	private static final String dmhtmlPath = "data/html/mods/events/dm/";
	private static final String lmhtmlPath = "data/html/mods/events/lm/";
	
	public EventManager(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player playerInstance, String command)
	{
		CTFEvent.onBypass(command, playerInstance);
		TvTEvent.onBypass(command, playerInstance);
		DMEvent.onBypass(command, playerInstance);
	//	LMEvent.onBypass(command, playerInstance);
	}
	
	@Override
	public void showChatWindow(Player playerInstance, int val)
	{
		if (playerInstance == null)
			return;
		
		if (TvTEvent.isParticipating())
		{
			final boolean isParticipant = TvTEvent.isPlayerParticipant(playerInstance.getObjectId());
			final String htmContent;
			
			if (!isParticipant)
				htmContent = HtmCache.getInstance().getHtm(TvthtmlPath + "Participation.htm");
			else
				htmContent = HtmCache.getInstance().getHtm(TvthtmlPath + "RemoveParticipation.htm");
			
			if (htmContent != null)
			{
				int[] teamsPlayerCounts = TvTEvent.getTeamsPlayerCounts();
				NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());
				
				npcHtmlMessage.setHtml(htmContent);
				npcHtmlMessage.replace("%objectId%", String.valueOf(getObjectId()));
				npcHtmlMessage.replace("%team1name%", TvTConfig.TVT_EVENT_TEAM_1_NAME);
				npcHtmlMessage.replace("%team1playercount%", String.valueOf(teamsPlayerCounts[0]));
				npcHtmlMessage.replace("%team2name%", TvTConfig.TVT_EVENT_TEAM_2_NAME);
				npcHtmlMessage.replace("%team2playercount%", String.valueOf(teamsPlayerCounts[1]));
				npcHtmlMessage.replace("%playercount%", String.valueOf(teamsPlayerCounts[0] + teamsPlayerCounts[1]));
				if (!isParticipant)
				{
					npcHtmlMessage.replace("%fee%", TvTEvent.getParticipationFee());
				}
				
				playerInstance.sendPacket(npcHtmlMessage);
			}
		}
		else if (TvTEvent.isStarting() || TvTEvent.isStarted())
		{
			final String htmContent = HtmCache.getInstance().getHtm(TvthtmlPath + "Status.htm");
			
			if (htmContent != null)
			{
				int[] teamsPlayerCounts = TvTEvent.getTeamsPlayerCounts();
				int[] teamsPointsCounts = TvTEvent.getTeamsPoints();
				NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());
				
				npcHtmlMessage.setHtml(htmContent);
				npcHtmlMessage.replace("%team1name%", TvTConfig.TVT_EVENT_TEAM_1_NAME);
				npcHtmlMessage.replace("%team1playercount%", String.valueOf(teamsPlayerCounts[0]));
				npcHtmlMessage.replace("%team1points%", String.valueOf(teamsPointsCounts[0]));
				npcHtmlMessage.replace("%team2name%", TvTConfig.TVT_EVENT_TEAM_2_NAME);
				npcHtmlMessage.replace("%team2playercount%", String.valueOf(teamsPlayerCounts[1]));
				npcHtmlMessage.replace("%team2points%", String.valueOf(teamsPointsCounts[1])); // <---- array index from 0 to 1 thx DaRkRaGe
				playerInstance.sendPacket(npcHtmlMessage);
			}
		}
		else if (DMEvent.isParticipating())
		{
			final boolean isParticipant = DMEvent.isPlayerParticipant(playerInstance.getObjectId()); 
			final String htmContent;

			if (!isParticipant)
			    htmContent = HtmCache.getInstance().getHtm(dmhtmlPath + "Participation.htm");
			else
				htmContent = HtmCache.getInstance().getHtm(dmhtmlPath + "RemoveParticipation.htm");

	    	if (htmContent != null)
	    	{
	    		int PlayerCounts = DMEvent.getPlayerCounts();
	    		NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());

				npcHtmlMessage.setHtml(htmContent);
	    		npcHtmlMessage.replace("%objectId%", String.valueOf(getObjectId()));
				npcHtmlMessage.replace("%playercount%", String.valueOf(PlayerCounts));
				if (!isParticipant)
					npcHtmlMessage.replace("%fee%", DMEvent.getParticipationFee());

				playerInstance.sendPacket(npcHtmlMessage);
	    	}
		}
		else if (DMEvent.isStarting() || DMEvent.isStarted())
		{
			final String htmContent = HtmCache.getInstance().getHtm(dmhtmlPath + "Status.htm");

	    	if (htmContent != null)
	    	{
	    		String[] firstPositions = DMEvent.getFirstPosition(DMConfig.DM_REWARD_FIRST_PLAYERS);
	    		NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());
	    		
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
	    		
				npcHtmlMessage.setHtml(htmContent);
				npcHtmlMessage.replace("%positions%", htmltext);
				playerInstance.sendPacket(npcHtmlMessage);
	    	}
		}
		/*	else if (LMEvent.isParticipating())
		{
			final boolean isParticipant = LMEvent.isPlayerParticipant(playerInstance.getObjectId()); 
			final String htmContent;

			if (!isParticipant)
			    htmContent = HtmCache.getInstance().getHtm(lmhtmlPath + "Participation.htm");
			else
			    htmContent = HtmCache.getInstance().getHtm(lmhtmlPath + "RemoveParticipation.htm");

	    	if (htmContent != null)
	    	{
	    		int PlayerCounts = LMEvent.getPlayerCounts();
	    		NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());

				npcHtmlMessage.setHtml(htmContent);
	    		npcHtmlMessage.replace("%objectId%", String.valueOf(getObjectId()));
				npcHtmlMessage.replace("%playercount%", String.valueOf(PlayerCounts));
				if (!isParticipant)
					npcHtmlMessage.replace("%fee%", LMEvent.getParticipationFee());

				playerInstance.sendPacket(npcHtmlMessage);
	    	}
		}
		else if (LMEvent.isStarting() || LMEvent.isStarted())
		{
			final String htmContent = HtmCache.getInstance().getHtm(lmhtmlPath + "Status.htm");

	    	if (htmContent != null)
	    	{
	    		NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());
	    		String htmltext = "";
	    		htmltext = String.valueOf(LMEvent.getPlayerCounts());
				npcHtmlMessage.setHtml(htmContent);
				npcHtmlMessage.replace("%countplayer%", htmltext);
				playerInstance.sendPacket(npcHtmlMessage);
	    	}
		}*/
		else if (CTFEvent.isParticipating())
		{
			final boolean isParticipant = CTFEvent.isPlayerParticipant(playerInstance.getObjectId());
			final String htmContent;
			
			if (!isParticipant)
				htmContent = HtmCache.getInstance().getHtm(ctfhtmlPath + "Participation.htm");
			else
				htmContent = HtmCache.getInstance().getHtm(ctfhtmlPath + "RemoveParticipation.htm");
			
			if (htmContent != null)
			{
				int[] teamsPlayerCounts = CTFEvent.getTeamsPlayerCounts();
				NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());
				
				npcHtmlMessage.setHtml(htmContent);
				npcHtmlMessage.replace("%objectId%", String.valueOf(getObjectId()));
				npcHtmlMessage.replace("%team1name%", CTFConfig.CTF_EVENT_TEAM_1_NAME);
				npcHtmlMessage.replace("%team1playercount%", String.valueOf(teamsPlayerCounts[0]));
				npcHtmlMessage.replace("%team2name%", CTFConfig.CTF_EVENT_TEAM_2_NAME);
				npcHtmlMessage.replace("%team2playercount%", String.valueOf(teamsPlayerCounts[1]));
				npcHtmlMessage.replace("%playercount%", String.valueOf(teamsPlayerCounts[0] + teamsPlayerCounts[1]));
				if (!isParticipant)
				{
					npcHtmlMessage.replace("%fee%", CTFEvent.getParticipationFee());
				}
				
				playerInstance.sendPacket(npcHtmlMessage);
			}
		}
		else if (CTFEvent.isStarting() || CTFEvent.isStarted())
		{
			final String htmContent = HtmCache.getInstance().getHtm(ctfhtmlPath + "Status.htm");
			
			if (htmContent != null)
			{
				int[] teamsPlayerCounts = CTFEvent.getTeamsPlayerCounts();
				int[] teamsPointsCounts = CTFEvent.getTeamsPoints();
				NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());
				
				npcHtmlMessage.setHtml(htmContent);
				npcHtmlMessage.replace("%team1name%", CTFConfig.CTF_EVENT_TEAM_1_NAME);
				npcHtmlMessage.replace("%team1playercount%", String.valueOf(teamsPlayerCounts[0]));
				npcHtmlMessage.replace("%team1points%", String.valueOf(teamsPointsCounts[0]));
				npcHtmlMessage.replace("%team2name%", CTFConfig.CTF_EVENT_TEAM_2_NAME);
				npcHtmlMessage.replace("%team2playercount%", String.valueOf(teamsPlayerCounts[1]));
				npcHtmlMessage.replace("%team2points%", String.valueOf(teamsPointsCounts[1])); // <---- array index from 0 to 1 thx DaRkRaGe
				playerInstance.sendPacket(npcHtmlMessage);
			}
		}

		playerInstance.sendPacket(ActionFailed.STATIC_PACKET);
	}
}