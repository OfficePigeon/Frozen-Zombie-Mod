package fun.wich.mixin;

import fun.wich.ZombieFreezeTracker;
import fun.wich.FrozenZombiesMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieEntity.class)
public abstract class FrozenZombies_ZombieEntityMixin extends HostileEntity implements ZombieFreezeTracker {
	@Unique @SuppressWarnings("WrongEntityDataParameterClass")
	private static final TrackedData<Boolean> ZOMBIE_CONVERTING_IN_SNOW = DataTracker.registerData(ZombieEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	@Unique
	private int inPowderSnowTime;
	@Unique
	private int ticksUntilSnowConversion;
	protected FrozenZombies_ZombieEntityMixin(EntityType<? extends HostileEntity> entityType, World world) { super(entityType, world); }
	@Inject(method="initDataTracker", at=@At("TAIL"))
	protected void Mixin_InitDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(ZOMBIE_CONVERTING_IN_SNOW, false);
	}
	@Override
	public boolean canFreeze() { return this.getType() != EntityType.ZOMBIE && super.canFreeze(); }
	@Override
	public boolean ZombieFreezeTracker_IsShaking() { return this.getDataTracker().get(ZOMBIE_CONVERTING_IN_SNOW); }
	@Inject(method="writeCustomDataToNbt", at=@At("TAIL"))
	protected void Mixin_WriteCustomData(NbtCompound view, CallbackInfo ci) {
		view.putInt("InPowderSnow", this.isTouchingWater() ? this.inPowderSnowTime : -1);
		view.putInt("SnowConversionTime", this.getDataTracker().get(ZOMBIE_CONVERTING_IN_SNOW) ? this.ticksUntilSnowConversion : -1);
	}
	@Inject(method="readCustomDataFromNbt", at=@At("TAIL"))
	protected void Mixin_ReadCustomData(NbtCompound view, CallbackInfo ci) {
		this.inPowderSnowTime = view.contains("InPowderSnow") ? view.getInt("InPowderSnow") : -1;
		int i = view.contains("SnowConversionTime") ? view.getInt("SnowConversionTime") : -1;
		if (i < 0) this.ticksUntilSnowConversion = -1;
		this.getDataTracker().set(ZOMBIE_CONVERTING_IN_SNOW, false);
	}
	@Inject(method="tick", at=@At("HEAD"))
	public void Mixin_Tick(CallbackInfo ci) {
		if (this.getType() != EntityType.ZOMBIE) return; //only default zombies can convert
		World world = this.getEntityWorld();
		if (!world.isClient() && this.isAlive() && !this.isAiDisabled()) {
			if (this.inPowderSnow || this.wasInPowderSnow) {
				if (this.getDataTracker().get(ZOMBIE_CONVERTING_IN_SNOW)) {
					--this.ticksUntilSnowConversion;
					if (this.ticksUntilSnowConversion < 0) {
						this.convertTo(FrozenZombiesMod.FROZEN_ZOMBIE, true);
						if (!this.isSilent()) {
							this.getEntityWorld().playSound(null, this.getBlockPos(), FrozenZombiesMod.ENTITY_ZOMBIE_CONVERTED_TO_FROZEN_ZOMBIE, SoundCategory.HOSTILE, 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1);
						}
					}
				}
				else {
					++this.inPowderSnowTime;
					if (this.inPowderSnowTime >= 600) {
						this.ticksUntilSnowConversion = 300;
						this.getDataTracker().set(ZOMBIE_CONVERTING_IN_SNOW, true);
					}
				}
			}
			else {
				this.inPowderSnowTime = -1;
				this.getDataTracker().set(ZOMBIE_CONVERTING_IN_SNOW, false);
			}
		}
	}
}
