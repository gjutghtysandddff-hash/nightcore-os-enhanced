package com.usuario.bibliasagrada.event;

import com.usuario.bibliasagrada.item.BibliaSagradaItem;
import com.usuario.bibliasagrada.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * Enquanto o jogador estiver com a Bíblia Sagrada em uma das mãos,
 * monstros hostis num raio pequeno são repelidos suavemente a cada tick,
 * como se houvesse uma "aura sagrada" ao redor do jogador.
 */
public class RepelEventHandler {

    private static final double RAIO_AURA = 5.0D;
    private static final double FORCA_AURA = 0.35D;
    private static final int INTERVALO_PARTICULAS = 10; // a cada 10 ticks

    private int contadorTicks = 0;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        if (player.level().isClientSide) return;

        if (!estaSegurandoBiblia(player)) return;

        contadorTicks++;

        AABB area = player.getBoundingBox().inflate(RAIO_AURA);
        List<Monster> monstros = player.level().getEntitiesOfClass(Monster.class, area);

        for (Monster monstro : monstros) {
            BibliaSagradaItem.empurrarParaFora(player, monstro, FORCA_AURA);
        }

        if (contadorTicks % INTERVALO_PARTICULAS == 0 && player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    4, RAIO_AURA * 0.4, 0.5, RAIO_AURA * 0.4, 0.01);
        }
    }

    private boolean estaSegurandoBiblia(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        return mainHand.is(ModItems.BIBLIA_SAGRADA.get()) || offHand.is(ModItems.BIBLIA_SAGRADA.get());
    }
}
