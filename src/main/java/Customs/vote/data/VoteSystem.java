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
package Customs.vote.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Customs.vote.*;
import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;

import Custom.CustomConfig;

/**
 * @author Anarchy
 *
 */
public abstract class VoteSystem implements Runnable
{
    private static List<VoteSystem> voteSystems = new ArrayList<>();

    protected int votesDiff;
    protected boolean allowReport;
    protected int boxes;
    protected Map<Integer, Integer> rewards;
    protected int checkMins;
    protected int lastVotes = 0;
    private Map<String, Integer> playerHWID = new HashMap<>();
    private static Map<Integer, Integer> re = new HashMap<>();


    public static void initialize()
    {
        if (CustomConfig.ALLOW_TOPZONE_VOTE_REWARD)
            voteSystems.add(new TopzoneNew(CustomConfig.TOPZONE_VOTES_DIFFERENCE, CustomConfig.ALLOW_TOPZONE_GAME_SERVER_REPORT, CustomConfig.TOPZONE_DUALBOXES_ALLOWED, CustomConfig.TOPZONE_REWARD, CustomConfig.TOPZONE_REWARD_CHECK_TIME));

        if (CustomConfig.ALLOW_HOPZONE_VOTE_REWARD)
            voteSystems.add(new Hopzone(CustomConfig.HOPZONE_VOTES_DIFFERENCE, CustomConfig.ALLOW_HOPZONE_GAME_SERVER_REPORT, CustomConfig.HOPZONE_DUALBOXES_ALLOWED, CustomConfig.HOPZONE_REWARD, CustomConfig.HOPZONE_REWARD_CHECK_TIME));

        if (CustomConfig.ALLOW_NETWORK_VOTE_REWARD)
            voteSystems.add(new Network(CustomConfig.NETWORK_VOTES_DIFFERENCE, CustomConfig.ALLOW_NETWORK_GAME_SERVER_REPORT, CustomConfig.NETWORK_DUALBOXES_ALLOWED, CustomConfig.NETWORK_REWARD, CustomConfig.NETWORK_REWARD_CHECK_TIME));

        if (CustomConfig.ALLOW_TOPCO_VOTE_REWARD)
            voteSystems.add(new Topco(CustomConfig.TOPCO_VOTES_DIFFERENCE, CustomConfig.ALLOW_TOPCO_GAME_SERVER_REPORT, CustomConfig.TOPCO_DUALBOXES_ALLOWED, CustomConfig.TOPCO_REWARD, CustomConfig.TOPCO_REWARD_CHECK_TIME));

        if (CustomConfig.ALLOW_MMO_VOTE_REWARD)
            voteSystems.add(new MMOtop(CustomConfig.MMO_VOTES_DIFFERENCE, CustomConfig.ALLOW_MMO_GAME_SERVER_REPORT, CustomConfig.MMO_DUALBOXES_ALLOWED, CustomConfig.MMO_REWARD, CustomConfig.MMO_REWARD_CHECK_TIME));


    }

    public static VoteSystem getVoteSystem(String name)
    {
        for (VoteSystem vs : voteSystems)
            if (vs.getSiteName().equals(name))
                return vs;

        return null;
    }

    public VoteSystem(int votesDiff, boolean allowReport, int boxes, Map<Integer, Integer> rewards, int checkMins)
    {
        this.votesDiff = votesDiff;
        this.allowReport = allowReport;
        this.boxes = boxes;
        this.rewards = rewards;
        this.checkMins = checkMins;

        re.put(6393, 5);
        ThreadPool.scheduleAtFixedRate(this, checkMins*1000*60, checkMins*1000*60);

    }

    protected void reward()
    {
        int currentVotes = getVotes();

        if (currentVotes == -1)
        {
            System.out.println("There was a problem on getting server votes.");
            return;
        }

        if (lastVotes == 0)
        {
            lastVotes = currentVotes;

            World.announceToOnlinePlayers(getSiteName()+":"  + " Current vote count is " +currentVotes+".",true);
            World.announceToOnlinePlayers(getSiteName()+":" + " We need " +((lastVotes+votesDiff)-currentVotes)+ " vote(s) for reward.",true);
            if (allowReport)
            {
                System.out.println("Server votes on "+getSiteName()+": "+currentVotes);
                System.out.println("Votes needed for reward: "+((lastVotes+votesDiff)-currentVotes));
            }
            return;
        }

        if (currentVotes >= lastVotes+votesDiff)
        {

            if (allowReport)
            {
                System.out.println("Server votes on "+getSiteName()+": "+currentVotes);
                System.out.println("Votes needed for next reward: "+((currentVotes+votesDiff)-currentVotes));
            }

            World.announceToOnlinePlayers(getSiteName() + ":"+ " Everyone has been rewarded.",true);
            World.announceToOnlinePlayers(getSiteName() + ":"+ " Current vote count is " +currentVotes + ".",true);
            World.announceToOnlinePlayers(getSiteName() + ":"+ " We need " +votesDiff+" vote(s) for next reward.",true);


            rewardPlayers();

            lastVotes = currentVotes;
        }
        else
        {
            if (allowReport)
            {
                System.out.println("Server votes on "+getSiteName()+": "+currentVotes);
                System.out.println("Votes needed for next reward: "+((lastVotes+votesDiff)-currentVotes));
            }


            World.announceToOnlinePlayers(getSiteName()+":" + " Current vote count is " + +currentVotes+".",true);
            World.announceToOnlinePlayers(getSiteName()+":" + " We need " + +((lastVotes+votesDiff)-currentVotes)+ " vote(s) for reward.",true);

        }
    }

    public void rewardPlayers(){
        Collection<Player> pls = World.getInstance().getPlayers();
        for(Player p : pls){
            if (p.getClient() == null || p.getClient().isDetached()) // offline shops protection
                continue;



            boolean canReward = false;
            String hwid = p.gethwid();
            if(playerHWID.containsKey(hwid)){
                int count = playerHWID.get(hwid);
                if (count < boxes){
                    playerHWID.remove(hwid);
                    playerHWID.put(hwid, count+1);
                    canReward = true;
                }
            }
            else{
                canReward = true;
                playerHWID.put(hwid, 1);
            }
            if (canReward) {
                for (int i : rewards.keySet()) {
                    if(p.isVip())
                        p.addItem("Vote reward.", i, (rewards.get(i) * 2), p, true);  //Custom x2 mass vote reward for VIP
                    else
                        p.addItem("Vote reward.", i, rewards.get(i), p, true);
                }
            }
            else
                p.sendMessage("Already "+boxes+" character(s) of your ip have been rewarded, so this character won't be rewarded!");

        }
        playerHWID.clear();
    }

    public String getResponse(String url, String userAgent) {
        String response = "";
        try {
            final URL obj = new URL(url);
            final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // add request header
            con.setConnectTimeout(1000);
            con.addRequestProperty("User-Agent", userAgent);

            final int responseCode = con.getResponseCode();
            if (responseCode == 200) // OK
            {
                final StringBuilder sb = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                }
                response = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public abstract int getVotes();
    public abstract String getSiteName();

}