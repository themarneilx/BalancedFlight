package net.minecraft.world.level;

public class Level {
    public static final Object OVERWORLD = new Object();
    public boolean isClientSide;
    private final Object dimension;
    public Level(Object dimension) { this.dimension = dimension; }
    public Object dimension() { return dimension; }
}
