package xyz.snaker.tq.level.item;

import xyz.snaker.snakerlib.data.DefaultItemProperties;
import xyz.snaker.snakerlib.math.Maths;
import xyz.snaker.tq.level.world.dimension.Comatose;
import xyz.snaker.tq.rego.Effects;
import xyz.snaker.tq.rego.Keys;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

/**
 * Created by SnakerBone on 7/07/2023
 **/
public class Tourniquet extends Item
{
    public Tourniquet(Properties properties)
    {
        super(properties);
    }

    public Tourniquet()
    {
        super(DefaultItemProperties.SPECIAL);
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int remainingUseDuration)
    {
        MobEffectInstance syncopeEffect = new MobEffectInstance(Effects.SYNCOPE.get(), Maths.secondsToTicks(3));
        int useDuration = getUseDuration(stack);
        if (!level.isClientSide) {
            if (entity instanceof Player player) {
                player.addEffect(syncopeEffect);
                if (Maths.diffEquals(useDuration, remainingUseDuration, 50)) {
                    if (player.canChangeDimensions()) {
                        ResourceKey<Level> key = level.dimension() == Keys.COMATOSE ? Level.OVERWORLD : Keys.COMATOSE;
                        MinecraftServer server = level.getServer();
                        if (server != null) {
                            ServerLevel dimension = server.getLevel(key);
                            if (dimension != null) {
                                player.stopUsingItem();
                                player.level();
                                stack.hurtAndBreak(Integer.MAX_VALUE, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                                player.changeDimension(dimension, Comatose.getTeleporter());
                                if (player.hasEffect(syncopeEffect.getEffect())) {
                                    player.removeEffect(syncopeEffect.getEffect());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand)
    {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack)
    {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack)
    {
        return 72000;
    }
}
