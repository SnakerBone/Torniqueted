package xyz.snaker.tq.level.item.icon;

import java.util.function.Consumer;

import xyz.snaker.snakerlib.client.Icon;
import xyz.snaker.snakerlib.data.DefaultItemProperties;
import xyz.snaker.tq.client.render.icon.ItemTabIconRenderer;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import org.jetbrains.annotations.NotNull;

/**
 * Created by SnakerBone on 12/06/2023
 **/
public class ItemTabIcon extends Item implements Icon
{
    public ItemTabIcon(Properties properties)
    {
        super(properties);
    }

    public ItemTabIcon()
    {
        super(DefaultItemProperties.EMPTY);
    }


    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new IClientItemExtensions()
        {
            public ItemTabIconRenderer getRenderer()
            {
                return new ItemTabIconRenderer();
            }

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer()
            {
                return getRenderer();
            }
        });
    }
}
