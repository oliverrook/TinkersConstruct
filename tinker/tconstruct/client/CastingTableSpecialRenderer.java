package tinker.tconstruct.client;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texturefx.TextureCompassFX;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.storage.MapData;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import tinker.common.fancyitem.FancyEntityItem;
import tinker.tconstruct.logic.CastingTableLogic;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/* Special renderer, only used for drawing tools */

@SideOnly(Side.CLIENT)
public class CastingTableSpecialRenderer extends TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt (TileEntity logic, double var2, double var4, double var6, float var8)
	{
		//System.out.println("Render!!!");
		this.render((CastingTableLogic) logic, var2, var4, var6, var8);
	}

	public void render (CastingTableLogic logic, double posX, double posY, double posZ, float var8)
	{
		GL11.glPushMatrix();
		float var10 = (float) (posX - 0.5F);
		float var11 = (float) (posY - 0.5F);
		float var12 = (float) (posZ - 0.5F);
		GL11.glTranslatef(var10, var11, var12);
		this.func_82402_b(logic);
		GL11.glPopMatrix();
	}

	private void func_82402_b (CastingTableLogic logic)
	{
		ItemStack stack = logic.getStackInSlot(0);

		if (stack != null)
			renderItem(logic, stack);
		
		stack = logic.getStackInSlot(1);
		
		if (stack != null)
			renderItem(logic, stack);
	}
	
	void renderItem(CastingTableLogic logic, ItemStack stack)
	{
		FancyEntityItem entityitem = new FancyEntityItem(logic.worldObj, 0.0D, 0.0D, 0.0D, stack);
		entityitem.func_92014_d().stackSize = 1;
		entityitem.hoverStart = 0.0F;
		GL11.glPushMatrix();
		GL11.glTranslatef(1F, 1.478F, 0.55F);
		GL11.glRotatef(90F, 1, 0F, 0F);
		GL11.glScalef(2F, 2F, 2F);

		RenderItem.field_82407_g = true;
		RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
		RenderItem.field_82407_g = false;

		GL11.glPopMatrix();
	}
}
