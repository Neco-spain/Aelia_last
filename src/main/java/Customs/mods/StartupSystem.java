package Customs.mods;

import java.util.Iterator;

import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.cache.HtmCache;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.CameraMode;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.NormalCamera;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.TutorialCloseHtml;
import net.sf.l2j.gameserver.network.serverpackets.TutorialShowHtml;

import Custom.CustomConfig;
import Custom.Util;

public class StartupSystem
{
  public static void startSetup(Player activeChar)
  {
    if (activeChar.getSelectClasse()) {
      SelectClass(activeChar);
    } else if (activeChar.getSelectArmor()) {
      SelectArmor(activeChar);
    } else if (activeChar.getSelectWeapon()) {
      SelectWeapon(activeChar);
    } else if (activeChar.getFirstLog()) {
      endStartup(activeChar);
    }
  }
  
  public static void handleCommands(Player activeChar, String _command)
  {
    if (_command.startsWith("Duelist"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(88);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
     /* if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("DreadNought"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(89);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
    /*  if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Phoenix_Knight"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(90);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
    /*  if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Hell_Knight"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(91);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
   /*   if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Adventurer"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(93);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
   /*   if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Sagittarius"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(92);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
    /*  if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Archmage"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(94);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
   /*   if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Soultaker"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(95);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
   /*   if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Arcana_Lord"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(96);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
  /*    if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Cardinal"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(97);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
 /*     if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Hierophant"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(98);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
   /*   if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Eva_Templar"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(99);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
    /*  if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Sword_Muse"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(100);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
    /*  if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Wind_Rider"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(101);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
    /*  if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Moonlight_Sentinel"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(102);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
   /*   if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Mystic_Muse"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(103);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
  /*    if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Elemental_Master"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(104);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
   /*   if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Eva_Saint"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(105);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
     /* if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Shillien_Templar"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(106);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
  /*    if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Spectral_Dancer"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(107);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
   /*   if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Ghost_Hunter"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(108);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
    /*  if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Ghost_Sentinel"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(109);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
    /*  if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Storm_Screamer"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(110);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
    /*  if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Spectral_Master"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(111);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
     /* if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Shillen_Saint"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(112);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
     /* if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Titan"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(113);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
     /* if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Grand_Khauatari"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(114);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
     /* if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Dominator"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(115);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
    /*  if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Doomcryer"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(116);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
    /*  if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Fortune_Seeker"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(117);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
 /*     if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    else if (_command.startsWith("Maestro"))
    {
      if(checkClass(activeChar))
      {
        Util.handleIllegalPlayerAction(activeChar, "StartupSystem: player [" + activeChar.getName() + "] trying to change class exploit.", 2);
        return;
      }
      SelectArmor(activeChar);
      
      activeChar.setClassId(118);
      activeChar.broadcastUserInfo();
      activeChar.setBaseClass(activeChar.getActiveClass());
      activeChar.store();
      
      activeChar.setSelectClasse(false);
      activeChar.updateSelectClasse();
    /*  if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
        activeChar.checkAllowedSkills();
      }*/
    }
    if (_command.startsWith(CustomConfig.BYBASS_HEAVY_ITEMS))
    {
      SelectWeapon(activeChar);
      for (int[] reward : CustomConfig.SET_HEAVY_ITEMS)
      {
        ItemInstance PhewPew1 = activeChar.getInventory().addItem("Heavy Armor: ", reward[0], reward[1], activeChar, null);
        activeChar.getInventory().equipItemAndRecord(PhewPew1);
      }
      activeChar.setSelectArmor(false);
      activeChar.updateSelectArmor();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_LIGHT_ITEMS))
    {
      SelectWeapon(activeChar);
      for (int[] reward : CustomConfig.SET_LIGHT_ITEMS)
      {
        ItemInstance PhewPew1 = activeChar.getInventory().addItem("Light Armor: ", reward[0], reward[1], activeChar, null);
        activeChar.getInventory().equipItemAndRecord(PhewPew1);
      }
      activeChar.setSelectArmor(false);
      activeChar.updateSelectArmor();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_ROBE_ITEMS))
    {
      SelectWeapon(activeChar);
      for (int[] reward : CustomConfig.SET_ROBE_ITEMS)
      {
        ItemInstance PhewPew1 = activeChar.getInventory().addItem("Robe Armor: ", reward[0], reward[1], activeChar, null);
        activeChar.getInventory().equipItemAndRecord(PhewPew1);
      }
      activeChar.setSelectArmor(false);
      activeChar.updateSelectArmor();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_01_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_01_ID, 1, activeChar, null);
      ItemInstance item1 = activeChar.getInventory().addItem("Shield", CustomConfig.WP_SHIELD, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      activeChar.getInventory().equipItemAndRecord(item1);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_02_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_02_ID, 1, activeChar, null);
      ItemInstance item1 = activeChar.getInventory().addItem("Shield", CustomConfig.WP_SHIELD, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      activeChar.getInventory().equipItemAndRecord(item1);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_03_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_03_ID, 1, activeChar, null);
      ItemInstance item1 = activeChar.getInventory().addItem("Shield", CustomConfig.WP_SHIELD, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      activeChar.getInventory().equipItemAndRecord(item1);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_04_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_04_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_05_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_05_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_06_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_06_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_07_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_07_ID, 1, activeChar, null);
      ItemInstance item1 = activeChar.getInventory().addItem("Shield", CustomConfig.WP_SHIELD, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      activeChar.getInventory().equipItemAndRecord(item1);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_08_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_08_ID, 1, activeChar, null);
      ItemInstance item1 = activeChar.getInventory().addItem("Shield", CustomConfig.WP_SHIELD, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      activeChar.getInventory().equipItemAndRecord(item1);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_09_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_09_ID, 1, activeChar, null);
      ItemInstance item1 = activeChar.getInventory().addItem("Shield", CustomConfig.WP_SHIELD, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      activeChar.getInventory().equipItemAndRecord(item1);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_10_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_10_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_11_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_11_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_12_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_12_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_13_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_13_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_14_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_14_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_15_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_15_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_16_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_16_ID, 1, activeChar, null);
      
