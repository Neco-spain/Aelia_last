package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

import Customs.vote.API.L2VotesVoteReward;
import Customs.vote.API.VoteSite;

public class VoteL2VotesCommandHandler implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS = {"l2vote"};

    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String params)
     {
        if(activeChar.isEligibleToVote(VoteSite.L2VOTE) ){ //&& activeChar.isGM()
        	L2VotesVoteReward rewardSite = new L2VotesVoteReward();
            rewardSite.checkVoteReward(activeChar);
            return false;
        }
        activeChar.sendMessage("Available in " + activeChar.getVoteCountdown(VoteSite.L2VOTE));
        return true;
     }

    @Override
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
}