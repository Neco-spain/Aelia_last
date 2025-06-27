package net.sf.l2j.gameserver.model.actor.instance;

import java.util.StringTokenizer;

import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.data.manager.ZoneManager;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.model.zone.ZoneType;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;

import Custom.CustomConfig;
import Customs.Events.PartyFarm.PartyFarm;

/**
 * @author `Heroin Adapter Gandalf PartyTeleporter, Lucas Fernandes
 */
public class PartyTeleporter extends Folk
{
	private static final int npcid = CustomConfig.NPC_ID_PT_TELEPORTER; // npc id
	// -------------------------------------
	// Teleport Location Coordinates X,Y,Z.
	// Use /loc command in game to find them.
	private static final int locationX = CustomConfig.PARTY_TELE_LOCATION[0]; // npc id
	private static final int locationY = CustomConfig.PARTY_TELE_LOCATION[1]; // npc id
	private static final int locationZ = CustomConfig.PARTY_TELE_LOCATION[2]; // npc id
	// -------------------------------------
	// -------------------------------------
	// Select the id of your zone.
	// If you dont know how to find your zone id is simple.
	// Go to data/zones/(your zone file).xml and find your zone
	// E.g: <zone name="dion_monster_pvp" id="6" type="ArenaZone" shape="NPoly" minZ="-3596" maxZ="0">
	/** The id of your zone is id="6" */
	/** --------------------------------------------------------------------------- */
	/** WARNING: If your zone does not have any id or your location is not on any zone in data/zones/ folder, you have to add one by your self */ // required to calculate parties & players
	/** --------------------------------------------------------------------------- */
	private static final int ZoneId = CustomConfig.NPC_PT_ZONEID; // Here you have to set your zone Id
	// -------------------------------------
	private static final int MinPtMembers = CustomConfig.NPC_PT_MINPT_MEMBERS; // Minimum Party Members Count For Enter on Zone.
	private static final int ItemConsumeId = CustomConfig.NPC_PT_ITEMCONSUME_ID; // Item Consume id.
	private static final int ItemConsumeNum = CustomConfig.NPC_PT_ITEMCOMSUME_QT; // Item Consume Am.ount.
	private static final boolean ShowPlayersInside = CustomConfig.NPC_PT_SHOWINSIDE_PLAYERS; // If you set it true, NPC will show how many players are inside area.
	private static final boolean ShowPartiesInside = CustomConfig.NPC_PT_SHOWINSIDE_PARTIES; // If you set it true, NPC will show how many parties are inside area.
	private static String ItemName = ItemTable.getInstance().getTemplate(ItemConsumeId).getName(); // Item name, Dont Change this
	private String htmContent;
	private String htmContent1;
	
	public PartyTeleporter(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		
		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken(); // Get actual command
		
		if (actualCommand.equalsIgnoreCase("partytp") && PartyFarm.getInstance().isActivated())
		{
			TP(player);
		}
		super.onBypassFeedback(player, command);
	}
	
	public int getPartiesInside(int zoneId)// Calculating parties inside party area.
	{
		int i = 0;
		for (ZoneType zone : ZoneManager.getInstance().getZones(locationX, locationY, locationZ))
		{
			if (zone.getId() == zoneId)
			{
				for (Creature character : zone.getCharactersInside())
				{
					if ((character instanceof Player) && (!((Player) character).getClient().isDetached()) && (((Player) character).getParty() != null) && ((Player) character).getParty().isLeader((Player) character))
					{
						i++;
					}
				}
			}
		}
		return i;
	}
	
	public int getPlayerInside(int zoneId)// Calculating players inside party area.
	{
		int i = 0;
		for (ZoneType zone : ZoneManager.getInstance().getZones(locationX, locationY, locationZ))
		{
			if (zone.getId() == zoneId)
			{
				for (Creature character : zone.getCharactersInside())
				{
					if ((character instanceof Player) && (!((Player) character).getClient().isDetached()))
					{
						i++;
					}
				}
			}
		}
		return i;
	}
	
	private static boolean PartyItemsOk(Player player)
	// Checks if all party members have the item in their inventory.
	// If pt member has not enough items, party not allowed to enter.
	{
		try
		{
			for (Player member : player.getParty().getMembers()){

				if (member.getInventory().getItemByItemId(ItemConsumeId) == null){
					player.sendMessage("Your party member " + member.getName() + " does not have enough items.");
					return false;
				}
				if (member.getInventory().getItemByItemId(ItemConsumeId).getCount() < ItemConsumeNum){
					player.sendMessage("Your party member " + member.getName() + " does not have enough items.");
					return false;
				}
			}
			return true;
			
		}
		catch (Exception e)
		{
			player.sendMessage("Something went wrong try again.");
			return true;
		}
	}
	  
	private static boolean hwidCheck(Player player){
		for(Player member : player.getParty().getMembers()){
			if(member.equals(player))
				continue;
			if(player.gethwid().equals(member.gethwid())  ){ //|| player.getIpAddress().equals(member.getIpAddress())
				  player.sendMessage("Same IP can't register!!");
				  return false;					
			}
		}
		return true;
	}
	
	private static boolean hwidAllowed(Player player){
		for(Player member : player.getParty().getMembers()){
			if(member.equals(player))
				continue;
			if(player.gethwid().equals(member.gethwid()) ){
				  return false;					
			}
		}
		return true;
	}
	
	private static void proccessTP(Player player) // Teleporting party members to zone
	{
		for (Player member : player.getParty().getMembers())
		{
			member.teleportTo(locationX, locationY, locationZ, 1);// Location X, Y ,Z
		}
	}
	
