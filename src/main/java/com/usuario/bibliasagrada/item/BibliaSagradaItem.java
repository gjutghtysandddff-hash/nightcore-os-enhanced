package com.usuario.bibliasagrada.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Item "Bíblia Sagrada".
 *
 * - Enquanto está na mão (principal ou secundária), repele suavemente monstros
 *   próximos (ver RepelEventHandler, que roda a cada tick).
 * - Ao usar (clique direito), solta uma "rajada de fé": empurra com força todos
 *   os monstros num raio maior e cega-os por um instante.
 */
public class BibliaSagradaItem extends Item {

    public static final double RAIO_RAJADA = 8.0D;
    public static final double FORCA_EMPURRAO_RAJADA = 1.2D;
    public static final int COOLDOWN_TICKS = 20 * 15; // 15 segundos

    public BibliaSagradaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            usarRajadaDeFe(level, player);
        }

        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.4F);
        player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        player.swing(hand, true);

        return InteractionResultHolder.consume(stack);
    }

    private void usarRajadaDeFe(Level level, Player player) {
        AABB area = player.getBoundingBox().inflate(RAIO_RAJADA);
        List<Monster> monstros = level.getEntitiesOfClass(Monster.class, area);

        for (Monster monstro : monstros) {
            empurrarParaFora(player, monstro, FORCA_EMPURRAO_RAJADA);
            monstro.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20 * 3, 0));
            monstro.hurtMarked = true;

            if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.END_ROD,
                        monstro.getX(), monstro.getY() + 1.0, monstro.getZ(),
                        6, 0.3, 0.3, 0.3, 0.02);
            }
        }
    }

    /**
     * Empurra a entidade para longe da origem (o jogador), na horizontal.
     */
    public static void empurrarParaFora(LivingEntity origem, LivingEntity alvo, double forca) {
        double dx = alvo.getX() - origem.getX();
        double dz = alvo.getZ() - origem.getZ();
        double distancia = Math.sqrt(dx * dx + dz * dz);

        if (distancia < 0.1) {
            dx = 1.0;
            dz = 0.0;
            distancia = 1.0;
        }

        double dirX = dx / distancia;
        double dirZ = dz / distancia;

        // Quanto mais perto, mais forte o empurrão
        double intensidade = forca * Mth.clamp(1.5 - (distancia / 10.0), 0.15, 1.5);

        Vec3 motion = alvo.getDeltaMovement();
        alvo.setDeltaMovement(
                motion.x + dirX * intensidade,
                motion.y + 0.15,
                motion.z + dirZ * intensidade
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.bibliasagrada.biblia_sagrada.tooltip1"));
        tooltip.add(Component.translatable("item.bibliasagrada.biblia_sagrada.tooltip2"));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true; // Dá um brilho encantado no item
    }
}
