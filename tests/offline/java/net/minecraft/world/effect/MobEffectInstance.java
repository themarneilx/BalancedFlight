package net.minecraft.world.effect;

public class MobEffectInstance {
    public int duration;
    public int amplifier;
    public MobEffectInstance(Object effect, int duration) { this.duration = duration; }
    public int getAmplifier() { return amplifier; }
    public boolean endsWithin(int ticks) { return duration >= 0 && duration <= ticks; }
}
