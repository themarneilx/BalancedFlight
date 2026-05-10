package com.vice.balancedflight.foundation.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.Consumer;

public class ConfigBuilder
{
    private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();


    public ConfigBuilder(String comment)
    {
        COMMON_BUILDER.comment(comment).push("Settings");
    }

    public ModConfigSpec Save()
    {
        COMMON_BUILDER.pop();
        return COMMON_BUILDER.build();
    }

    public void Block(String name, Consumer<ModConfigSpec.Builder> func)
    {
        COMMON_BUILDER.push(name);
        func.accept(COMMON_BUILDER);
        COMMON_BUILDER.pop();
    }
}