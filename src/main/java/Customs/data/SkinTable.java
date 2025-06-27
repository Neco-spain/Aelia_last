package Customs.data;



import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.commons.data.xml.IXmlReader;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import Custom.CustomConfig;

public class SkinTable implements IXmlReader
{
  private static Logger _log = Logger.getLogger(SkinTable.class.getName());
  private static final Map<Integer, DressMeData> _map = new HashMap<>();
  
  public static SkinTable getInstance()
  {
    return SingletonHolder._instance;
  }
  
  @Override
  public void load() {
      parseFile("./data/xml/skins.xml");
  }
  
  @Override
  public void parseDocument(Document doc, Path path)
  {
    try
    {
      for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
        if ("list".equalsIgnoreCase(n.getNodeName())) {
          for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
            if ("skin".equalsIgnoreCase(d.getNodeName()))
            {
              NamedNodeMap attrs = d.getAttributes();
              
              int id = Integer.valueOf(attrs.getNamedItem("id").getNodeValue()).intValue();
              int hairId = Integer.valueOf(attrs.getNamedItem("hairId").getNodeValue()).intValue();
              int chestId = Integer.valueOf(attrs.getNamedItem("chestId").getNodeValue()).intValue();
              int legsId = Integer.valueOf(attrs.getNamedItem("legsId").getNodeValue()).intValue();
              int glovesId = Integer.valueOf(attrs.getNamedItem("glovesId").getNodeValue()).intValue();
              int feetId = Integer.valueOf(attrs.getNamedItem("feetId").getNodeValue()).intValue();
              
              _map.put(Integer.valueOf(id), new DressMeData(hairId, chestId, legsId, glovesId, feetId));
            }
          }
        }
      }
      _log.info("SkinTable: Loaded " + _map.size() + " skins.");
    }
    catch (Exception e)
    {
      _log.warning("SkinTable: Error while loading skin table: " + e);
    }
  }
  
  public DressMeData getSkin(int item)
  {
    return _map.get(Integer.valueOf(item));
  }
  
  public boolean getSkinId(int skinId)
  {
    if (!CustomConfig.ALLOW_DRESS_ME_SYSTEM) {
      return false;
    }
    return _map.containsKey(Integer.valueOf(skinId));
  }

  public int getHair(int skinId){
    return _map.get(Integer.valueOf(skinId)).getHairId();
  }
  public int getChest(int skinId){
    return _map.get(Integer.valueOf(skinId)).getChestId();
  }

  public int getLegs(int skinId){return _map.get(Integer.valueOf(skinId)).getLegsId(); }
  public int getGloves(int skinId){
    return _map.get(Integer.valueOf(skinId)).getGlovesId();
  }
  public int getBoots(int skinId){
    return _map.get(Integer.valueOf(skinId)).getBootsId();
  }

  private static class SingletonHolder
  {
    protected static final SkinTable _instance = new SkinTable();
  }
}
