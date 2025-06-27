package Customs.VIP;


public class RewardHolder
{
    private int _id;
    private int _mincount;
    private int _count;
    private int _chance;
    
    public RewardHolder(final int rewardId, final int rewardCount) {
        this._id = rewardId;
        this._count = rewardCount;
        this._chance = 100;
        _mincount = -1;
    }
    
    public RewardHolder(final int rewardId, final int rewardCount, final int rewardChance) {
        this._id = rewardId;
        this._count = rewardCount;
        this._chance = rewardChance;
        _mincount = -1;
    }
    
    public RewardHolder(final int rewardId,final int minCount, final int rewardCount, final int rewardChance) {
        this._id = rewardId;
        this._mincount = minCount;
        this._count = rewardCount;
        this._chance = rewardChance;
    }
    
    public int getRewardId() {
        return this._id;
    }
    
    public int getRewardMinCount() {
        return this._mincount;
    }
    
    public int getRewardCount() {
        return this._count;
    }
    
    public int getRewardChance() {
        return this._chance;
    }
    
    public void setId(final int id) {
        this._id = id;
    }
    
    public void setminCount(final int count) {
        this._mincount = count;
    }
    
    public void setCount(final int count) {
        this._count = count;
    }
    
    public void setChance(final int chance) {
        this._chance = chance;
    }
}
