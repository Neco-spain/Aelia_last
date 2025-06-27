package Customs.vote.API;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import net.sf.l2j.gameserver.model.actor.Player;


@VoteSiteInfo(voteSite = VoteSite.L2JBRASIL, apiKey = ApiData.L2JBRASIL)
public class l2jbrasilVoteReward extends VoteRewardSite {

    @Override
    protected String getEndpoint(Player player) {
        return String.format("https://top.l2jbrasil.com/votesystem/index.php?username=%s&ip=%s&type=json", getApiKey(), player.getIP());
    }	

    @Override
    protected boolean hasVoted(Player player) {
        String serverResponse = VoteApiService.getApiResponse(getEndpoint(player));

        if(serverResponse.length() == 0){
            player.sendMessage("Something went wrong with this request. Report it to the administrator.");
            return false;
        }

        JsonElement jelement = new JsonParser().parse(serverResponse);
        JsonObject topObject = jelement.getAsJsonObject();

        JsonObject votedObject = topObject.getAsJsonObject("vote");
        JsonPrimitive isVotedObject = votedObject.getAsJsonPrimitive("status");

        if(isVotedObject.getAsString().equals("1"))
        	return true;
        
        return false;
        
    }
}