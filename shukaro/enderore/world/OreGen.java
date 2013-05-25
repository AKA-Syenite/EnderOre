package shukaro.enderore.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import shukaro.enderore.EnderOre;
import shukaro.enderore.util.BlockCoord;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class OreGen implements IWorldGenerator
{
    private static List<Integer> blacklistedDimensions;
    private static List<String> blacklistedWorldtypes;
    private BlockCoord coord = new BlockCoord();
    private int x;
    private int y;
    private int z;
    private int x1;
    private int y1;
    private int z1;
    private List<BlockCoord> list = new ArrayList<BlockCoord>();
    private int genned;
    private int min;
    private int max;
    private int numberOfBlocks;
    
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        generateWorld(random, chunkX, chunkZ, world, true);
    }
    
    public void generateWorld(Random random, int chunkX, int chunkZ, World world, boolean newGen)
    {
        if (!EnderOre.genOre.getBoolean(true))
            return;
        
        if (blacklistedDimensions == null)
            blacklistedDimensions = EnderOre.dimBlacklist;
        
        if (blacklistedWorldtypes == null)
            blacklistedWorldtypes = EnderOre.worldTypeBlacklist;
        
        if (blacklistedDimensions.contains(world.provider.dimensionId))
            return;
        
        if (blacklistedWorldtypes.contains(world.getWorldInfo().getTerrainType().getWorldTypeName()))
            return;
        
        min = EnderOre.enderOreMinHeight.getInt();
        max = EnderOre.enderOreMaxHeight.getInt();
        
        for (int i=0; i<EnderOre.enderOreFrequency.getInt(); i++)
        {
            x = chunkX * 16 + random.nextInt(16);
            z = chunkZ * 16 + random.nextInt(16);
            y = min + (int)(Math.random() * ((max - min) + 1));
            generateOre(world, random, x, y, z);
        }
        
        if (!newGen)
            world.getChunkFromChunkCoords(chunkX, chunkZ).setChunkModified();
    }
    
    private boolean generateOre(World world, Random rand, int x, int y, int z)
    {
        numberOfBlocks = rand.nextInt(EnderOre.enderOreSize.getInt()) + 3;
        
        float f = rand.nextFloat() * (float) Math.PI;
        double d0 = x + 8 + MathHelper.sin(f) * numberOfBlocks / 8.0F;
        double d1 = x + 8 - MathHelper.sin(f) * numberOfBlocks / 8.0F;
        double d2 = z + 8 + MathHelper.cos(f) * numberOfBlocks / 8.0F;
        double d3 = z + 8 - MathHelper.cos(f) * numberOfBlocks / 8.0F;
        double d4 = y + rand.nextInt(3) - 2;
        double d5 = y + rand.nextInt(3) - 2;
        
        for (int l = 0; l <= numberOfBlocks; ++l)
        {
            double d6 = d0 + (d1 - d0) * l / numberOfBlocks;
            double d7 = d4 + (d5 - d4) * l / numberOfBlocks;
            double d8 = d2 + (d3 - d2) * l / numberOfBlocks;
            double d9 = rand.nextDouble() * numberOfBlocks / 16.0D;
            double d10 = (MathHelper.sin(l * (float) Math.PI / numberOfBlocks) + 1.0F) * d9 + 1.0D;
            double d11 = (MathHelper.sin(l * (float) Math.PI / numberOfBlocks) + 1.0F) * d9 + 1.0D;
            int i1 = (int)(d6 - d10 / 2.0D);
            int j1 = (int)(d7 - d11 / 2.0D);
            int k1 = (int)(d8 - d10 / 2.0D);
            int l1 = (int)(d6 + d10 / 2.0D);
            int i2 = (int)(d7 + d11 / 2.0D);
            int j2 = (int)(d8 + d10 / 2.0D);

            for (int k2 = i1; k2 <= l1; ++k2)
            {
                double d12 = (k2 + 0.5D - d6) / (d10 / 2.0D);
                if (d12 * d12 < 1.0D)
                {
                    for (int l2 = j1; l2 <= i2; ++l2)
                    {
                        double d13 = (l2 + 0.5D - d7) / (d11 / 2.0D);
                        if (d12 * d12 + d13 * d13 < 1.0D)
                        {
                            for (int i3 = k1; i3 <= j2; ++i3)
                            {
                                double d14 = (i3 + 0.5D - d8) / (d10 / 2.0D);
                                Block block = Block.blocksList[world.getBlockId(k2, l2, i3)];
                                if (block != null)
                                {
                                    if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D && block.blockID == Block.stone.blockID)
                                    {
                                        world.setBlock(k2, l2, i3, EnderOre.blockEnderOre.blockID, 0, 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
