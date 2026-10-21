package net.neoforged.fml;

import java.util.Optional;

public class ModList {
    public static ModList get() { return new ModList(); }
    public Optional<Object> getModContainerById(String id) {
        return id.equals("sable") && Boolean.getBoolean("test.sable") ? Optional.of(id) : Optional.empty();
    }
}
