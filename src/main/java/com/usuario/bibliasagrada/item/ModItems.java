package com.usuario.bibliasagrada.item;

import com.usuario.bibliasagrada.BibliaSagradaMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BibliaSagradaMod.MOD_ID);

    // O item principal do mod: a Bíblia Sagrada
    public static final RegistryObject<Item> BIBLIA_SAGRADA = ITEMS.register(
            "biblia_sagrada",
            () -> new BibliaSagradaItem(new Item.Properties().stacksTo(1))
    );

    // Espada de Miguel: puxa monstros pro inferno e invoca Jesus
    public static final RegistryObject<Item> ESPADA_MIGUEL = ITEMS.register(
            "espada_miguel",
            () -> new EspadaDeMiguelItem(new Item.Properties().stacksTo(1))
    );
}
