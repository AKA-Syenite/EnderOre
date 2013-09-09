package shukaro.enderore.block;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean advancedTooltips)
    {
    	infoList.add("Powdered essence");
    	infoList.add("\u00A78" + "\u00A7o" + "Strangely fine...");
    }
}
