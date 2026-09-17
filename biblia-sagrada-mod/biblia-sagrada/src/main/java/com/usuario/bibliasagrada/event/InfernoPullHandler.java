package com.usuario.bibliasagrada.event;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Quando um monstro é atingido pela Espada de Miguel, ele entra numa
 * "alma sendo puxada": por alguns segundos é arrastado para baixo com
 * efeito de correntes (partículas escuras), e ao final é teleportado
 * para o Nether (o inferno) e pega fogo.
 */
public class InfernoPullHandler {

    private static final int DURACAO_PUXADA_TICKS = 60; // 3 segundos
    private static final Map<LivingEntity, Integer> ALMAS_SENDO_PUXADAS = new WeakHashMap<>();

    public static void iniciarPuxada(LivingEntity alvo) {
        ALMAS_SENDO_PUXADAS.putIfAbsent(alvo, DURACAO_PUXADA_TICKS);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || ALMAS_SENDO_PUXADAS.isEmpty()) return;

        Iterator<Map.Entry<LivingEntity, Integer>> it = ALMAS_SENDO_PUXADAS.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<LivingEntity, Integer> entrada = it.next();
            LivingEntity alvo = entrada.getKey();

            if (alvo == null || !alvo.isAlive()) {
                it.remove();
                continue;
            }

            int restante = entrada.getValue() - 1;

            // Efeito de "correntes" puxando para baixo
            Vec3 mov = alvo.getDeltaMovement();
            alvo.setDeltaMovement(mov.x * 0.25, -0.4, mov.z * 0.25);
            alvo.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 30, 1, false, false));
            alvo.hurtMarked = true;

            if (alvo.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SQUID_INK,
                        alvo.getX(), alvo.getY() + 0.3, alvo.getZ(), 4, 0.25, 0.1, 0.25, 0.01);
                serverLevel.sendParticles(ParticleTypes.SMOKE,
                        alvo.getX(), alvo.getY() + 1.0, alvo.getZ(), 2, 0.2, 0.3, 0.2, 0.01);
            }

            if (restante <= 0) {
                enviarParaOInferno(alvo);
                it.remove();
            } else {
                entrada.setValue(restante);
            }
        }
    }

    private void enviarParaOInferno(LivingEntity alvo) {
        if (!(alvo.level() instanceof ServerLevel nivelAtual)) return;

        MinecraftServer servidor = nivelAtual.getServer();
        ServerLevel nether = servidor.getLevel(Level.NETHER);
        if (nether == null) return;

        boolean jaEstaNoNether = nivelAtual.dimension() == Level.NETHER;

        double x = jaEstaNoNether ? alvo.getX() : alvo.getX() / 8.0D;
        double z = jaEstaNoNether ? alvo.getZ() : alvo.getZ() / 8.0D;
        double y = Mth.clamp(alvo.getY(), 32.0D, 100.0D);

        // Forge oferece este teleporte seguro entre dimensões
        alvo.teleportTo(nether, x, y, z, Collections.emptySet(), alvo.getYRot(), alvo.getXRot());
        alvo.setSecondsOnFire(6);

        nether.sendParticles(ParticleTypes.FLAME, x, y, z, 25, 0.5, 0.5, 0.5, 0.06);
        nether.playSound(null, x, y, z, SoundEvents.GHAST_SCREAM, SoundSource.HOSTILE, 0.8F, 1.2F);
    }
}
