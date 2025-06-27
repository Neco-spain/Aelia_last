
package Customs.data;

import net.sf.l2j.gameserver.enums.items.CrystalType;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

public class L2EnchantScroll {
    private final CrystalType _grade;
    private final boolean _weapon;
    private final boolean _breaks;
    private final boolean _maintain;
  //  private final boolean _dec;
    private final boolean _crystal;
    private final byte[] _chance;
    

    public L2EnchantScroll(CrystalType grade, boolean weapon, boolean breaks, boolean maintain, boolean crystal, byte[] chance) {
        this._grade = grade;
        this._weapon = weapon;
        this._breaks = breaks;
        this._maintain = maintain;
        _crystal = crystal;
        this._chance = chance;
       // this._dec=_dec;
    
    }

    
    public final byte getChance(ItemInstance enchantItem) {
        int level = enchantItem.getEnchantLevel();
        if (enchantItem.getItem().getBodyPart() == 32768 && level != 0) {
            --level;
        }
        if (level >= this._chance.length) {
            return 0;
        }
        return this._chance[level];
    }

    public final boolean canBreak() {
        return this._breaks;
    }

    public final boolean canMaintain() {
        return this._maintain;
    }
	public final boolean isCrystal()
	{
		return _crystal;
	}
	
   /* public final boolean canDec() {
        return this._dec;
    }*/
    public final boolean isValid(ItemInstance enchantItem) {
        if (this._grade != enchantItem.getItem().getCrystalType()) {
            return false;
        }
        if (enchantItem.getEnchantLevel() >= this._chance.length) {
            return false;
        }
        switch (enchantItem.getItem().getType2()) {
            case 0: {
                if (this._weapon) break;
                return false;
            }
            case 1: 
            case 2: {
                if (!this._weapon) break;
                return false;
            }
            default: {
                return false;
            }
        }
        return true;
    }
}