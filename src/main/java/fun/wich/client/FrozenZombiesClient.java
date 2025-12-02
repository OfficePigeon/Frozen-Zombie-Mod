package fun.wich.client;

import fun.wich.FrozenZombiesMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class FrozenZombiesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(FrozenZombiesMod.FROZEN_ZOMBIE, FrozenZombieEntityRenderer::new);
		EntityRendererRegistry.register(FrozenZombiesMod.FROZEN_ZOMBIE_SNOWBALL, FlyingItemEntityRenderer::new);
	}
}
