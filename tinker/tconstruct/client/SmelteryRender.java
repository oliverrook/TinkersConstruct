package tinker.tconstruct.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.liquids.LiquidStack;

import org.lwjgl.opengl.GL11;

import tinker.common.BlockSkinRenderHelper;
import tinker.tconstruct.crafting.Smeltery;
import tinker.tconstruct.logic.SmelteryLogic;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class SmelteryRender implements ISimpleBlockRenderingHandler
{
	public static int smelteryModel = RenderingRegistry.getNextAvailableRenderId();
	@Override
	public void renderInventoryBlock (Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		if (modelID == smelteryModel)
		{
			renderStandardInvBlock(renderer, block, metadata);
		}
	}

	@Override
	public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelID, RenderBlocks renderer)
	{
		if (modelID == smelteryModel)
		{
			if (world.getBlockMetadata(x, y, z) == 0)
				return renderSmeltery(world, x, y, z, block, modelID, renderer);
			else
				renderer.renderStandardBlock(block, x, y, z);
		}
		return false;
	}
	
	public boolean renderSmeltery(IBlockAccess world, int x, int y, int z, Block block, int modelID, RenderBlocks renderer)
	{
		renderer.renderStandardBlock(block, x, y, z);
		SmelteryLogic logic = (SmelteryLogic) world.getBlockTileEntity(x, y, z);
		if (logic.validStructure)
		{
			int posX = logic.centerPos.x - 1, posY = logic.centerPos.y, posZ = logic.centerPos.z - 1;
			//Melting
			for (int i = 0; i < 9; i++)
			{
				ItemStack input = logic.getStackInSlot(i);
				if (input != null && logic.getTempForSlot(i) > 20)
				{
					ItemStack blockToRender = Smeltery.getRenderIndex(input);
					float blockHeight = input.stackSize / (float) blockToRender.stackSize;
					renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, MathHelper.clamp_float(blockHeight, 0.01F, 1.0F), 1.0F);
					
					if (blockToRender.itemID < 4096) //Block
					{
						Block liquidBlock = Block.blocksList[blockToRender.itemID];
						ForgeHooksClient.bindTexture(liquidBlock.getTextureFile(), 0);
						BlockSkinRenderHelper.renderMetadataBlock(liquidBlock, blockToRender.getItemDamage(), posX + i % 3, posY, posZ + i / 3, renderer, world);
					}
					else //Item
					{
						Item liquidItem = Item.itemsList[blockToRender.itemID];
						ForgeHooksClient.bindTexture(liquidItem.getTextureFile(), 0);
						int metadata = blockToRender.getItemDamage();
						BlockSkinRenderHelper.renderFakeBlock(liquidItem.getIconFromDamage(metadata), metadata, posX, posY, posZ, renderer, world);
					}
				}
			}
			
			//Liquids
			float base = 0F;
			for (LiquidStack liquid : logic.moltenMetal)
			{
				float height = liquid.amount / 10000F;
				renderer.setRenderBounds(0.0F, base, 0.0F, 1.0F, height + base, 1.0F);
				base += height;
				
				if (liquid.itemID < 4096) //Block
				{
					Block liquidBlock = Block.blocksList[liquid.itemID];
					ForgeHooksClient.bindTexture(liquidBlock.getTextureFile(), 0);
					for (int i = 0; i < 9; i++)
						BlockSkinRenderHelper.renderMetadataBlock(liquidBlock, liquid.itemMeta, posX + i % 3, posY, posZ + i / 3, renderer, world);
				}
				else //Item
				{
					Item liquidItem = Item.itemsList[liquid.itemID];
					ForgeHooksClient.bindTexture(liquidItem.getTextureFile(), 0);
					for (int i = 0; i < 9; i++)
						BlockSkinRenderHelper.renderFakeBlock(liquidItem.getIconFromDamage(liquid.itemMeta), liquid.itemMeta, posX, posY, posZ, renderer, world);
				}
			}
		}
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory ()
	{
		return true;
	}

	@Override
	public int getRenderId ()
	{
		return smelteryModel;
	}

	private void renderStandardInvBlock(RenderBlocks renderblocks, Block block, int meta)
	{
		Tessellator tessellator = Tessellator.instance;
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(0, meta));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(1, meta));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(2, meta));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(3, meta));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(4, meta));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(5, meta));
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
}
