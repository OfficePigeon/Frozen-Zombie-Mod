package fun.wich.client;

import fun.wich.FrozenZombieEntity;
import fun.wich.FrozenZombiesMod;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.DrownedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FrozenZombieOverlayFeatureRenderer extends FeatureRenderer<FrozenZombieEntity, DrownedEntityModel<FrozenZombieEntity>> {
	private static final Identifier SKIN = Identifier.of(FrozenZombiesMod.MOD_ID, "textures/entity/zombie/frozen_outer_layer.png");
	private final DrownedEntityModel<FrozenZombieEntity> model;
	public FrozenZombieOverlayFeatureRenderer(FeatureRendererContext<FrozenZombieEntity, DrownedEntityModel<FrozenZombieEntity>> context, EntityModelLoader loader) {
		super(context);
		this.model = new DrownedEntityModel<>(loader.getModelPart(EntityModelLayers.DROWNED_OUTER));
	}
	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider orderedRenderCommandQueue, int light, FrozenZombieEntity state, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		render(this.getContextModel(), this.model, SKIN, matrixStack, orderedRenderCommandQueue, light, state, limbAngle, limbDistance, animationProgress, headYaw, headPitch, tickDelta, -1);
	}
}
