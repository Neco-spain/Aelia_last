package net.sf.l2j.gameserver.model.actor.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.manager.BufferManager;
import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.ValidateLocation;

/**
*
* @author Caparso
*/

public final class Buffer extends Folk
{
	private static final int PAGE_LIMIT = 6;
	
    int fighterbuffsbers [] = {1397,1087,1044,1243,1304,1259,1204,1068,1388,1062,1040,1036,1035,1048,1045,1077,1242,1086,1043,1240,1268,1032,1033,1191,1189,1182,1354,1353,1352,1392,1393,4699,1416,1363,277,307,309,311,310,272,271,275,274,264,269,265,270,267,268,266,364,305,349,308,306,304};
    int magebuffsbers [] = {1397,1087,1044,1243,1304,1259,1204,1040,1389,1036,1035,1062,1048,1045,1085,1078,1303,1059,1032,1033,1191,1189,1182,1353,1354,1352,1392,1393,4703,1416,1363,273,276,365,307,309,311,264,265,270,267,268,266,363,349,308,306,304};
   
    int fighterbuffs [] = {1397,1087,1044,1243,1304,1259,1204,1068,1388,1040,1036,1035,1048,1045,1077,1242,1086,1043,1240,1268,1032,1033,1191,1189,1182,1354,1353,1352,1392,1393,4699,1416,1363,277,307,309,311,310,272,271,275,274,264,269,265,270,267,268,266,364,305,349,308,306,304};
    int magebuffs [] = {1397,1087,1044,1243,1304,1259,1204,1040,1389,1036,1035,1048,1045,1085,1078,1303,1059,1032,1033,1191,1189,1182,1353,1354,1352,1392,1393,4703,1416,1363,273,276,365,307,309,311,264,265,270,267,268,266,363,349,308,306,304};
   
    int voterewards [] = {3470};
    
    public Buffer(int objectId, NpcTemplate template)
    {
        super(objectId, template);
    }
    
