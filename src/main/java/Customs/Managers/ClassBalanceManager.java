package Customs.Managers;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.commons.data.xml.IXmlReader;

//import net.sf.l2j.commons.data.xml.XMLDocument;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.enums.AttackType;
import Customs.Balance.holder.ClassBalanceHolder;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import Custom.CustomConfig;

/**
 * Class Balancer Manager for load/rewrite XML files.
 */
public class ClassBalanceManager implements IXmlReader
{
private static Logger _log = Logger.getLogger(ClassBalanceManager.class.getName());

private final Map<String, ClassBalanceHolder> _classes = new ConcurrentHashMap<>();

private int _balanceSize = 0;
private int _olyBalanceSize = 0;
private boolean _hasModify = false;

public ClassBalanceManager()
{
	load();
}

@Override
public void load()
{
	parseFile("./data/xml/balancer/classbalance/ClassBalance.xml");
	//LOGGER.info("Loaded {} zones classes and total {} zones.", _zones.size(), _zones.values().stream().mapToInt(Map::size).sum());

	
	_log.info(getClass().getSimpleName() + ": Loaded " + _classes.size() + " balanced classe(s).");
}

@Override
public void parseDocument(Document doc, Path path)
{
	for (Node o = doc.getFirstChild(); o != null; o = o.getNextSibling())
	{
		if (!"list".equalsIgnoreCase(o.getNodeName()))
			continue;
		
		for (Node d = o.getFirstChild(); d != null; d = d.getNextSibling())
		{
			if (d.getNodeName().equals("balance"))
			{
				final int classId = Integer.parseInt(d.getAttributes().getNamedItem("classId").getNodeValue());
				final int targetClassId = Integer.parseInt(d.getAttributes().getNamedItem("targetClassId").getNodeValue());
				final ClassBalanceHolder cbh = new ClassBalanceHolder(classId, targetClassId);
				
				for (Node set = d.getFirstChild(); set != null; set = set.getNextSibling())
				{
					if (set.getNodeName().equals("set"))
					{
						final double val = Double.parseDouble(set.getAttributes().getNamedItem("val").getNodeValue());
						final AttackType atkType = AttackType.valueOf(set.getAttributes().getNamedItem("type").getNodeValue());
						cbh.addNormalBalance(atkType, val);
						
						_balanceSize += 1;
					}
					else if (set.getNodeName().equals("olyset"))
					{
						final double val = Double.parseDouble(set.getAttributes().getNamedItem("val").getNodeValue());
						final AttackType atkType = AttackType.valueOf(set.getAttributes().getNamedItem("type").getNodeValue());
						cbh.addOlyBalance(atkType, val);
						
						_olyBalanceSize += 1;
					}
				}
				
				_classes.put(classId + ";" + targetClassId, cbh);
			}
		}
	}
}

public Map<String, ClassBalanceHolder> getAllBalances()
{
	final Map<String, ClassBalanceHolder> map = new TreeMap<>(new ClassComparator());
	map.putAll(_classes);
	
	return map;
}

public List<ClassBalanceHolder> getClassBalances(int classId)
{
	final List<ClassBalanceHolder> list = new ArrayList<>();
	for (Map.Entry<String, ClassBalanceHolder> data : _classes.entrySet())
	{
		if (Integer.valueOf(data.getKey().split(";")[0]).intValue() == classId)
			list.add(data.getValue());
	}
	
	return list;
}

public int getClassBalanceSize(int classId, boolean olysize)
{
	int size = 0;
	for (ClassBalanceHolder data : getClassBalances(classId))
		size += (!olysize ? data.getNormalBalance().size() : data.getOlyBalance().size());
	
	return size;
}

public ClassBalanceHolder getBalanceHolder(String key)
{
	return _classes.get(key);
}

private class ClassComparator implements Comparator<String>
{
	public ClassComparator()
	{
		
	}
	
