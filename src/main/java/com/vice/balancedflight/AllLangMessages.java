package com.vice.balancedflight;

import com.tterrag.registrate.providers.ProviderType;

public class AllLangMessages
{
    public static final String ANCHOR_PONDER_TITLE = "Powered flight with Rotational Force";
    public static final String ANCHOR_PONDER_POWER = "The flight anchor requires an immense amount of power to operate.";
    public static final String ANCHOR_PONDER_RANGE = "For each RPM, you will be able to fly one block around the anchor.";
    public static final String ANCHOR_PONDER_COVERAGE = "Higher speeds cover a much higher surface area.";

    public static void init() {
        BalancedFlight.CREATE_REGISTRATE.addLang("tooltip", BalancedFlight.FLIGHT_ANCHOR_BLOCK.getId(), "Allows flight in a 25 block radius. Only works in the overworld.");
        BalancedFlight.CREATE_REGISTRATE.addLang("tooltip", BalancedFlight.ASCENDED_FLIGHT_RING.getId(), "An incredibly dense golden ring, and yet, it allows flight anywhere.");
        BalancedFlight.CREATE_REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            provider.add("balancedflight.ponder.tag.flight_anchor", "Flight Anchor");
            provider.add("balancedflight.ponder.tag.flight_anchor.description", ANCHOR_PONDER_TITLE);
            provider.add("balancedflight.ponder.flight_anchor.header", ANCHOR_PONDER_TITLE);
            provider.add("balancedflight.ponder.flight_anchor.text_1", ANCHOR_PONDER_POWER);
            provider.add("balancedflight.ponder.flight_anchor.text_2", ANCHOR_PONDER_RANGE);
            provider.add("balancedflight.ponder.flight_anchor.text_3", ANCHOR_PONDER_COVERAGE);
            provider.add("tooltip.balancedflight.flight_anchor.sable", "Works on Sable physics structures and sub-levels when Sable is installed.");
        });
    }
}