     @Override
    public void onBypassFeedback(Player player, String command)
    {
      StringTokenizer st = new StringTokenizer(command, " ");
      String actualCommand = st.nextToken();

      int buffid = 0;
      int bufflevel = 1;
        String nextWindow = null;
      if (st.countTokens() == 3) {
         buffid = Integer.valueOf(st.nextToken());
         bufflevel = Integer.valueOf(st.nextToken());
            nextWindow = st.nextToken();
      }
      else if (st.countTokens() == 1)
      buffid = Integer.valueOf(st.nextToken());


      if (actualCommand.equalsIgnoreCase("getbuff"))
      {
         if (buffid != 0)
         {
            MagicSkillUse mgc = new MagicSkillUse(this, player, buffid, bufflevel, -1, 0);
            
            SkillTable.getInstance().getInfo(buffid, bufflevel).getEffects(this, player);
            showMessageWindow(player);
            player.broadcastPacket(mgc);
                showChatWindow(player, nextWindow);
         }
      }
		else if (command.startsWith("Link"))
		{
			String path = command.substring(5).trim();
			if (path.indexOf("..") != -1)
				return;
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/" + path);
			html.replace("%objectId%", getObjectId());
			player.sendPacket(html);
		}
      
      else  if (actualCommand.equalsIgnoreCase("schemebuffer")){
    	  
  		final NpcHtmlMessage html = new NpcHtmlMessage(0);
  		
  		//getHtmlPathScheme(50008, 0);
  		html.setFile(getHtmlPathScheme(50008, 0));
  		player.sendPacket(html);
      }
      
      //with bers
      else if (actualCommand.equalsIgnoreCase("fightersetbers"))
      {
          for (int id: fighterbuffsbers)
          {
              SkillTable.getInstance().getInfo(id, SkillTable.getInstance().getMaxLevel(id)).getEffects(this, player);
          }
          showChatWindow(player);
      }
      else if (actualCommand.equalsIgnoreCase("magesetbers"))
      {
          for (int id: magebuffsbers)
          {
              SkillTable.getInstance().getInfo(id, SkillTable.getInstance().getMaxLevel(id)).getEffects(this, player);
          }
          showChatWindow(player);
      }
      //no bers
      else if (actualCommand.equalsIgnoreCase("fighterset"))
      {
          for (int id: fighterbuffs)
          {
              SkillTable.getInstance().getInfo(id, SkillTable.getInstance().getMaxLevel(id)).getEffects(this, player);
          }
          showChatWindow(player);
      }
      else if (actualCommand.equalsIgnoreCase("mageset"))
      {
          for (int id: magebuffs)
          {
              SkillTable.getInstance().getInfo(id, SkillTable.getInstance().getMaxLevel(id)).getEffects(this, player);
          }
          showChatWindow(player);
      }
      else if (actualCommand.equalsIgnoreCase("rewards"))
      {
        if (player.destroyItemByItemId("voterewards",9670,1, player.getCurrentFolk(), true))
        {
            for (int id: voterewards)
            {
                SkillTable.getInstance().getInfo(id, SkillTable.getInstance().getMaxLevel(id)).getEffects(this, player);
            }
        }    
        else
        {
            player.sendMessage("You don't have heroic's certifications to exchange.");
        }
        showChatWindow(player);
      }
      else if (actualCommand.equalsIgnoreCase("restore"))
      {
          player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
          player.setCurrentCp(player.getMaxCp());
          showMessageWindow(player);
      }
      else if (actualCommand.equalsIgnoreCase("cancel")) {
         player.stopAllEffects();
         showMessageWindow(player);
      }

	  else if (actualCommand.equalsIgnoreCase("noble")) {

		  MagicSkillUse mgc = new MagicSkillUse(this, player, 1323, 1, -1, 0);
		  SkillTable.getInstance().getInfo(1323,1);

		  player.broadcastPacket(mgc);
		  showMessageWindow(player);
	  }
      //custom scheme buffer on same
      
      else if (actualCommand.startsWith("menu"))
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(getHtmlPath(getNpcId(), 0));
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}
	else if (actualCommand.startsWith("cleanup"))
	{
		player.stopAllEffectsExceptThoseThatLastThroughDeath();
		
		final Summon summon = player.getSummon();
		if (summon != null)
			summon.stopAllEffectsExceptThoseThatLastThroughDeath();
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(getHtmlPath(getNpcId(), 0));
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}
	else if (actualCommand.startsWith("heal"))
	{
		player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
		player.setCurrentCp(player.getMaxCp());
		
		final Summon summon = player.getSummon();
		if (summon != null)
			summon.setCurrentHpMp(summon.getMaxHp(), summon.getMaxMp());
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(getHtmlPath(getNpcId(), 0));
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}
	else if (actualCommand.startsWith("support"))
	{
		showGiveBuffsWindow(player);
	}
	else if (actualCommand.startsWith("givebuffs"))
	{
		final String schemeName = st.nextToken();
		final int cost = Integer.parseInt(st.nextToken());
		String targetType = null;
		
		Creature target = null;
		if (st.hasMoreTokens())
		{
			targetType = st.nextToken();
			if (targetType != null && targetType.equalsIgnoreCase("pet"))
				target = player.getSummon();
		}
		else
			target = player;
		
		if (target == null)
			player.sendMessage("You don't have a pet.");
		else if (cost == 0 || player.reduceAdena("NPC Buffer", cost, this, true))
		{
			for (int skillId : BufferManager.getInstance().getScheme(player.getObjectId(), schemeName))
				SkillTable.getInstance().getInfo(skillId, SkillTable.getInstance().getMaxLevel(skillId)).getEffects(this, target);
		}
		showGiveBuffsWindow(player, targetType);
	}
	else if (actualCommand.startsWith("skill"))
	{
		final String groupType = st.nextToken();
		final String schemeName = st.nextToken();
		
		final int skillId = Integer.parseInt(st.nextToken());
		final int page = Integer.parseInt(st.nextToken());
		
		final List<Integer> skills = BufferManager.getInstance().getScheme(player.getObjectId(), schemeName);
		
		if (actualCommand.startsWith("skillselect") && !schemeName.equalsIgnoreCase("none"))
		{
			if (skills.size() < player.getMaxBuffCount())
				skills.add(skillId);
			else
				player.sendMessage("This scheme has reached the maximum amount of buffs.");
		}
		else if (actualCommand.startsWith("skillunselect"))
			skills.remove(Integer.valueOf(skillId));
		
		showEditSchemeWindow(player, groupType, schemeName, page);
	}
      
      
	else if (actualCommand.startsWith("createscheme"))
	{
		try
		{
			final String schemeName = st.nextToken();
			if (schemeName.length() > 14)
			{
				player.sendMessage("Scheme's name must contain up to 14 chars. Spaces are trimmed.");
				return;
			}
			
			final Map<String, ArrayList<Integer>> schemes = BufferManager.getInstance().getPlayerSchemes(player.getObjectId());
			if (schemes != null)
			{
				if (schemes.size() == Config.BUFFER_MAX_SCHEMES)
				{
					player.sendMessage("Maximum schemes amount is already reached.");
					return;
				}
				
				if (schemes.containsKey(schemeName))
				{
					player.sendMessage("The scheme name already exists.");
					return;
				}
			}
			
			BufferManager.getInstance().setScheme(player.getObjectId(), schemeName.trim(), new ArrayList<Integer>());
			showGiveBuffsWindow(player);
		}
		catch (Exception e)
		{
			player.sendMessage("Scheme's name must contain up to 14 chars. Spaces are trimmed.");
		}
	}
	else if (actualCommand.startsWith("editschemes"))
	{
		showEditSchemeWindow(player, st.nextToken(), st.nextToken(), Integer.parseInt(st.nextToken()));
	}
	else if (actualCommand.startsWith("deletescheme"))
	{
		try
		{
			final String schemeName = st.nextToken();
			final Map<String, ArrayList<Integer>> schemes = BufferManager.getInstance().getPlayerSchemes(player.getObjectId());
			
			if (schemes != null && schemes.containsKey(schemeName))
				schemes.remove(schemeName);
		}
		catch (Exception e)
		{
			player.sendMessage("This scheme name is invalid.");
		}
		showGiveBuffsWindow(player);
	}
    else if (actualCommand.startsWith("manageschemes"))
      {
          showManageSchemeWindow(player);
      }
      
