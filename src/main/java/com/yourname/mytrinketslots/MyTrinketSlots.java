package com.yourname.mytrinketslots;

import eu.pb4.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MyTrinketSlots implements ModInitializer {
	@Override
	public void onInitialize() {
		ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {
			if (entity instanceof ServerPlayer player) {
				// 获取玩家的饰品附件
				var attachment = TrinketsApi.getAttachment(player);

				// 获取所有已装备的物品（所有槽位）
				var allEquipped = attachment.getAllEquipped();

				// 遍历所有已装备的饰品，查找不死图腾
				for (var entry : allEquipped) {
					ItemStack stack = entry.getB();
					if (stack.is(Items.TOTEM_OF_UNDYING)) {
						// 触发不死图腾效果
						player.setHealth(1.0F);
						player.removeAllEffects();
						player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
						player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
						player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
						player.level().broadcastEntityEvent(player, (byte) 35);

						// 消耗一个图腾
						stack.shrink(1);
						return false; // 阻止死亡
					}
				}
			}
			return true; // 允许死亡（如果没有图腾）
		});
	}
}