      activeChar.getInventory().addItem("Arrow", CustomConfig.WP_ARROW, 1, activeChar, null);
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_17_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_17_ID, 1, activeChar, null);
      
      activeChar.getInventory().addItem("Arrow", CustomConfig.WP_ARROW, 1, activeChar, null);
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_18_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_18_ID, 1, activeChar, null);
      
      activeChar.getInventory().addItem("Arrow", CustomConfig.WP_ARROW, 1, activeChar, null);
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_19_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_19_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_20_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_20_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_21_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_21_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_22_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_22_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_23_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_23_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_24_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_24_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_25_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_25_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_26_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_26_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_27_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_27_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_28_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_28_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_29_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_29_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else if (_command.startsWith(CustomConfig.BYBASS_WP_30_ITEM))
    {
      endStartup(activeChar);
      
      ItemInstance item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_30_ID, 1, activeChar, null);
      
      activeChar.getInventory().equipItemAndRecord(item);
      
      activeChar.setSelectWeapon(false);
      activeChar.updateSelectWeapon();
    }
    else
    {
      Object item;
      if (_command.startsWith(CustomConfig.BYBASS_WP_31_ITEM))
      {
        endStartup(activeChar);
        
        item = activeChar.getInventory().addItem("Weapon", CustomConfig.WP_31_ID, 1, activeChar, null);
        
        activeChar.getInventory().equipItemAndRecord((ItemInstance)item);
        
        activeChar.setSelectWeapon(false);
        activeChar.updateSelectWeapon();
      }
      else if (_command.startsWith("closeit")) {
    	  activeChar.sendPacket(TutorialCloseHtml.STATIC_PACKET);
    	  
      }
      else if (_command.startsWith("end_setup"))
      {
        activeChar.sendPacket(TutorialCloseHtml.STATIC_PACKET);
//        activeChar.sendPacket(new SocialAction(activeChar, 3));
        activeChar.getAllAvailableSkills();
        if (activeChar.isMageClass())
        {
          if(canGetBuff(activeChar)){
            for (item = CustomConfig.START_MAGE_BUFF_LIST.iterator(); ((Iterator)item).hasNext();)
            {
              Integer skillid = (Integer)((Iterator)item).next();

              L2Skill skill = SkillTable.getInstance().getInfo(skillid.intValue(), SkillTable.getInstance().getMaxLevel(skillid.intValue()));
              if (skill != null) {
                skill.getEffects(activeChar, activeChar);
              }
            }

          activeChar.setCurrentHpMp(activeChar.getMaxHp(), activeChar.getMaxMp());
          activeChar.setCurrentCp(activeChar.getMaxCp());
          }

          activeChar.getInventory().addItem("Mana Potion", 728, 1, activeChar, null);
          activeChar.getInventory().addItem("Greater Healing Potion", 1539, 1, activeChar, null);
          activeChar.getInventory().addItem("Scroll of Scape", 736, 50, activeChar, null);
          activeChar.getInventory().addItem("Blessed Soul Shot", 2512, 1, activeChar, null);
        }
        else
        {
          if(canGetBuff(activeChar)) {
            for (item = CustomConfig.START_FIGHTER_BUFF_LIST.iterator(); ((Iterator) item).hasNext(); ) {
              Integer skillid = (Integer) ((Iterator) item).next();

              L2Skill skill = SkillTable.getInstance().getInfo(skillid.intValue(), SkillTable.getInstance().getMaxLevel(skillid.intValue()));
              if (skill != null) {
                skill.getEffects(activeChar, activeChar);
              }
            }

          activeChar.setCurrentHpMp(activeChar.getMaxHp(), activeChar.getMaxMp());
          activeChar.setCurrentCp(activeChar.getMaxCp());
          }

          activeChar.getInventory().addItem("Mana Potion", 728, 1, activeChar, null);
          activeChar.getInventory().addItem("Greater Healing Potion", 1539, 1, activeChar, null);
          activeChar.getInventory().addItem("Scroll of Scape", 736, 50, activeChar, null);
          activeChar.getInventory().addItem("Soul Shot", 1465, 1, activeChar, null);
         /* if (CustomConfig.CHECK_SKILLS_ON_ENTER) {
            activeChar.checkAllowedSkills();
          }*/
        }
        ThreadPool.schedule(new Runnable()
        {
          @Override
		public void run()
          {
            activeChar.sendPacket(new CameraMode(0));
            activeChar.sendPacket(NormalCamera.STATIC_PACKET);
            
            activeChar.sendPacket(new InventoryUpdate());
            activeChar.sendPacket(new ItemList(activeChar, false));
            activeChar.sendPacket(new StatusUpdate(activeChar));
            
            activeChar.getInventory().reloadEquippedItems();
            
            activeChar.setFirstLog(false);
            activeChar.updateFirstLog();
            
            activeChar.getAppearance().setVisible();
            activeChar.broadcastUserInfo();
            
//            StartupSystem.RandomTeleport(activeChar);
          }
        }, 5000L);
      }
    }
  }
  
  public static void SelectClass(Player activeChar)
  {
    activeChar.sendPacket(new SocialAction(activeChar, 2));
    if (activeChar.getSelectClasse()) {
      if (activeChar.getClassId().getId() == 0)
      {
        String msg = HtmCache.getInstance().getHtm("data/html/mods/Startup/Classes/Human_Fighter.htm");
        
        msg = msg.replace("%name%", activeChar.getName());
        activeChar.sendPacket(new TutorialShowHtml(msg));
      }
    }
    if (activeChar.getSelectClasse()) {
      if (activeChar.getClassId().getId() == 10)
      {
        String msg = HtmCache.getInstance().getHtm("data/html/mods/Startup/Classes/Human_Mystic.htm");
        
        msg = msg.replace("%name%", activeChar.getName());
        activeChar.sendPacket(new TutorialShowHtml(msg));
      }
    }
    if (activeChar.getSelectClasse()) {
      if (activeChar.getClassId().getId() == 18)
      {
        String msg = HtmCache.getInstance().getHtm("data/html/mods/Startup/Classes/Elven_Fighter.htm");
        
        msg = msg.replace("%name%", activeChar.getName());
        activeChar.sendPacket(new TutorialShowHtml(msg));
      }
    }
    if (activeChar.getSelectClasse()) {
      if (activeChar.getClassId().getId() == 25)
      {
        String msg = HtmCache.getInstance().getHtm("data/html/mods/Startup/Classes/Elven_Mystic.htm");
        
        msg = msg.replace("%name%", activeChar.getName());
        activeChar.sendPacket(new TutorialShowHtml(msg));
      }
    }
    if (activeChar.getSelectClasse()) {
      if (activeChar.getClassId().getId() == 31)
      {
        String msg = HtmCache.getInstance().getHtm("data/html/mods/Startup/Classes/Dark_Fighter.htm");
        
        msg = msg.replace("%name%", activeChar.getName());
        activeChar.sendPacket(new TutorialShowHtml(msg));
      }
    }
    if (activeChar.getSelectClasse()) {
      if (activeChar.getClassId().getId() == 38)
      {
        String msg = HtmCache.getInstance().getHtm("data/html/mods/Startup/Classes/Dark_Mystic.htm");
        
        msg = msg.replace("%name%", activeChar.getName());
        activeChar.sendPacket(new TutorialShowHtml(msg));
      }
    }
    if (activeChar.getSelectClasse()) {
      if (activeChar.getClassId().getId() == 44)
      {
        String msg = HtmCache.getInstance().getHtm("data/html/mods/Startup/Classes/Orc_Fighter.htm");
        
        msg = msg.replace("%name%", activeChar.getName());
        activeChar.sendPacket(new TutorialShowHtml(msg));
      }
    }
    if (activeChar.getSelectClasse()) {
      if (activeChar.getClassId().getId() == 49)
      {
        String msg = HtmCache.getInstance().getHtm("data/html/mods/Startup/Classes/Orc_Mystic.htm");
        
        msg = msg.replace("%name%", activeChar.getName());
        activeChar.sendPacket(new TutorialShowHtml(msg));
      }
    }
    if (activeChar.getSelectClasse()) {
      if (activeChar.getClassId().getId() == 53)
      {
        String msg = HtmCache.getInstance().getHtm("data/html/mods/Startup/Classes/Dwarf_Fighter.htm");
        
        msg = msg.replace("%name%", activeChar.getName());
        activeChar.sendPacket(new TutorialShowHtml(msg));
      }
    }
  }
  
  public static void SelectArmor(Player activeChar)
  {
//    activeChar.sendPacket(new SocialAction(activeChar, 11));
    if (activeChar.getSelectArmor())
    {
      String msg = HtmCache.getInstance().getHtm("data/html/mods/Startup/StartArmor.htm");
      
      msg = msg.replace("%name%", activeChar.getName());
      activeChar.sendPacket(new TutorialShowHtml(msg));
    }
  }
  
  public static void SelectWeapon(Player activeChar)
  {
//    activeChar.sendPacket(new SocialAction(activeChar, 12));
    if (activeChar.getSelectWeapon())
    {
      String msg = HtmCache.getInstance().getHtm("data/html/mods/Startup/StartWeapon.htm");
      
      msg = msg.replace("%name%", activeChar.getName());
      activeChar.sendPacket(new TutorialShowHtml(msg));
    }
  }
  
  public static void endStartup(Player activeChar)
  {
//    activeChar.sendPacket(new SocialAction(activeChar, 9));
    if (activeChar.getFirstLog())
    {
      String msg = HtmCache.getInstance().getHtm("data/html/mods/Startup/Finish.htm");
      
      msg = msg.replace("%name%", activeChar.getName());
      activeChar.sendPacket(new TutorialShowHtml(msg));
    }
  }
  
  public static void RandomTeleport(Player activeChar)
  {
    switch (Rnd.get(5))
    {
    case 0: 
      int x = 82533 + Rnd.get(100);
      int y = 149122 + Rnd.get(100);
      activeChar.teleportTo(x, y, 62062, 0);
      break;
    case 1: 
      x = 82571 + Rnd.get(100);
      y = 148060 + Rnd.get(100);
      activeChar.teleportTo(x, y, 62069, 0);
      break;
    case 2: 
      x = 81376 + Rnd.get(100);
      y = 148042 + Rnd.get(100);
      activeChar.teleportTo(x, y, 62062, 0);
      break;
    case 3: 
       x = 81359 + Rnd.get(100);
       y = 149218 + Rnd.get(100);
      activeChar.teleportTo(x, y, 62062, 0);
      break;
    case 4: 
       x = 82862 + Rnd.get(100);
       y = 148606 + Rnd.get(100);
      activeChar.teleportTo(x, y, 62062, 0);
      break;
    }
  }

  public static boolean canGetBuff(Player player) {
    if (!player.isInsideZone(ZoneId.TOWN) || player.isDead() || player.isSitting() || player.isInOlympiadMode() || player.isOlympiadStart()){
      player.sendMessage("Your current state doesn't allow you to get start up buffs.");
      return false;
    }

    return true;
  }

  public static boolean checkClass(Player activeChar){
    if(activeChar.getClassId().level() > 0){
      return true;
    }
    return false;
  }


}
