package com.noobanidus.whoops;

import net.minecraftforge.common.config.Config;

import static net.minecraftforge.common.config.Config.*;

@Config(modid = Whoops.MODID)
public class WhoopsConfig {
    @Comment("Set to false to disable the functionality of this mod.")
    @Name("Enable Pure Daisy right-click")
    public static boolean ENABLED = true;
}
