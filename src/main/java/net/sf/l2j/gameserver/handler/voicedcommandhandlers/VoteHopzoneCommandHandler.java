package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

import Customs.vote.API.HopzoneVoteReward;
import Customs.vote.API.VoteSite;

public class VoteHopzoneCommandHandler implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS = {"votehop"};

    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String params)
      {
        if(activeChar.isEligibleToVote(VoteSite.HOPZONE)){
            HopzoneVoteReward rewardSite = new HopzoneVoteReward();
            rewardSite.checkVoteReward(activeChar);
            return false;
        }
        activeChar.sendMessage("Available in " + activeChar.getVoteCountdown(VoteSite.HOPZONE));
        return true;
      }

    @Override
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
}