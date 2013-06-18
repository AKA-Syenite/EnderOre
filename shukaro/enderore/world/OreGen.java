package shukaro.enderore.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import shukaro.enderore.EnderOre;
import shukaro.enderore.util.BlockCoord;
import shukaro.enderore.util.ChunkCoord;

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
    private ChunkCoord chunk;
    private List<BlockCoord> blocks = new ArrayList<BlockCoord>();
    int x;
    int y;
    int z;
    
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
        
        chunk = new ChunkCoord(chunkX, chunkZ);
        
        int min = EnderOre.enderOreMinHeight.getInt();
        int max = EnderOre.enderOreMaxHeight.getInt();
        int freq = EnderOre.enderOreFrequency.getInt();
        
        int oreSize = EnderOre.enderOreSize.getInt();
        int size = oreSize + (random.nextInt(oreSize) / 2) - (random.nextInt(oreSize) / 2);
        
        for (int i=0; i<freq; i++)
        {
            x = chunkX * 16 + random.nextInt(16);
            z = chunkZ * 16 + random.nextInt(16);
            y = min + (int)(Math.random() * ((max - min) + 1));
            generateOre(world, random, x, y, z, size);
        }
        
        if (!newGen)
            world.getChunkFromChunkCoords(chunkX, chunkZ).setChunkModified();
    }
    
    private boolean generateOre(World world, Random rand, int x, int y, int z, int size)
    {
    	coord.set(x, y, z);
    	
    	if (!canGenHere(world, chunk, coord))
    		return false;
    	
    	int genned = 0;
    	
    	world.setBlock(coord.x, coord.y, coord.z, EnderOre.blockEnderOre.blockID, 0, 0);
		genned++;
    	
    	while (genned < size)
    	{
			for (BlockCoord t : coord.getAdjacent())
			{
				if (rand.nextInt(10) > 4)
					continue;
				if (canGenHere(world, chunk, t))
					blocks.add(t);
			}
			if (blocks.size() == 0)
				break;
			coord.set(blocks.get(rand.nextInt(blocks.size())));
			blocks.remove(coord);
			world.setBlock(coord.x, coord.y, coord.z, EnderOre.blockEnderOre.blockID, 0, 0);
			genned++;
    	}
    	
		return true;
	}

	public boolean canGenHere(World world, ChunkCoord c, BlockCoord b)
	{
		return c.contains(b) && b.getBlock(world) != null && b.getBlock(world) == Block.stone;
	}
}
