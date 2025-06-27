package Customs.mods.fakepc;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.commons.data.xml.IXmlReader;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class FakePcsTable implements IXmlReader
{
   private static Logger _log = Logger.getLogger(FakePcsTable.class.getName());
   private Map<Integer, FakePc> _fakePcs;
  
   public static FakePcsTable getInstance()
   {
       return SingletonHolder._instance;
   }
  
   protected FakePcsTable()
   {
       _fakePcs = new HashMap<>();
       load();
   }
  
   @Override
   public void load()
   {
	   parseFile("./data/xml/fake_pcs.xml");
   }
   
   public void reload()
   {
       _fakePcs.clear();
       load();
   }
  
   @Override
public void parseDocument(Document doc, Path path)
   {
	   try
		{
			Node n = doc.getFirstChild();
			for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
			{
				if (d.getNodeName().equalsIgnoreCase("npc"))
				{
					FakePc fpc = new FakePc();
					
					int npcId = Integer.valueOf(d.getAttributes().getNamedItem("id").getNodeValue());
					
					for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling())
					{
						if ("appearance".equalsIgnoreCase(cd.getNodeName()))
						{
							fpc.name = cd.getAttributes().getNamedItem("name").getNodeValue();
							fpc.nameColor = Integer.decode("0x" + cd.getAttributes().getNamedItem("name_color").getNodeValue());
							fpc.title = cd.getAttributes().getNamedItem("title").getNodeValue();
							fpc.titleColor = Integer.decode("0x" + cd.getAttributes().getNamedItem("title_color").getNodeValue());
							fpc.radius = Integer.valueOf(cd.getAttributes().getNamedItem("radius").getNodeValue());
							fpc.height = Integer.valueOf(cd.getAttributes().getNamedItem("height").getNodeValue());
							fpc.race = Integer.valueOf(cd.getAttributes().getNamedItem("race").getNodeValue());
							fpc.sex = Integer.valueOf(cd.getAttributes().getNamedItem("sex").getNodeValue());
							fpc.classId = Integer.valueOf(cd.getAttributes().getNamedItem("class").getNodeValue());
							fpc.hairStyle = Integer.valueOf(cd.getAttributes().getNamedItem("hair_style").getNodeValue());
							fpc.hairColor = Integer.valueOf(cd.getAttributes().getNamedItem("hair_color").getNodeValue());
							fpc.face = Integer.valueOf(cd.getAttributes().getNamedItem("face").getNodeValue());
							fpc.hero = Byte.parseByte(cd.getAttributes().getNamedItem("hero").getNodeValue());
							fpc.enchant = Integer.valueOf(cd.getAttributes().getNamedItem("enchant").getNodeValue());
						}
						else if ("items".equalsIgnoreCase(cd.getNodeName()))
						{
							fpc.rightHand = Integer.valueOf(cd.getAttributes().getNamedItem("right_hand").getNodeValue());
							fpc.leftHand = Integer.valueOf(cd.getAttributes().getNamedItem("left_hand").getNodeValue());
							fpc.chest = Integer.valueOf(cd.getAttributes().getNamedItem("chest").getNodeValue());
							fpc.legs = Integer.valueOf(cd.getAttributes().getNamedItem("legs").getNodeValue());
							fpc.gloves = Integer.valueOf(cd.getAttributes().getNamedItem("gloves").getNodeValue());
							fpc.feet = Integer.valueOf(cd.getAttributes().getNamedItem("feet").getNodeValue());
							fpc.hair = Integer.valueOf(cd.getAttributes().getNamedItem("hair").getNodeValue());
							fpc.hair2 = Integer.valueOf(cd.getAttributes().getNamedItem("hair2").getNodeValue());
						}
						else if ("clan".equalsIgnoreCase(cd.getNodeName()))
						{
							fpc.clanId = Integer.valueOf(cd.getAttributes().getNamedItem("clan_id").getNodeValue());
							fpc.clanCrest = Integer.valueOf(cd.getAttributes().getNamedItem("clan_crest").getNodeValue());
							fpc.allyId = Integer.valueOf(cd.getAttributes().getNamedItem("ally_id").getNodeValue());
							fpc.allyCrest = Integer.valueOf(cd.getAttributes().getNamedItem("ally_crest").getNodeValue());
							fpc.pledge = Integer.valueOf(cd.getAttributes().getNamedItem("pledge").getNodeValue());
						}
						
					}
					
					_fakePcs.put(npcId, fpc);
               }
           }
       }
       catch (Exception e)
       {
           _log.log(Level.WARNING, "FakePcsTable: Error loading from database:" + e.getMessage(), e);
       }
      
       _log.info("-FakePcsTable: Loaded " + _fakePcs.size() + " NPC to PC templates.");
   }
  
   public FakePc getFakePc(int npcId)
   {
       return _fakePcs.get(npcId);
   }
  
   private static class SingletonHolder
   {
       protected static final FakePcsTable _instance = new FakePcsTable();
   }
}