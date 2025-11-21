package fun.wich;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;

import java.util.function.Function;

public class FrozenZombiesMod implements ModInitializer {
	public static final String MOD_ID = "wich";
	public static final SoundEvent ENTITY_FROZEN_ZOMBIE_AMBIENT = register("entity.frozen_zombie.ambient");
	public static final SoundEvent ENTITY_FROZEN_ZOMBIE_DEATH = register("entity.frozen_zombie.death");
	public static final SoundEvent ENTITY_FROZEN_ZOMBIE_HURT = register("entity.frozen_zombie.hurt");
	public static final SoundEvent ENTITY_FROZEN_ZOMBIE_STEP = register("entity.frozen_zombie.step");
	public static final SoundEvent ENTITY_FROZEN_ZOMBIE_CONVERTED_TO_ZOMBIE = register("entity.frozen_zombie.converted_to_zombie");
	public static final SoundEvent ENTITY_ZOMBIE_CONVERTED_TO_FROZEN_ZOMBIE = register("entity.zombie.converted_to_frozen_zombie");
	public static final SoundEvent ENTITY_PARROT_IMITATE_FROZEN_ZOMBIE = register("entity.parrot.imitate.sunken_skeleton");
	private static SoundEvent register(String path) {
		Identifier id = Identifier.of(MOD_ID, path);
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}
	public static final TrackedData<Boolean> ZOMBIE_CONVERTING_IN_SNOW = DataTracker.registerData(ZombieEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public static final TagKey<Biome> TAG_SPAWNS_FROZEN_ZOMBIES = TagKey.of(RegistryKeys.BIOME, Identifier.of(MOD_ID, "spawns_frozen_zombies"));
	public static final EntityType<FrozenZombieEntity> FROZEN_ZOMBIE = register(
			"frozen_zombie",
			EntityType.Builder.create(FrozenZombieEntity::new, SpawnGroup.MONSTER)
					.dimensions(0.6F, 1.99F)
					.eyeHeight(1.74F)
					.vehicleAttachment(-0.7F)
					.maxTrackingRange(8)
					.notAllowedInPeaceful()
	);
	public static final EntityType<FrozenZombieSnowballEntity> FROZEN_ZOMBIE_SNOWBALL = register(
			"frozen_zombie_snowball",
			EntityType.Builder.<FrozenZombieSnowballEntity>create(FrozenZombieSnowballEntity::new, SpawnGroup.MISC)
					.dropsNothing()
					.dimensions(0.25F, 0.25F)
					.maxTrackingRange(4)
					.trackingTickInterval(10)
	);
	private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> type) {
		RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MOD_ID, name));
		EntityType<T> entityType = type.build(key);
		Registry.register(Registries.ENTITY_TYPE, key, entityType);
		return entityType;
	}
	public static final Item FROZEN_ZOMBIE_SPAWN_EGG = register("frozen_zombie_spawn_egg", SpawnEggItem::new, new Item.Settings().spawnEgg(FROZEN_ZOMBIE));
	public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, name));
		Item item = itemFactory.apply(settings.registryKey(key));
		Registry.register(Registries.ITEM, key, item);
		return item;
	}
	@Override
	public void onInitialize() {
		//Attributes
		FabricDefaultAttributeRegistry.register(FROZEN_ZOMBIE, FrozenZombieEntity.createZombieAttributes());
		//Spawning
		SpawnRestriction.register(FROZEN_ZOMBIE, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, FrozenZombieEntity::canSpawn);
		BiomeModifications.addSpawn(BiomeSelectors.tag(TAG_SPAWNS_FROZEN_ZOMBIES),
				SpawnGroup.MONSTER, FROZEN_ZOMBIE, 40, 1, 4);
		//Items
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(itemGroup -> itemGroup.add(FROZEN_ZOMBIE_SPAWN_EGG));
	}
}