package com.noobanidus.whoops;

import com.google.common.collect.Lists;
import com.noobanidus.whoops.events.WhoopsEvents;
import com.noobanidus.whoops.proxy.ISidedProxy;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

@Mod.EventBusSubscriber
@Mod(modid = Whoops.MODID, name = Whoops.MODNAME, version = Whoops.VERSION, dependencies = Whoops.DEPENDS)
@SuppressWarnings("WeakerAccess")
public class Whoops {
    public static final String MODID = "whoops";
    public static final String MODNAME = "Whoops";
    public static final String VERSION = "GRADLE:VERSION";
    public static final String DEPENDS = "required-after:botania;";

    @SuppressWarnings("unused")
    public final static Logger LOG = LogManager.getLogger(MODID);
    public final static Configuration CONFIG = new Configuration(new File("config", "whoops.cfg"), true);
    @SidedProxy(modId = MODID, clientSide = "com.noobanidus.whoops.proxy.ClientProxy", serverSide = "com.noobanidus.whoops.proxy.CommonProxy")
    public static ISidedProxy proxy;

    @Mod.Instance(Whoops.MODID)
    public static Whoops instance;

    public static List<Class> EventClasses = Lists.newArrayList(WhoopsEvents.class);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    // TODO: Better config attention

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        proxy.loadComplete(event);
    }

}
