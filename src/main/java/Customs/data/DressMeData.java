/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package Customs.data;

/**
 * @author Anarchy
 * Tayran.JavaDev
 *
 */

public class DressMeData
{
	private int chestId,
	legsId,
	glovesId,
	feetId,
	hairId,
	weaponId;
	
	public DressMeData()
	{
		chestId = 0;
		legsId = 0;
		glovesId = 0;
		feetId = 0;
		hairId = 0;
		weaponId = 0;
	}
	
	public DressMeData(int hairId2, int chestId2, int legsId2, int glovesId2, int feetId2)
	{
		chestId = chestId2;
		legsId = legsId2;
		glovesId = glovesId2;
		feetId = feetId2;
		hairId = hairId2;
		weaponId = 0;
	}
	
	public int getChestId()
	{
		return chestId;
	}
	
	public int getLegsId()
	{
		return legsId;
	}
	
	public int getGlovesId()
	{
		return glovesId;
	}
	
	public int getBootsId()
	{
		return feetId;
	}

	public int getHairId()
	{
		return hairId;
	}

	public int getWeaponId()
	{
		return weaponId;
	}
	
	public void setChestId(int val)
	{
		chestId = val;
	}
	
	public void setLegsId(int val)
	{
		legsId = val;
	}
	
	public void setGlovesId(int val)
	{
		glovesId = val;
	}
	
	public void setBootsId(int val)
	{
		feetId = val;
	}
	
	public void setHairId(int val)
	{
		hairId = val;
	}
	public void setWeaponId(int val)
	{
		weaponId = val;
	}
	
	
	public void reset()
	{
		chestId= 0;
		legsId= 0;
		glovesId= 0;
		feetId= 0;
		hairId= 0;
		weaponId = 0;
	}
	
	public void resetArmor()
	{
		chestId= 0;
		legsId= 0;
		glovesId= 0;
		feetId= 0;
		hairId= 0;
	}
	
	public void resetWeapon()
	{
		weaponId = 0;
	}
}
