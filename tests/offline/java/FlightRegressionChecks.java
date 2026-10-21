import com.vice.balancedflight.BalancedFlight;
import com.vice.balancedflight.content.flightAnchor.FlightController;
import com.vice.balancedflight.content.flightAnchor.entity.FlightAnchorEntity;
import com.vice.balancedflight.foundation.compat.SableCompat;
import com.vice.balancedflight.foundation.config.BalancedFlightConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FlightRegressionChecks {
    private static int checks;

    public static void main(String[] args) {
        check(BalancedFlightConfig.enableSlowFallingOnFlightLoss.get(), "Slow falling stays enabled by default");
        Level server = new Level(Level.OVERWORLD);
        Player player = new Player(server);
        player.ring = true;
        FlightController.tick(player);
        check(player.abilities.mayfly, "Ring grants flight");
        player.abilities.flying = true;
        player.ring = false;
        FlightController.tick(player);
        check(!player.abilities.mayfly && !player.abilities.flying, "Flight is revoked on permission loss");
        check(player.effect != null && player.effect.duration == 200, "Flying players receive ten seconds of protection");
        FlightController.tick(player);
        check(player.effectApplications == 1, "Protection is not reapplied every tick");
        player.ring = true;
        FlightController.tick(player);
        check(player.effect == null, "Regaining flight clears this mod's protection");

        player.abilities.flying = true;
        player.ring = false;
        BalancedFlightConfig.enableSlowFallingOnFlightLoss.set(false);
        FlightController.tick(player);
        check(player.effect == null && !player.abilities.mayfly, "Config disables protection without keeping flight");
        BalancedFlightConfig.enableSlowFallingOnFlightLoss.set(true);

        Player walking = new Player(server);
        walking.ring = true;
        FlightController.tick(walking);
        walking.ring = false;
        FlightController.tick(walking);
        check(walking.effect == null, "Walking out of range does not apply slow falling");

        Player potion = new Player(server);
        MobEffectInstance externalEffect = new MobEffectInstance(MobEffects.SLOW_FALLING, 1200);
        potion.effect = externalEffect;
        potion.ring = true;
        FlightController.tick(potion);
        check(potion.effect == externalEffect, "Entering range preserves potion effects");
        potion.abilities.flying = true;
        potion.ring = false;
        FlightController.tick(potion);
        check(potion.effect == externalEffect, "Leaving range preserves potion effects");

        Player externalFlight = new Player(server);
        externalFlight.abilities.mayfly = true;
        externalFlight.abilities.flying = true;
        FlightController.tick(externalFlight);
        check(externalFlight.abilities.mayfly && externalFlight.abilities.flying, "Unowned flight is not revoked");

        Player spectator = new Player(server);
        spectator.spectator = true;
        spectator.abilities.mayfly = true;
        FlightController.tick(spectator);
        check(spectator.abilities.mayfly && spectator.effect == null, "Spectator flight is left intact");
        spectator.spectator = false;
        spectator.creative = true;
        FlightController.tick(spectator);
        check(spectator.abilities.mayfly && spectator.effect == null, "Creative flight is left intact");

        Level client = new Level(Level.OVERWORLD);
        client.isClientSide = true;
        Player clientPlayer = new Player(client);
        clientPlayer.ring = true;
        FlightController.tick(clientPlayer);
        check(!clientPlayer.abilities.mayfly && clientPlayer.abilityUpdates == 0, "Client tick cannot mutate abilities");

        Player departingClient = new Player(client);
        departingClient.abilities.mayfly = true;
        departingClient.abilities.flying = true;
        departingClient.data.putBoolean("balancedflight:flight_granted", true);
        departingClient.position = new Vec3(1000, 64, 1000);
        FlightController.tick(departingClient);
        check(departingClient.effect == null && departingClient.effectApplications == 0,
                "Leaving range on the client cannot create a ghost effect");
        check(departingClient.abilityUpdates == 0 && departingClient.abilities.mayfly,
                "Client waits for authoritative server ability updates on flight loss");

        Player expiring = new Player(server);
        expiring.clientView = new Player(client);
        expiring.ring = true;
        FlightController.tick(expiring);
        expiring.abilities.flying = true;
        expiring.ring = false;
        expiring.position = new Vec3(1000, 64, 1000);
        expiring.clientView.position = expiring.position;
        FlightController.tick(expiring);
        check(expiring.effect != null && expiring.clientView.effect != null,
                "Flight loss synchronizes the enabled slow falling effect to the client");
        for (int tick = 0; tick < 199; tick++) {
            expiring.clientView.tickEffects();
            expiring.tickEffects();
            FlightController.tick(expiring.clientView);
            FlightController.tick(expiring);
        }
        check(expiring.effect != null && expiring.effect.duration == 1
                        && expiring.clientView.effect.duration == 1,
                "Slow falling counts down without refreshing while far from anchors");
        expiring.clientView.tickEffects();
        check(expiring.clientView.effect != null && expiring.clientView.effect.duration == 0,
                "Fixture models the zero-duration client effect awaiting server removal");
        expiring.tickEffects();
        check(expiring.effect == null && expiring.clientView.effect == null,
                "Server expiration removes the effect on both sides after 200 ticks");
        for (int tick = 0; tick < 200; tick++) {
            FlightController.tick(expiring.clientView);
            FlightController.tick(expiring);
        }
        check(expiring.effect == null && expiring.clientView.effect == null && expiring.effectApplications == 1,
                "Remaining outside range cannot resurrect an expired 0:00 effect");

        Player beforeLogout = new Player(server);
        beforeLogout.ring = true;
        FlightController.tick(beforeLogout);
        Player afterLogin = new Player(server);
        afterLogin.data = beforeLogout.data;
        afterLogin.abilities.mayfly = true;
        FlightController.tick(afterLogin);
        check(!afterLogin.abilities.mayfly, "Owned permissions are revoked after reconnecting outside range");

        FlightAnchorEntity anchor = new FlightAnchorEntity(server, new BlockPos(0, 64, 0));
        FlightAnchorEntity.ActiveAnchors.add(anchor);
        Player nearby = new Player(server);
        FlightController.tick(nearby);
        check(nearby.abilities.mayfly, "Nearby anchor grants flight");
        nearby.position = new Vec3(100, 64, 0);
        nearby.abilities.flying = true;
        FlightController.tick(nearby);
        check(!nearby.abilities.mayfly && nearby.effect != null, "Crossing the anchor boundary revokes flight and protects the player");
        nearby.position = new Vec3(0, 64, 0);
        FlightController.tick(nearby);
        check(nearby.abilities.mayfly && nearby.effect == null, "Reentering the anchor boundary clears protection");
        Player otherLevel = new Player(new Level(Level.OVERWORLD));
        FlightController.tick(otherLevel);
        check(!otherLevel.abilities.mayfly, "Anchors from another level instance cannot grant flight");
        anchor.removed = true;
        Player removedAnchor = new Player(server);
        FlightController.tick(removedAnchor);
        check(!removedAnchor.abilities.mayfly, "Removed anchors cannot grant flight");
        FlightAnchorEntity.ActiveAnchors.clear();

        Player elytra = new Player(server);
        elytra.ring = true;
        check(FlightController.AllowedFlightModes(elytra, true).canElytraFly(), "Ascended Elytra flight remains available");
        BalancedFlightConfig.CreativeAscended.set(false);
        check(FlightController.AllowedFlightModes(elytra, true) == FlightController.FlightMode.Elytra, "Elytra-only configuration is preserved");
        BalancedFlightConfig.CreativeAscended.set(true);

        boolean sableExpected = Boolean.getBoolean("test.sable")
                && !Boolean.getBoolean("test.sable.missing") && !Boolean.getBoolean("test.sable.serviceFailure");
        check(SableCompat.isLoaded() == sableExpected, "Sable presence is detected safely");
        check(SableCompat.horizontalDistanceSqr(server, new Vec3(3, 999, 4), new BlockPos(0, 0, 0)) == 25,
                "Range is horizontal and normal coordinates remain unchanged");
        Player plotPlayer = new Player(new Level(new Object()));
        plotPlayer.position = new Vec3(1601, 64, -1);
        check(SableCompat.allowsAnchorFlightInLevel(plotPlayer) == sableExpected,
                "Plot lookup converts block coordinates to chunks, including negative coordinates");
        plotPlayer.position = new Vec3(0, 64, 0);
        check(!SableCompat.allowsAnchorFlightInLevel(plotPlayer), "Other dimensions outside plots remain excluded");

        if (sableExpected) {
            check(SableCompat.horizontalDistanceSqr(server, new Vec3(2, 64, 0), new BlockPos(1600, 64, 0)) == 4,
                    "Moving anchors are projected to world space");
            check(SableCompat.horizontalDistanceSqr(server, new Vec3(1602, 64, 0), new BlockPos(0, 64, 0)) == 4,
                    "Players inside plots are projected to world space");
            System.setProperty("test.sable.failure", "true");
            check(Double.isInfinite(SableCompat.horizontalDistanceSqr(server, new Vec3(0, 0, 0), new BlockPos(1600, 0, 0))),
                    "Projection failures do not grant flight using mixed coordinates");
            check(!SableCompat.isLoaded(), "Broken API is disabled");
            SableCompat.horizontalDistanceSqr(server, new Vec3(0, 0, 0), new BlockPos(0, 0, 0));
            check(BalancedFlight.LOGGER.warnings == 1, "API failures do not spam warnings every tick");
        }

        System.out.println(checks + " checks passed (Sable=" + sableExpected
                + ", missingAPI=" + Boolean.getBoolean("test.sable.missing")
                + ", serviceFailure=" + Boolean.getBoolean("test.sable.serviceFailure") + ")");
    }

    private static void check(boolean condition, String message) {
        if (!condition)
            throw new AssertionError(message);
        checks++;
    }
}
