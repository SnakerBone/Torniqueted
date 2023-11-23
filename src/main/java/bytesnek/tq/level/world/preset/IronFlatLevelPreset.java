package bytesnek.tq.level.world.preset;

import java.util.List;
import java.util.Optional;

import bytesnek.snakerlib.level.levelgen.flat.FlatLayer;
import bytesnek.snakerlib.level.levelgen.flat.FlatLayerList;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created by SnakerBone on 18/10/2023
 **/
public class IronFlatLevelPreset
{
    public static FlatLevelGeneratorPreset create(BootstapContext<FlatLevelGeneratorPreset> context)
    {
        HolderGetter<Biome> biomeSearch = context.lookup(Registries.BIOME);
        FlatLevelGeneratorSettings settings = new FlatLevelGeneratorSettings(Optional.empty(), biomeSearch.getOrThrow(Biomes.PLAINS), List.of());
        List<FlatLayerInfo> infos = settings.getLayersInfo();
        FlatLayerList list = FlatLayerList.of();

        list.addAll(FlatLayer.of(5, Blocks.IRON_BLOCK), FlatLayer.of(Blocks.BEDROCK));

        for (int i = list.size() - 1; i >= 0; i--) {
            infos.add(list.getInfo(i));
        }

        return new FlatLevelGeneratorPreset(ForgeRegistries.ITEMS.getDelegateOrThrow(Items.IRON_BLOCK), settings);
    }
}
