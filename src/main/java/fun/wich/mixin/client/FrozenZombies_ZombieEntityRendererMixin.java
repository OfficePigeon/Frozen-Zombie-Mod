package fun.wich.mixin.client;

import fun.wich.ZombieFreezeTracker;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ZombieEntityRenderer.class)
public abstract class FrozenZombies_ZombieEntityRendererMixin extends ZombieBaseEntityRenderer<ZombieEntity, ZombieEntityModel<ZombieEntity>> {
	protected FrozenZombies_ZombieEntityRendererMixin(EntityRendererFactory.Context ctx, ZombieEntityModel<ZombieEntity> bodyModel, ZombieEntityModel<ZombieEntity> legsArmorModel, ZombieEntityModel<ZombieEntity> bodyArmorModel) {
		super(ctx, bodyModel, legsArmorModel, bodyArmorModel);
	}
	@Override
	protected boolean isShaking(ZombieEntity zombieEntity) {
		if (zombieEntity instanceof ZombieFreezeTracker freeze && freeze.ZombieFreezeTracker_IsShaking()) return true;
		return super.isShaking(zombieEntity) || zombieEntity.isConvertingInWater();
	}
}
