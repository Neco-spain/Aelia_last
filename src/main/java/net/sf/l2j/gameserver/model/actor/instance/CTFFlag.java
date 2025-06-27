package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.data.cache.HtmCache;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import Customs.Events.CTF.CTFConfig;
import Customs.Events.CTF.CTFEvent;

public class CTFFlag extends Npc
{
    private static final String flagsPath = "data/html/mods/events/ctf/flags/";
    
    public CTFFlag(final int objectId, final NpcTemplate template) {
        super(objectId, template);
    }
    
    @Override
    public void showChatWindow(final Player playerInstance, final int val) {
        if (playerInstance == null) {
            return;
        }
        if (CTFEvent.isStarting() || CTFEvent.isStarted()) {
            final String flag = this.getTitle();
            final String team = CTFEvent.getParticipantTeam(playerInstance.getObjectId()).getName();
            final String enemyteam = CTFEvent.getParticipantEnemyTeam(playerInstance.getObjectId()).getName();
            if (flag == team) {
                if (CTFEvent.getEnemyCarrier(playerInstance) != null) {
                    final String htmContent = HtmCache.getInstance().getHtm("data/html/mods/events/ctf/flags/flag_friendly_missing.htm");
                    final NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(this.getObjectId());
                    npcHtmlMessage.setHtml(htmContent);
                    npcHtmlMessage.replace("%enemyteam%", enemyteam);
                    npcHtmlMessage.replace("%team%", team);
                    npcHtmlMessage.replace("%player%", playerInstance.getName());
                    playerInstance.sendPacket(npcHtmlMessage);
                }
                else if (playerInstance == CTFEvent.getTeamCarrier(playerInstance)) {
                    if (CTFConfig.CTF_EVENT_CAPTURE_SKILL > 0) {
                        playerInstance.broadcastPacket(new MagicSkillUse(playerInstance, CTFConfig.CTF_EVENT_CAPTURE_SKILL, 1, 1, 1));
                    }
                    CTFEvent.removeFlagCarrier(playerInstance);
                    CTFEvent.getParticipantTeam(playerInstance.getObjectId()).increasePoints();
                    CTFEvent.broadcastScreenMessage(playerInstance.getName() + " scored for the " + team + " team!", 7);
                    
                    //custom
                    CTFEvent.broadCastTitleUpdate();
                }
                else {
                    final String htmContent = HtmCache.getInstance().getHtm("data/html/mods/events/ctf/flags/flag_friendly.htm");
                    final NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(this.getObjectId());
                    npcHtmlMessage.setHtml(htmContent);
                    npcHtmlMessage.replace("%enemyteam%", enemyteam);
                    npcHtmlMessage.replace("%team%", team);
                    npcHtmlMessage.replace("%player%", playerInstance.getName());
                    playerInstance.sendPacket(npcHtmlMessage);
                }
            }
            else if (CTFEvent.playerIsCarrier(playerInstance)) {
                final String htmContent = HtmCache.getInstance().getHtm("data/html/mods/events/ctf/flags/flag_enemy.htm");
                final NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(this.getObjectId());
                npcHtmlMessage.setHtml(htmContent);
                npcHtmlMessage.replace("%enemyteam%", enemyteam);
                npcHtmlMessage.replace("%team%", team);
                npcHtmlMessage.replace("%player%", playerInstance.getName());
                playerInstance.sendPacket(npcHtmlMessage);
            }
            else if (CTFEvent.getTeamCarrier(playerInstance) != null) {
                final String htmContent = HtmCache.getInstance().getHtm("data/html/mods/events/ctf/flags/flag_enemy_missing.htm");
                final NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(this.getObjectId());
                npcHtmlMessage.setHtml(htmContent);
                npcHtmlMessage.replace("%enemyteam%", enemyteam);
                npcHtmlMessage.replace("%player%", CTFEvent.getTeamCarrier(playerInstance).getName());
                playerInstance.sendPacket(npcHtmlMessage);
            }
            else {
                if (CTFConfig.CTF_EVENT_CAPTURE_SKILL > 0) {
                    playerInstance.broadcastPacket(new MagicSkillUse(playerInstance, CTFConfig.CTF_EVENT_CAPTURE_SKILL, 1, 1, 1));
                }
                CTFEvent.setCarrierUnequippedWeapons(playerInstance, playerInstance.getInventory().getPaperdollItem(7), playerInstance.getInventory().getPaperdollItem(8));
                playerInstance.getInventory().equipItem(playerInstance.addItem("ctf", CTFEvent.getEnemyTeamFlagId(playerInstance), 1, playerInstance, true));
                playerInstance.getInventory().blockAllItems();
                playerInstance.broadcastUserInfo();
                CTFEvent.setTeamCarrier(playerInstance);
                CTFEvent.broadcastScreenMessage(playerInstance.getName() + " has taken the " + enemyteam + " flag team!", 5);
            }
        }
        playerInstance.sendPacket(ActionFailed.STATIC_PACKET);
    }
}
