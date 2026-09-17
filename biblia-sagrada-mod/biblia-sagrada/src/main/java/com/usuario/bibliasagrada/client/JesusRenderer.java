package com.usuario.bibliasagrada.client;

import com.usuario.bibliasagrada.entity.JesusEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import com.usuario.bibliasagrada.BibliaSagradaMod;

/**
 * Renderiza a entidade Jesus reaproveitando o modelo humanoide padrão
 * (mesmo esqueleto usado pelo jogador), com uma textura própria.
 */
public class JesusRenderer extends HumanoidMobRenderer<JesusEntity, HumanoidModel<JesusEntity>> {

    private static final ResourceLocation TEXTURA =
            new ResourceLocation(BibliaSagradaMod.MOD_ID, "textures/entity/jesus.png");

    public JesusRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(JesusEntity entity) {
        return TEXTURA;
    }
}
