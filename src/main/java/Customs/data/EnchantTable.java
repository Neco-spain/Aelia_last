package Customs.data;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.commons.data.xml.IXmlReader;

import net.sf.l2j.gameserver.enums.items.CrystalType;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class EnchantTable implements IXmlReader{
    private static Logger _log = Logger.getLogger(EnchantTable.class.getName());
    private static final Map<Integer, L2EnchantScroll> _map = new HashMap<Integer, L2EnchantScroll>();
    private boolean isBlessed,isCrystal ;

    public void reload()
    {
      _map.clear();
      load();
    }
    
    public EnchantTable() {
  	  load();
    }
    
	@Override
	public void load()
	{
	     parseFile("./data/xml/enchants.xml");
	  //   LOGGER.info("Loaded {} Enchants.", _map.size());
	}

	@Override
	public void parseDocument(Document doc, Path path) {
        try {

            for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
                if (!"list".equalsIgnoreCase(n.getNodeName())) continue;
                for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
                    if (!"enchant".equalsIgnoreCase(d.getNodeName())) continue;
                    NamedNodeMap attrs = d.getAttributes();
                    int id = Integer.valueOf(attrs.getNamedItem("id").getNodeValue());
                    //if(id==6577 || id==6578) setisBlessed(true);
                    
                    byte grade = Byte.valueOf(attrs.getNamedItem("grade").getNodeValue()).byteValue();
                    boolean weapon = Boolean.valueOf(attrs.getNamedItem("weapon").getNodeValue());
                    boolean breaks = Boolean.valueOf(attrs.getNamedItem("break").getNodeValue());
                    boolean maintain = Boolean.valueOf(attrs.getNamedItem("maintain").getNodeValue());
                    boolean crystal = Boolean.valueOf(attrs.getNamedItem("crystal").getNodeValue());
                   // boolean decenchantbyone = Boolean.valueOf(attrs.getNamedItem("decenc").getNodeValue());
                    String[] list = attrs.getNamedItem("chance").getNodeValue().split(";");
                    byte[] chance = new byte[list.length];
                    for (int i = 0; i < list.length; ++i) {
                    	//if(id == 961 && i>=14) break;
                        chance[i] = Byte.valueOf(list[i]).byteValue();
                    }
                    CrystalType grade_test = CrystalType.NONE;
                    switch (grade) {
                        case 1: {
                            grade_test = CrystalType.D;
                            break;
                        }
                        case 2: {
                            grade_test = CrystalType.C;
                            break;
                        }
                        case 3: {
                            grade_test = CrystalType.B;
                            break;
                        }
                        case 4: {
                            grade_test = CrystalType.A;
                            break;
                        }
                        case 5: {
                            grade_test = CrystalType.S;
                        }
                    }
                    _map.put(id, new L2EnchantScroll(grade_test, weapon, breaks, maintain, crystal, chance));
                }
            }
            _log.info("-EnchantTable: Loaded " + _map.size() + " enchants.");
        }
        catch (Exception e) {
            _log.warning("EnchantTable: Error while loading enchant table: " + e);
        }
    }

    public L2EnchantScroll getEnchantScroll(ItemInstance item) {
        return _map.get(item.getItemId());
    }

    public void setisBlessed(boolean ans){
    	isBlessed = ans;
    }
    public boolean getBlessed(){
    	return isBlessed;
    }
    
    
    public static EnchantTable getInstance() {
        return SingletonHolder._instance;
    }

    private static class SingletonHolder {
        protected static final EnchantTable _instance = new EnchantTable();

    }

}