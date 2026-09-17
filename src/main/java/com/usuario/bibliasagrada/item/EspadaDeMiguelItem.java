package com.usuario.bibliasagrada.item;

import com.usuario.bibliasagrada.entity.JesusEntity;
import com.usuario.bibliasagrada.entity.ModEntities;
import com.usuario.bibliasagrada.event.InfernoPullHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Espada de Miguel.
 *
 * - Ataque corpo a corpo: monstros atingidos começam a ser "puxados"
 *   por correntes para o Inferno (Nether) — ver InfernoPullHandler.
 * - Clique direito: invoca Jesus para lutar ao seu lado por um tempo.
 */
public class EspadaDeMiguelItem extends SwordItem {

    public static final int COOLDOWN_INVOCACAO_TICKS = 20 * 60; // 60 segundos

    public EspadaDeMiguelItem(Properties properties) {
        super(Tiers.NETHERITE, 6, -2.4F, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean resultado = super.hurtEnemy(stack, target, attacker);

        if (!target.level().isClientSide && target instanceof Monster && attacker instanceof Player) {
            InfernoPullHandler.iniciarPuxada(target);
        }

        return resultado;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            invocarJesus(level, player);
        }

        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.2F);
        player.getCooldowns().addCooldown(this, COOLDOWN_INVOCACAO_TICKS);
        player.swing(hand, true);

        return InteractionResultHolder.consume(stack);
    }

    private void invocarJesus(Level level, Player player) {
        JesusEntity jesus = ModEntities.JESUS.get().create(level);
        if (jesus == null) return;

        double angulo = Math.toRadians(player.getYRot());
        double x = player.getX() - Math.sin(angulo) * 2.0;
        double z = player.getZ() + Math.cos(angulo) * 2.0;

        jesus.moveTo(x, player.getY(), z, player.getYRot(), 0.0F);
        level.addFreshEntity(jesus);

        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.END_ROD, x, player.getY() + 1.0, z, 30, 0.4, 0.8, 0.4, 0.05);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.bibliasagrada.espada_miguel.tooltip1"));
        tooltip.add(Component.translatable("item.bibliasagrada.espada_miguel.tooltip2"));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
