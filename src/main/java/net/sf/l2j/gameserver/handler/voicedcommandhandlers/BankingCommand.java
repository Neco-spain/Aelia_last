package net.sf.l2j.gameserver.handler.voicedcommandhandlers;


import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;

import Custom.CustomConfig;

public class BankingCommand implements IVoicedCommandHandler
{
	private static String[] _voicedCommands =
	{
		"withdraw",
		"deposit"
	};

	@Override
	public boolean useVoicedCommand(final String command, final Player activeChar, final String target)
	{
		if (command.equalsIgnoreCase("deposit") && !(CustomConfig.BANKING_SYSTEM_GOLDBARS <= 0))
		{
			if (activeChar.getInventory().getInventoryItemCount(57, 0) >= CustomConfig.BANKING_SYSTEM_ADENA)
			{
				activeChar.getInventory().reduceAdena("Goldbar", CustomConfig.BANKING_SYSTEM_ADENA, activeChar, null);
				activeChar.getInventory().addItem("Goldbar", 3470, CustomConfig.BANKING_SYSTEM_GOLDBARS, activeChar, null);
				activeChar.getInventory().updateDatabase();
				activeChar.sendPacket(new ItemList(activeChar, true));
				activeChar.sendMessage("Thank you, now you have " + CustomConfig.BANKING_SYSTEM_GOLDBARS + " Goldbar(s), and " + CustomConfig.BANKING_SYSTEM_ADENA + " less adena.");
			}
			else
				activeChar.sendMessage("You do not have enough Adena to convert to Goldbar(s), you need " + CustomConfig.BANKING_SYSTEM_ADENA + " Adena.");
		}
		else if (command.equalsIgnoreCase("withdraw") && !(CustomConfig.BANKING_SYSTEM_GOLDBARS <= 0))
		{
			// If player hasn't enough space for adena
			final long a = activeChar.getInventory().getInventoryItemCount(57, 0);
			final long b = CustomConfig.BANKING_SYSTEM_ADENA;
			if (a + b > Integer.MAX_VALUE)
			{
				activeChar.sendMessage("You do not have enough space for all the adena in inventory!");
				return false;
			}

			if (activeChar.getInventory().getInventoryItemCount(3470, 0) >= CustomConfig.BANKING_SYSTEM_GOLDBARS)
			{
				activeChar.getInventory().destroyItemByItemId("Adena", 3470, CustomConfig.BANKING_SYSTEM_GOLDBARS, activeChar, null);
				activeChar.getInventory().addAdena("Adena", CustomConfig.BANKING_SYSTEM_ADENA, activeChar, null);
				activeChar.getInventory().updateDatabase();
				activeChar.sendPacket(new ItemList(activeChar, true));
				activeChar.sendMessage("Thank you, now you have " + CustomConfig.BANKING_SYSTEM_ADENA + " Adena, and " + CustomConfig.BANKING_SYSTEM_GOLDBARS + " less Goldbar(s).");
			}
			else
				activeChar.sendMessage("You do not have any Goldbars to turn into " + CustomConfig.BANKING_SYSTEM_ADENA + " Adena.");
		}
		return true;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}