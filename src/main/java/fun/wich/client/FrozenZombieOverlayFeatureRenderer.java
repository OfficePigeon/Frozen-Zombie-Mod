package fun.wich.client;

import fun.wich.FrozenZombiesMod;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.DrownedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.state.ZombieEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FrozenZombieOverlayFeatureRenderer extends FeatureRenderer<ZombieEntityRenderState, DrownedEntityModel> {
	private static final Identifier SKIN = Identifier.of(FrozenZombiesMod.MOD_ID, "textures/entity/zombie/frozen_outer_layer.png");
	private final DrownedEntityModel model;
	private final DrownedEntityModel babyModel;
	public FrozenZombieOverlayFeatureRenderer(FeatureRendererContext<ZombieEntityRenderState, DrownedEntityModel> context, LoadedEntityModels loader) {
		super(context);
		this.model = new DrownedEntityModel(loader.getModelPart(EntityModelLayers.DROWNED_OUTER));
		this.babyModel = new DrownedEntityModel(loader.getModelPart(EntityModelLayers.DROWNED_BABY_OUTER));
	}
	@Override
	public void render(MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, int i, ZombieEntityRenderState state, float headYaw, float headPitch) {
		render(state.baby ? this.babyModel : this.model, SKIN, matrixStack, orderedRenderCommandQueue, i, state, -1, 1);
	}
}
