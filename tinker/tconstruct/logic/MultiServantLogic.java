package tinker.tconstruct.logic;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tinker.common.CoordTuple;

public class MultiServantLogic extends TileEntity
{
	boolean hasMaster;
	CoordTuple master;
	short masterID;
	byte masterMeat; //Typo, it stays!
	
	public boolean canUpdate()
    {
        return false;
    }

	public boolean hasValidMaster ()
	{
		if (!hasMaster)
			return false;
		
		if (worldObj.getBlockId(master.x, master.y, master.z) == masterID && worldObj.getBlockMetadata(master.x, master.y, master.z) == masterMeat)
			return true;
		else
		{
			hasMaster = false;
			master = null;
			return false;
		}
	}

	public CoordTuple getMasterPosition ()
	{
		return master;
	}
	
	public void overrideMaster(int x, int y, int z)
	{
		hasMaster = true;
		master = new CoordTuple(x, y, z);
		masterID = (short) worldObj.getBlockId(x, y, z);
		masterMeat = (byte) worldObj.getBlockMetadata(x, y, z);
	}
	
	public void removeMaster()
	{
		hasMaster = false;
		master = null;
		masterID = 0;
		masterMeat = 0;
	}
	
	public boolean verifyMaster(int x, int y, int z)
	{
		if (master.equals(x, y, z) && worldObj.getBlockId(x, y, z) == masterID && worldObj.getBlockMetadata(x, y, z) == masterMeat)
			return true;
		else
			return false;
	}
	
	public boolean setMaster(int x, int y, int z)
	{
		if (!hasMaster || worldObj.getBlockId(master.x, master.y, master.z) != masterID || (worldObj.getBlockMetadata(master.x, master.y, master.z) != masterMeat))
		{
			overrideMaster(x, y, z);
			return true;
		}
		else
		{
			return false;
		}
	}

	public void readFromNBT (NBTTagCompound tags)
	{
		super.readFromNBT(tags);
		hasMaster = tags.getBoolean("HasMaster");
		if (hasMaster)
		{
			int xCenter = tags.getInteger("xCenter");
			int yCenter = tags.getInteger("yCenter");
			int zCenter = tags.getInteger("zCenter");
			master = new CoordTuple(xCenter, yCenter, zCenter);
			masterID = tags.getShort("MasterID");
			masterMeat = tags.getByte("masterMeat");
		}
	}

	public void writeToNBT (NBTTagCompound tags)
	{
		super.writeToNBT(tags);
		tags.setBoolean("HasMaster", hasMaster);
		if (hasMaster)
		{
			tags.setInteger("xCenter", master.x);
			tags.setInteger("yCenter", master.y);
			tags.setInteger("zCenter", master.z);
			tags.setShort("MasterID", masterID);
			tags.setByte("masterMeat", masterMeat);
		}
	}
}
