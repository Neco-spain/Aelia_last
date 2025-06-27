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
import org.json.JSONObject;

import Customs.vote.data.VoteSystem;

/**
 * @author Anarchy
 *
 */

public class Hopzone extends VoteSystem
{
	int resp;
	private String response ="";
	private String url="";
	private JSONObject rs;
       public Hopzone(int votesDiff, boolean allowReport, int boxes, Map<Integer, Integer> rewards, int checkMins)
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
    	   	int count = -1;
   			
    	   	try{
    	   		url = getEndpoint();
    	   		response = getResponse(url);
    	   		rs = new JSONObject(response);
    	   		if(rs != null) {
    	   				count = rs.getInt("totalvotes");
    	   		}
    	   	}
    	   	catch(Exception e){
    	   		e.printStackTrace();
    	   	}
            return count;
       }
       
       public String getResponse(String url){
    	   try
    	  {
    		   final URL obj = new URL(url);
    		   final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    		   
      			con.setRequestMethod("GET");
       			con.setConnectTimeout(4000);
       			con.setRequestProperty("User-Agent", "L2Hopzone");
       			resp = con.getResponseCode();
       			
       			if(resp == 200) {
       				StringBuffer sb = new StringBuffer();
       				try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))){
       					String line ;
       					while ((line= in.readLine()) != null) {
       						sb.append(line);
       					}
       					in.close();
       				}
				response = sb.toString();
       			}
    	  }
    	   catch(Exception e){
    		   e.printStackTrace();
    		   System.out.println("Error while getting server vote count from "+getSiteName()+".");
    	   }
    	   
		return response;
       }
       
       
     protected String getEndpoint() {
         return String.format("https://api.hopzone.net/lineage2/votes?token=" + CustomConfig.HOPZONE_SERVER_API_KEY);
     }
     @Override
     public String getSiteName()
     {
    	 return "Hopzone";
     }

}
