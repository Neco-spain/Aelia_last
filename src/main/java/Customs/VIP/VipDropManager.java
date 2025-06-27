package Customs.VIP;


import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

import Custom.CustomConfig;
public class VipDropManager
{
    protected static Logger _log;
    private static boolean _canReward;
    private static HashMap<String, Integer> _playerHwids;

    protected VipDropManager() {
        _log = Logger.getLogger(VipDropManager.class.getName());
        _canReward = false;
        _playerHwids = new HashMap<>();
    }
    
    public static final void addReward(final Creature killer, final Attackable attackable) {
        if (killer instanceof Playable) {
            final Player player = killer.getActingPlayer();
            if (player.isInParty()) {
                final List<Player> party = player.getParty().getMembers();
                for (final Player member : party) {
                    final String pHwid = member.gethwid();
                    
                    if(!member.isVip())
                    	continue;

                    if (!_playerHwids.containsKey(pHwid)) {
                        _playerHwids.put(pHwid, 1);
                        _canReward = true;
                    }
                    else {
                        final int count = _playerHwids.get(pHwid);
                        if (count < 1) {
                            _playerHwids.remove(pHwid);
                            _playerHwids.put(pHwid, count + 1);
                            _canReward = true;
                        }
                        else {
                            member.sendMessage("Already 1 member of your PC have been rewarded, so this character won't be rewarded.");
                            _canReward = false;
                        }
                    }
                    if (_canReward) {
                   		 if (member.isInsideRadius(attackable.getX(), attackable.getY(), attackable.getZ(), 1000, false, false))
                             RandomReward(member,attackable);
                         else
                             member.sendMessage("You are too far from your party to be rewarded.");
                    }
                }
                _playerHwids.clear();
            }
            else {
                RandomReward(player,attackable);
            }
        }
    }
    
    public static void RandomReward(final Player player , Attackable attackable) {
    	if(!player.isVip())
    		return;

        if(attackable.isRaidBoss() && CustomConfig.VIP_RAID_IDS.contains(attackable.getNpcId())) {
            for (final RewardHolder reward : CustomConfig.VIP_RAID_REWARDS) {
                if (Rnd.get(100) <= reward.getRewardChance()) {
                    int totalReward;
                    if(reward.getRewardMinCount() != -1) {
                        totalReward = Rnd.get(reward.getRewardMinCount(), reward.getRewardCount());
                        player.getInventory().addItem("VIP Reward", reward.getRewardId(), totalReward , player, null);
                    }
                    else {
                        totalReward = reward.getRewardCount();
                        player.getInventory().addItem("VIP Reward", reward.getRewardId(),  reward.getRewardCount() /* * CustomConfig.VIP_DROP_RATE */ , player, null);
                    }
                    if (reward.getRewardCount() > 1) {
                        player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S).addItemName(reward.getRewardId()).addItemNumber(totalReward));
                    }
                    else {
                        player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1).addItemName(reward.getRewardId()));
                    }
                }
            }
        }
        else if (CustomConfig.VIP_PARTY_IDS.contains(attackable.getNpcId())) { //party
            for (final RewardHolder reward : CustomConfig.VIP_PARTY_REWARDS) {
                if (Rnd.get(100) <= reward.getRewardChance()) {
                    int totalReward;
                    if(reward.getRewardMinCount() != -1) {
                        totalReward = Rnd.get(reward.getRewardMinCount(), reward.getRewardCount());
                        player.getInventory().addItem("VIP Reward", reward.getRewardId(), totalReward , player, null);
                    }
                    else {
                        totalReward = reward.getRewardCount();
                        player.getInventory().addItem("VIP Reward", reward.getRewardId(),  reward.getRewardCount() /* * CustomConfig.VIP_DROP_RATE */ , player, null);
                    }
                    if (reward.getRewardCount() > 1) {
                        player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S).addItemName(reward.getRewardId()).addItemNumber(totalReward));
                    }
                    else {
                        player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1).addItemName(reward.getRewardId()));
                    }
                }
            }
        }
        else if(CustomConfig.VIP_MONSTERS_ID.contains(attackable.getNpcId())) { //mobs
            for (final RewardHolder reward : CustomConfig.VIP_MONSTER_REWARDS) {
                if (Rnd.get(100) <= reward.getRewardChance()) {
                    int totalReward;
                    if(reward.getRewardMinCount() != -1) {
                        totalReward = Rnd.get(reward.getRewardMinCount(), reward.getRewardCount());
                        player.getInventory().addItem("VIP Reward", reward.getRewardId(), totalReward , player, null);
                    }
                    else {
                        totalReward = reward.getRewardCount();
                        player.getInventory().addItem("VIP Reward", reward.getRewardId(),  reward.getRewardCount() /* * CustomConfig.VIP_DROP_RATE */ , player, null);
                    }
                    if (reward.getRewardCount() > 1) {
                        player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S).addItemName(reward.getRewardId()).addItemNumber(totalReward));
                    }
                    else {
                        player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1).addItemName(reward.getRewardId()));
                    }
                }
            }
        }
    }

    private static class SingletonHolder
    {
      protected static final VipDropManager INSTANCE = new VipDropManager();
    }
    public static final VipDropManager getInstance() {
        return SingletonHolder.INSTANCE;
    }
    
}
