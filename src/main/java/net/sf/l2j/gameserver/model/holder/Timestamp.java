package net.sf.l2j.gameserver.model.holder;

import net.sf.l2j.gameserver.model.L2Skill;

/**
 * A class extending {@link IntIntHolder} containing all neccessary information to maintain valid timestamps and reuse for skills upon relog.
 */
public final class Timestamp extends IntIntHolder
{
	private final long _reuse;
	private final long _stamp;
	//custom
	private final int _skillId;
	private final int _skillLvl;
	
	public Timestamp(L2Skill skill, long reuse)
	{
		super(skill.getId(), skill.getLevel());
		
		_skillId = skill.getId();
		_skillLvl = skill.getLevel();
		
		_reuse = reuse;
		_stamp = System.currentTimeMillis() + reuse;
	}
	
	public Timestamp(L2Skill skill, long reuse, long systime)
	{
		super(skill.getId(), skill.getLevel());
		
		_skillId = skill.getId();
		_skillLvl = skill.getLevel();
		
		_reuse = reuse;
		_stamp = systime;
	}
	
	public long getStamp()
	{
		return _stamp;
	}
	
	public long getReuse()
	{
		return _reuse;
	}
	
	public long getRemaining()
	{
		return Math.max(_stamp - System.currentTimeMillis(), 0);
	}
	
	public boolean hasNotPassed()
	{
		return System.currentTimeMillis() < _stamp;
	}
	
	//custom
	
	public int getSkillId()
	{
		return _skillId;
	}
	
	public int getSkillLvl()
	{
		return _skillLvl;
	}
	
}