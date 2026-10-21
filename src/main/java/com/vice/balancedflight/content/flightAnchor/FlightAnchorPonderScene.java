package com.vice.balancedflight.content.flightAnchor;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.vice.balancedflight.AllLangMessages;
import com.vice.balancedflight.content.flightAnchor.entity.FlightAnchorBehaviour;
import com.vice.balancedflight.content.flightAnchor.entity.FlightAnchorEntity;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class FlightAnchorPonderScene {
    public FlightAnchorPonderScene() { }

    public static void ponderScene(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("flight_anchor", AllLangMessages.ANCHOR_PONDER_TITLE);
        scene.configureBasePlate(1, 0, 5);

        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.world().showSection(util.select().layer(1), Direction.UP);
        scene.world().setKineticSpeed(util.select().everywhere(), 0.0F);

        Selection flightAnchorSelect = util.select().position(3, 1, 2);//
        scene.overlay().showText(90).placeNearTarget().text(AllLangMessages.ANCHOR_PONDER_POWER).pointAt(flightAnchorSelect.getCenter());
        scene.idle(100);

        BlockPos flightAnchorPos = util.grid().at(3, 1, 2);
        scene.world().modifyBlockEntity(flightAnchorPos, FlightAnchorEntity.class, entity -> {
            FlightAnchorBehaviour.beaconTick(entity.getLevel(), entity.getBlockPos(), entity);
        });

        scene.world().setKineticSpeed(util.select().everywhere(), 32.0F);
        scene.overlay().showText(90).placeNearTarget().text(AllLangMessages.ANCHOR_PONDER_RANGE).pointAt(flightAnchorSelect.getCenter());
        scene.idle(100);

        scene.world().setKineticSpeed(util.select().everywhere(), 128.0F);
        scene.overlay().showText(1000).placeNearTarget().text(AllLangMessages.ANCHOR_PONDER_COVERAGE).pointAt(flightAnchorSelect.getCenter());
        scene.idle(100);

        scene.markAsFinished();
    }
}
