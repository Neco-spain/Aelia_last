package net.sf.l2j.gameserver.model.actor.instance;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.manager.CastleManager;
import net.sf.l2j.gameserver.data.manager.HeroManager;
import net.sf.l2j.gameserver.data.sql.PlayerInfoTable;
import net.sf.l2j.gameserver.data.xml.MultisellData;
import net.sf.l2j.gameserver.data.xml.SkillTreeData;
import net.sf.l2j.gameserver.enums.actors.Sex;
import net.sf.l2j.gameserver.model.L2Augmentation;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.holder.skillnode.EnchantSkillNode;
import net.sf.l2j.gameserver.model.item.Henna;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.ExEnchantSkillList;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SiegeInfo;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

import Custom.CustomConfig;
import Customs.tasks.HeroTaskManager;

	public class DonateShop extends Folk
	{
		public DonateShop(int objectId, NpcTemplate template)
		{
			super(objectId, template);
		}
		
		@Override
		public void onBypassFeedback(Player player, String command)
		{
			if (player == null)
				return;
			
			StringTokenizer st1 = new StringTokenizer(command, " ");
			String actualCommand = st1.nextToken(); // Get actual command
			
			if (command.startsWith("donate"))
			{
				StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				try
				{
					String type = st.nextToken();
					switch (type)
					{
						case "Noblesse":
							Nobless(player);
							break;
						case "Hero7":
							Hero(player,1);
							break;	
						case "Hero30":
							//Hero7(player);
							Hero(player,7);
							break;	
						case "Hero999":
							//Hero14(player);
							Hero(player,999);
							break;	
						case "ChangeSex":
							Sex(player);
							break;
						case "CleanPk":
							CleanPk(player);
							break;
						case "FullRec":
							Rec(player);
							break;
						case "ChangeClass":
							final NpcHtmlMessage html = new NpcHtmlMessage(0);
							html.setFile("data/html/mods/donateshop/50091-2.htm");
							player.sendPacket(html);
							break;
					}
				}
				catch (Exception e)
				{
				}
			}
			else if (actualCommand.equalsIgnoreCase("Multisell"))
			{
				if (st1.countTokens() < 1)
					return;
				
				MultisellData.getInstance().separateAndSend(st1.nextToken(), player, this, false);
			}
			else if (actualCommand.equals("skillEnch"))
			{
				showEnchantSkillList(player);
			}
			else if (command.startsWith("clan"))
			{
				StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				try
				{
					String type = st.nextToken();
					switch (type)
					{
						case "ClanLevel":
							Clanlvl(player);
							break;
						case "ClanRep_20k":
							ClanRep(player);
							break;
						case "ClanSkills":
							ClanSkill(player);
							break;
					}
				}
				catch (Exception e)
				{
				}
			}
			else if (command.startsWith("siege"))
			{
				
				StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				try
				{
					String type = st.nextToken();
					int castleId = 0;
					
					if (type.startsWith("Gludio"))
						castleId = 1;
					else if (type.startsWith("Dion"))
						castleId = 2;
					else if (type.startsWith("Giran"))
						castleId = 3;
					else if (type.startsWith("Oren"))
						castleId = 4;
					else if (type.startsWith("Aden"))
						castleId = 5;
					else if (type.startsWith("Innadril"))
						castleId = 6;
					else if (type.startsWith("Goddard"))
						castleId = 7;
					else if (type.startsWith("Rune"))
						castleId = 8;
					else if (type.startsWith("Schuttgart"))
						castleId = 9;
					
					Castle castle = CastleManager.getInstance().getCastleById(castleId);
					
					
					if (castle != null && castleId != 0)
						player.sendPacket(new SiegeInfo(castle));
				}
				catch (Exception e)
				{
				}
			}
			else if (command.startsWith("color"))
			{
				StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				int colorId; //1Green 0x009900, 2Blue 0xff7f00, 3Purple 0xff00ff,4Yellow 0x00ffff,5Gold 0x0099ff
				try 
				{
					String type = null ;
					
					if(st.hasMoreTokens())
						type = st.nextToken();

					if(type != null)
						setColor(player,type,1);

				}
				catch (Exception e)
				{
				}
			}
			else if (command.startsWith("active"))
			{
				StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				try
				{
					String type = st.nextToken();
					switch (type)
					{
						case "Might":
							augments(player, 1062079106, 3132, 10);
							break;
						case "Empower":
							augments(player, 1061423766, 3133, 10);
							break;
						case "DuelMight":
							augments(player, 1062406807, 3134, 10);
							break;
						case "Shield":
							augments(player, 968884225, 3135, 10);
							break;
						case "MagicBarrier":
							augments(player, 956760065, 3136, 10);
							break;
						case "WildMagic":
							augments(player, 1067850844, 3142, 10);
							break;
						case "Focus":
							augments(player, 1067523168, 3141, 10);
							break;
						case "BattleRoar":
							augments(player, 968228865, 3125, 10);
							break;
						case "BlessedBody":
							augments(player, 991625216, 3124, 10);
							break;
						case "Agility":
							augments(player, 1060444351, 3139, 10);
							break;
						case "Heal":
							augments(player, 1061361888, 3123, 10);
							break;
						case "HydroBlast":
							augments(player, 1063590051, 3167, 10);
							break;
						case "AuraFlare":
							augments(player, 1063455338, 3172, 10);
							break;
						case "Hurricane":
							augments(player, 1064108032, 3168, 10);
							break;
						case "ReflectDamage":
							augments(player, 1067588698, 3204, 3);
							break;
						case "Celestial":
							augments(player, 974454785, 3158, 1);
							break;
						case "Stone":
							augments(player, 1060640984, 3169, 10);
							break;
						case "HealEmpower":
							augments(player, 1061230760, 3138, 10);
							break;
						case "ShadowFlare":
							augments(player, 1063520931, 3171, 10);
							break;
						case "Prominence":
							augments(player, 1063327898, 3165, 10);
							break;
							
						case "BlessedSoul":
							augments(player, 991690752, 3128, 10);
							break;	
						case "Stun":
							augments(player, 969867264, 3189, 10);
							break;	
						case "SpellRefresh":
							augments(player, 1068302336, 3200, 3);
							break;
						case "SkillRefresh":
							augments(player, 1068040192, 3199, 3);
							break;	
						case "SolarFlare":
							augments(player, 1061158912, 3177, 10);
							break;
						case "ManaBurn":
							augments(player, 956825600, 3154, 10);
							break;	
						case "Refresh":
							augments(player, 997392384, 3202, 3);
							break;
						case "Prayer":
							augments(player, 991297536, 3126, 10);
							break;

						//missed
						case "Cheer":
							augments(player, 1061492889, 3131, 10);
							break;
						case "Guidance":
							augments(player, 1061034153, 3140, 10);
							break;

					}
				}
				catch (Exception e)
				{
					player.sendMessage("Usage : Bar>");
				}
			}
			else if (command.startsWith("passive"))
			{
				StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				try
				{
					String type = st.nextToken();
					switch (type)
					{
						case "DuelMight":
							augments(player, 1067260101, 3243, 10);
							break;
						case "Might":
							augments(player, 1067125363, 3240, 10);
							break;
						case "Shield":
							augments(player, 1067194549, 3244, 10);
							break;
						case "MagicBarrier":
							augments(player, 962068481, 3245, 10);
							break;
						case "Empower":
							augments(player, 1066994296, 3241, 10);
							break;
						case "Agility":
							augments(player, 965279745, 3247, 10);
							break;
						case "Guidance":
							augments(player, 1070537767, 3248, 10);
							break;
						case "Focus":
							augments(player, 1070406728, 3249, 10);
							break;
						case "WildMagic":
							augments(player, 1070599653, 3250, 10);
							break;
						case "ReflectDamage":
							augments(player, 1070472227, 3259, 3);
							break;
						case "HealEmpower":
							augments(player, 1066866909, 3246, 10);
							break;
						case "Prayer":
							augments(player, 1066932422, 3238, 10);
							break;
						
					}
				}
				catch (Exception e)
				{
					player.sendMessage("Usage : Bar>");
				}
			}
			else if (command.startsWith("name"))
			{
				StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				
				String newName = "";
				try
				{
					if (st.hasMoreTokens())
					{
						newName = st.nextToken();
					}
				}
				catch (Exception e)
				{
				}
				if (!conditionsname(newName, player))
					return;
				if(newName.equals("") || newName.equals(" "))
					return;
				
				player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.NAME_ITEM_COUNT, player, true);
				player.setName(newName);
				player.store();
				player.sendMessage("Your new character name is " + newName);
				player.broadcastUserInfo();
				
			}
			else if (command.startsWith("password"))
			{
				StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				
				String newPass = "";
				String repeatNewPass = "";
				
				try
				{
					if (st.hasMoreTokens())
					{
						newPass = st.nextToken();
						repeatNewPass = st.nextToken();
					}
				}
				catch (Exception e)
				{
					player.sendMessage("Please fill all the blanks before requesting for a password change.");
					return;
				}
				
				if (!conditions(newPass, repeatNewPass, player))
					return;
				changePassword(newPass, repeatNewPass, player);
			}
			else if (command.startsWith("enchant"))
			{
				StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				try
				{
					String type = st.nextToken();
					switch (type)
					{
						case "Weapon":
							Enchant(player, CustomConfig.ENCHANT_MAX_VALUE, Inventory.PAPERDOLL_RHAND);
							break;
						case "Shield":
							Enchant(player, CustomConfig.ENCHANT_MAX_VALUE, Inventory.PAPERDOLL_LHAND);
							break;
						case "R-Earring":
							Enchant(player, CustomConfig.ENCHANT_MAX_VALUE, Inventory.PAPERDOLL_REAR);
							break;
						case "L-Earring":
							Enchant(player, CustomConfig.ENCHANT_MAX_VALUE, Inventory.PAPERDOLL_LEAR);
							break;
						case "R-Ring":
							Enchant(player, CustomConfig.ENCHANT_MAX_VALUE, Inventory.PAPERDOLL_RFINGER);
							break;
						case "L-Ring":
							Enchant(player, CustomConfig.ENCHANT_MAX_VALUE, Inventory.PAPERDOLL_LFINGER);
							break;
						case "Necklace":
							Enchant(player, CustomConfig.ENCHANT_MAX_VALUE, Inventory.PAPERDOLL_NECK);
							break;
						case "Helmet":
							Enchant(player, CustomConfig.ENCHANT_MAX_VALUE, Inventory.PAPERDOLL_HEAD);
							break;
						case "Boots":
							Enchant(player, CustomConfig.ENCHANT_MAX_VALUE, Inventory.PAPERDOLL_FEET);
							break;
						case "Gloves":
							Enchant(player, CustomConfig.ENCHANT_MAX_VALUE, Inventory.PAPERDOLL_GLOVES);
							break;
						case "Chest":
							Enchant(player, CustomConfig.ENCHANT_MAX_VALUE, Inventory.PAPERDOLL_CHEST);
							break;
						case "Legs":
							Enchant(player, CustomConfig.ENCHANT_MAX_VALUE, Inventory.PAPERDOLL_LEGS);
							break;
						case "Tattoo":
							Enchant(player, CustomConfig.ENCHANT_MAX_VALUE, Inventory.PAPERDOLL_UNDER);
							break;
					}
				}
				
				catch (Exception e)
				{
				}
			}
			else if (command.startsWith("Chat"))
				showChatWindow(player, command);
		}
		
		@Override
		public void showChatWindow(Player player, String command)
		{
			StringTokenizer st = new StringTokenizer(command," ");
			int page =0,val=0;

			try{
				val = Integer.parseInt(command.substring(5));
			}
			catch (IndexOutOfBoundsException ioobe)
			{
			}
			catch (NumberFormatException nfe)
			{
			}
			
			String filename = "data/html/mods/donateshop/" + getNpcId() + "-" +val   + ".htm";
			ItemInstance item = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile(filename);
			html.replace("%objectId%", getObjectId());
			html.replace("%npcname%", getName());
	
			//added for active's passive's to see on npc current augment level.
			if (item != null && item.isAugmented() && item.getAugmentation() != null && item.getAugmentation().getSkill() != null && item.getAugmentation().getSkill().getLevel() >= 1)
				html.replace("%level%", item.getAugmentation().getSkill().getName() + " " + item.getAugmentation().getSkill().getLevel());
			html.replace("%level%", "None");
			
			player.sendPacket(html);
		}
		
		public static void Nobless(Player player)
		{
			if (player.isNoble())
				player.sendMessage("You Are Already A Noblesse!");
			else if (player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.NOBL_ITEM_COUNT, player, true))
			{
				player.broadcastPacket(new SocialAction(player, 16));
				player.setNoble(true, true);
				player.sendMessage("You Are Now a Noble! Check your skills.");
				player.broadcastUserInfo();
			}
			else
				player.sendMessage("You do not have enough Donate Coins.");
		}
		
		public static void Hero(Player player , int days)
		{
			if (player.isHero() || player.isCustomHero())
			{
				player.sendMessage("Your character has already Hero Status!");
				return;
			}
			if (player.isInOlympiadMode())
			{
				player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			
			switch(days){
				case 1:
					if(player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.HERO_1_COUNT, player, true))
						AddHeroStatus(player, player, days);
					else
						player.sendMessage("You do not have enough Donate Coins.");
					break;
				case 7:
					if(player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.HERO_7_COUNT, player, true))
						AddHeroStatus(player, player, days);
					else
						player.sendMessage("You do not have enough Donate Coins.");
					break;
				case 999:
					if(player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.HERO_14_COUNT, player, true))
						AddHeroStatus(player, player, days);
					else
						player.sendMessage("You do not have enough Donate Coins.");
					break;
			}
				
			

		}

		public static void AddHeroStatus(Player target, Player player, int time)
		{
			target.broadcastPacket(new SocialAction(target, 16));
			target.setCustomHero(true);
			HeroTaskManager.getInstance().add(target);
			long remainingTime = target.getMemos().getLong("TimeOfHero", 0);
			if (remainingTime > 0)
			{
				target.getMemos().set("TimeOfHero", remainingTime + TimeUnit.DAYS.toMillis(time));
				if(time == 999)
					target.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Hero Manager", "Dear " + player.getName() + ", your Hero status has been extended 4ever."));
				else
					target.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Hero Manager", "Dear " + player.getName() + ", your Hero status has been extended by " + time + " day(s)."));
				target.broadcastUserInfo();
			}
			else
			{
				target.getMemos().set("TimeOfHero", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(time));
				if(time == 999)
					target.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Hero Manager", "Dear " + player.getName() + ", you got Hero Status 4ever."));
				else
					target.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Hero Manager", "Dear " + player.getName() + ", you got Hero Status for " + time + " day(s)."));
				target.broadcastUserInfo();
			}
		}
		
		public static void setColor(Player player,String color,int colorId)
		{
			if (player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.COLOR_ITEM_COUNT, player, true))
			{
				player.setDonateColor(color);
				player.getAppearance().setNameColor(Integer.decode("0x" + color.substring(4,6) + color.substring(2,4) + color.substring(0,2)));
				player.broadcastUserInfo();
				player.sendMessage("Your color name has changed!");
			}
			else
				player.sendMessage("You do not have enough Donate Coins.");
		}
	
		public static void Sex(Player player)
		{
			if (player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.SEX_ITEM_COUNT, player, true))
			{
				player.getAppearance().setSex(player.getAppearance().getSex() == Sex.MALE ? Sex.FEMALE : Sex.MALE);
				player.sendMessage("Your gender has been changed, you will be disconected in 3 Seconds!");
				player.broadcastUserInfo();
				player.decayMe();
				player.spawnMe();
				ThreadPool.schedule(() -> player.logout(false), 3000);
			}
			else
				player.sendMessage("You do not have enough Donate Coins.");
		}
		
		public static void Rec(Player player)
		{
			if (player.getRecomHave() == 255)
				player.sendMessage("You already have full recommends.");
			else if (player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.REC_ITEM_COUNT, player, true))
			{
				player.setRecomHave(255);
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement ps = con.prepareStatement("UPDATE characters SET rec_have=? WHERE obj_Id=?");
					ps.setInt(1, 255);
					ps.setInt(2, player.getObjectId());
					ps.execute();
					ps.close();

				}
				catch (Exception e)
				{
					LOGGER.error("Couldn't update player recommendations.", e);
				}
				player.sendMessage("Added 255 recommends.");
				player.broadcastUserInfo();
			}
			else
				player.sendMessage("You do not have enough Donate Coins.");
		}

		public static void CleanPk(Player player)
		{
			if (player.getPkKills() < CustomConfig.PK_CLEAN)
				player.sendMessage("You do not have enough Pk kills for clean.");
			else if (player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.PK_ITEM_COUNT, player, true))
			{
				player.setPkKills(player.getPkKills() - CustomConfig.PK_CLEAN);
				player.sendMessage("You have successfully clean " + CustomConfig.PK_CLEAN + " pks!");
				player.broadcastUserInfo();
			}
			else
				player.sendMessage("You do not have enough Donate Coins.");
			
		}
		
		public static void ClanRep(Player player)
		{
			if (player.getClan() == null)
				player.sendPacket(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER);
			else if (!player.isClanLeader())
				player.sendPacket(SystemMessageId.NOT_AUTHORIZED_TO_BESTOW_RIGHTS);
			else if (player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.CLAN_REP_ITEM_COUNT, player, true))
			{
				player.getClan().addReputationScore(CustomConfig.CLAN_REPS);
				player.getClan().broadcastClanStatus();
				player.sendMessage("Your clan reputation score has been increased.");
			}
			else
				player.sendMessage("You do not have enough Donate Coins.");
		}
		
		public static void Clanlvl(Player player)
		{
			if (player.getClan() == null)
				player.sendPacket(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER);
			else if (!player.isClanLeader())
				player.sendPacket(SystemMessageId.NOT_AUTHORIZED_TO_BESTOW_RIGHTS);
			if (player.getClan().getLevel() == 8)
				player.sendMessage("Your clan is already level 8.");
			else if (player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.CLAN_ITEM_COUNT, player, true))
			{
				player.getClan().changeLevel(player.getClan().getLevel() + 1);
				player.getClan().broadcastClanStatus();
				player.broadcastPacket(new MagicSkillUse(player, player, 5103, 1, 1000, 0));
			}
			else
				player.sendMessage("You do not have enough Donate Coins.");
		}
		
		public static void augments(Player player, int attributes, int idaugment, int levelaugment)
		{
			ItemInstance rhand = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
			
			if (rhand != null)
			{
				if (rhand.getItem().getCrystalType().getId() == 0 || rhand.getItem().getCrystalType().getId() == 1 || rhand.getItem().getCrystalType().getId() == 2)
					player.sendMessage("You can't augment under " + rhand.getItem().getCrystalType() + " Grade Weapon!");
				else if (rhand.isHeroItem())
					player.sendMessage("You Cannot be add Augment On " + rhand.getItemName() + " !");
				else if(rhand.isAugmented())
					player.sendMessage("This weapon has already augment.");
				else if (!rhand.isAugmented() && player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.AUGM_ITEM_COUNT, player, true))
				{
					player.sendMessage("Successfully To Add " + SkillTable.getInstance().getInfo(idaugment, levelaugment).getName() + ".");
					augmentweapondatabase(player, attributes, idaugment, levelaugment);
				}
				else
					player.sendMessage("You do not have enough Donate Coins."); //This weapon has already augment.
			}
			else
				player.sendMessage("You do not have a Weapon to Augment.");
		}
		
		public static void augmentweapondatabase(Player player, int attributes, int id, int level)
		{
			ItemInstance item = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
			L2Augmentation augmentation = new L2Augmentation(attributes, id, level);
			augmentation.applyBonus(player);
			item.setAugmentation(augmentation);
			player.disarmWeapons();
			
			try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement statement = con.prepareStatement("REPLACE INTO augmentations VALUES(?,?,?,?)"))
			{
				statement.setInt(1, item.getObjectId());
				statement.setInt(2, attributes);
				statement.setInt(3, id);
				statement.setInt(4, level);
				InventoryUpdate iu = new InventoryUpdate();
				player.sendPacket(iu);
				statement.execute();
				statement.close();
			}
			catch (SQLException e)
			{
				System.out.println(e);
			}
		}
		
		/**
		 * @param player
		 */
		public static void removeAugmentation(Player player)
		{
			ItemInstance item = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
			
			if (item == null)
			{
				player.sendMessage("Equipped first a weapon!");
				return;
			}
			
			if (!item.isAugmented())
			{
				player.sendMessage("The weapon is not augmented.");
				return;
			}
			
			item.getAugmentation().removeBonus(player);
			item.removeAugmentation();
			{
				player.disarmWeapons();
				player.sendMessage("Your augmented has been removed!");
			}
		}
		
		public static void ClanSkill(Player player)
		{
			if (!player.isClanLeader())
				player.sendMessage("Only a clan leader can add clan skills.!");
			else if (player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.CLAN_SKILL_ITEM_COUNT, player, true))
			{
				for(int i=370;i<=390;i++)
					player.getClan().addNewSkill(SkillTable.getInstance().getInfo(i, 3));
				player.getClan().addNewSkill(SkillTable.getInstance().getInfo(391, 1));
			}
			else
				player.sendMessage("You do not have enough Donate Coins.");
		}
		
		public static void Enchant(Player player, int enchant, int type)
		{
			ItemInstance item = player.getInventory().getPaperdollItem(type);
			
			if (item != null)
			{
				if (item.getEnchantLevel() == CustomConfig.ENCHANT_MAX_VALUE || item.getEnchantLevel() == CustomConfig.ENCHANT_MAX_VALUE)
					player.sendMessage("Your " + item.getItemName() + " is already on maximun enchant!");
				else if (item.getItem().getCrystalType().getId() == 0)
					player.sendMessage("You can't Enchant under " + item.getItem().getCrystalType() + " Grade Items!");
				else if (item.isHeroItem())
					player.sendMessage("You Cannot be Enchant On " + item.getItemName() + " !");
				else if (player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.ENCHANT_ITEM_COUNT, player, true))
				{
					item.setEnchantLevel(enchant);
					item.updateDatabase();
					player.sendPacket(new ItemList(player, false));
					player.broadcastUserInfo();
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_S2_SUCCESSFULLY_ENCHANTED).addNumber(item.getEnchantLevel()).addItemName(item.getItemId()));
				}
				else
					player.sendMessage("You do not have enough Coins.");
			}
			else
				player.sendMessage("That item doesn't exist in your inventory.");
		}
		
		public static boolean conditionsclass(Player player)
		{
			if (player.isSubClassActive())
			{
				player.sendMessage("You cannot change your Main Class while you're with Sub Class.");
				return false;
			}
			else if (OlympiadManager.getInstance().isRegisteredInComp(player))
			{
				player.sendMessage("You cannot change your Main Class while you have been registered for olympiad match.");
				return false;
			}
			else if (player.getInventory().getInventoryItemCount(CustomConfig.DONATE_ITEM, -1) < CustomConfig.CLASS_ITEM_COUNT)
			{
				player.sendMessage("You do not have enough Donate Coins.");
				return false;
			}
			return true;
		}
		
		public static boolean conditionsname(String newName, Player player)
		{
			if (!newName.matches("^[a-zA-Z0-9]+$"))
			{
				player.sendMessage("Incorrect name. Please try again.");
				return false;
			}
			else if (newName.equals(player.getName()))
			{
				player.sendMessage("Please, choose a different name.");
				return false;
			}
			else if (PlayerInfoTable.getInstance().getPlayerObjectId(newName) > 0)
			{
				player.sendMessage("The name " + newName + " already exists.");
				return false;
			}
			else if (player.getInventory().getInventoryItemCount(CustomConfig.DONATE_ITEM, -1) < CustomConfig.NAME_ITEM_COUNT)
			{
				player.sendMessage("You do not have enough Donate Coins.");
				return false;
			}
			else if(newName.equals("") || newName.equals(" ") || newName.isEmpty()){
				player.sendMessage("You can't put empty Name.");
				return false;
			}
			return true;
		}
		
		public static boolean conditions(String newPass, String repeatNewPass, Player player)
		{
			if (newPass.length() < 5)
			{
				player.sendMessage("The new password is too short,must be > 5!");
				return false;
			}
			else if (newPass.length() > 45)
			{
				player.sendMessage("The new password is too long!");
				return false;
			}
			else if (!newPass.equals(repeatNewPass))
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PASSWORD_ENTERED_INCORRECT2));
				return false;
			}
			else if (player.getInventory().getInventoryItemCount(CustomConfig.DONATE_ITEM, -1) < CustomConfig.PASSWORD_ITEM_COUNT)
			{
				player.sendMessage("You do not have enough Donate Coins.");
				return false;
			}
			return true;
		}
		
		public static void changePassword(String newPass, String repeatNewPass, Player activeChar)
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement("UPDATE accounts SET password=? WHERE login=?"))
			{
				byte[] newPassword = MessageDigest.getInstance("SHA").digest(newPass.getBytes("UTF-8"));
				
				ps.setString(1, Base64.getEncoder().encodeToString(newPassword));
				ps.setString(2, activeChar.getAccountName());
				ps.executeUpdate();
				
				activeChar.sendMessage("Congratulations! Your password has been changed. You will now be disconnected for security reasons. Please login again.");
				activeChar.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.PASSWORD_ITEM_COUNT, activeChar, true);
				ThreadPool.schedule(() -> activeChar.logout(false), 3000);
			}
			catch (Exception e)
			{
				
			}
		}
		public static void Classes(String command, final Player player)
		{
			if (!conditionsclass(player))
				return;

			int newClass = Integer.parseInt(command.substring(command.indexOf("_") + 1));

			if(player.getBaseClass() == newClass){
				player.sendMessage("You cant change to your main class.");
				return;
			}

			if (player.destroyItemByItemId("Consume", CustomConfig.DONATE_ITEM, CustomConfig.CLASS_ITEM_COUNT, player, true))
			{
				for (final L2Skill skill : player.getSkills().values())
					player.removeSkill(skill.getId(),true);

				int baseClassBeforeChange = player.getBaseClass();
				
				int Hero = 0;
				Integer isClassInOlyNobless = 0;

				if(player.isNoble() && Olympiad.getInstance().getCompetitionDone(player.getObjectId())  > 0 )
					isClassInOlyNobless = Olympiad.getInstance().getClassNames1(baseClassBeforeChange);
				
				if(player.isHero())
					 Hero = HeroManager.getInstance().getHeroByClass(baseClassBeforeChange);

				//remove char dyes
				for(Henna dyes : player.getHennaList().getHennas())
					player.getHennaList().remove(dyes);

				player.setClassId(newClass);
				player.setBaseClass(newClass);

				player.store();
				player.broadcastUserInfo();
				player.sendSkillList();
				player.rewardSkills();

				if(isClassInOlyNobless != 0 && isClassInOlyNobless == baseClassBeforeChange) 
					Olympiad.getInstance().updateDonate(player.getBaseClass(), player.getObjectId());
				
				//Move hero to new Class
				if(Hero != 0) {
					player.setBaseClassDona(baseClassBeforeChange);
					player.updateDonaClass();
				}

				player.sendMessage("Your base class has been changed! You will Be Disconected in 5 Seconds!");
				ThreadPool.schedule(() -> player.logout(false), 5000);
			}
			else
				player.sendMessage("You do not have enough Donate Coins.");
		}
		
		@Override
		public String getHtmlPath(int npcId, int val)
		{
			String filename = "";
			
			if (val == 0)
				filename = "" + npcId;
			else
				filename = npcId + "-" + val;
			
			return "data/html/mods/donateshop/" + filename + ".htm";
		}

		@Override
		public void showEnchantSkillList(Player player)
		{
			boolean empty = true;
			if (!getTemplate().canTeach(player.getClassId()))
			{
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/trainer/" + getTemplate().getNpcId() + "-noskills.htm");
				player.sendPacket(html);
				return;
			}

			if (player.getClassId().level() < 3)
			{
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setHtml("<html><body> You must have 3rd class change quest completed.</body></html>");
				player.sendPacket(html);
				return;
			}

			final List<EnchantSkillNode> skk = new ArrayList<>();
			final List<EnchantSkillNode> skills = SkillTreeData.getInstance().getEnchantSkillsForCustom(player);

			for (EnchantSkillNode skill : skills)
			{
				L2Skill sk = SkillTable.getInstance().getInfo(skill.getId(), skill.getValue());
				if (sk == null)
					continue;

				if ( skill.getEnchantRate(80) == 0)
					continue;

				EnchantSkillNode data = SkillTreeData.getInstance().getEnchantSkillData(skill.getValue());
				if (data == null)
					continue;

				if( (skill.getValue() > 112 && skill.getValue() < 140)  || skill.getValue() > 152)
					continue;

				if (skill.getValue() <= 112 || (skill.getValue() >= 140 && skill.getValue() <= 152)) {
					skk.add(skill);
				}
				empty = false;
			}

			final ExEnchantSkillList e = new ExEnchantSkillList(skk);

			if (skills.isEmpty() || empty || skk.isEmpty())
			{
				player.sendPacket(SystemMessageId.THERE_IS_NO_SKILL_THAT_ENABLES_ENCHANT);

				if (player.getLevel() < 74)
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.DO_NOT_HAVE_FURTHER_SKILLS_TO_LEARN_S1).addNumber(74));
				else
					player.sendPacket(SystemMessageId.NO_MORE_SKILLS_TO_LEARN);
			}
			else
				player.sendPacket(e);


			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
	}