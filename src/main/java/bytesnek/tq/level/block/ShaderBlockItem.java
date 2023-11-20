package bytesnek.tq.level.block;

import java.util.function.Consumer;

import xyz.snaker.snakerlib.utility.item.ItemProperties;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.RegistryObject;

import org.jetbrains.annotations.NotNull;

import bytesnek.tq.client.renderer.block.ShaderBlockItemRenderer;
import bytesnek.tq.client.renderer.type.ItemLikeRenderType;

/**
 * Created by SnakerBone on 28/04/2023
 **/
public class ShaderBlockItem extends BlockItem
{
    public ShaderBlockItem(RegistryObject<Block> block)
    {
        super(block.get(), ItemProperties.EMPTY);
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer)
    {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions()
        {
            public BlockEntityWithoutLevelRenderer getRenderer()
            {
                return new ShaderBlockItemRenderer(ItemLikeRenderType.getFromBlockItem(ShaderBlockItem.this).get());
            }

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer()
            {
                return getRenderer();
            }
        });
    }
}