package xyz.snaker.tq.client.render.type;

import java.util.Map;
import java.util.function.Supplier;

import xyz.snaker.snakerlib.client.render.processor.SimpleRenderTypeProcessor;
import xyz.snaker.snakerlib.concurrent.AsyncHashMap;
import xyz.snaker.snakerlib.utility.Checks;
import xyz.snaker.tq.Tourniqueted;
import xyz.snaker.tq.client.Shaders;
import xyz.snaker.tq.rego.BlockEntities;
import xyz.snaker.tq.rego.Blocks;
import xyz.snaker.tq.rego.Items;

import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.datafixers.util.Pair;

/**
 * Created by SnakerBone on 12/08/2023
 **/
public enum ItemLikeRenderType implements SimpleRenderTypeProcessor
{
    BLACK_STARS(Shaders::getBlackStars),
    WHITE_STARS(Shaders::getWhiteStars),
    RED_STARS(Shaders::getRedStars),
    GREEN_STARS(Shaders::getGreenStars),
    BLUE_STARS(Shaders::getBlueStars),
    YELLOW_STARS(Shaders::getYellowStars),
    PINK_STARS(Shaders::getPinkStars),
    PURPLE_STARS(Shaders::getPurpleStars),
    PULSE(Shaders::getPulse),
    FIRE(Shaders::getFire),
    BLUR_FOG(Shaders::getBlurFog),
    PLAIN(Shaders::getPlain),
    SWIRL(Shaders::getSwirl),
    SNOWFLAKE(Shaders::getSnowflake),
    WATERCOLOUR(Shaders::getWatercolour),
    MULTICOLOUR(Shaders::getMulticolour),
    CLIP(Shaders::getClip),
    BURN(Shaders::getBurn),
    STRANDS(Shaders::getStrands);

    private final Supplier<ShaderInstance> shader;
    private final RenderType type;
    private static final Map<ItemLikeRenderType, Block> overlayMap = Util.make(new AsyncHashMap<>(), map ->
    {
        map.put(SWIRL, Blocks.SHIMMER_OVERLAY.get());
        map.put(SNOWFLAKE, Blocks.TURQUOISE_OVERLAY.get());
        map.put(WATERCOLOUR, Blocks.CREME_OVERLAY.get());
        map.put(MULTICOLOUR, Blocks.SKIN_OVERLAY.get());
        map.put(BLACK_STARS, Blocks.WHITE_OVERLAY.get());
        map.put(BURN, Blocks.LIGHT_OVERLAY.get());
        map.put(BLUR_FOG, Blocks.BRIGHT_OVERLAY.get());
        map.put(STRANDS, Blocks.BRIGHT_OVERLAY.get());
        map.put(CLIP, Blocks.DARK_OVERLAY.get());
        map.put(FIRE, Blocks.DARK_OVERLAY.get());
    });
    private static final Map<BlockEntityType<?>, ItemLikeRenderType> blockEntityMap = Util.make(new AsyncHashMap<>(), map ->
    {
        map.put(BlockEntities.SWIRL.get(), SWIRL);
        map.put(BlockEntities.SNOWFLAKE.get(), SNOWFLAKE);
        map.put(BlockEntities.WATERCOLOUR.get(), WATERCOLOUR);
        map.put(BlockEntities.MULTICOLOUR.get(), MULTICOLOUR);
        map.put(BlockEntities.FLARE.get(), FIRE);
        map.put(BlockEntities.STARRY.get(), BLACK_STARS);
        map.put(BlockEntities.GEOMETRIC.get(), CLIP);
        map.put(BlockEntities.BURNING.get(), BURN);
        map.put(BlockEntities.FOGGY.get(), BLUR_FOG);
        map.put(BlockEntities.STATIC.get(), STRANDS);
    });
    private static final Map<Item, ItemLikeRenderType> blockItemMap = Util.make(new AsyncHashMap<>(), map ->
    {
        map.put(Items.SWIRL_BLOCK.get(), ItemLikeRenderType.SWIRL);
        map.put(Items.SNOWFLAKE_BLOCK.get(), ItemLikeRenderType.SNOWFLAKE);
        map.put(Items.WATERCOLOUR_BLOCK.get(), ItemLikeRenderType.WATERCOLOUR);
        map.put(Items.MULTICOLOUR_BLOCK.get(), ItemLikeRenderType.MULTICOLOUR);
        map.put(Items.FLARE_BLOCK.get(), ItemLikeRenderType.FIRE);
        map.put(Items.STARRY_BLOCK.get(), ItemLikeRenderType.BLACK_STARS);
        map.put(Items.GEOMETRIC_BLOCK.get(), ItemLikeRenderType.CLIP);
        map.put(Items.BURNING_BLOCK.get(), ItemLikeRenderType.BURN);
        map.put(Items.FOGGY_BLOCK.get(), ItemLikeRenderType.BLUR_FOG);
        map.put(Items.STATIC_BLOCK.get(), ItemLikeRenderType.STRANDS);
    });

    ItemLikeRenderType(Supplier<ShaderInstance> shader)
    {
        this.shader = shader;
        this.type = create(Tourniqueted.MODID + ":" + name().toLowerCase(), new Pair<>(DefaultVertexFormat.BLOCK, normal(shader)));
    }

    public Supplier<ShaderInstance> supplier()
    {
        return shader;
    }

    public ShaderInstance shader()
    {
        return shader.get();
    }

    public RenderType get()
    {
        return type;
    }

    public static ItemLikeRenderType getFromBlockEntity(BlockEntityType<?> type)
    {
        Checks.checkKeyExists(blockEntityMap, type);
        return blockEntityMap.get(type);
    }

    public static ItemLikeRenderType getFromBlockItem(BlockItem item)
    {
        Checks.checkKeyExists(blockItemMap, item);
        return blockItemMap.get(item);
    }

    public static Block getOverlayFromType(ItemLikeRenderType type)
    {
        Checks.checkKeyExists(overlayMap, type);
        return overlayMap.get(type);
    }

    public static Block getOverlayFromBlockEntity(BlockEntityType<?> type)
    {
        Checks.checkKeyExists(blockEntityMap, type);
        return getOverlayFromType(blockEntityMap.get(type));
    }

    public static Block getOverlayFromBlockItem(Item item)
    {
        Checks.checkKeyExists(blockItemMap, item);
        return getOverlayFromType(blockItemMap.get(item));
    }
}
