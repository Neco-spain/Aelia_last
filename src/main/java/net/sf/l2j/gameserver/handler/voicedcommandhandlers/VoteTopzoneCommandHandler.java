package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

import Customs.vote.API.TopzoneVoteReward;
import Customs.vote.API.VoteSite;

public class VoteTopzoneCommandHandler implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS = {"votetop"};

    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String params)
     {
        if(activeChar.isEligibleToVote(VoteSite.TOPZONE)){
        	TopzoneVoteReward rewardSite = new TopzoneVoteReward();
            rewardSite.checkVoteReward(activeChar);
            return false;
        }
        activeChar.sendMessage("Available in " + activeChar.getVoteCountdown(VoteSite.TOPZONE));
        return true;
     }

    @Override
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
}