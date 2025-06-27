package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

/*
+ * This program is free software: you can redistribute it and/or modify it under
+ * the terms of the GNU General Public License as published by the Free Software
+ * Foundation, either version 3 of the License, or (at your option) any later
+ * version.
+ * 
+ * This program is distributed in the hope that it will be useful, but WITHOUT
+ * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
+ * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
+ * details.
+ * 
+ * You should have received a copy of the GNU General Public License along with
+ * this program. If not, see <http://www.gnu.org/licenses/>.
+ */

import java.text.DecimalFormat;
import java.util.StringTokenizer;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.data.xml.NpcData;
import net.sf.l2j.gameserver.enums.items.CrystalType;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.item.DropCategory;
import net.sf.l2j.gameserver.model.item.DropData;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import Customs.data.IconsTable;

public class DropInfo implements IVoicedCommandHandler
{
	private static final String[] COMMANDS =
	{
		"drop"
	};
	
	private final static int PAGE_LIMIT = 8;
	
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		int page = 1;
		int npcid = 1;
		try
		{
			final StringTokenizer st = new StringTokenizer(target, " ");
			if(st.hasMoreTokens())
			{
				npcid = Integer.parseInt(st.nextToken());
			}
			page = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : 1;
		}
		catch(Exception e)
		{
			
		}
		try
		{
			NpcHtmlMessage html = new NpcHtmlMessage(0);
			if(npcid == 1)
			{
				Object targetmob = activeChar.getTarget();
				Npc npc = (Npc) targetmob;
				npcid = npc.getTemplate().getNpcId();//getIdTemplate
			}
			final NpcTemplate npcData = NpcData.getInstance().getTemplate(npcid);
			String droptext = "";
			if (command.startsWith("drop"))
			{
				try
				{
					html.setFile("data/html/mods/mobinfo/mobdrop.htm");
					
					if (npcData.getDropData().isEmpty())
					{
						droptext = "WARNING: This Npc has no Drops!";
						html.replace("%drops%", droptext);
						activeChar.sendPacket(html);
						return false;
					}
					String champ = "";
					String imgsg = "<img src=\"l2ui.squaregray\" width=\"274\" height=\"1\">";
					String ta_op = "<table bgcolor=000000 cellspacing=2 cellpadding=1><tr><td height=38 fixwidth=36><img src=\"";
					String ta_op2 = "\" height=32 width=32></td><td fixwidth=234><table VALIGN=top valing = top width=234 cellpadding=0 cellspacing=0><tr>";

					int myPage = 1;
					int i = 0;
					int shown = 0;
					boolean hasMore = false;
				
					//final StringBuilder droptext1 = StringBuilder(9000);
					
					final StringBuilder droptext1 = new StringBuilder(2000);
					StringUtil.append(droptext1, "<html><title>Show droplist page ", page, "</title><body><center><font color=\"LEVEL\">", npcData.getName(),"</font></center><br>");
					
					for (DropCategory cat : npcData.getDropData())
					{
						if (shown == PAGE_LIMIT)
						{
							hasMore = true;
							break;
						}
						for (DropData drop : cat.getAllDrops())
						{
							final Item item = ItemTable.getInstance().getTemplate(drop.getItemId());
							if (item == null)
								continue;
							
							
							
							final String type = ((cat.isSweep()) ? "Spoil" : "Drop");
							
							if (myPage != page)
							{
								i++;
								if (i == PAGE_LIMIT)
								{
									myPage++;
									i = 0;
								}
								continue;
							}
							
							if (shown == PAGE_LIMIT)
							{
								hasMore = true;
								break;
							}
					
							String smind = null, drops = null;
							String name = item.getName();
							double chance = ((double)drop.getChance()/10000);
							
							if (item.getCrystalType() == CrystalType.NONE)
							{
								smind = "<img src=\"L2UI_CH3.joypad_shortcut\" width=16 height=16>";
							}
							else if (item.getCrystalType() == CrystalType.D)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_D\" width=16 height=16>";
							}
							else if (item.getCrystalType() == CrystalType.C)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_C\" width=16 height=16>";
							}
							else if (item.getCrystalType() == CrystalType.B)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_B\" width=16 height=16>";
							}
							else if (item.getCrystalType() == CrystalType.A)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_A\" width=16 height=16>";
							}
							else if (item.getCrystalType() == CrystalType.S)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_S\" width=16 height=16>";
							}
		
