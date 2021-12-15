package xyz.tehbrian.iteminator.util;

import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This entire class can be incinerated once switch pattern-matching arrives.
public final class ItemMetaToRequiredTypes {

    private static final Map<Class<? extends ItemMeta>, List<Material>> converter;

    static {
        converter = new HashMap<>();
        converter.put(TropicalFishBucketMeta.class, List.of(Material.TROPICAL_FISH_BUCKET));
        converter.put(ArmorStandMeta.class, List.of(Material.ARMOR_STAND));
    }

    private ItemMetaToRequiredTypes() {
    }

    /**
     * @param itemMetaType the {@code ItemMeta} type
     * @return the types required for that {@code ItemMeta} or null if there is no type requirement
     */
    public static @Nullable List<Material> get(final @NonNull Class<? extends ItemMeta> itemMetaType) {
        return converter.get(itemMetaType);
    }

}
