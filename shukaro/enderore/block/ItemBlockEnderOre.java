package shukaro.enderore.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockEnderOre extends ItemBlock
{
	public ItemBlockEnderOre(int id)
	{
		super(id);
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean advancedTooltips)
    {
    	infoList.add("Glowing ore");
    	infoList.add("\u00A78" + "\u00A7o" + "Exudes an odd aura");
    }
}