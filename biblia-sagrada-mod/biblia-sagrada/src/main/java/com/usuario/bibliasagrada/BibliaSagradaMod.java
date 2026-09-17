package com.usuario.bibliasagrada;

import com.usuario.bibliasagrada.entity.JesusEntity;
import com.usuario.bibliasagrada.entity.ModEntities;
import com.usuario.bibliasagrada.event.InfernoPullHandler;
import com.usuario.bibliasagrada.event.RepelEventHandler;
import com.usuario.bibliasagrada.item.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BibliaSagradaMod.MOD_ID)
public class BibliaSagradaMod {

    public static final String MOD_ID = "bibliasagrada";

    public BibliaSagradaMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registra os itens e entidades do mod
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);

        // Adiciona os itens na aba de criativo de "Ingredientes"
        modEventBus.addListener(this::buildCreativeTabs);

        // Registra os atributos da entidade Jesus (obrigatório, senão o jogo trava ao spawná-la)
        modEventBus.addListener(this::registerAttributes);

        // Handlers de eventos (aura que repele monstros / puxada pro inferno)
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new RepelEventHandler());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new InfernoPullHandler());
    }

    private void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.BIBLIA_SAGRADA);
        } else if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.ESPADA_MIGUEL);
        }
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.JESUS.get(), JesusEntity.createAttributes().build());
    }
}
