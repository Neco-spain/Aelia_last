package Customs.mods;

import java.util.Map;

import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.effects.EffectTemplate;

public class CustomCancelTask implements Runnable
{
	private Player _player = null;
	private Map<L2Skill, int[]> _buffs = null;
	
	public CustomCancelTask(Player _player, Map<L2Skill, int[]> cancelledBuffs)
	{
		this._player = _player;
		this._buffs = cancelledBuffs;
	}
	
	@Override
	public void run()
	{
		if (_player == null || !_player.isOnline() /*|| _player.isPlayingOlympiad()*/)
			return;

		for (L2Skill s : _buffs.keySet()){
			if (s == null)
				continue;

			 L2Effect ef;
			 for (EffectTemplate et : s.getEffectTemplates())
			 {
				ef = et.getEffect(_player,_player,s);
				 if (ef != null)
				 {
					 ef.setCount(_buffs.get(s)[0]);
					 ef.setFirstTime(_buffs.get(s)[1]);
					 ef.scheduleEffect();
				 }
			 }
		}
	}
}
