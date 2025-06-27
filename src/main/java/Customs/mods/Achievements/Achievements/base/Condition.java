package Customs.mods.Achievements.Achievements.base;

import net.sf.l2j.gameserver.model.actor.Player;

/**
 * @author Matim
 * @version 1.0
 */
public abstract class Condition
{
    private Object _value;
    private String _name;

    public Condition(Object value)
    {
       _value = value;
    }

    public Object getValue()
    {
       return _value;
    }

    public void setName(String s)
    {
           _name = s;
    }
    public String getName()
    {
           return _name;
    }
    public abstract boolean meetConditionRequirements(Player player);
    public abstract String getStatus(Player player);

}