package Customs.vote.API;
import net.sf.l2j.gameserver.model.actor.Player;

@VoteSiteInfo(voteSite = VoteSite.TOPCO, apiKey = ApiData.TOPCO)
public class TopcoVoteReward extends VoteRewardSite {

    @Override
    protected String getEndpoint(Player player) {
        return String.format("https://l2top.co/reward/VoteCheck.php?id=%s&ip=%s",  getApiKey() , player.getIpAddress());
    }

    @Override
    protected boolean hasVoted(Player player) {
        String serverResponse = VoteApiService.getApiResponse(getEndpoint(player));
        if(serverResponse.length() == 0){
            player.sendMessage("Something went wrong with this request. Report it to the administrator.");
            return false;
        }

        
        try{
        	if((serverResponse.trim().equals("TRUE")))
        		return true;
        }catch (Exception e){
            player.sendMessage("Something went wrong with this request. Report it to the administrator!");
            return false;
        }
        
        return false;

    }
}