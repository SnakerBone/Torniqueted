package bytesnek.tq.level.world.feature;

import bytesnek.snakerlib.level.levelgen.feature.RubbleFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

import com.mojang.serialization.Codec;

import bytesnek.tq.level.world.dimension.Comatose;

/**
 * Created by SnakerBone on 25/08/2023
 **/
public class SwirlRubbleFeature extends RubbleFeature
{
    public SwirlRubbleFeature(Codec<BlockStateConfiguration> codec)
    {
        super(codec, 7, Comatose.getBlockList(), 3, 5);
    }

    @Override
    public void placeStud(BlockPos pos, BlockState state)
    {
        placeBlock(pos.above(), state);
        placeBlock(pos.below(), state);
        for (BlockPos under = pos.below(); isReplaceableAt(under); under = under.below()) {
            placeBlock(under, state);
        }
    }

    @Override
    public void placeBase(RandomSource random, BlockPos pos, BlockState state)
    {
        if (placeBlock(pos.north(), state) && random.nextBoolean()) {
            placeStud(pos.north(), state);
        }
        if (placeBlock(pos.south(), state) && random.nextBoolean()) {
            placeStud(pos.south(), state);
        }
        if (placeBlock(pos.east(), state) && random.nextBoolean()) {
            placeStud(pos.east(), state);
        }
        if (placeBlock(pos.west(), state) && random.nextBoolean()) {
            placeStud(pos.west(), state);
        }
    }
}
