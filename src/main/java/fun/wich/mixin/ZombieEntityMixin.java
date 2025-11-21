package fun.wich.mixin;

import fun.wich.FreezeConversionEntity;
import fun.wich.FrozenZombiesMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.conversion.EntityConversionContext;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends HostileEntity implements FreezeConversionEntity {
	@Shadow
	public abstract EntityType<? extends ZombieEntity> getType();

	@Unique
	private int inPowderSnowTime;
	@Unique
	private int ticksUntilSnowConversion;

	protected ZombieEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}
	@Inject(method="initDataTracker", at=@At("TAIL"))
	protected void Mixin_InitDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(FrozenZombiesMod.ZOMBIE_CONVERTING_IN_SNOW, false);
	}
	@Override
	public boolean canFreeze() { return this.getType() != EntityType.ZOMBIE && super.canFreeze(); }
	@Unique
	public boolean Mixin_IsConvertingInSnow() {
		return this.getDataTracker().get(FrozenZombiesMod.ZOMBIE_CONVERTING_IN_SNOW);
	}
	@Override
	public boolean FreezeConversionEntity_IsShaking() { return this.Mixin_IsConvertingInSnow(); }
	@Inject(method="writeCustomData", at=@At("TAIL"))
	protected void Mixin_WriteCustomData(WriteView view, CallbackInfo ci) {
		view.putInt("InPowderSnow", this.isTouchingWater() ? this.inPowderSnowTime : -1);
		view.putInt("SnowConversionTime", this.Mixin_IsConvertingInSnow() ? this.ticksUntilSnowConversion : -1);
	}
	@Inject(method="readCustomData", at=@At("TAIL"))
	protected void Mixin_ReadCustomData(ReadView view, CallbackInfo ci) {
		this.inPowderSnowTime = view.getInt("InPowderSnow", -1);
		int i = view.getInt("SnowConversionTime", -1);
		if (i != -1) this.Mixin_SetTicksUntilSnowConversion(i);
		this.Mixin_SetConvertingInSnow(false);
	}
	@Unique
	private void Mixin_SetTicksUntilSnowConversion(int ticksUntilConversion) {
		this.ticksUntilSnowConversion = ticksUntilConversion;
		this.Mixin_SetConvertingInSnow(true);
	}
	@Unique
	private void Mixin_SetConvertingInSnow(boolean converting) {
		this.getDataTracker().set(FrozenZombiesMod.ZOMBIE_CONVERTING_IN_SNOW, converting);
	}
	@Inject(method="tick", at=@At("HEAD"))
	public void Mixin_Tick(CallbackInfo ci) {
		if (this.getType() != EntityType.ZOMBIE) return; //only default skeletons can convert
		World world = this.getEntityWorld();
		if (!world.isClient() && this.isAlive() && !this.isAiDisabled()) {
			if (this.inPowderSnow || this.wasInPowderSnow) {
				if (this.Mixin_IsConvertingInSnow()) {
					--this.ticksUntilSnowConversion;
					if (this.ticksUntilSnowConversion < 0) this.Mixin_ConvertToFrozenZombie();
				}
				else {
					++this.inPowderSnowTime;
					if (this.inPowderSnowTime >= 600) this.Mixin_SetTicksUntilSnowConversion(300);
				}
			}
			else {
				this.inPowderSnowTime = -1;
				this.Mixin_SetConvertingInSnow(false);
			}
		}
	}
	@Unique
	protected void Mixin_ConvertToFrozenZombie() {
		this.convertTo(FrozenZombiesMod.FROZEN_ZOMBIE, EntityConversionContext.create(this, true, true), zombie -> {
			if (!this.isSilent()) {
				this.getEntityWorld().playSound(null, this.getBlockPos(), FrozenZombiesMod.ENTITY_ZOMBIE_CONVERTED_TO_FROZEN_ZOMBIE, SoundCategory.HOSTILE, 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1);
			}
		});
	}
}
