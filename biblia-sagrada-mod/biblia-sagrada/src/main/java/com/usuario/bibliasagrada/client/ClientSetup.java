package com.usuario.bibliasagrada.client;

import com.usuario.bibliasagrada.BibliaSagradaMod;
import com.usuario.bibliasagrada.entity.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Registrado apenas no lado cliente (Dist.CLIENT), para o servidor
 * dedicado nunca tentar carregar classes de renderização.
 */
@Mod.EventBusSubscriber(modid = BibliaSagradaMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.JESUS.get(), JesusRenderer::new);
    }
}
