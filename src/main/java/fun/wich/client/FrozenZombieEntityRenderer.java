package fun.wich.client;

import fun.wich.FrozenZombieEntity;
import fun.wich.FrozenZombiesMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FrozenZombieEntityRenderer extends ZombieBaseEntityRenderer<FrozenZombieEntity, DrownedEntityModel<FrozenZombieEntity>> {
	private static final Identifier TEXTURE = Identifier.of(FrozenZombiesMod.MOD_ID, "textures/entity/zombie/frozen.png");
	public FrozenZombieEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new DrownedEntityModel<>(context.getPart(EntityModelLayers.DROWNED)), new DrownedEntityModel<>(context.getPart(EntityModelLayers.DROWNED_INNER_ARMOR)), new DrownedEntityModel<>(context.getPart(EntityModelLayers.DROWNED_OUTER_ARMOR)));
		this.addFeature(new FrozenZombieOverlayFeatureRenderer(this, context.getModelLoader()));
	}
	@Override public Identifier getTexture(FrozenZombieEntity state) { return TEXTURE; }
}

