package net.sf.l2j.gameserver.model.actor.instance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.StringTokenizer;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.GameClient;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.ValidateLocation;
/**
 * @author squallcs
 *
 * @Reworked Abyssal
 */
public class BugReport extends Folk
{
	private static String _type;
	
	public BugReport(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (command.startsWith("send_report"))
		{
			StringTokenizer st = new StringTokenizer(command);
			st.nextToken();
			String msg = "";
			String type = null;
			type = st.nextToken();
			st.nextToken();
			try
			{
				while (st.hasMoreTokens())
				{
					msg = msg + " " + st.nextToken();
				}
				
				sendReport(player, type, msg);
			}
			catch (StringIndexOutOfBoundsException e)
			{
			}
		}
	}
	
	static
	{
		new File("log/BugReports/").mkdirs();
	}
	
	private static void sendReport(Player player, String command, String msg)
	{
		String type = command;
		GameClient info = player.getClient().getConnection().getClient();
		
		if (type.equals("General"))
			_type = "General";
		if (type.equals("Fatal"))
			_type = "Fatal";
		if (type.equals("Misuse"))
			_type = "Misuse";
		if (type.equals("Balance"))
			_type = "Balance";
		if (type.equals("Other"))
			_type = "Other";
		
		try
		{
			String fname = "log/BugReports/" + player.getName() + ".txt";
			File file = new File(fname);
			boolean exist = file.createNewFile();
			if (!exist)
			{
				player.sendMessage("You have already sent a bug report, GMs must check it first.");
				return;
			}
			FileWriter fstream = new FileWriter(fname);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("Character Info: " + info + "\r\nBug Type: " + _type + "\r\nMessage: " + msg);
			player.sendMessage("Report sent. GMs will check it soon. Thanks...");
			
			System.out.println("Character: " + player.getName() + " sent a bug report.");
			out.close();
		}
		catch (Exception e)
		{
			player.sendMessage("Something went wrong try again.");
		}
	}
	
	@Override
	public void onAction(Player player)
	{
		if (this != player.getTarget())
		{
			player.setTarget(this);
			
			player.sendPacket(new MyTargetSelected(getObjectId(), 0));
			
			player.sendPacket(new ValidateLocation(this));
		}
		else if (!canInteract(player))
		{
			player.getAI().setIntention(IntentionType.INTERACT, this);
		}
		else
		{
			showHtmlWindow(player);
		}
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	private void showHtmlWindow(Player activeChar)
	{
		StringBuilder tb = new StringBuilder("<html><head><title>Bug Report Manager</title></head><body>");
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		tb.append("<center><td align=\"center\"><img src=\"L2Server.logo\" width=256 height=100></td> </center>");

		tb.append("<body><br><br><center>");

		tb.append("<table width=240>");
		tb.append("<tr><td align=center><font color=\"b09979\">Hello " + activeChar.getName() + ".</font></td></tr>");
		tb.append("<tr><td align=center><font color=\"b09979\">There are no Gms online</font></td></tr>");
		tb.append("<tr><td align=center><font color=\"b09979\">and you want to report something?</font></td></tr>");
		tb.append("</table><br>");
		tb.append("<img src=\"L2UI.SquareGray\" width=300 height=1><br><br>");
		tb.append("<table width=250><tr>");
		tb.append("<td><font color=\"ff4d4d\">Select Report Type:</font></td>");
		tb.append("<td><combobox width=105 var=type list=General;Fatal;Misuse;Balance;Other></td>");
		tb.append("</tr></table><br><br>");
		tb.append("<multiedit var=\"msg\" width=250 height=50><br>");
		tb.append("<br><img src=\"L2UI.SquareGray\" width=300 height=1><br><br>");
		tb.append("<button value=\"Send Report\" action=\"bypass -h npc_" + getObjectId() + "_send_report $type $msg\" width=225 height=18 back=\"BotoesNpc.botaomensagem_over\" fore=\"BotoesNpc.botaomensagem\">");

		tb.append("</center></body></html>");
		htm.setHtml(tb.toString());
		activeChar.sendPacket(htm);

	 activeChar.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
}