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
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import Custom.CustomConfig;
import Customs.vote.data.VoteSystem;


/**
 * @author Anarchy
 *
 */
public class Network extends VoteSystem
{
	private String API_URL = "https://l2network.eu/api.php";

	private String API_KEY = CustomConfig.NETWORK_SERVER_API_KEY;

       public Network(int votesDiff, boolean allowReport, int boxes, Map<Integer, Integer> rewards, int checkMins)
       {
               super(votesDiff, allowReport, boxes, rewards, checkMins);
       }
      
       @Override
       public void run()
       {
               reward();
       }

       @SuppressWarnings("resource")
	@Override
       public int getVotes()
       {
    	   try{
			String postParameters = "apiKey=" + API_KEY;
			postParameters += "&type=" + 1;

			byte[] postData = postParameters.getBytes(Charset.forName("UTF-8"));
			
			URL url = new URL(API_URL);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36"); // so cloudflare doesn't 403 us
			conn.setDoOutput(true);
			
			DataOutputStream os = new DataOutputStream(conn.getOutputStream());
			os.write(postData);
			os.flush();
			os.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			

			return Integer.parseInt(response.toString());
			
               }
               catch (Exception e)
               {
                       e.printStackTrace();
                       System.out.println("Error while getting server vote count from "+getSiteName()+".");
               }
              
               return -1;
       }

       @Override
       public String getSiteName()
       {
               return "Network";
       }
}