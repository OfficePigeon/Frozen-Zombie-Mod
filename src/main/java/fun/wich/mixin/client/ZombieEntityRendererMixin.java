package fun.wich.mixin.client;

import fun.wich.FreezeConversionEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.client.render.entity.state.ZombieEntityRenderState;
import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ZombieEntityRenderer.class)
public abstract class ZombieEntityRendererMixin extends ZombieBaseEntityRenderer<ZombieEntity, ZombieEntityRenderState, ZombieEntityModel<ZombieEntityRenderState>> {
	protected ZombieEntityRendererMixin(EntityRendererFactory.Context context, ZombieEntityModel<ZombieEntityRenderState> mainModel, ZombieEntityModel<ZombieEntityRenderState> babyMainModel, EquipmentModelData<ZombieEntityModel<ZombieEntityRenderState>> equipmentModelData, EquipmentModelData<ZombieEntityModel<ZombieEntityRenderState>> equipmentModelData2) {
		super(context, mainModel, babyMainModel, equipmentModelData, equipmentModelData2);
	}
	@Override
	public void updateRenderState(ZombieEntity zombieEntity, ZombieEntityRenderState state, float f) {
		super.updateRenderState(zombieEntity, state, f);
		if (zombieEntity instanceof FreezeConversionEntity freeze && freeze.FreezeConversionEntity_IsShaking()) state.shaking = true;
	}
}
