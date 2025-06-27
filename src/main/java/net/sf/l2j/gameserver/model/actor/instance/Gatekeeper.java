package net.sf.l2j.gameserver.model.actor.instance;

import java.util.Calendar;
import java.util.StringTokenizer;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.data.cache.HtmCache;
import net.sf.l2j.gameserver.data.manager.ZoneManager;
import net.sf.l2j.gameserver.data.xml.TeleportLocationData;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.model.location.TeleportLocation;
import net.sf.l2j.gameserver.model.zone.ZoneType;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;

import Custom.CustomConfig;
import Customs.Events.PartyFarm.PartyFarm;
import Customs.PvpZone.RandomZoneManager;

/**
 * An instance type extending {@link Folk}, used for teleporters.<br>
 * <br>
 * A teleporter allows {@link Player}s to teleport to a specific location, for a fee.
 */
public final class Gatekeeper extends Folk
{

	
	public Gatekeeper(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String filename = "";
		if (val == 0)
			filename = "" + npcId;
		else
			filename = npcId + "-" + val;
		
		return "data/html/teleporter/" + filename + ".htm";
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		// Generic PK check. Send back the HTM if found and cancel current action.
		if (!Config.KARMA_PLAYER_CAN_USE_GK && player.getKarma() > 0 && showPkDenyChatWindow(player, "teleporter"))
			return;
		

		if (command.startsWith("goto"))
		{
			final StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			
			// No more tokens.
			if (!st.hasMoreTokens())
				return;
			
			// No interaction possible with the NPC.
			if (!canInteract(player))
				return;
			
			// Retrieve the list.
			final TeleportLocation list = TeleportLocationData.getInstance().getTeleportLocation(Integer.parseInt(st.nextToken()));
			if (list == null)
				return;
			
			// Siege is currently in progress in this location.
			//custom disabled
			/*if (CastleManager.getInstance().getActiveSiege(list.getX(), list.getY(), list.getZ()) != null)
			{
				player.sendPacket(SystemMessageId.CANNOT_PORT_VILLAGE_IN_SIEGE);
				return;
			}*/
			
			
			// The list is for noble, but player isn't noble.
			if (list.isNoble() && !player.isNoble())
			{
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/teleporter/nobleteleporter-no.htm");
				html.replace("%objectId%", getObjectId());
				html.replace("%npcname%", getName());
				player.sendPacket(html);
				
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			// Retrieve price list. Potentially cut it by 2 depending of current date.
			int price = list.getPrice();
			
			if (!list.isNoble())
			{
				Calendar cal = Calendar.getInstance();
				if (cal.get(Calendar.HOUR_OF_DAY) >= 20 && cal.get(Calendar.HOUR_OF_DAY) <= 23 && (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7))
					price /= 2;
			}
			
			// Delete related items, and if successful teleport the player to the location.
			if (player.destroyItemByItemId("Teleport ", (list.isNoble()) ? 6651 : 57, price, this, true))
				player.teleportTo(list, 20);
			
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
		else if (command.startsWith("Chat"))
		{
			int val = 0;
			try
			{
				val = Integer.parseInt(command.substring(5));
			}
			catch (IndexOutOfBoundsException ioobe)
			{
			}
			catch (NumberFormatException nfe)
			{
			}
			
			// Show half price HTM depending of current date. If not existing, use the regular "-1.htm".
			if (val == 1)
			{
				Calendar cal = Calendar.getInstance();
				if (cal.get(Calendar.HOUR_OF_DAY) >= 20 && cal.get(Calendar.HOUR_OF_DAY) <= 23 && (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7))
				{
					final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					
					String content = HtmCache.getInstance().getHtm("data/html/teleporter/half/" + getNpcId() + ".htm");
					if (content == null)
						content = HtmCache.getInstance().getHtmForce("data/html/teleporter/" + getNpcId() + "-1.htm");
					
					html.setHtml(content);
					html.replace("%objectId%", getObjectId());
					html.replace("%npcname%", getName());
					player.sendPacket(html);
					
					player.sendPacket(ActionFailed.STATIC_PACKET);
					return;
				}
			}
			showChatWindow(player, val);
		}
		else if(command.startsWith("showPartyHtml")){
			/*final int npcId = CustomConfig.NPC_ID_PT_TELEPORTER;
			if (npcId == npcid)
			{*/
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
					
				//	if(PartyFarm.getInstance().isActivated()) {
						npcHtmlMessage.replace("%parties%", ShowPartiesInside ? "<font color=FFA500>Parties Inside: " + getPartiesInside(_zoneId) + "</font><br>" : "");// Parties inside
						npcHtmlMessage.replace("%players%", ShowPlayersInside ? "<font color=FFA500>Players Inside: " + getPlayerInside(_zoneId) + "</font><br>" : "");// Players Inside
				//	}
					player.sendPacket(npcHtmlMessage);
				}
				
				player.sendPacket(ActionFailed.STATIC_PACKET);
			//}
		}

		else if(command.startsWith("partytp")){
			if (PartyFarm.getInstance().isActivated())
				TP(player);
		}

		//custom pvp zone
		else if (command.startsWith("pvpzone")){
			if (RandomZoneManager.getInstance().getCurrentZone() != null)
				player.teleportTo(RandomZoneManager.getInstance().getCurrentZone().getLoc(), 20);
		}
		
		
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(Player player, int val)
	{
		// Generic PK check. Send back the HTM if found and cancel current action.
		if (!Config.KARMA_PLAYER_CAN_USE_GK && player.getKarma() > 0 && showPkDenyChatWindow(player, "teleporter"))
			return;
		
		showChatWindow(player, getHtmlPath(getNpcId(), val));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	 //private static final int npcid = CustomConfig.NPC_ID_PT_TELEPORTER; // npc id
	// -------------------------------------
	// Teleport Location Coordinates X,Y,Z.
	// Use /loc command in game to find them.
	
	private static final int locationX = CustomConfig.PARTY_TELE_LOCATION[0]; // npc id
	private static final int locationY = CustomConfig.PARTY_TELE_LOCATION[1]; // npc id
	private static final int locationZ = CustomConfig.PARTY_TELE_LOCATION[2]; // npc id
	
	static int rndLoc = Rnd.get(CustomConfig.PARTY_TELE_LOCATION1.size());
	
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
	private static final int _zoneId = CustomConfig.NPC_PT_ZONEID; // Here you have to set your zone Id
	// -------------------------------------
	private static final int MinPtMembers = CustomConfig.NPC_PT_MINPT_MEMBERS; // Minimum Party Members Count For Enter on Zone.
	private static final int ItemConsumeId = CustomConfig.NPC_PT_ITEMCONSUME_ID; // Item Consume id.
	private static final int ItemConsumeNum = CustomConfig.NPC_PT_ITEMCOMSUME_QT; // Item Consume Am.ount.
	private static final boolean ShowPlayersInside = CustomConfig.NPC_PT_SHOWINSIDE_PLAYERS; // If you set it true, NPC will show how many players are inside area.
	private static final boolean ShowPartiesInside = CustomConfig.NPC_PT_SHOWINSIDE_PARTIES; // If you set it true, NPC will show how many parties are inside area.
	private static String ItemName = ItemTable.getInstance().getTemplate(ItemConsumeId).getName(); // Item name, Dont Change this
	private String htmContent;
	private String htmContent1;

	
	public static  int getPartiesInside(int zoneId)// Calculating parties inside party area.
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
	
	public static  int getPlayerInside(int zoneId)// Calculating players inside party area.
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
	
	private static boolean PartyItemsOk(Player player,boolean party)
	// Checks if all party members have the item in their inventory.
	// If pt member has not enough items, party not allowed to enter.
	{
		try
		{
			
			if(party) {
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
			}
			else {
				if (player.getInventory().getItemByItemId(ItemConsumeId) == null){
					player.sendMessage("You do not have enough items to join.");
					return false;
				}
				if (player.getInventory().getItemByItemId(ItemConsumeId).getCount() < ItemConsumeNum){
					player.sendMessage("You do not have enough items to join.");
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
		if(CustomConfig.PARTY_FARM_HWID_CHECK) {
			for(Player member : player.getParty().getMembers()){
				if(member.equals(player))
					continue;
				if(player.gethwid().equals(member.gethwid()) /*|| player.getIpAddress().equals(member.getIpAddress())*/ ){
					  player.sendMessage("You are allowed to have 1 player from your PC.");
					  return false;					
				}
			}
		}
		return true;
	}

	private static void proccessTP(Player player,boolean party) // Teleporting party members to zone
	{
		if(party) {
			for (Player member : player.getParty().getMembers())
			{
				member.teleportTo(CustomConfig.PARTY_TELE_LOCATION1.get(rndLoc)[0], CustomConfig.PARTY_TELE_LOCATION1.get(rndLoc)[1], CustomConfig.PARTY_TELE_LOCATION1.get(rndLoc)[2], 1);// Location X, Y ,Z
			}
		}
		else
			player.teleportTo(CustomConfig.PARTY_TELE_LOCATION1.get(rndLoc)[0] , CustomConfig.PARTY_TELE_LOCATION1.get(rndLoc)[1], CustomConfig.PARTY_TELE_LOCATION1.get(rndLoc)[2], 1);// Location
	}
	
	
	private static boolean checkPvp(Player player) {
		if(CustomConfig.PARTY_FARM_PVP_CHECK) {
			for(Player member : player.getParty().getMembers()){
				if(member.getPvpKills() < CustomConfig.PARTY_FARM_PVPS){
					member.sendMessage("You have less than allowed Pvp's (" + CustomConfig.PARTY_FARM_PVPS  + ")");
					  return false;					
				}
			}
			return true;
		}
		return true;
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

			if (pt.getMembersCount() < MinPtMembers)
			{
				player.sendMessage("You are going to need a bigger party " + "in order to enter party area.");
				return;
			}

			if(!hwidCheck(player))
				return;

			if(!checkPvp(player))
				return;
			
			int count = 0;
			Player memberPos = player;
			for(Player member : pt.getMembers()) {
				if(member.isInsideZone(ZoneId.PARTY)) {
					count++;
					memberPos = member;
				}
			}
			if(count > 0) { //his pt is on Zone , then teleport only him
				if (!PartyItemsOk(player,false)) return;
				if(memberPos.isDead()){
					player.sendMessage("You cant teleport to a Dead player.");
					return;
				}
				player.teleportTo(memberPos.getX(), memberPos.getY(), memberPos.getZ(), 1);
				destroyItems(player,null,false);
			}
			else {
				if (!PartyItemsOk(player,true)) return;
				proccessTP(player,true);
				destroyItems(player,pt,true);
			}
		}
		catch (Exception e)
		{
			player.sendMessage("Something went wrong try again.");
		}
	}
	
	private static void destroyItems(Player player,Party pt,boolean party) {
		if(party) {
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
		else {
			player.sendMessage(ItemConsumeNum + " " + ItemName + " have been dissapeared.");// Item delete from inventory message
			player.getInventory().destroyItemByItemId("Party_Teleporter", ItemConsumeId, ItemConsumeNum, player, player);// remove item from inventory
		}
	}

	public static String nextEvent(){
		String next = "";
		if(PartyFarm.getInstance().getTime() != null){
			if(PartyFarm.getInstance().isStarted()){
				next = "<center><table width=300>"
				  	+ "<tr><td><center>"
				  	+ "<font color=\"ae9988\">Party zone will be closed in: " + PartyFarm.getInstance().getNpcTimeEnd() +"</font>"
				  	+ "</center></td></tr></table></center>";
			}
			else{
				next = "<center><table width=300>"
				  	+ "<tr><td><center>"
				  	+ "<font color=\"ae9988\">Party zone will be opened in: " + PartyFarm.getInstance().getNpcTime() +"</font>"
				  	+ "</center></td></tr></table></center>";
			}
		}
	return  next;
	}
	

	private static boolean isAllowedEnter(Player player) // Checks if player & his party is allowed to teleport.
	{
		if (player.getParty() != null)
		{
			if ((player.getParty().getMembersCount() >= MinPtMembers) && hwidCheck(player) && PartyItemsOk(player,true) && checkPvp(player) ) // Party Length & Item Checker
			{
				return true;
			}
			return false;
		}
		return false;
	}
}