package net.minecraft.world.entity.player;

import java.util.HashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Player {
    public final Abilities abilities = new Abilities();
    public PersistentData data = new PersistentData();
    public final Level level;
    public Vec3 position = new Vec3(0, 64, 0);
    public boolean creative, spectator, ring;
    public MobEffectInstance effect;
    public Player clientView;
    public int effectApplications, abilityUpdates;
    public Player(Level level) { this.level = level; }
    public Level level() { return level; }
    public boolean isCreative() { return creative; }
    public boolean isSpectator() { return spectator; }
    public Abilities getAbilities() { return abilities; }
    public PersistentData getPersistentData() { return data; }
    public void onUpdateAbilities() {
        abilityUpdates++;
        if (clientView != null) {
            clientView.abilities.mayfly = abilities.mayfly;
            clientView.abilities.flying = abilities.flying;
        }
    }
    public Vec3 position() { return position; }
    public BlockPos blockPosition() { return new BlockPos((int) Math.floor(position.x), (int) Math.floor(position.y), (int) Math.floor(position.z)); }
    public boolean hasEffect(Object type) { return effect != null; }
    public MobEffectInstance getEffect(Object type) { return effect; }
    public boolean addEffect(MobEffectInstance value) {
        effect = value;
        effectApplications++;
        if (clientView != null)
            clientView.addEffect(new MobEffectInstance(null, value.duration));
        return true;
    }
    public boolean removeEffect(Object type) {
        effect = null;
        if (clientView != null)
            clientView.removeEffect(type);
        return true;
    }
    public void tickEffects() {
        if (effect != null && effect.duration > 0) {
            effect.duration--;
            // Match LivingEntity.tickEffects: expiration is removed only on the server.
            if (effect.duration == 0 && !level.isClientSide)
                removeEffect(null);
        }
    }
    public static class Abilities { public boolean mayfly, flying; }
    public static class PersistentData extends HashMap<String, Boolean> {
        public boolean getBoolean(String key) { return getOrDefault(key, false); }
        public void putBoolean(String key, boolean value) { put(key, value); }
    }
}
