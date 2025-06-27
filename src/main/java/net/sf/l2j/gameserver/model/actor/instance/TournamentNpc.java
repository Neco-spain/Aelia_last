package net.sf.l2j.gameserver.model.actor.instance;

import Customs.Events.Tournaments.AbstractTournament;
import Customs.Events.Tournaments.TournamentManager;
import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class TournamentNpc extends Folk
{
    public TournamentNpc(int objectId, NpcTemplate template)
    {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command)
    {
        if (command.startsWith("match"))
        {
            int id = Integer.parseInt(command.substring(6));

            final AbstractTournament tournament = TournamentManager.getTournament(id);

            if (tournament == null)
            {
                player.sendMessage("Tournament does not exist.");
                return;
            }

            tournament.handleRegUnReg(player);
        }
        else
            super.onBypassFeedback(player, command);
    }

    @Override
    public void showChatWindow(Player player, int val)
    {
        final StringBuilder sb = new StringBuilder();

        if (player.isRegisteredInTournament()) {
            StringUtil.append(sb, "<center>You are currently registered at: " + player.getTournament().getTeamSize() + " vs " + player.getTournament().getTeamSize() + "<br><button action=\"bypass -h npc_" + getObjectId() + "_match " + player.getTournament().getTeamSize() + " \" value=\"" + "Unregister" + "\" width=75 height=21 back=\"L2UI_ch3.Btn1_normalOn\" fore=\"L2UI_ch3.Btn1_normal\"></center>");
            StringUtil.append(sb, "<br>Registered: <font color=00ff00>" + player.getTournament().getAllPlayer().size() + "</font>");
        }
        else
        {
            StringUtil.append(sb, "<br>");

            for (final AbstractTournament match : TournamentManager.getTournaments()) {
                StringUtil.append(sb, "<table width=300>");
                StringUtil.append(sb, "<tr>");
                StringUtil.append(sb, "<td align=center>");
                StringUtil.append(sb, "<button action=\"bypass -h npc_" + getObjectId() + "_match " + match.getTeamSize() + " \" value=\"" + match.getTeamSize() + " vs " + match.getTeamSize() + "\" width=75 height=21 back=\"L2UI_ch3.Btn1_normalOn\" fore=\"L2UI_ch3.Btn1_normal\">");
                StringUtil.append(sb, "</td>");
                StringUtil.append(sb, "<td align=center>");
                StringUtil.append(sb, "Registered: <font color=00ff00>" + match.getAllPlayer().size() + "</font>");
                StringUtil.append(sb, "</td>");
                StringUtil.append(sb, "</tr>");
                StringUtil.append(sb, "</table>");
                StringUtil.append(sb, "<br>");

            }
        }

        final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile("data/html/mods/tournament/index.htm");
        html.replace("%tournament%", sb.toString());
        html.replace("%objectId%", String.valueOf(getObjectId()));
        player.sendPacket(html);
    }
}