package Customs.vote.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.l2j.gameserver.model.actor.Player;

@VoteSiteInfo(voteSite = VoteSite.NETWORK, apiKey = ApiData.NETWORK)
public class NetworkVoteReward extends VoteRewardSite {

    @Override
    protected String getEndpoint(Player player) {
        return String.format("https://l2network.eu/index.php?a=in&u=%s&ipc=%s", getApiKey(), player.getIpAddress());
    }

    @Override
    protected boolean hasVoted(Player player) {
    	final String NETWORK_API_URL = "https://l2network.eu/index.php?a=in&u=%s&ipc=%s";

		try
		{
			final URL obj = new URL(String.format(NETWORK_API_URL, getApiKey(), player.getClient().getConnection().getInetAddress().getHostAddress()));
			final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestProperty("User-Agent", "L2Network");
			con.setConnectTimeout(5000);
			
			final int responseCode = con.getResponseCode();
			if (responseCode == 200)
			{
				final StringBuilder sb = new StringBuilder();
				try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
				{
					String inputLine;
					while ((inputLine = in.readLine()) != null)
					{
						sb.append(inputLine);
					}
				}
				return sb.toString().equals("1");
			}
		}
		catch (Exception e)
		{
			System.out.println("There was a problem on getting vote status on NetWork for player: "+player.getName());
		}
		return false;
    }

}