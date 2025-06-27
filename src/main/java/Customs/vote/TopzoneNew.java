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

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import Custom.CustomConfig;
import Customs.vote.data.VoteSystem;
import Customs.vote.data.Topzone.TopZoneGlobalResponse;

/**
 * @author Anarchy
 *
 */
public class TopzoneNew extends VoteSystem
{
       public TopzoneNew(int votesDiff, boolean allowReport, int boxes, Map<Integer, Integer> rewards, int checkMins)
       {
               super(votesDiff, allowReport, boxes, rewards, checkMins);
       }
      
       @Override
       public void run()
       {
               reward();
       }

       @Override
       public int getVotes()
       {
		   		String url = getApiUrlTemplateTotal();
		   		String response = getResponse(url, "L2TopZone");
		   		int votes = -1;
		   		TopZoneGlobalResponse jsonResponse = null;
               try
               {
	       			jsonResponse = new ObjectMapper().readValue(response, TopZoneGlobalResponse.class);
	    			if (jsonResponse != null) {
	    				return votes = jsonResponse.getResult().getTotalVotes();
	    			}
               }
               catch (Exception e)
               {
                       e.printStackTrace();
                       System.out.println("TOPZONE is offline. We will check reward as it will be online again.");
               }
              
               return -1;
       }

       
  /* 	private static String getApiUrlTemplatePersonal(String ip) {
		return String.format("%svote?token=%s&ip=%s", CustomConfig.VOTE_TOPZONE_API_URL, CustomConfig.VOTE_TOP_ZONE_TOKEN, ip);
	}
*/
	private static String getApiUrlTemplateTotal() {
		return String.format("%sserver_%s/getServerData", CustomConfig.VOTE_TOPZONE_API_URL, CustomConfig.VOTE_TOP_ZONE_TOKEN);
	}
       	
       @Override
       public String getSiteName()
       {
               return "Topzone";
       }
}