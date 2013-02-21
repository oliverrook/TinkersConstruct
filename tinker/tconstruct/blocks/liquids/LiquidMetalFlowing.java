package tinker.tconstruct.blocks.liquids;

import java.util.Random;

import tinker.tconstruct.TContent;
import tinker.tconstruct.client.liquidrender.RenderLiquidMetal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;

public abstract class LiquidMetalFlowing extends BlockFluid implements ILiquid
{
	boolean isOptimalFlowDirection[] = new boolean[4];
	int flowCost[] = new int[4];

	public LiquidMetalFlowing(int id)
	{
		super(id, Material.lava);
	}

	@Override
	public int getRenderType ()
	{
		return RenderLiquidMetal.liquidModel;
	}

	@Override
	public String getTextureFile ()
	{
		return TContent.liquidTexture;
	}

	private void updateFlow (World world, int x, int y, int z)
	{
		//System.out.println("x: "+x+", y: "+y+", z: "+z);
		int meta = world.getBlockMetadata(x, y, z);
		world.setBlockAndMetadata(x, y, z, stillLiquidId(), meta);
		world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
		world.markBlockForUpdate(x, y, z);
	}

	@Override
	public void updateTick (World world, int x, int y, int z, Random random)
	{
		//System.out.println("x: "+x+", y: "+y+", z: "+z);
		int l = getFlowDecay(world, x, y, z);
		byte byte0 = 1;
		boolean flag = true;
		if (l > 0)
		{
			int i1 = -100;
			i1 = getSmallestFlowDecay(world, x - 1, y, z, i1);
			i1 = getSmallestFlowDecay(world, x + 1, y, z, i1);
			i1 = getSmallestFlowDecay(world, x, y, z - 1, i1);
			i1 = getSmallestFlowDecay(world, x, y, z + 1, i1);
			int j1 = i1 + byte0;
			if (j1 >= 8 || i1 < 0)
			{
				j1 = -1;
			}
			if (getFlowDecay(world, x, y + 1, z) >= 0)
			{
				int l1 = getFlowDecay(world, x, y + 1, z);
				if (l1 >= 8)
				{
					j1 = l1;
				}
				else
				{
					j1 = l1 + 8;
				}
			}
			if (j1 != l)
			{
				l = j1;
				if (l < 0)
				{
					world.setBlockWithNotify(x, y, z, 0);
				}
				else
				{
					world.setBlockMetadataWithNotify(x, y, z, l);
					world.scheduleBlockUpdate(x, y, z, blockID, tickRate());
					world.notifyBlocksOfNeighborChange(x, y, z, blockID);
				}
			}
			else if (flag)
			{
				updateFlow(world, x, y, z);
			}
		}
		else
		{
			updateFlow(world, x, y, z);
		}
		if (liquidCanDisplaceBlock(world, x, y - 1, z))
		{
			if (l >= 8)
			{
				world.setBlockAndMetadataWithNotify(x, y - 1, z, blockID, l);
			}
			else
			{
				world.setBlockAndMetadataWithNotify(x, y - 1, z, blockID, l + 8);
			}
		}
		else if (l >= 0 && (l == 0 || blockBlocksFlow(world, x, y - 1, z)))
		{
			boolean aflag[] = getOptimalFlowDirections(world, x, y, z);
			int k1 = l + byte0;
			if (l >= 8)
			{
				k1 = 1;
			}
			if (k1 >= 8)
				return;
			if (aflag[0])
			{
				flowIntoBlock(world, x - 1, y, z, k1);
			}
			if (aflag[1])
			{
				flowIntoBlock(world, x + 1, y, z, k1);
			}
			if (aflag[2])
			{
				flowIntoBlock(world, x, y, z - 1, k1);
			}
			if (aflag[3])
			{
				flowIntoBlock(world, x, y, z + 1, k1);
			}
		}
	}

	private void flowIntoBlock (World world, int x, int y, int z, int meta)
	{
		if (liquidCanDisplaceBlock(world, x, y, z))
		{
			int bID = world.getBlockId(x, y, z);
			if (bID > 0)
			{
				Block.blocksList[bID].dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			}
			world.setBlockAndMetadataWithNotify(x, y, z, blockID, meta);
		}
	}

