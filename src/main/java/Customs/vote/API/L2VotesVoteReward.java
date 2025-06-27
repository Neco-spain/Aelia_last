package Customs.vote.API;

import net.sf.l2j.gameserver.model.actor.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@VoteSiteInfo(voteSite = VoteSite.L2VOTE, apiKey = ApiData.L2VOTE)
public class L2VotesVoteReward extends VoteRewardSite {

    @Override
    protected String getEndpoint(Player player) {
        return String.format("https://l2votes.com/api.php?apiKey=%s&ip=%s", getApiKey() ,player.getIpAddress());
    }

    @Override
    protected boolean hasVoted(Player player) {
        String serverResponse = VoteApiService.getApiResponse(getEndpoint(player));
        if(serverResponse.length() == 0){
            player.sendMessage("Something went wrong with this request. Report it to the administrator.");
            return false;
        }
        JSONArray topObject;
		try
		{
			topObject = new JSONArray(serverResponse);
			JSONObject a = topObject.getJSONObject(0);
			int votedObject = a.getInt("status");

	        if(votedObject == 1)
	        	return true;

		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        return false;

    }
}
