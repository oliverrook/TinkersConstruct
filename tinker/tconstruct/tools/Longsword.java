package tinker.tconstruct.tools;

import tinker.tconstruct.AbilityHelper;
import tinker.tconstruct.TContent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Longsword extends Weapon
{
	public Longsword(int itemID, String tex)
	{
		super(itemID, 4, tex);
		this.setItemName("InfiTool.Longsword");
	}
	
	/*public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.bow;
    }*/
	
	/*public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        NBTTagCompound tags = stack.getTagCompound();
        tags.getCompoundTag("InfiTool").setBoolean("InUse", true);
        return stack;
    }*/
	
	public float chargeAttack()
	{
		return 1.2f;
	}
	
	/*public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int time)
    {
		if (time > 5)
			AbilityHelper.thrust(stack, world, player);
		player.swingItem();
    }*/

	@Override
	protected Item getHeadItem ()
	{
		return TContent.swordBlade;
	}

	@Override
	protected Item getAccessoryItem ()
	{
		return TContent.medGuard;
	}
	
	/*@Override
	public boolean hitEntity(ItemStack stack, EntityLiving mob, EntityLiving player)
	{
		NBTTagCompound tags = stack.getTagCompound();
		System.out.println("Inuuse: "+tags.getCompoundTag("InfiTool").getBoolean("InUse"));
		if (tags.getCompoundTag("InfiTool").getBoolean("InUse"))
		{
			AbilityHelper.hitEntity(stack, mob, player, damageVsEntity, 2.0f);
			AbilityHelper.knockbackEntity(mob, 3);
			tags.getCompoundTag("InfiTool").setBoolean("InUse", false);
		}
		else
			AbilityHelper.hitEntity(stack, mob, player, damageVsEntity);
		
		return true;
	}*/
	
	protected String getRenderString (int renderPass, boolean broken)
	{
		switch (renderPass)
		{
		case 0:
			return "_longsword_handle.png";
		case 1:
			if (broken)
				return "_longsword_blade_broken.png";
			else
				return "_longsword_blade.png";
		case 2:
			return "_longsword_accessory.png";
		default:
			return "";
		}
	}

	protected String getEffectString (int renderPass)
	{
		return "_longsword_effect.png";
	}
}
