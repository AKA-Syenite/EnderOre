package shukaro.enderore.event;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

import shukaro.enderore.EnderOre;
import shukaro.enderore.util.ChunkCoord;

import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class WorldTicker implements ITickHandler
{
    public static HashMap chunksToGen = new HashMap();
    private int count = 0;
    private int dim;
    private World world;
    private ArrayList chunks;
    private long worldSeed;
    private long xSeed;
    private long zSeed;
    private ChunkCoord c;
    private Random rand;
    
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
    }
    
    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        world = (World) tickData[0];
        dim = world.provider.dimensionId;
        
        chunks = (ArrayList) chunksToGen.get(Integer.valueOf(dim));
        
        if ((chunks != null) && (chunks.size() > 0))
        {
            count++;
            c = (ChunkCoord) chunks.get(0);
            worldSeed = world.getSeed();
            rand = new Random(worldSeed);
            xSeed = rand.nextLong() >> 3;
            zSeed = rand.nextLong() >> 3;
            rand.setSeed(xSeed * c.chunkX + zSeed * c.chunkZ ^ worldSeed);
            EnderOre.worldGen.generateWorld(rand, c.chunkX, c.chunkZ, world, false);
            chunks.remove(0);
            chunksToGen.put(Integer.valueOf(dim), chunks);
            EnderOre.logger.log(Level.INFO, "Regenerated " + count + " chunks. " + Math.max(0, chunks.size()) + " chunks left");
        }
    }
    
    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.WORLD);
    }
    
    @Override
    public String getLabel()
    {
        return "EnderOre";
    }
    
}
