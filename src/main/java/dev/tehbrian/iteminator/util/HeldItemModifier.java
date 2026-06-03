package dev.tehbrian.iteminator.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.PlayerSource;

import java.util.function.Function;

import static dev.tehbrian.iteminator.util.ItemModifier.itemModifier;

public final class HeldItemModifier {

	private HeldItemModifier() {
	}

	public static void modify(
			final CommandContext<PlayerSource> context,
			final Function<ItemModifier, @Nullable ItemModifier> operator
	) {
		modify(context.sender().source(), operator);
	}

	/**
	 * Applies the given operator to the item in the player's main hand.
	 * If the operator returns null, nothing will happen to the item.
	 *
	 * @param player   the player to target
	 * @param operator the operator to apply to the item in the main hand
	 */
	public static void modify(
			final Player player,
			final Function<ItemModifier, @Nullable ItemModifier> operator
	) {
		modifyRaw(
				player, i -> {
					final @Nullable ItemModifier modifier = operator.apply(itemModifier(i));
					if (modifier == null) {
						return null;
					}

					return modifier.yank();
				}
		);
	}

	public static void modifyRaw(
			final CommandContext<PlayerSource> context,
			final Function<ItemStack, @Nullable ItemStack> operator
	) {
		modifyRaw(context.sender().source(), operator);
	}

	/**
	 * Applies the given operator to the item in the player's main hand.
	 * If the operator returns null, nothing will happen to the item.
	 *
	 * @param player   the player to target
	 * @param operator the operator to apply to the item in the main hand
	 */
	public static void modifyRaw(
			final Player player,
			final Function<ItemStack, @Nullable ItemStack> operator
	) {
		final PlayerInventory inventory = player.getInventory();
		final ItemStack item = inventory.getItemInMainHand();

		if (item.getType().equals(Material.AIR)) {
			// it's air and therefore cannot be modified.
			return;
		}

		final @Nullable ItemStack modifiedItem = operator.apply(item);
		if (modifiedItem == null) {
			return;
		}

		inventory.setItemInMainHand(modifiedItem);
	}

}
