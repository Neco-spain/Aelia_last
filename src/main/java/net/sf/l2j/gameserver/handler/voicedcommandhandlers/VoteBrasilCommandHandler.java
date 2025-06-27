package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

import Customs.vote.API.VoteSite;
import Customs.vote.API.l2jbrasilVoteReward;

public class VoteBrasilCommandHandler implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS = {"votebrasil"};

    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String params)
     {
        if(activeChar.isEligibleToVote(VoteSite.L2JBRASIL)){
        	l2jbrasilVoteReward rewardSite = new l2jbrasilVoteReward();
            rewardSite.checkVoteReward(activeChar);
            return false;
        }
        activeChar.sendMessage("Available in " + activeChar.getVoteCountdown(VoteSite.L2JBRASIL));
        return true;
     }

    @Override
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
}