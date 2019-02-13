package com.noobanidus.whoops.proxy;

import com.noobanidus.whoops.Whoops;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;

public class CommonProxy implements ISidedProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Whoops.EventClasses.forEach(MinecraftForge.EVENT_BUS::register);
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void loadComplete(FMLLoadCompleteEvent event) {
        Whoops.LOG.info("Whoops: Load Complete.");
        Whoops.CONFIG.save();
    }

    public void serverStarting(FMLServerStartingEvent event) {
    }

    public void serverStarted(FMLServerStartedEvent event) {
    }
}
