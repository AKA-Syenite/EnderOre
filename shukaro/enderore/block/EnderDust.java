package shukaro.enderore.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;

public class EnderDust extends Item
{
	private static Icon itemIcon;
	
	public EnderDust(int par1)
	{
		super(par1);
		this.setUnlocalizedName("enderore.enderdust");
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister reg)
	{
		itemIcon = reg.registerIcon("enderore:enderdust");
	}
	
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1)
    {
        return itemIcon;
    }

}
