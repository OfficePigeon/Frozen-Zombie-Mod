package fun.wich.client;

import fun.wich.FrozenZombieEntity;
import fun.wich.FrozenZombiesMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.entity.state.ZombieEntityRenderState;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FrozenZombieEntityRenderer extends ZombieBaseEntityRenderer<FrozenZombieEntity, ZombieEntityRenderState, DrownedEntityModel> {
	private static final Identifier TEXTURE = Identifier.of(FrozenZombiesMod.MOD_ID, "textures/entity/zombie/frozen.png");
	public FrozenZombieEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new DrownedEntityModel(context.getPart(EntityModelLayers.DROWNED)), new DrownedEntityModel(context.getPart(EntityModelLayers.DROWNED_BABY)), EquipmentModelData.mapToEntityModel(EntityModelLayers.DROWNED_EQUIPMENT, context.getEntityModels(), DrownedEntityModel::new), EquipmentModelData.mapToEntityModel(EntityModelLayers.DROWNED_BABY_EQUIPMENT, context.getEntityModels(), DrownedEntityModel::new));
		this.addFeature(new FrozenZombieOverlayFeatureRenderer(this, context.getEntityModels()));
	}
	@Override public ZombieEntityRenderState createRenderState() { return new ZombieEntityRenderState(); }
	@Override public Identifier getTexture(ZombieEntityRenderState state) { return TEXTURE; }
}

