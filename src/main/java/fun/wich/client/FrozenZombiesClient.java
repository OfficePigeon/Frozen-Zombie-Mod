package fun.wich.client;

import fun.wich.FrozenZombiesMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class FrozenZombiesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererFactories.register(FrozenZombiesMod.FROZEN_ZOMBIE, FrozenZombieEntityRenderer::new);
		EntityRendererFactories.register(FrozenZombiesMod.SLOWING_SNOWBALL, FlyingItemEntityRenderer::new);
	}
}