	@Override
	public int compare(String l, String r)
	{
		final int left = Integer.valueOf(l.split(";")[0]).intValue();
		final int right = Integer.valueOf(r.split(";")[0]).intValue();
		if (left > right)
			return 1;
		
		if (left < right)
			return -1;
		
		if (Integer.valueOf(l.split(";")[1]).intValue() > Integer.valueOf(r.split(";")[1]).intValue())
			return 1;
		
		if (Integer.valueOf(r.split(";")[1]).intValue() > Integer.valueOf(l.split(";")[1]).intValue())
			return -1;
		
		Random x = new Random();
		
		return x.nextInt(2) == 1 ? 1 : 1;
	}
}

public double getBalancedClass(AttackType type, Creature attacker, Creature victim)
{
	if (CustomConfig.BALANCER_ALLOW)
	{
		if (attacker instanceof Player && victim instanceof Player)
		{
			final int classId = attacker.getActingPlayer().getClassId().getId();

			//Custom Appy to all classes
			if(_classes.containsKey(classId + ";-3"))
				return _classes.get(classId + ";-3").getBalanceValue(type);

			final int targetClassId = victim.getActingPlayer().getClassId().getId();
			if ((attacker.getActingPlayer().isInOlympiadMode()) && (victim.getActingPlayer().isInOlympiadMode()))
			{
				if (attacker.getActingPlayer().getOlympiadGameId() == victim.getActingPlayer().getOlympiadGameId())
				{
					if (_classes.containsKey(classId + ";" + targetClassId))
						return _classes.get(classId + ";" + targetClassId).getOlyBalanceValue(type);
				}
				
				return _classes.containsKey(classId + ";-2") ? _classes.get(classId + ";-2").getOlyBalanceValue(type) : 1.0D;
			}
			
			if (_classes.containsKey(classId + ";" + targetClassId))
				return _classes.get(classId + ";" + targetClassId).getBalanceValue(type);
			
			return _classes.containsKey(classId + ";-2") ? _classes.get(classId + ";-2").getBalanceValue(type) : 1.0D;
		}
		
		if (attacker instanceof Player && victim instanceof Monster)
		{
			final int classId = attacker.getActingPlayer().getClassId().getId();
			if (_classes.containsKey(classId + ";-1"))
				return _classes.get(classId + ";-1").getBalanceValue(type);
		}
	}
	
	return 1.0D;
}

public void removeClassBalance(String key, AttackType type, boolean isOly)
{
	if (_classes.containsKey(key))
	{
		if (!_hasModify)
			_hasModify = true;
		
		if (isOly)
		{
			_classes.get(key).removeOlyBalance(type);
			_olyBalanceSize -= 1;
			return;
		}
		
		_classes.get(key).remove(type);
		_balanceSize -= 1;
	}
}

public void addClassBalance(String key, ClassBalanceHolder cbh, boolean isEdit)
{
    if (!_hasModify)
    	_hasModify = true;
    
	_classes.put(key, cbh);
	
	if (!isEdit)
	{
		if (!cbh.getOlyBalance().isEmpty())
			_olyBalanceSize += 1;
		else
			_balanceSize += 1;
	}
}

public void store(Player player)
{
	if (!_hasModify)
	{
		if (player != null)
			player.sendMessage("ClassBalanceManager: Nothing for saving!");
		
		return;
	}
	
	try
	{
		File file = new File("./data/xml/balancer/classbalance/ClassBalance.xml");
		if (file.exists())
		{
			if (!file.renameTo(new File("./data/xml/balancer/classbalance/ClassBalance_Backup_[" + new SimpleDateFormat("YYYY-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTimeInMillis()) + "].xml")))
			{
				if (player != null)
					player.sendMessage("ClassBalanceManager: can't save backup file!");
			}
		}
		
		file = new File("./data/xml/balancer/classbalance/ClassBalance.xml");
		file.createNewFile();
		
		final FileWriter fstream = new FileWriter(file);
		final BufferedWriter out = new BufferedWriter(fstream);
		
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		out.write("<list>\n");
		
		for (ClassBalanceHolder cbh : _classes.values())
		{
			if (!cbh.getNormalBalance().isEmpty() || !cbh.getOlyBalance().isEmpty())
			{
				String xml = "	<balance classId=\"" + cbh.getActiveClass() + "\" targetClassId=\"" + cbh.getTargetClass() + "\">\n";
				
				for (Map.Entry<AttackType, Double> info : cbh.getNormalBalance().entrySet())
					xml += "		<set type=\"" + info.getKey().toString() + "\" val=\"" + info.getValue() + "\"/>\n";
				
				for (Map.Entry<AttackType, Double> info : cbh.getOlyBalance().entrySet())
					xml += "		<olyset type=\"" + info.getKey().toString() + "\" val=\"" + info.getValue() + "\"/>\n";
				
				xml = xml + "	</balance>\n";
				
				out.write(xml);
			}
		}
		
		out.write("</list>");
		out.close();
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	
	if (player != null)
		player.sendMessage("ClassBalanceManager: Modified data was saved!");
	
	_hasModify = false;
}

public static final ClassBalanceManager getInstance()
{
	return SingletonHolder._instance;
}

public int getSize(boolean olysize)
{
	return olysize ? _olyBalanceSize : _balanceSize;
}

private static class SingletonHolder
{
	protected static final ClassBalanceManager _instance = new ClassBalanceManager();
}
}