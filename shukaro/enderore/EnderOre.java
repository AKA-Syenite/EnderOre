package shukaro.enderore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import net.minecraftforge.oredict.ShapedOreRecipe;

import shukaro.enderore.block.BlockEnderOre;
import shukaro.enderore.block.EnderDust;
import shukaro.enderore.net.CommonProxy;
import shukaro.enderore.world.OreGen;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = EnderOre.modID, name = EnderOre.modName, version = EnderOre.modVersion)
public class EnderOre
{
    @SidedProxy(clientSide = "shukaro.enderore.net.ClientProxy", serverSide = "shukaro.enderore.net.CommonProxy")
    public static CommonProxy proxy;
    
    public static final String modID = "EnderOre";
    public static final String modName = "Ender Ore";
    public static final String modVersion = "1.5.2R1.0";
    
    public static OreGen worldGen;
    public static Logger logger;
    
    public static Block blockEnderOre;
    public static Block blockEnderOreGlowing;
    public static Property blockEnderOreID;
    public static Property blockEnderOreGlowingID;
    public static Property enderOreFrequency;
    public static Property enderOreMaxHeight;
    public static Property enderOreMinHeight;
    public static Property enderOreSize;
    public static Property genOre;
    
    public static Item enderDust;
    public static Property enderDustID;
    
    public static Property dimBlacklistProperty;
    
    public static Property regenKey;
    
    public static List<Integer> dimBlacklist = new ArrayList<Integer>();
    public static List<String> worldTypeBlacklist = new ArrayList<String>();
    
    public static File configFolder;
    
    @Instance(modID)
    public static EnderOre instance;
    
    @PreInit
    public void preInit(FMLPreInitializationEvent e)
    {
        Configuration c = new Configuration(e.getSuggestedConfigurationFile());
        try
        {
            c.load();
            blockEnderOreID = c.getBlock("blockEnderOre", 3050);
            blockEnderOreGlowingID = c.getBlock("blockEnderOreGlowing", 3051);
            enderDustID = c.getItem("enderDust", 4700);
            genOre = c.get("World Generation", "Generate Ore", true);
            enderOreFrequency = c.get("World Generation", "Ore Frequency", 8);
            enderOreMaxHeight = c.get("World Generation", "Ore Max Height", 32);
            enderOreMinHeight = c.get("World Generation", "Ore Min Height", 1);
            enderOreSize = c.get("World Generation", "Ore Deposit Size", 6);
            dimBlacklistProperty = c.get("World Generation", "Dimension Blacklist", "");
            dimBlacklistProperty.comment = "Comma-seperated list of dimensions to ignore";
            regenKey = c.get("World Generation", "Regeneration Key", "DEFAULT");
            regenKey.comment = "Change to regenerate ore";
        }
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Ender Ore couldn't load its config file!");
            ex.printStackTrace();
        }
        finally
        {
            c.save();
        }
        
        setConfigFolderBase(e.getModConfigurationDirectory());
        extractLang(new String[] {"en_US" });
        loadLang();
        
        logger = e.getModLog();
        
        setDimBlacklist();
        
        worldTypeBlacklist.add("flat");
    }
    
    @Init
    public void init(FMLInitializationEvent e)
    {
        blockEnderOre = new BlockEnderOre(blockEnderOreID.getInt(), false);
        blockEnderOreGlowing = new BlockEnderOre(blockEnderOreGlowingID.getInt(), true);
        enderDust = new EnderDust(enderDustID.getInt());
        
        GameRegistry.registerBlock(blockEnderOre, blockEnderOre.getUnlocalizedName());
        GameRegistry.registerItem(enderDust, enderDust.getUnlocalizedName());
        
        GameRegistry.registerWorldGenerator(worldGen = new OreGen());
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Item.enderPearl.itemID, 1, 0), new Object[] {
            "XX",
            "XX",
            'X', new ItemStack(enderDust.itemID, 1, 0)
        }));
    }
    
    private static void setDimBlacklist()
    {
        String blacklist = dimBlacklistProperty.getString().trim();
        
        for (String dim : blacklist.split(","))
        {
            try
            {
                Integer dimID = Integer.parseInt(dim);
                if (!dimBlacklist.contains(dimID))
                    dimBlacklist.add(dimID);
            }
            catch (Exception e)
            {
            }
        }
    }
    
    public static void setConfigFolderBase(File folder)
    {
        configFolder = new File(folder.getAbsolutePath() + "/shukaro/enderore/");
    }
    
    public static void extractLang(String[] languages)
    {
        String langResourceBase = "/shukaro/enderore/lang/";
        for (String lang : languages)
        {
            InputStream is = EnderOre.instance.getClass().getResourceAsStream(langResourceBase + lang + ".lang");
            try
            {
                File f = new File(configFolder.getAbsolutePath() + "/lang/" + lang + ".lang");
                if (!f.exists())
                    f.getParentFile().mkdirs();
                OutputStream os = new FileOutputStream(f);
                byte[] buffer = new byte[1024];
                int read = 0;
                while ((read = is.read(buffer)) != -1)
                {
                    os.write(buffer, 0, read);
                }
                is.close();
                os.flush();
                os.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public static void loadLang()
    {
        File f = new File(configFolder.getAbsolutePath() + "/lang/");
        for (File langFile : f.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".lang");
            }
        }))
        {
            try
            {
                Properties langPack = new Properties();
                langPack.load(new FileInputStream(langFile));
                String lang = langFile.getName().replace(".lang", "");
                LanguageRegistry.instance().addStringLocalization(langPack, lang);
            }
            catch (FileNotFoundException x)
            {
                x.printStackTrace();
            }
            catch (IOException x)
            {
                x.printStackTrace();
            }
        }
    }
}