							if (chance <= 0.001)
							{
								DecimalFormat df = new DecimalFormat("#.####");
								drops = df.format(chance);
							}
							else if (chance <= 0.01)
							{
								DecimalFormat df = new DecimalFormat("#.###");
								drops = df.format(chance);
							}
							else
							{
								DecimalFormat df = new DecimalFormat("##.##");
								drops = df.format(chance);
							}	
							if (name.startsWith("Recipe - Sealed"))
								name = "<font color=00FF00>(Re)</font><font color=FF00FF>(Sl)</font>" + name.substring(16);
							if (name.startsWith("Sealed "))
								name = "<font color=FF00FF>(Sl)</font>" + name.substring(7);
							if (name.startsWith("Common Item - "))
								name = "<font color=00FFFF>(Ci)</font>" + name.substring(14);
							if (name.startsWith("Recipe: "))
								name = "<font color=00FF00>(Re)</font>" + name.substring(8);
							if (name.startsWith("Recipe -"))
								name = "<font color=00FF00>(Re)</font>" + name.substring(8);
							if (name.startsWith("Mid-Grade Life Stone"))
								name = "<font color=fff600>Mid-Grade LS</font>" + name.substring(20);
							if (name.startsWith("High-Grade Life Stone"))
								name = "<font color=fff600>High-Grade LS</font>" + name.substring(21);
							if (name.startsWith("Top-Grade Life Stone"))
								name = "<font color=fff600>Top-Grade LS</font>" + name.substring(20);
							if (name.startsWith("Forgotten Scroll - "))
								name = "<font color=fff600>FS - </font>" + name.substring(19);
							if (name.startsWith("Greater Dye of "))
								name = "<font color=fff600>G Dye of </font>" + name.substring(15);
							
							droptext1.append(ta_op+ IconsTable.getIcon(item.getItemId()) +ta_op2+"<td align=left width=16>" +smind+ "</td><td align=left width=260><font color=fff600>" +name+ "</font></td></tr><tr><td align=left width=16><img src=\"L2UI_CH3.QuestWndToolTipBtn\" width=16 height=16></td><td align=left width=55><font color=E15656>"+type+" Count : "+drop.getMinDrop()+"-"+drop.getMaxDrop()+"  Chance : " +drops+ "%</font></td></tr></table></td></tr></table>" + imgsg);
							shown++;
						}
					}
					droptext1.append("<table width=\"100%\" bgcolor=000000><tr>");
					
					if (page > 1)
					{
						droptext1.append("<td width=120><a action=\"bypass -h voice_drop ");
						droptext1.append(npcid);
						droptext1.append(" ");
						droptext1.append(page - 1);
						droptext1.append("\">Prev Page</a></td>");
						if (!hasMore)
						{
							droptext1.append("<td width=100>Page ");
							droptext1.append(page);
							droptext1.append("</td><td width=70></td></tr>");
						}
					}
					
					if (hasMore)
					{
						if (page <= 1)
							droptext1.append("<td width=120></td>");
							
						
						droptext1.append("<td width=100>Page ");
							droptext1.append(page);
							droptext1.append("</td><td width=70><a action=\"bypass -h voice_drop ");
							droptext1.append(npcid);
							droptext1.append(" ");
							droptext1.append(page + 1);
							droptext1.append("\">Next Page</a></td></tr>");
						
					}
					droptext1.append("</table>");
					droptext = droptext1.toString();
					html.replace("%drops%", droptext);
					html.replace("%npcname%", npcData.getName());
					html.replace("%npcid%", npcData.getNpcId());
					activeChar.sendPacket(html);
				}
				catch (Exception e)
				{
					activeChar.sendMessage("Something went wrong with the drop preview.");
				}
			}
		}
		catch (Exception e)
		{
			activeChar.sendMessage("You cant use this option with this target.");
		}
		return false;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return COMMANDS;
	}
}