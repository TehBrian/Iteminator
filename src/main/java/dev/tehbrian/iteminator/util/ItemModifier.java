package dev.tehbrian.iteminator.util;

import io.papermc.paper.datacomponent.DataComponentType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

import static com.google.common.base.Predicates.alwaysTrue;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public final class ItemModifier {

	private final ItemStack item;

	private ItemModifier(final ItemStack item) {
		this.item = item.clone();
	}

	private ItemModifier(final ItemType type) {
		this.item = type.createItemStack();
	}

	public static ItemModifier itemModifier(final ItemStack item) {
		return new ItemModifier(item);
	}

	public static ItemModifier itemModifier(final ItemType type) {
		return new ItemModifier(type);
	}

	public ItemStack yank() {
		return this.item;
	}

	public ItemModifier amount(final int amount) {
		this.item.setAmount(amount);
		return this;
	}

	public ItemModifier type(final ItemType type) {
		final var modifier = itemModifier(type);
		modifier.item.copyDataFrom(this.item, alwaysTrue());
		return modifier;
	}

	public ItemModifier set(final DataComponentType.NonValued type) {
		this.item.setData(type);
		return this;
	}

	public <T> @Nullable T get(final DataComponentType.Valued<T> type) {
		return this.item.getData(type);
	}

	public <T> ItemModifier set(final DataComponentType.Valued<T> type, final T data) {
		this.item.setData(type, data);
		return this;
	}

	public ItemModifier unset(final DataComponentType type) {
		this.item.unsetData(type);
		return this;
	}

	public ItemModifier reset(final DataComponentType type) {
		this.item.resetData(type);
		return this;
	}

	public <T> ItemModifier modify(final DataComponentType.Valued<T> type, final Function<@Nullable T, @Nullable T> operator) {
		final T data = operator.apply(this.get(type));
		if (data == null) {
			return this;
		}
		return this.set(type, data);
	}

}
