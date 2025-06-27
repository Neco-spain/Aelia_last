package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

import Customs.vote.API.NetworkVoteReward;
import Customs.vote.API.VoteSite;

public class VoteNetworkCommandHandler implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS = {"votenet"};

    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String params)
     {
        if(activeChar.isEligibleToVote(VoteSite.NETWORK)){
        	NetworkVoteReward rewardSite = new NetworkVoteReward();
            rewardSite.checkVoteReward(activeChar);
            return false;
        }
        activeChar.sendMessage("Available in " + activeChar.getVoteCountdown(VoteSite.NETWORK));
        return true;
     }

    @Override
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
}