	private int calculateFlowCost (World world, int x, int y, int z, int l, int i1)
	{
		int j1 = 1000;
		for (int k1 = 0; k1 < 4; k1++)
		{
			if (k1 == 0 && i1 == 1 || k1 == 1 && i1 == 0 || k1 == 2 && i1 == 3 || k1 == 3 && i1 == 2)
			{
				continue;
			}
			int posX = x;
			int posY = y;
			int posZ = z;
			if (k1 == 0)
			{
				posX--;
			}
			if (k1 == 1)
			{
				posX++;
			}
			if (k1 == 2)
			{
				posZ--;
			}
			if (k1 == 3)
			{
				posZ++;
			}
			if (blockBlocksFlow(world, posX, posY, posZ) || world.getBlockMaterial(posX, posY, posZ) == blockMaterial && world.getBlockMetadata(posX, posY, posZ) == 0)
			{
				continue;
			}
			if (!blockBlocksFlow(world, posX, posY - 1, posZ))
				return l;
			if (l >= 4)
			{
				continue;
			}
			int k2 = calculateFlowCost(world, posX, posY, posZ, l + 1, k1);
			if (k2 < j1)
			{
				j1 = k2;
			}
		}

		return j1;
	}

	private boolean[] getOptimalFlowDirections (World world, int x, int y, int z)
	{
		for (int iter = 0; iter < 4; iter++)
		{
			flowCost[iter] = 1000;
			int posX = x;
			int posY = y;
			int posZ = z;
			if (iter == 0)
			{
				posX--;
			}
			if (iter == 1)
			{
				posX++;
			}
			if (iter == 2)
			{
				posZ--;
			}
			if (iter == 3)
			{
				posZ++;
			}
			if (blockBlocksFlow(world, posX, posY, posZ) || world.getBlockMaterial(posX, posY, posZ) == blockMaterial && world.getBlockMetadata(posX, posY, posZ) == 0)
			{
				continue;
			}
			if (!blockBlocksFlow(world, posX, posY - 1, posZ))
			{
				flowCost[iter] = 0;
			}
			else
			{
				flowCost[iter] = calculateFlowCost(world, posX, posY, posZ, 1, iter);
			}
		}

		int cost = flowCost[0];
		for (int k1 = 1; k1 < 4; k1++)
		{
			if (flowCost[k1] < cost)
			{
				cost = flowCost[k1];
			}
		}

		for (int l1 = 0; l1 < 4; l1++)
		{
			isOptimalFlowDirection[l1] = flowCost[l1] == cost;
		}

		return isOptimalFlowDirection;
	}

	private boolean blockBlocksFlow (World world, int x, int y, int z)
	{
		int l = world.getBlockId(x, y, z);
		if (l == Block.doorWood.blockID || l == Block.doorSteel.blockID || l == Block.signPost.blockID || l == Block.ladder.blockID || l == Block.reed.blockID)
			return true;
		if (l == 0)
			return false;
		Material material = Block.blocksList[l].blockMaterial;
		return material.isSolid();
	}

	protected int getSmallestFlowDecay (World world, int x, int y, int z, int l)
	{
		int i1 = getFlowDecay(world, x, y, z);
		if (i1 < 0)
			return l;
		if (i1 >= 8)
		{
			i1 = 0;
		}
		return l >= 0 && i1 >= l ? l : i1;
	}

	private boolean liquidCanDisplaceBlock (World world, int x, int y, int z)
	{
		Material material = world.getBlockMaterial(x, y, z);
		if (material == blockMaterial)
			return false;
		else
			return !blockBlocksFlow(world, x, y, z);
	}

	@Override
	public void onBlockAdded (World world, int x, int y, int z)
	{
		//System.out.println("x: "+x+", y: "+y+", z: "+z);
		super.onBlockAdded(world, x, y, z);
		if (world.getBlockId(x, y, z) == blockID)
		{
			world.scheduleBlockUpdate(x, y, z, blockID, tickRate());
		}
	}

	@Override
	public int stillLiquidMeta ()
	{
		return 0;
	}
	
	@Override
	public boolean isMetaSensitive ()
	{
		return false;
	}
}
