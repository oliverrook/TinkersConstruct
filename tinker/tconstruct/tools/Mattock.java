package tinker.tconstruct.tools;

import tinker.tconstruct.AbilityHelper;
import tinker.tconstruct.TContent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public class Mattock extends DualHarvestTool
{
	public Mattock(int itemID, String tex)
	{
		super(itemID, 3, tex);
		this.setItemName("InfiTool.Mattock");
	}

	@Override
	protected Material[] getEffectiveMaterials ()
	{
		return axeMaterials;
	}

	@Override
	protected Material[] getEffectiveSecondaryMaterials ()
	{
		return shovelMaterials;
	}

	@Override
	protected String getHarvestType ()
	{
		return "axe";
	}

	@Override
	protected String getSecondHarvestType ()
	{
		return "shovel";
	}
	
	static Material[] axeMaterials = { Material.wood, Material.circuits, Material.cactus, Material.pumpkin, Material.plants };
	static Material[] shovelMaterials = { Material.grass, Material.ground, Material.clay };
	
	public float getDurabilityModifier ()
	{
		return 1.2f;
	}
	
	/* Mattock specific */
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ)
    {
        return AbilityHelper.hoeGround(stack, player, world, x, y, z, side, random);
    }

	@Override
	protected Item getHeadItem ()
	{
		return TContent.axeHead;
	}

	@Override
	protected Item getAccessoryItem ()
	{
		return TContent.shovelHead;
	}
	
	protected String getRenderString (int renderPass, boolean broken)
	{
		switch (renderPass)
		{
		case 0:
			return "_mattock_handle.png";
		case 1:
			if (broken)
				return "_mattock_head_broken.png";
			else
				return "_mattock_head.png";
		case 2:
			return "_mattock_back.png";
		default:
			return "";
		}
	}

	protected String getEffectString (int renderPass)
	{
		return "_mattock_effect.png";
	}
}