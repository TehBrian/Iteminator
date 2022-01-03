package xyz.tehbrian.iteminator.util;

import com.destroystokyo.paper.MaterialTags;
import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This entire class can be incinerated once switch pattern-matching arrives.
public final class ItemMetaRequiredTypes {

    private static final Map<Class<? extends ItemMeta>, List<Material>> CONVERTER;

    static {
        CONVERTER = new HashMap<>();
        CONVERTER.put(ArmorStandMeta.class, List.of(Material.ARMOR_STAND));
        CONVERTER.put(AxolotlBucketMeta.class, List.of(Material.AXOLOTL_BUCKET));
        CONVERTER.put(BannerMeta.class, Tag.BANNERS.getValues().stream().toList());
        CONVERTER.put(BookMeta.class, List.of(Material.WRITABLE_BOOK, Material.WRITTEN_BOOK));
        CONVERTER.put(EnchantmentStorageMeta.class, List.of(Material.ENCHANTED_BOOK));
        CONVERTER.put(FireworkMeta.class, List.of(Material.FIREWORK_ROCKET));
        CONVERTER.put(FireworkEffectMeta.class, List.of(Material.FIREWORK_STAR));
        CONVERTER.put(KnowledgeBookMeta.class, List.of(Material.KNOWLEDGE_BOOK));
        CONVERTER.put(LeatherArmorMeta.class, List.of(
                Material.LEATHER_HORSE_ARMOR,
                Material.LEATHER_HELMET,
                Material.LEATHER_CHESTPLATE,
                Material.LEATHER_LEGGINGS,
                Material.LEATHER_BOOTS
        ));
        CONVERTER.put(MapMeta.class, List.of(Material.FILLED_MAP));
        CONVERTER.put(PotionMeta.class, List.of(Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION, Material.TIPPED_ARROW));
        CONVERTER.put(SkullMeta.class, MaterialTags.SKULLS.getValues().stream().toList());
        CONVERTER.put(SuspiciousStewMeta.class, List.of(Material.SUSPICIOUS_STEW));
        CONVERTER.put(TropicalFishBucketMeta.class, List.of(Material.TROPICAL_FISH_BUCKET));
    }

    private ItemMetaRequiredTypes() {
    }

    /**
     * @param itemMetaType the {@code ItemMeta} type
     * @return the types required for that {@code ItemMeta}, or null if there is no type requirement
     */
    public static @Nullable List<Material> get(final @NonNull Class<? extends ItemMeta> itemMetaType) {
        return CONVERTER.get(itemMetaType);
    }

}
