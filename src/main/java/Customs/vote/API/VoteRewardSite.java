package Customs.vote.API;

import java.util.logging.Level;
import java.util.logging.Logger;

import Custom.CustomConfig;
import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;

public abstract class VoteRewardSite {

    private static final Logger LOGGER = Logger.getLogger(VoteRewardSite.class.getName());

    protected abstract String getEndpoint(Player player);

    public void checkVoteReward(Player player){
        try
        {
            if(player.isVoting())
            {
                player.sendMessage("You are already voting.");
                player.sendPacket(ActionFailed.STATIC_PACKET);
                return;
            }

            if(World.getInstance().getPlayers().stream().filter(Player::isVoting).count() >= 5){
                player.sendMessage("Many people are voting currently. Try again in a couple of seconds.");
                player.sendPacket(ActionFailed.STATIC_PACKET);
                return;
            }

            player.setIsVoting(true);

            if(!player.isEligibleToVote(getVoteSiteInfo().voteSite()))
            {
                player.setIsVoting(false);
                player.sendMessage("You can't vote for us yet.");
                player.sendPacket(ActionFailed.STATIC_PACKET);
                return;
            }

            ThreadPool.execute(() -> {
                try{
                    if(!hasVoted(player)){
                        player.setIsVoting(false);
                        player.sendMessage(String.format("You haven't voted yet. Go to %s and vote for us.", getVoteSiteInfo().voteSite().getName()));
                        player.sendPacket(ActionFailed.STATIC_PACKET);
                        return;
                    }

                    long dateTimevoted = System.currentTimeMillis();

                    VotedRecord votedRecord = new VotedRecord(player.getAccountName(), player.getIpAddress(), dateTimevoted, getVoteSiteInfo().voteSite().getName());

                    VoteDao.addVotedRecord(votedRecord);
                    player.setLastVotedTimestamp(getVoteSiteInfo().voteSite(), dateTimevoted);
                    reward(player);
                    
                    //Custom modified vote system
                    player.setVotesToSites(1); 					//increase by 1 the vote Sites that player voted
                    VoteDao.addVoteBuff(player,dateTimevoted);  //Insert player Data to DB
                    player.addHwidVotes(player.gethwid(),1); 	//save them to player Data for future usage
                    

                    player.setIsVoting(false);
                }catch (Exception e){
                    handleExceptionForVoteAttempt(player, e);
                }

            });

        }catch (Exception e){
            handleExceptionForVoteAttempt(player, e);
        }
    }

    protected abstract boolean hasVoted(Player player);

    protected void reward(Player player){
        player.sendMessage("Thanks for voting! Here's your reward.");

        player.addItem("Vote reward.", CustomConfig.VOTE_MANAGER_REWARD_ID, CustomConfig.VOTE_MANAGER_REWARD_AMOUNT,player, true);

    }

    private VoteSiteInfo getVoteSiteInfo() {
        return getClass().getAnnotation(VoteSiteInfo.class);
    }

    String getApiKey(){
        return getVoteSiteInfo().apiKey().getName();
    }

    private static void handleExceptionForVoteAttempt(Player player, Exception e) {
        player.setIsVoting(false);
        player.sendPacket(ActionFailed.STATIC_PACKET);
        LOGGER.log(Level.WARNING, "There was an error during a vote attempt", e);
    }

    @Override
    public String toString() {
        return getClass().getAnnotation(VoteSiteInfo.class).voteSite().getName();
    }
}