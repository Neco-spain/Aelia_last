package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import java.util.StringTokenizer;

import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import Customs.data.SkinTable;

public class trySkin implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS;
    
    @Override
    public boolean useVoicedCommand(final String command, final Player activeChar, final String target) {
        if (command.equals("skintry")) {
            showTrySkinHtml(activeChar);
        }
        if (command.startsWith("trySkin")) {
            if (activeChar.getTrySkin() != 0) {
                activeChar.sendMessage("Wait, you are experiencing a skin.");
                return false;
            }
            if (!activeChar.isInsideZone(ZoneId.TOWN)) {
                activeChar.sendMessage("This command can only be used within a city.");
                return false;
            }
            final StringTokenizer st = new StringTokenizer(command);
            st.nextToken();
            final int skinId = Integer.parseInt(st.nextToken());
            if (!SkinTable.getInstance().getSkinId(skinId)) {
                activeChar.sendMessage("Invalid skin.");
                return false;
            }
            activeChar.setTrySkin(skinId);
            activeChar.broadcastUserInfo();
            ThreadPool.schedule(() -> {
                activeChar.setTrySkin(0);
                activeChar.broadcastUserInfo();
                return;
            }, 3000L);
        }
        return true;
    }
    
    private static void showTrySkinHtml(final Player activeChar) {
        final NpcHtmlMessage html = new NpcHtmlMessage(0);
        html.setFile("data/html/mods/skin/trySkin.htm");
        activeChar.sendPacket(html);
    }
    
    @Override
    public String[] getVoicedCommandList() {
        return trySkin.VOICED_COMMANDS;
    }
    
    static {
        VOICED_COMMANDS = new String[] { "skintry", "trySkin" };
    }
}
