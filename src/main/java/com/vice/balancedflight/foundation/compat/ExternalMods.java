package com.vice.balancedflight.foundation.compat;

import net.neoforged.fml.ModList;

public enum ExternalMods {
    CURIOS("curios"),
    SABLE("sable");

    private final boolean loaded;

    ExternalMods(String modid) {
        this.loaded = ModList.get() != null && ModList.get().getModContainerById(modid).isPresent();
    }

    public boolean isLoaded() {
        return this.loaded;
    }
}
