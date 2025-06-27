/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package Customs.vote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import Custom.CustomConfig;
import Customs.vote.data.JSON;
import Customs.vote.data.VoteSystem;

/**
 * @author @IcathiaLord
 *
 */
public class MMOtop extends VoteSystem
{
	public MMOtop(int votesDiff, boolean allowReport, int boxes, Map<Integer, Integer> rewards, int checkMins)
	{
		super(votesDiff, allowReport, boxes, rewards, checkMins);
	}

	@Override
	public void run()
	{
		reward();
	}

	@Override
	public int getVotes(){
		int votes = -1;

		votes = no();

		return votes;
	}

	public int no()
	{
		int votes = -1;
		try
		{
			final URL obj = new URL("https://mmotop.eu/l2/info/" + 242 + "/");
			final HttpURLConnection con = (HttpURLConnection) obj.openConnection();


			con.addRequestProperty("User-Agent", "MMOTOP_EU VoteManager v1");

			con.setConnectTimeout(5000);

			final int responseCode = con.getResponseCode();
			if (responseCode == 200)
			{
				try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
				{
					String inputLine;
					while ((inputLine = in.readLine()) != null)
					{
						if (inputLine.contains("<td style=\"color: #337ab7;\">"))
						{
							votes = Integer.valueOf(inputLine.split(";>")[0].replace("</span>", ""));
							break;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Server MMO-TOP is offline Trying to Reconnect");
		}

		return votes;
	}
	public int API()
	{
		int votes = -1;
		try
		{
			final URL obj = new URL("https://mmotop.eu/l2/data/" + CustomConfig.MMOTOP_API_KEY + "/info/");
			final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.addRequestProperty("User-Agent", "MMOTOP_EU VoteManager v1");
			con.setConnectTimeout(5000);

			int responseCode = con.getResponseCode();
			if (responseCode == 200)
			{
				try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
				{
					StringBuilder sb = new StringBuilder();
					String text = null;
					while((text = in.readLine()) != null)
						sb.append(text);

					JSON json = new JSON(sb.toString());
					if(json.getInteger("error") == 0)
						votes = json.getInteger("total_votes");
					else if(json.getString("description") != null)
						throw new Exception(json.getString("description"));
				}
			}
			else throw new Exception("Server returned unexpected answer!");
		}
		catch (Exception e)
		{
			System.out.println("[MMOTOP.EU] Failed to get votes count! " + e.getMessage());
		}
		return votes;
	}

	@Override
	public String getSiteName()
	{
		return "MMOTOP";
	}

	protected String getEndpoint() {
		return String.format(CustomConfig.MMOTOP_API_KEY);
	}

}