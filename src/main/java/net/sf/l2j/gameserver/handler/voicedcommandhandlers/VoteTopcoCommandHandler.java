package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

import Customs.vote.API.TopcoVoteReward;
import Customs.vote.API.VoteSite;

public class VoteTopcoCommandHandler implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS = {"votetopco"};

    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String params)
     {
        if(activeChar.isEligibleToVote(VoteSite.TOPCO) ){ //&& activeChar.isGM()
        	TopcoVoteReward rewardSite = new TopcoVoteReward();
            rewardSite.checkVoteReward(activeChar);
            return false;
        }
        activeChar.sendMessage("Available in " + activeChar.getVoteCountdown(VoteSite.TOPCO));
        return true;
     }

    @Override
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
}