	private static void TP(Player player) // Teleport player & his party
	{
		try
		{
			Party pt = player.getParty();
			if (pt == null)
			{
				player.sendMessage("You are not currently on party.");
				return;
			}
			if (!pt.isLeader(player))
			{
				player.sendMessage("You are not party leader.");
				return;
			}
			if (pt.getMembersCount() < MinPtMembers)
			{
				player.sendMessage("You are going to need a bigger party " + "in order to enter party area.");
				return;
			}
			if (!PartyItemsOk(player))
			{
				return;
			}
			if(!hwidCheck(player))
				return;
			
			for (Player ppl : pt.getMembers()){
				if(ppl.getPvpKills() < 2){
					player.sendMessage("You must have at least 1 Pvp in order to enter party area.");
					return;
				}
			}
			
			proccessTP(player);
			for (Player ppl : pt.getMembers())
			{
				if (ppl.getObjectId() != player.getObjectId()) // Dont send this message to pt leader.
				{
					ppl.sendMessage("Your party leader asked to teleport on party area!");// Message only to party members
				}
				ppl.sendMessage(ItemConsumeNum + " " + ItemName + " have been dissapeared.");// Item delete from inventory message
				ppl.getInventory().destroyItemByItemId("Party_Teleporter", ItemConsumeId, ItemConsumeNum, ppl, ppl);// remove item from inventory
				ppl.sendPacket(new InventoryUpdate());// Update
				ppl.sendPacket(new ItemList(ppl, false));// Update
				ppl.sendPacket(new StatusUpdate(ppl));// Update
			}
			// Sends message to party leader.
			player.sendMessage((ItemConsumeNum * player.getParty().getMembersCount()) + " " + ItemName + " dissapeard from your party.");
			
		}
		catch (Exception e)
		{
			player.sendMessage("Something went wrong try again.");
		}
	}
	
/*	public String nextEvent(){
		String next = "";
		  
		if(InitialPartyFarm.getInstance().formatDate() != null){
			next = "<center><table bgcolor=000000 width=300>"
			  	+ "<tr><td><center>"
			  	+ "<font color=\"ae9988\">Next Event in: " + InitialPartyFarm.getInstance().formatDate() +"</font>"
			  	+ "</center></td></tr></table></center>";
			return next;
		}
		return  next;
	}*/
	
	public String nextEvent(){
		String next = "";
		if(PartyFarm.getInstance().getTime() != null){
			if(PartyFarm.getInstance().isStarted()){
				next = "<center><table bgcolor=000000 width=300>"
				  	+ "<tr><td><center>"
				  	+ "<font color=\"ae9988\">Party Farm ending in: " + PartyFarm.getInstance().getTimeEnd() +"</font>"
				  	+ "</center></td></tr></table></center>";
			}
			else{
				next = "<center><table bgcolor=000000 width=300>"
				  	+ "<tr><td><center>"
				  	+ "<font color=\"ae9988\">Next Party Farm in: " + PartyFarm.getInstance().getTime() +"</font>"
				  	+ "</center></td></tr></table></center>";
			}
		}
	return  next;
	}
	
	@Override
	public void showChatWindow(Player player)
	{
		final int npcId = CustomConfig.NPC_ID_PT_TELEPORTER;
		if (npcId == npcid)
		{
			htmContent = "data/html/mods/PartyTeleporter/PartyTeleporter.htm";
			htmContent1 = "data/html/mods/PartyTeleporter/PartyTeleporter-no.htm";
			if (htmContent != null)
			{
				final NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());
				
				if(PartyFarm.getInstance().isActivated())
					npcHtmlMessage.setFile(htmContent);
				else
					npcHtmlMessage.setFile(htmContent1);
				
				npcHtmlMessage.replace("%objectId%", String.valueOf(getObjectId()));
				npcHtmlMessage.replace("%nextEvent%", nextEvent() );
				npcHtmlMessage.replace("%player%", player.getName());// Replaces %player% with player name on html
				npcHtmlMessage.replace("%itemname%", ItemName);// Item name replace on html
				npcHtmlMessage.replace("%price%", player.getParty() != null ? "" + (ItemConsumeNum * player.getParty().getMembersCount()) + "" : "0");// Price calculate replace
				npcHtmlMessage.replace("%minmembers%", "" + MinPtMembers);// Mimum entry party members replace
				npcHtmlMessage.replace("%allowed%", isAllowedEnter(player) ? "<font color=00FF00>allowed</font>" : "<font color=FF0000>not allowed</font>");// Condition checker replace on html
				npcHtmlMessage.replace("%parties%", ShowPartiesInside ? "<font color=FFA500>Parties Inside: " + getPartiesInside(ZoneId) + "</font><br>" : "");// Parties inside
				npcHtmlMessage.replace("%players%", ShowPlayersInside ? "<font color=FFA500>Players Inside: " + getPlayerInside(ZoneId) + "</font><br>" : "");// Players Inside
				player.sendPacket(npcHtmlMessage);
			}
			
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
	}
	
	private static boolean isAllowedEnter(Player player) // Checks if player & his party is allowed to teleport.
	{
		if (player.getParty() != null)
		{
			if ((player.getParty().getMembersCount() >= MinPtMembers) && PartyItemsOk(player) && player.getPvpKills() >= 1 && hwidAllowed(player) ) // Party Length & Item Checker
			{
				return true;
			}
			return false;
		}
		return false;
	}
	
}