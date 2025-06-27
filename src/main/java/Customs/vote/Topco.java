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
import java.net.URL;
import java.util.Map;

import Custom.CustomConfig;
import Customs.vote.data.VoteSystem;

/**
 * @author @IcathiaLord
 *
 */
public class Topco extends VoteSystem
{
       public Topco(int votesDiff, boolean allowReport, int boxes, Map<Integer, Integer> rewards, int checkMins)
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

   		URL url = null;
   		InputStreamReader isr = null;
   		try
   		{
   			url = new URL(getEndpoint());
   			isr = new InputStreamReader(url.openStream());

   			BufferedReader br = new BufferedReader(isr);
   			String strLine;
   			while((strLine = br.readLine()) != null) // Read File Line By Line
   			{
  				count = Integer.parseInt(strLine);
   			}

   			isr.close(); // Close the input stream
   		}
   		catch(Exception e) // Catch exception if any
   		{
   			//_log.error("VoteRead: ERROR: ", e);
   			return -1;
   		}
           
           
              return count;
       }
       
       @Override
       public String getSiteName()
       {
               return "TopCo";
       }
       
       protected String getEndpoint() {
           return String.format(CustomConfig.TOPCO_SERVER_LINK);
       }
       
}