    else if (actualCommand.equalsIgnoreCase("openlist"))
      {
          String category = st.nextToken();
           String htmfile = st.nextToken();
          
           NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
         
          if (category.equalsIgnoreCase("null"))
           {
               html.setFile("data/html/mods/buffer/" + htmfile + ".htm");
              
               // First Page
               if (htmfile.equals("index"))
               {
                   html.replace("%name%", player.getName());
                   html.replace("%buffcount%", "You have " + player.getBuffCount() + "/" + player.getMaxBuffCount() + " buffs.");
               }
           }
           else
               html.setFile("data/html/mods/buffer/" + category + "/" + htmfile + ".htm");
          
           html.replace("%objectId%", String.valueOf(getObjectId()));
           player.sendPacket(html);
       }
     
      
      
      
      
      else
         super.onBypassFeedback(player, command);
    }

 /*   @Override
    public void onAction(Player player)
    {
  /*  if (player.isInCombat() || player.getPvpFlag() != 0){
    	player.sendMessage("Cant get buffs while you are in PvP/Combat mode.");
    	//return;  //stack problem on target
    	player.sendPacket(new ActionFailed());
    }
    */
 /*     if (this != player.getTarget()) {
         player.setTarget(this);
         player.sendPacket(new MyTargetSelected(getObjectId(), player.getLevel() - getLevel()));
         player.sendPacket(new ValidateLocation(this));
      }
      else if (isIn3DRadius(player, Npc.INTERACTION_DISTANCE)) {
          SocialAction sa = new SocialAction(this, Rnd.get(8));
         broadcastPacket(sa);
         player.setCurrentFolk(this);
         showMessageWindow(player);
         player.sendPacket(ActionFailed.STATIC_PACKET);
      }
      else {
    	 player.getAI().tryTo(IntentionType.IDLE, this, false);	//player.getAI().setIntention(IntentionType.INTERACT, this);
         player.sendPacket(ActionFailed.STATIC_PACKET);
      }
    }
   */
    private void showMessageWindow(Player player)
    {
     String filename;
   
     if (player.isInCombat() || player.getPvpFlag() != 0)
    	  filename = "data/html/buffer/" +getNpcId() + "-onCombat" + ".htm";
     else{
    	  filename = "data/html/buffer/" + getNpcId() + ".htm";
    	 // filename = getHtmlPath(getNpcId(), 0);
     }
     
     NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
     html.setFile(filename);
     html.replace("%objectId%", String.valueOf(getObjectId()));
     html.replace("%npcname%", getName());
     player.sendPacket(html);   
      
  /*  filename = getHtmlPath(getNpcId(), 0);
      NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
      html.setFile(filename);
      html.replace("%objectId%", String.valueOf(getObjectId()));
      html.replace("%npcname%", getName());
      player.sendPacket(html);  
      */    
    }
   @Override
   public String getHtmlPath(int npcId, int val)
   {
      String pom = "";
      if (val == 0)
         pom = "" + npcId;
      else
         pom = npcId + "-" + val;
      
      return "data/html/buffer/" + pom + ".htm";
   }   
   
	public static String getHtmlPathScheme(int npcId, int val)
	{
		String filename = "";
		if (val == 0)
			filename = "" + npcId;
		else
			filename = npcId + "-" + val;
		
		return "data/html/mods/buffer/" + filename + ".htm";
	}
   
   
   
   //custom shemce
   
	/**
	 * Sends an html packet to player with Give Buffs menu info for player and pet, depending on targetType parameter {player, pet}
	 * @param player : The player to make checks on.
	 */
	private void showGiveBuffsWindow(Player player)
	{
		final StringBuilder sb = new StringBuilder(200);
		
		final Map<String, ArrayList<Integer>> schemes = BufferManager.getInstance().getPlayerSchemes(player.getObjectId());
		if (schemes == null || schemes.isEmpty())
			sb.append("<font color=\"LEVEL\">You haven't defined any scheme.</font>");
		else
		{
			for (Map.Entry<String, ArrayList<Integer>> scheme : schemes.entrySet())
			{
				final int cost = getFee(scheme.getValue());
				StringUtil.append(sb, "<font color=\"LEVEL\">", scheme.getKey(), " [", scheme.getValue().size(), " / ", player.getMaxBuffCount(), "]", ((cost > 0) ? " - cost: " + StringUtil.formatNumber(cost) : ""), "</font><br1>");
				StringUtil.append(sb, "<a action=\"bypass npc_%objectId%_givebuffs ", scheme.getKey(), " ", cost, "\">Use on Me</a>&nbsp;|&nbsp;");
				StringUtil.append(sb, "<a action=\"bypass npc_%objectId%_givebuffs ", scheme.getKey(), " ", cost, " pet\">Use on Pet</a>&nbsp;|&nbsp;");
				StringUtil.append(sb, "<a action=\"bypass npc_%objectId%_editschemes Buffs ", scheme.getKey(), " 1\">Edit</a>&nbsp;|&nbsp;");
				StringUtil.append(sb, "<a action=\"bypass npc_%objectId%_deletescheme ", scheme.getKey(), "\">Delete</a><br>");
			}
		}
		
	/*     if (player.isInCombat() || player.getPvpFlag() != 0)
	    	  filename = "data/html/buffer/" +getNpcId() + "-onCombat" + ".htm";
	     else{
	    	  filename = "data/html/buffer/" + getNpcId() + ".htm";
	    	 // filename = getHtmlPath(getNpcId(), 0);
	     }
	     
	     NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
	     html.setFile(filename);
	     html.replace("%objectId%", String.valueOf(getObjectId()));
	     html.replace("%npcname%", getName());
	     player.sendPacket(html);  
	     */
		 NpcHtmlMessage html = new NpcHtmlMessage(0);
		 html.setFile("data/html/mods/buffer/schememanager/index-1.htm");
	   
		//final NpcHtmlMessage html = new NpcHtmlMessage(0);
		//html.setFile(getHtmlPath(getNpcId(), 1));
		html.replace("%schemes%", sb.toString());
		html.replace("%max_schemes%", Config.BUFFER_MAX_SCHEMES);
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}
	
	/**
	 * This sends an html packet to player with Edit Scheme Menu info. This allows player to edit each created scheme (add/delete skills)
	 * @param player : The player to make checks on.
	 * @param groupType : The group of skills to select.
	 * @param schemeName : The scheme to make check.
	 * @param page The page.
	 */
	private void showEditSchemeWindow(Player player, String groupType, String schemeName, int page)
	{
		/*final NpcHtmlMessage html = new NpcHtmlMessage(0);
		final List<Integer> schemeSkills = BufferManager.getInstance().getScheme(player.getObjectId(), schemeName);
		
		html.setFile(getHtmlPath(getNpcId(), 2));
		html.replace("%schemename%", schemeName);
		html.replace("%count%", schemeSkills.size() + " / " + player.getMaxBuffCount());
		html.replace("%typesframe%", getTypesFrame(groupType, schemeName));
		html.replace("%skilllistframe%", getGroupSkillList(player, groupType, schemeName, page));
		//html.replace("%objectId%", getObjectId());
		player.sendPacket(html);*/
		
	    final NpcHtmlMessage html = new NpcHtmlMessage(0);
	         
	           if (schemeName.equalsIgnoreCase("none"))
	               html.setFile("data/html/mods/buffer/schememanager/index-3.htm");
	           else
	           {
	               if (groupType.equalsIgnoreCase("none"))
	                   html.setFile("data/html/mods/buffer/schememanager/index-4.htm");
	               else
	               {
	                   html.setFile("data/html/mods/buffer/schememanager/index-5.htm");
	                   html.replace("%skilllistframe%", getGroupSkillList(player, groupType, schemeName,page));
	               }
	               html.replace("%schemename%", schemeName);
	               html.replace("%myschemeframe%", getGroupSkillList(player, groupType, schemeName,page));
	               html.replace("%typesframe%", getTypesFrame(groupType, schemeName));
	           }
	           html.replace("%schemes%", getPlayerSchemes(player, schemeName));
	           html.replace("%objectId%", getObjectId());
	           player.sendPacket(html);
		
		
		
	}
	
	  /**
	    * Sends an html packet to player with Give Buffs menu info for player and pet, depending on targetType parameter {player, pet}
	    * @param player : The player to make checks on.
	    * @param targetType : a String used to define if the player or his pet must be used as target.
	    */
	   private void showGiveBuffsWindow(Player player, String targetType)
	   {
	       final StringBuilder sb = new StringBuilder(200);
	      
	       final Map<String, ArrayList<Integer>> schemes = BufferManager.getInstance().getPlayerSchemes(player.getObjectId());
	       if (schemes == null || schemes.isEmpty())
	           sb.append("<font color=\"LEVEL\">You haven't defined any scheme, please go to 'Manage my schemes' and create at least one valid scheme.</font>");
	       else
	       {
	           for (Map.Entry<String, ArrayList<Integer>> scheme : schemes.entrySet())
	           {
	               final int cost = getFee(scheme.getValue());
	               StringUtil.append(sb, "<font color=\"LEVEL\"><a action=\"bypass -h npc_%objectId%_givebuffs ", targetType, " ", scheme.getKey(), " ", cost, "\">", scheme.getKey(), " (", scheme.getValue().size(), " skill(s))</a>", ((cost > 0) ? " - Adena cost: " + cost : ""), "</font><br1>");
	           }
	       }
	      
	       final NpcHtmlMessage html = new NpcHtmlMessage(0);
	       html.setFile("data/html/mods/buffer/schememanager/index-1.htm");
	       html.replace("%schemes%", sb.toString());
	       html.replace("%targettype%", (targetType.equalsIgnoreCase("pet") ? "&nbsp;<a action=\"bypass -h npc_%objectId%_support player\">yourself</a>&nbsp;|&nbsp;your pet" : "yourself&nbsp;|&nbsp;<a action=\"bypass -h npc_%objectId%_support pet\">your pet</a>"));
	       html.replace("%objectId%", getObjectId());
	       player.sendPacket(html);
	   }
	  
	
	  /**
	    * @param player : The player to make checks on.
	    * @param schemeName : The name to don't link (previously clicked).
	    * @return a String listing player's schemes. The scheme currently on selection isn't linkable.
	    */
	   private static String getPlayerSchemes(Player player, String schemeName)
	   {
	       final Map<String, ArrayList<Integer>> schemes = BufferManager.getInstance().getPlayerSchemes(player.getObjectId());
	       if (schemes == null || schemes.isEmpty())
	           return "Please create at least one scheme.";
	      
	       final StringBuilder sb = new StringBuilder(200);
	       sb.append("<table>");
	      
	       for (Map.Entry<String, ArrayList<Integer>> scheme : schemes.entrySet())
	       {
	           if (schemeName.equalsIgnoreCase(scheme.getKey()))
	               StringUtil.append(sb, "<tr><td width=200>", scheme.getKey(), " (<font color=\"LEVEL\">", scheme.getValue().size(), "</font> / ", Config.BUFFER_MAX_SCHEMES, " skill(s))</td></tr>");
	           else
	               StringUtil.append(sb, "<tr><td width=200><a action=\"bypass -h npc_%objectId%_editschemes none ", scheme.getKey(), "\">", scheme.getKey(), " (", scheme.getValue().size(), " / ", Config.BUFFER_MAX_SCHEMES, " skill(s))</a></td></tr>");
	       }
	      
	       sb.append("</table>");
	      
	       return sb.toString();
	   }
	
	/**
	 * @param player : The player to make checks on.
	 * @param groupType : The group of skills to select.
	 * @param schemeName : The scheme to make check.
	 * @param page The page.
	 * @return a String representing skills available to selection for a given groupType.
	 */
	private String getGroupSkillList(Player player, String groupType, String schemeName, int page)
	{
		// Retrieve the entire skills list based on group type.
		List<Integer> skills = BufferManager.getInstance().getSkillsIdsByType(groupType);
		if (skills.isEmpty())
			return "That group doesn't contain any skills.";
		
		// Calculate page number.
		final int max = MathUtil.countPagesNumber(skills.size(), PAGE_LIMIT);
		if (page > max)
			page = max;
		
		// Cut skills list up to page number.
		skills = skills.subList((page - 1) * PAGE_LIMIT, Math.min(page * PAGE_LIMIT, skills.size()));
		
		final List<Integer> schemeSkills = BufferManager.getInstance().getScheme(player.getObjectId(), schemeName);
		final StringBuilder sb = new StringBuilder(skills.size() * 150);
		
		int row = 0;
		for (int skillId : skills)
		{
			sb.append(((row % 2) == 0 ? "<table width=\"280\" bgcolor=\"000000\"><tr>" : "<table width=\"280\"><tr>"));
			
			if (skillId < 100)
			{
				if (schemeSkills.contains(skillId))
					StringUtil.append(sb, "<td height=40 width=40><img src=\"icon.skill00", skillId, "\" width=32 height=32></td><td width=190>", SkillTable.getInstance().getInfo(skillId, 1).getName(), "<br1><font color=\"B09878\">", BufferManager.getInstance().getAvailableBuff(skillId).getDescription(), "</font></td><td><button action=\"bypass npc_%objectId%_skillunselect ", groupType, " ", schemeName, " ", skillId, " ", page, "\" width=32 height=32 back=\"L2UI_CH3.mapbutton_zoomout2\" fore=\"L2UI_CH3.mapbutton_zoomout1\"></td>");
				else
					StringUtil.append(sb, "<td height=40 width=40><img src=\"icon.skill00", skillId, "\" width=32 height=32></td><td width=190>", SkillTable.getInstance().getInfo(skillId, 1).getName(), "<br1><font color=\"B09878\">", BufferManager.getInstance().getAvailableBuff(skillId).getDescription(), "</font></td><td><button action=\"bypass npc_%objectId%_skillselect ", groupType, " ", schemeName, " ", skillId, " ", page, "\" width=32 height=32 back=\"L2UI_CH3.mapbutton_zoomin2\" fore=\"L2UI_CH3.mapbutton_zoomin1\"></td>");
			}
			else if (skillId < 1000)
			{
				if (schemeSkills.contains(skillId))
					StringUtil.append(sb, "<td height=40 width=40><img src=\"icon.skill0", skillId, "\" width=32 height=32></td><td width=190>", SkillTable.getInstance().getInfo(skillId, 1).getName(), "<br1><font color=\"B09878\">", BufferManager.getInstance().getAvailableBuff(skillId).getDescription(), "</font></td><td><button action=\"bypass npc_%objectId%_skillunselect ", groupType, " ", schemeName, " ", skillId, " ", page, "\" width=32 height=32 back=\"L2UI_CH3.mapbutton_zoomout2\" fore=\"L2UI_CH3.mapbutton_zoomout1\"></td>");
				else
					StringUtil.append(sb, "<td height=40 width=40><img src=\"icon.skill0", skillId, "\" width=32 height=32></td><td width=190>", SkillTable.getInstance().getInfo(skillId, 1).getName(), "<br1><font color=\"B09878\">", BufferManager.getInstance().getAvailableBuff(skillId).getDescription(), "</font></td><td><button action=\"bypass npc_%objectId%_skillselect ", groupType, " ", schemeName, " ", skillId, " ", page, "\" width=32 height=32 back=\"L2UI_CH3.mapbutton_zoomin2\" fore=\"L2UI_CH3.mapbutton_zoomin1\"></td>");
			}
			else
			{
				if (schemeSkills.contains(skillId))
					StringUtil.append(sb, "<td height=40 width=40><img src=\"icon.skill", skillId, "\" width=32 height=32></td><td width=190>", SkillTable.getInstance().getInfo(skillId, 1).getName(), "<br1><font color=\"B09878\">", BufferManager.getInstance().getAvailableBuff(skillId).getDescription(), "</font></td><td><button action=\"bypass npc_%objectId%_skillunselect ", groupType, " ", schemeName, " ", skillId, " ", page, "\" width=32 height=32 back=\"L2UI_CH3.mapbutton_zoomout2\" fore=\"L2UI_CH3.mapbutton_zoomout1\"></td>");
				else
					StringUtil.append(sb, "<td height=40 width=40><img src=\"icon.skill", skillId, "\" width=32 height=32></td><td width=190>", SkillTable.getInstance().getInfo(skillId, 1).getName(), "<br1><font color=\"B09878\">", BufferManager.getInstance().getAvailableBuff(skillId).getDescription(), "</font></td><td><button action=\"bypass npc_%objectId%_skillselect ", groupType, " ", schemeName, " ", skillId, " ", page, "\" width=32 height=32 back=\"L2UI_CH3.mapbutton_zoomin2\" fore=\"L2UI_CH3.mapbutton_zoomin1\"></td>");
			}
			
			sb.append("</tr></table><img src=\"L2UI.SquareGray\" width=277 height=1>");
			row++;
		}
		
		// Build page footer.
		sb.append("<br><img src=\"L2UI.SquareGray\" width=277 height=1><table width=\"100%\" bgcolor=000000><tr>");
		
		if (page > 1)
			StringUtil.append(sb, "<td align=left width=70><a action=\"bypass npc_" + getObjectId() + "_editschemes ", groupType, " ", schemeName, " ", page - 1, "\">Previous</a></td>");
		else
			StringUtil.append(sb, "<td align=left width=70>Previous</td>");
		
		StringUtil.append(sb, "<td align=center width=100>Page ", page, "</td>");
		
		if (page < max)
			StringUtil.append(sb, "<td align=right width=70><a action=\"bypass npc_" + getObjectId() + "_editschemes ", groupType, " ", schemeName, " ", page + 1, "\">Next</a></td>");
		else
			StringUtil.append(sb, "<td align=right width=70>Next</td>");
		
		sb.append("</tr></table><img src=\"L2UI.SquareGray\" width=277 height=1>");
		
		return sb.toString();
	}
	
	/**
	 * @param groupType : The group of skills to select.
	 * @param schemeName : The scheme to make check.
	 * @return a string representing all groupTypes available. The group currently on selection isn't linkable.
	 */
	private static String getTypesFrame(String groupType, String schemeName)
	{
		final StringBuilder sb = new StringBuilder(500);
		sb.append("<table>");
		
		int count = 0;
		for (String type : BufferManager.getInstance().getSkillTypes())
		{
			if (count == 0)
				sb.append("<tr>");
			
			if (groupType.equalsIgnoreCase(type))
				StringUtil.append(sb, "<td width=65>", type, "</td>");
			else
				StringUtil.append(sb, "<td width=65><a action=\"bypass npc_%objectId%_editschemes ", type, " ", schemeName, " 1\">", type, "</a></td>");
			
			count++;
			if (count == 4)
			{
				sb.append("</tr>");
				count = 0;
			}
		}
		
		if (!sb.toString().endsWith("</tr>"))
			sb.append("</tr>");
		
		sb.append("</table>");
		
		return sb.toString();
	}
	
	
	 /**
	    * Sends an html packet to player with Manage scheme menu info. This allows player to create/delete/clear schemes
	    * @param player : The player to make checks on.
	    */
	   private void showManageSchemeWindow(Player player)
	   {
	       final StringBuilder sb = new StringBuilder(200);
	      
	       final Map<String, ArrayList<Integer>> schemes = BufferManager.getInstance().getPlayerSchemes(player.getObjectId());
	       if (schemes == null || schemes.isEmpty())
	           sb.append("<font color=\"LEVEL\">You haven't created any scheme.</font>");
	       else
	       {
	           sb.append("<table>");
	           for (Map.Entry<String, ArrayList<Integer>> scheme : schemes.entrySet())
	               StringUtil.append(sb, "<tr><td width=140>", scheme.getKey(), " (", scheme.getValue().size(), " skill(s))</td><td width=60><button value=\"Clear\" action=\"bypass -h npc_%objectId%_clearscheme ", scheme.getKey(), "\" width=55 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td width=60><button value=\"Drop\" action=\"bypass -h npc_%objectId%_deletescheme ", scheme.getKey(), "\" width=55 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
	          
	           sb.append("</table>");
	       }
	      
	       final NpcHtmlMessage html = new NpcHtmlMessage(0);
	       html.setFile("data/html/mods/buffer/schememanager/index-2.htm");
	       html.replace("%schemes%", sb.toString());
	       html.replace("%max_schemes%", Config.BUFFER_MAX_SCHEMES);
	       html.replace("%objectId%", getObjectId());
	       player.sendPacket(html);
	   }
	
	
	/**
	 * @param list : A list of skill ids.
	 * @return a global fee for all skills contained in list.
	 */
	private static int getFee(ArrayList<Integer> list)
	{
		if (Config.BUFFER_STATIC_BUFF_COST > 0)
			return list.size() * Config.BUFFER_STATIC_BUFF_COST;
		
		int fee = 0;
		for (int sk : list)
			fee += BufferManager.getInstance().getAvailableBuff(sk).getValue();
		
		return fee;
	}
}