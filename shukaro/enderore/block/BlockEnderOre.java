package shukaro.enderore.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import shukaro.enderore.EnderOre;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEnderOre extends Block
{
    private static Icon blockIcon;
    private boolean isOn;
    
    public BlockEnderOre(int par1)
    {
        super(par1, Material.rock);
        this.setHardness(3.0F);
        this.setResistance(5.0F);
        this.setStepSound(soundStoneFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setUnlocalizedName("enderore.enderore");
        this.setTickRandomly(true);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        return blockIcon;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister reg)
    {
        blockIcon = reg.registerIcon("enderore:enderore");
    }
    
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        if (world.getBlockMetadata(x, y, z) == 1)
            return 8;
        else
            return 0;
    }
    
    @Override
    public int tickRate(World world)
    {
        return 30;
    }
    
    @Override
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        this.glow(par1World, par2, par3, par4);
    }
    
    @Override
    public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        this.glow(par1World, par2, par3, par4);
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        this.glow(par1World, par2, par3, par4);
        return false;
    }
    
    private void glow(World par1World, int par2, int par3, int par4)
    {
    	this.sparkle(par1World, par2, par3, par4);
    	
        if (par1World.getBlockMetadata(par2, par3, par4) == 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 2);
        }
    }
    
    private void sparkle(World par1World, int par2, int par3, int par4)
    {
    	Random rand = par1World.rand;
    	double x = (double) par2 + 0.5D + rand.nextDouble() - rand.nextDouble();
    	double y = (double) par3 + 0.5D + rand.nextDouble() - rand.nextDouble();
    	double z = (double) par4 + 0.5D + rand.nextDouble() - rand.nextDouble();
    	par1World.spawnParticle("portal", x, y, z, rand.nextDouble() - rand.nextDouble(), rand.nextDouble() - rand.nextDouble(), rand.nextDouble() - rand.nextDouble());
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	if (par1World.getBlockMetadata(par2, par3, par4) == 1)
    	{
    		this.sparkle(par1World, par2, par3, par4);
    	}
    }

	@Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par1World.getBlockMetadata(par2, par3, par4) == 1)
        {
        	par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 2);
        }
    }
    
    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return EnderOre.enderDust.itemID;
    }
    
    @Override
    public int quantityDroppedWithBonus(int par1, Random par2Random)
    {
        return this.quantityDropped(par2Random) + par2Random.nextInt(par1 + 1);
    }
    
    @Override
    public int quantityDropped(Random par1Random)
    {
        return 2 + par1Random.nextInt(2);
    }
    
    @Override
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);

        if (this.idDropped(par5, par1World.rand, par7) != this.blockID)
        {
            int j1 = 1 + par1World.rand.nextInt(5);
            this.dropXpOnBlockBreak(par1World, par2, par3, par4, j1);
        }
    }
    
    @Override
    protected ItemStack createStackedBlock(int par1)
    {
        return new ItemStack(EnderOre.blockEnderOre);
    }

}
