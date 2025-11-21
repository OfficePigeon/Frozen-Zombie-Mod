package fun.wich;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class FrozenZombieEntity extends ZombieEntity implements RangedAttackMob {
	public FrozenZombieEntity(EntityType<? extends ZombieEntity> entityType, World world) { super(entityType, world); }

	@Override
	protected void initCustomGoals() {
		this.goalSelector.add(2, new FrozenZombieAttackGoal(this, 1.0, false));
		this.goalSelector.add(2, new SlowingSnowballEntity.SlowingProjectileAttackGoal(this, 1.25, 20, 10.0f));
		this.goalSelector.add(6, new MoveThroughVillageGoal(this, 1.0, true, 4, this::canBreakDoors));
		this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
		this.targetSelector.add(1, (new RevengeGoal(this)).setGroupRevenge(ZombifiedPiglinEntity.class));
		this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, false));
		this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
		this.targetSelector.add(5, new ActiveTargetGoal<>(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
	}

	@Override
	public boolean tryAttack(ServerWorld world, Entity target) {
		boolean bl = super.tryAttack(world, target);
		if (bl && this.getMainHandStack().isEmpty() && target instanceof LivingEntity living) {
			float f = this.getEntityWorld().getLocalDifficulty(this.getBlockPos()).getLocalDifficulty();
			living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 140 * (int)f), this);
		}
		return bl;
	}
	public static class FrozenZombieAttackGoal extends ZombieAttackGoal {
		public FrozenZombieAttackGoal(ZombieEntity zombie, double speed, boolean pauseWhenMobIdle) { super(zombie, speed, pauseWhenMobIdle); }
		@Override
		public boolean canStart() { return super.canStart() && SlowingSnowballEntity.targetSlowed(this.mob.getTarget()); }
		@Override
		public boolean shouldContinue() { return super.shouldContinue() && SlowingSnowballEntity.targetSlowed(this.mob.getTarget()); }
	}
	public static boolean canSpawn(EntityType<FrozenZombieEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		BlockPos blockPos = pos;
		while (true) { if (!world.getBlockState(blockPos = blockPos.up()).isOf(Blocks.POWDER_SNOW)) break; }
		return canSpawnInDark(type, world, spawnReason, pos, random) && (spawnReason == SpawnReason.SPAWNER || world.isSkyVisible(blockPos.down()));
	}

	@Override
	public void shootAt(LivingEntity target, float pullProgress) {
		double d = target.getX() - this.getX();
		double e = target.getEyeY() - 1.1;
		double f = target.getZ() - this.getZ();
		double g = Math.sqrt(d * d + f * f) * 0.2;
		World var12 = this.getEntityWorld();
		if (var12 instanceof ServerWorld serverWorld) {
			ItemStack itemStack = new ItemStack(Items.SNOWBALL);
			ProjectileEntity.spawn(new SlowingSnowballEntity(serverWorld, this, itemStack), serverWorld, itemStack, (entity) -> entity.setVelocity(d, e + g - entity.getY(), f, 1.6F, 12.0F));
		}
		this.playSound(SoundEvents.ENTITY_SNOWBALL_THROW, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
	}
	@Override protected SoundEvent getAmbientSound() { return FrozenZombiesMod.ENTITY_FROZEN_ZOMBIE_AMBIENT; }
	@Override protected SoundEvent getHurtSound(DamageSource source) { return FrozenZombiesMod.ENTITY_FROZEN_ZOMBIE_HURT; }
	@Override protected SoundEvent getDeathSound() { return FrozenZombiesMod.ENTITY_FROZEN_ZOMBIE_DEATH; }
	@Override protected SoundEvent getStepSound() { return FrozenZombiesMod.ENTITY_FROZEN_ZOMBIE_STEP; }

	protected boolean canConvertInWater() { return true; }
	protected void convertInWater() {
		this.convertTo(EntityType.ZOMBIE);
		this.getEntityWorld().playSound(null, this.getBlockPos(), FrozenZombiesMod.ENTITY_FROZEN_ZOMBIE_CONVERTED_TO_ZOMBIE, SoundCategory.HOSTILE, 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
	}
	@Override public boolean canFreeze() { return false; }
	@Override protected void initEquipment(Random random, LocalDifficulty localDifficulty) { }
}
