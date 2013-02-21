package tinker.tconstruct.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import tinker.tconstruct.AbilityHelper;
import tinker.tconstruct.TContent;
import tinker.tconstruct.logic.EquipLogic;

public class FryingPan extends Weapon
{
	public FryingPan(int itemID, String tex)
	{
		super(itemID, 2, tex);
		this.setItemName("InfiTool.FryingPan");
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLiving mob, EntityLiving player)
	{
		//AbilityHelper.hitEntity(stack, mob, player, damageVsEntity);
		AbilityHelper.knockbackEntity(mob, 1.7f);
		mob.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 0)); //5 seconds of stun
		//Play "thunk" sfx
		return true;
	}
	
	public String getToolName()
	{
		return "Frying Pan";
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ)
    {
        if (side == 0 || !player.isSneaking())
        {
            return false;
        }
        else if (!world.getBlockMaterial(x, y, z).isSolid())
        {
            return false;
        }
        else
        {
            if (side == 1)
            {
                ++y;
            }

            if (side == 2)
            {
                --z;
            }

            if (side == 3)
            {
                ++z;
            }

            if (side == 4)
            {
                --x;
            }

            if (side == 5)
            {
                ++x;
            }

            if (!player.canPlayerEdit(x, y, z, side, stack))
            {
                return false;
            }
            else if (!TContent.heldItemBlock.canPlaceBlockAt(world, x, y, z))
            {
                return false;
            }
            else
            {
                world.setBlockAndMetadataWithNotify(x, y, z, TContent.heldItemBlock.blockID, 0);

                EquipLogic logic = (EquipLogic) world.getBlockTileEntity(x, y, z);
    			logic.setEquipmentItem(stack);
                --stack.stackSize;

                return true;
            }
        }
    }

	@Override
	protected Item getHeadItem ()
	{
		return TContent.frypanHead;
	}

	@Override
	protected Item getAccessoryItem ()
	{
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderPasses (int metadata)
	{
		return 2;
	}
	
	protected String getRenderString (int renderPass, boolean broken)
	{
		switch (renderPass)
		{
		case 0:
			return "_frypan_handle.png";
		case 1:
			if (broken)
				return "_frypan_head_broken.png";
			else
				return "_frypan_head.png";
		default:
			return "";
		}
	}

	protected String getEffectString (int renderPass)
	{
		return "_frypan_effect.png";
	}
}
