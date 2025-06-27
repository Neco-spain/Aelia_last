package net.sf.l2j.gameserver.handler;

import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.actor.Player;

public interface ITutorialHandler
{
       public static Logger _log = Logger.getLogger(ITutorialHandler.class.getName());

       public boolean useLink(String command, Player activeChar, String params);

       public String[] getLinkList();
}