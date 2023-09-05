package dev.tehbrian.iteminator.util;

import broccolai.corn.paper.item.AbstractPaperItemBuilder;
import broccolai.corn.paper.item.PaperItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Function;

public final class HeldItemModifier {

  private HeldItemModifier() {
  }

  /**
   * I am most certainly going to the deepest, darkest level of programmer hell
   * for this terribleness. At least I don't have to re-type the same thing
   * everywhere! \_o¬o_/
   *
   * @param player         the player to target
   * @param operator       the operator to apply to the item in the main hand
   * @param builderCreator a function that creates the builder
   * @param <T>            the builder type
   * @throws IllegalArgumentException if the player's held item meta cannot
   *                                  be modified by the provided builder
   */
  public static <T extends AbstractPaperItemBuilder<T, ?>> void modifySpecial(
      final Player player,
      final Function<T, @Nullable T> operator,
      final Function<ItemStack, T> builderCreator
  ) throws IllegalArgumentException {
    modifyItemStack(player, i -> {
      final T builder = builderCreator.apply(i);
      final @Nullable T modifiedBuilder = operator.apply(builder);
      if (modifiedBuilder == null) {
        return null;
      }
      return modifiedBuilder.build();
    });
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
      final Function<PaperItemBuilder, @Nullable PaperItemBuilder> operator
  ) {
    modifyItemStack(player, i -> {
      final @Nullable PaperItemBuilder modifiedBuilder = operator.apply(PaperItemBuilder.of(i));
      if (modifiedBuilder == null) {
        return null;
      }
      return modifiedBuilder.build();
    });
  }

  /**
   * Applies the given operator to the item in the player's main hand.
   * If the operator returns null, nothing will happen to the item.
   *
   * @param player   the player to target
   * @param operator the operator to apply to the item in the main hand
   */
  public static void modifyItemStack(
      final Player player,
      final Function<ItemStack, @Nullable ItemStack> operator
  ) {
    final PlayerInventory inventory = player.getInventory();
    final ItemStack item = inventory.getItemInMainHand();
    if (item.getItemMeta() == null) { // it's air and therefore cannot be modified
      return;
    }
    final @Nullable ItemStack modifiedItem = operator.apply(item);
    if (modifiedItem == null) {
      return;
    }
    inventory.setItemInMainHand(modifiedItem);
  }

}
