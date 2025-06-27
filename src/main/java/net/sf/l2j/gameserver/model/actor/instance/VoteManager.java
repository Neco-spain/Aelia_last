package net.sf.l2j.gameserver.model.actor.instance;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import Customs.vote.API.VoteDao;
import Customs.vote.API.VoteSite;


public class VoteManager extends Folk
{
    public VoteManager(int objectId, NpcTemplate template)
    {
        super(objectId, template);
    }
   
    @Override
    public void onBypassFeedback(final Player player, String command)
    {
        if (player == null)
            return;
    }
   
    @Override
    public void showChatWindow(Player player)
    {
        NpcHtmlMessage html = new NpcHtmlMessage(0);
        final StringBuilder sb1 = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        final StringBuilder sb3 = new StringBuilder();
        final StringBuilder sb4 = new StringBuilder();
        final StringBuilder sb5 = new StringBuilder();
        final StringBuilder sb6 = new StringBuilder();

       	
      if (player.isEligibleToVote(VoteSite.TOPZONE))
        	sb1.append("<button value=\"Reward Topzone\" action=\"bypass -h voiced_votetop\" width=225 height=18 back=\"BotoesNpc.botaomensagem_over\" fore=\"BotoesNpc.botaomensagem\">");
        else
            sb1.append(String.format("L2 Topzone: Vote again in: %s <br1>", player.getVoteCountdown(VoteSite.TOPZONE)));

        if (player.isEligibleToVote(VoteSite.L2JBRASIL))
        	sb1.append("<button value=\"Reward L2jBrasil\" action=\"bypass -h voiced_votebrasil\" width=225 height=18 back=\"BotoesNpc.botaomensagem_over\" fore=\"BotoesNpc.botaomensagem\">");
        else
            sb1.append(String.format("L2j Brasil: Vote again in: %s <br1>", player.getVoteCountdown(VoteSite.L2JBRASIL)));  
        
        
        if (player.isEligibleToVote(VoteSite.HOPZONE))
        	sb2.append("<button value=\"Reward Hopzone\" action=\"bypass -h voiced_votehop\" width=225 height=18 back=\"BotoesNpc.botaomensagem_over\" fore=\"BotoesNpc.botaomensagem\">");
        else
            sb2.append(String.format("L2 Hopzone: Vote again in: %s <br1>", player.getVoteCountdown(VoteSite.HOPZONE)));
       
        if (player.isEligibleToVote(VoteSite.NETWORK))
        	sb3.append("<button value=\"Reward Network\" action=\"bypass -h voiced_votenet\" width=225 height=18 back=\"BotoesNpc.botaomensagem_over\" fore=\"BotoesNpc.botaomensagem\">");
        else
            sb3.append(String.format("L2Network: Vote again in: %s <br1>", player.getVoteCountdown(VoteSite.NETWORK)));
        
        if (player.isEligibleToVote(VoteSite.TOPCO))
        	sb4.append("<button value=\"Reward TopCo\" action=\"bypass -h voiced_votetopco\" width=225 height=18 back=\"BotoesNpc.botaomensagem_over\" fore=\"BotoesNpc.botaomensagem\">");
        else
            sb4.append(String.format("L2TopCo: Vote again in: %s <br1>", player.getVoteCountdown(VoteSite.TOPCO)));
        
        if (player.isEligibleToVote(VoteSite.L2VOTE))
        	sb5.append("<button value=\"Reward L2Votes\" action=\"bypass -h voiced_l2vote\" width=225 height=18 back=\"BotoesNpc.botaomensagem_over\" fore=\"BotoesNpc.botaomensagem\">");
        else
            sb5.append(String.format("L2Votes: Vote again in: %s <br1>", player.getVoteCountdown(VoteSite.L2VOTE)));
        
        
        
        
		html.setFile(getHtmlPath(getNpcId(), 0));
        html.replace("%VoteTopZone%", sb1.toString());
		html.replace("%VoteBrasil%", sb1.toString());
        html.replace("%VoteHopZone%", sb2.toString());
        html.replace("%VoteNetwork%", sb3.toString());
        html.replace("%voteTopco%", sb4.toString());
        html.replace("%voteL2vote%", sb5.toString());
        
        html.replace("%char_name%", player.getName());
        html.replace("%votebuff%", VoteDao.cangetVoteBuff(player) ? reward(player) : "<br1>"  );
        player.sendPacket(html);
    }
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String filename = "";
		if (val == 0)
			filename = "" + npcId;
		else
			filename = npcId + "-"  +val;
		
		return "data/html/mods/votemanager/"  +filename + ".htm";
	}
	
	
	public static String reward(Player player) {
		final StringBuilder sb1 = new StringBuilder();
		
		sb1.append("<button value=\"Get Vote Buff\" action=\"bypass -h voiced_votebuff\" width=225 height=18 back=\"BotoesNpc.botaomensagem_over\" fore=\"BotoesNpc.botaomensagem\">");//	width=94 height=21 back=\"ormgk.butb\" fore=\"ormgk.butb\">"
		
		return sb1.toString();
	}
}