package shukaro.enderore.event;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import shukaro.enderore.EnderOre;
import shukaro.enderore.util.ChunkCoord;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkDataEvent;

public class EventHandler
{
    private List<Integer> dimBlacklist;
    private int dim;
    private ArrayList chunks;
    
    @ForgeSubscribe
    public void chunkSave(ChunkDataEvent.Save e)
    {
        e.getData().setString("EnderOre", EnderOre.regenKey.getString());
    }
    
    @ForgeSubscribe
    public void chunkLoad(ChunkDataEvent.Load e)
    {
        dim = e.world.provider.dimensionId;
        ChunkCoordIntPair c = e.getChunk().getChunkCoordIntPair();
        
        if (dimBlacklist == null)
            dimBlacklist = EnderOre.dimBlacklist;
        
        if (dimBlacklist.contains(dim))
            return;
        
        if (!e.getData().getString("EnderOre").equals(EnderOre.regenKey.getString()))
        {
            EnderOre.logger.log(Level.WARNING, "World gen was never run for chunk at " + e.getChunk().getChunkCoordIntPair() + ". Adding to queue for regeneration.");
            chunks = (ArrayList) WorldTicker.chunksToGen.get(Integer.valueOf(dim));
            if (chunks == null)
            {
                WorldTicker.chunksToGen.put(Integer.valueOf(dim), new ArrayList());
                chunks = (ArrayList) WorldTicker.chunksToGen.get(Integer.valueOf(dim));
            }
            if (chunks != null)
            {
                chunks.add(new ChunkCoord(c.chunkXPos, c.chunkZPos));
                WorldTicker.chunksToGen.put(Integer.valueOf(dim), chunks);
            }
        }
    }
}
