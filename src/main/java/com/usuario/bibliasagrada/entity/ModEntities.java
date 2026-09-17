package com.usuario.bibliasagrada.entity;

import com.usuario.bibliasagrada.BibliaSagradaMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BibliaSagradaMod.MOD_ID);

    public static final RegistryObject<EntityType<JesusEntity>> JESUS = ENTITY_TYPES.register(
            "jesus",
            () -> EntityType.Builder.of(JesusEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(BibliaSagradaMod.MOD_ID, "jesus").toString())
    );
}
