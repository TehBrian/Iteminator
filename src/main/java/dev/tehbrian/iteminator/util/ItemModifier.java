package dev.tehbrian.iteminator.util;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.AttackRange;
import io.papermc.paper.datacomponent.item.BannerPatternLayers;
import io.papermc.paper.datacomponent.item.BlockItemDataProperties;
import io.papermc.paper.datacomponent.item.BlocksAttacks;
import io.papermc.paper.datacomponent.item.BundleContents;
import io.papermc.paper.datacomponent.item.ChargedProjectiles;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.DamageResistant;
import io.papermc.paper.datacomponent.item.DeathProtection;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import io.papermc.paper.datacomponent.item.Enchantable;
import io.papermc.paper.datacomponent.item.Equippable;
import io.papermc.paper.datacomponent.item.Fireworks;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.ItemAdventurePredicate;
import io.papermc.paper.datacomponent.item.ItemArmorTrim;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import io.papermc.paper.datacomponent.item.ItemContainerContents;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.JukeboxPlayable;
import io.papermc.paper.datacomponent.item.KineticWeapon;
import io.papermc.paper.datacomponent.item.LodestoneTracker;
import io.papermc.paper.datacomponent.item.MapDecorations;
import io.papermc.paper.datacomponent.item.MapId;
import io.papermc.paper.datacomponent.item.MapItemColor;
import io.papermc.paper.datacomponent.item.OminousBottleAmplifier;
import io.papermc.paper.datacomponent.item.PiercingWeapon;
import io.papermc.paper.datacomponent.item.PotDecorations;
import io.papermc.paper.datacomponent.item.PotionContents;
import io.papermc.paper.datacomponent.item.Repairable;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import io.papermc.paper.datacomponent.item.SeededContainerLoot;
import io.papermc.paper.datacomponent.item.SuspiciousStewEffects;
import io.papermc.paper.datacomponent.item.SwingAnimation;
import io.papermc.paper.datacomponent.item.Tool;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import io.papermc.paper.datacomponent.item.UseCooldown;
import io.papermc.paper.datacomponent.item.UseEffects;
import io.papermc.paper.datacomponent.item.UseRemainder;
import io.papermc.paper.datacomponent.item.Weapon;
import io.papermc.paper.datacomponent.item.WritableBookContent;
import io.papermc.paper.datacomponent.item.WrittenBookContent;
import io.papermc.paper.item.MapPostProcessing;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Art;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.MusicInstrument;
import org.bukkit.block.banner.PatternType;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Salmon;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.ZombieNautilus;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.trim.TrimMaterial;

import java.util.List;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public final class ItemModifier {

	private final ItemStack item;

	private ItemModifier(final ItemStack item) {
		this.item = new ItemStack(item);
	}

	public static ItemModifier itemModifier(final ItemStack item) {
		return new ItemModifier(item);
	}

	public static ItemModifier itemModifier(final ItemType type) {
		return new ItemModifier(type.createItemStack());
	}

	public ItemStack yank() {
		return this.item;
	}

	public ItemModifier amount(final int amount) {
		this.item.setAmount(amount);
		return this;
	}

	public ItemModifier setData(final DataComponentType.NonValued type) {
		this.item.setData(type);
		return this;
	}

	public <T> ItemModifier setData(final DataComponentType.Valued<T> type, final T data) {
		this.item.setData(type, data);
		return this;
	}

	public ItemModifier unsetData(final DataComponentType type) {
		this.item.unsetData(type);
		return this;
	}

	public ItemModifier resetData(final DataComponentType type) {
		this.item.resetData(type);
		return this;
	}

	public ItemModifier setUnbreakable() {
		return this.setData(DataComponentTypes.UNBREAKABLE);
	}

	public ItemModifier unsetUnbreakable() {
		return this.unsetData(DataComponentTypes.UNBREAKABLE);
	}

	public ItemModifier resetUnbreakable() {
		return this.resetData(DataComponentTypes.UNBREAKABLE);
	}

	public ItemModifier setIntangibleProjectile() {
		return this.setData(DataComponentTypes.INTANGIBLE_PROJECTILE);
	}

	public ItemModifier unsetIntangibleProjectile() {
		return this.unsetData(DataComponentTypes.INTANGIBLE_PROJECTILE);
	}

	public ItemModifier resetIntangibleProjectile() {
		return this.resetData(DataComponentTypes.INTANGIBLE_PROJECTILE);
	}

	public ItemModifier setGlider() {
		return this.setData(DataComponentTypes.GLIDER);
	}

	public ItemModifier unsetGlider() {
		return this.unsetData(DataComponentTypes.GLIDER);
	}

	public ItemModifier resetGlider() {
		return this.resetData(DataComponentTypes.GLIDER);
	}

	public ItemModifier setMaxStackSize(final Integer data) {
		return this.setData(DataComponentTypes.MAX_STACK_SIZE, data);
	}

	public ItemModifier unsetMaxStackSize() {
		return this.unsetData(DataComponentTypes.MAX_STACK_SIZE);
	}

	public ItemModifier resetMaxStackSize() {
		return this.resetData(DataComponentTypes.MAX_STACK_SIZE);
	}

	public ItemModifier setMaxDamage(final Integer data) {
		return this.setData(DataComponentTypes.MAX_DAMAGE, data);
	}

	public ItemModifier unsetMaxDamage() {
		return this.unsetData(DataComponentTypes.MAX_DAMAGE);
	}

	public ItemModifier resetMaxDamage() {
		return this.resetData(DataComponentTypes.MAX_DAMAGE);
	}

	public ItemModifier setDamage(final Integer data) {
		return this.setData(DataComponentTypes.DAMAGE, data);
	}

	public ItemModifier unsetDamage() {
		return this.unsetData(DataComponentTypes.DAMAGE);
	}

	public ItemModifier resetDamage() {
		return this.resetData(DataComponentTypes.DAMAGE);
	}

	public ItemModifier setUseEffects(final UseEffects data) {
		return this.setData(DataComponentTypes.USE_EFFECTS, data);
	}

	public ItemModifier unsetUseEffects() {
		return this.unsetData(DataComponentTypes.USE_EFFECTS);
	}

	public ItemModifier resetUseEffects() {
		return this.resetData(DataComponentTypes.USE_EFFECTS);
	}

	public ItemModifier setCustomName(final Component data) {
		return this.setData(DataComponentTypes.CUSTOM_NAME, data);
	}

	public ItemModifier unsetCustomName() {
		return this.unsetData(DataComponentTypes.CUSTOM_NAME);
	}

	public ItemModifier resetCustomName() {
		return this.resetData(DataComponentTypes.CUSTOM_NAME);
	}

	public ItemModifier setMinimumAttackCharge(final Float data) {
		return this.setData(DataComponentTypes.MINIMUM_ATTACK_CHARGE, data);
	}

	public ItemModifier unsetMinimumAttackCharge() {
		return this.unsetData(DataComponentTypes.MINIMUM_ATTACK_CHARGE);
	}

	public ItemModifier resetMinimumAttackCharge() {
		return this.resetData(DataComponentTypes.MINIMUM_ATTACK_CHARGE);
	}

	public ItemModifier setDamageType(final DamageType data) {
		return this.setData(DataComponentTypes.DAMAGE_TYPE, data);
	}

	public ItemModifier unsetDamageType() {
		return this.unsetData(DataComponentTypes.DAMAGE_TYPE);
	}

	public ItemModifier resetDamageType() {
		return this.resetData(DataComponentTypes.DAMAGE_TYPE);
	}

	public ItemModifier setItemName(final Component data) {
		return this.setData(DataComponentTypes.ITEM_NAME, data);
	}

	public ItemModifier unsetItemName() {
		return this.unsetData(DataComponentTypes.ITEM_NAME);
	}

	public ItemModifier resetItemName() {
		return this.resetData(DataComponentTypes.ITEM_NAME);
	}

	public ItemModifier setItemModel(final Key data) {
		return this.setData(DataComponentTypes.ITEM_MODEL, data);
	}

	public ItemModifier unsetItemModel() {
		return this.unsetData(DataComponentTypes.ITEM_MODEL);
	}

	public ItemModifier resetItemModel() {
		return this.resetData(DataComponentTypes.ITEM_MODEL);
	}

	public ItemModifier setLore(final ItemLore data) {
		return this.setData(DataComponentTypes.LORE, data);
	}

	public ItemModifier unsetLore() {
		return this.unsetData(DataComponentTypes.LORE);
	}

	public ItemModifier resetLore() {
		return this.resetData(DataComponentTypes.LORE);
	}

	public ItemModifier setRarity(final ItemRarity data) {
		return this.setData(DataComponentTypes.RARITY, data);
	}

	public ItemModifier unsetRarity() {
		return this.unsetData(DataComponentTypes.RARITY);
	}

	public ItemModifier resetRarity() {
		return this.resetData(DataComponentTypes.RARITY);
	}

	public ItemModifier setEnchantments(final ItemEnchantments data) {
		return this.setData(DataComponentTypes.ENCHANTMENTS, data);
	}

	public ItemModifier unsetEnchantments() {
		return this.unsetData(DataComponentTypes.ENCHANTMENTS);
	}

	public ItemModifier resetEnchantments() {
		return this.resetData(DataComponentTypes.ENCHANTMENTS);
	}

	public ItemModifier setCanPlaceOn(final ItemAdventurePredicate data) {
		return this.setData(DataComponentTypes.CAN_PLACE_ON, data);
	}

	public ItemModifier unsetCanPlaceOn() {
		return this.unsetData(DataComponentTypes.CAN_PLACE_ON);
	}

	public ItemModifier resetCanPlaceOn() {
		return this.resetData(DataComponentTypes.CAN_PLACE_ON);
	}

	public ItemModifier setCanBreak(final ItemAdventurePredicate data) {
		return this.setData(DataComponentTypes.CAN_BREAK, data);
	}

	public ItemModifier unsetCanBreak() {
		return this.unsetData(DataComponentTypes.CAN_BREAK);
	}

	public ItemModifier resetCanBreak() {
		return this.resetData(DataComponentTypes.CAN_BREAK);
	}

	public ItemModifier setAttributeModifiers(final ItemAttributeModifiers data) {
		return this.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, data);
	}

	public ItemModifier unsetAttributeModifiers() {
		return this.unsetData(DataComponentTypes.ATTRIBUTE_MODIFIERS);
	}

	public ItemModifier resetAttributeModifiers() {
		return this.resetData(DataComponentTypes.ATTRIBUTE_MODIFIERS);
	}

	public ItemModifier setCustomModelData(final CustomModelData data) {
		return this.setData(DataComponentTypes.CUSTOM_MODEL_DATA, data);
	}

	public ItemModifier unsetCustomModelData() {
		return this.unsetData(DataComponentTypes.CUSTOM_MODEL_DATA);
	}

	public ItemModifier resetCustomModelData() {
		return this.resetData(DataComponentTypes.CUSTOM_MODEL_DATA);
	}

	public ItemModifier setTooltipDisplay(final TooltipDisplay data) {
		return this.setData(DataComponentTypes.TOOLTIP_DISPLAY, data);
	}

	public ItemModifier unsetTooltipDisplay() {
		return this.unsetData(DataComponentTypes.TOOLTIP_DISPLAY);
	}

	public ItemModifier resetTooltipDisplay() {
		return this.resetData(DataComponentTypes.TOOLTIP_DISPLAY);
	}

	public ItemModifier setRepairCost(final Integer data) {
		return this.setData(DataComponentTypes.REPAIR_COST, data);
	}

	public ItemModifier unsetRepairCost() {
		return this.unsetData(DataComponentTypes.REPAIR_COST);
	}

	public ItemModifier resetRepairCost() {
		return this.resetData(DataComponentTypes.REPAIR_COST);
	}

	public ItemModifier setEnchantmentGlintOverride(final Boolean data) {
		return this.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, data);
	}

	public ItemModifier unsetEnchantmentGlintOverride() {
		return this.unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
	}

	public ItemModifier resetEnchantmentGlintOverride() {
		return this.resetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
	}

	public ItemModifier setFood(final FoodProperties data) {
		return this.setData(DataComponentTypes.FOOD, data);
	}

	public ItemModifier unsetFood() {
		return this.unsetData(DataComponentTypes.FOOD);
	}

	public ItemModifier resetFood() {
		return this.resetData(DataComponentTypes.FOOD);
	}

	public ItemModifier setConsumable(final Consumable data) {
		return this.setData(DataComponentTypes.CONSUMABLE, data);
	}

	public ItemModifier unsetConsumable() {
		return this.unsetData(DataComponentTypes.CONSUMABLE);
	}

	public ItemModifier resetConsumable() {
		return this.resetData(DataComponentTypes.CONSUMABLE);
	}

	public ItemModifier setUseRemainder(final UseRemainder data) {
		return this.setData(DataComponentTypes.USE_REMAINDER, data);
	}

	public ItemModifier unsetUseRemainder() {
		return this.unsetData(DataComponentTypes.USE_REMAINDER);
	}

	public ItemModifier resetUseRemainder() {
		return this.resetData(DataComponentTypes.USE_REMAINDER);
	}

	public ItemModifier setUseCooldown(final UseCooldown data) {
		return this.setData(DataComponentTypes.USE_COOLDOWN, data);
	}

	public ItemModifier unsetUseCooldown() {
		return this.unsetData(DataComponentTypes.USE_COOLDOWN);
	}

	public ItemModifier resetUseCooldown() {
		return this.resetData(DataComponentTypes.USE_COOLDOWN);
	}

	public ItemModifier setDamageResistant(final DamageResistant data) {
		return this.setData(DataComponentTypes.DAMAGE_RESISTANT, data);
	}

	public ItemModifier unsetDamageResistant() {
		return this.unsetData(DataComponentTypes.DAMAGE_RESISTANT);
	}

	public ItemModifier resetDamageResistant() {
		return this.resetData(DataComponentTypes.DAMAGE_RESISTANT);
	}

	public ItemModifier setTool(final Tool data) {
		return this.setData(DataComponentTypes.TOOL, data);
	}

	public ItemModifier unsetTool() {
		return this.unsetData(DataComponentTypes.TOOL);
	}

	public ItemModifier resetTool() {
		return this.resetData(DataComponentTypes.TOOL);
	}

	public ItemModifier setWeapon(final Weapon data) {
		return this.setData(DataComponentTypes.WEAPON, data);
	}

	public ItemModifier unsetWeapon() {
		return this.unsetData(DataComponentTypes.WEAPON);
	}

	public ItemModifier resetWeapon() {
		return this.resetData(DataComponentTypes.WEAPON);
	}

	public ItemModifier setEnchantable(final Enchantable data) {
		return this.setData(DataComponentTypes.ENCHANTABLE, data);
	}

	public ItemModifier unsetEnchantable() {
		return this.unsetData(DataComponentTypes.ENCHANTABLE);
	}

	public ItemModifier resetEnchantable() {
		return this.resetData(DataComponentTypes.ENCHANTABLE);
	}

	public ItemModifier setEquippable(final Equippable data) {
		return this.setData(DataComponentTypes.EQUIPPABLE, data);
	}

	public ItemModifier unsetEquippable() {
		return this.unsetData(DataComponentTypes.EQUIPPABLE);
	}

	public ItemModifier resetEquippable() {
		return this.resetData(DataComponentTypes.EQUIPPABLE);
	}

	public ItemModifier setRepairable(final Repairable data) {
		return this.setData(DataComponentTypes.REPAIRABLE, data);
	}

	public ItemModifier unsetRepairable() {
		return this.unsetData(DataComponentTypes.REPAIRABLE);
	}

	public ItemModifier resetRepairable() {
		return this.resetData(DataComponentTypes.REPAIRABLE);
	}

	public ItemModifier setTooltipStyle(final Key data) {
		return this.setData(DataComponentTypes.TOOLTIP_STYLE, data);
	}

	public ItemModifier unsetTooltipStyle() {
		return this.unsetData(DataComponentTypes.TOOLTIP_STYLE);
	}

	public ItemModifier resetTooltipStyle() {
		return this.resetData(DataComponentTypes.TOOLTIP_STYLE);
	}

	public ItemModifier setDeathProtection(final DeathProtection data) {
		return this.setData(DataComponentTypes.DEATH_PROTECTION, data);
	}

	public ItemModifier unsetDeathProtection() {
		return this.unsetData(DataComponentTypes.DEATH_PROTECTION);
	}

	public ItemModifier resetDeathProtection() {
		return this.resetData(DataComponentTypes.DEATH_PROTECTION);
	}

	public ItemModifier setBlocksAttacks(final BlocksAttacks data) {
		return this.setData(DataComponentTypes.BLOCKS_ATTACKS, data);
	}

	public ItemModifier unsetBlocksAttacks() {
		return this.unsetData(DataComponentTypes.BLOCKS_ATTACKS);
	}

	public ItemModifier resetBlocksAttacks() {
		return this.resetData(DataComponentTypes.BLOCKS_ATTACKS);
	}

	public ItemModifier setPiercingWeapon(final PiercingWeapon data) {
		return this.setData(DataComponentTypes.PIERCING_WEAPON, data);
	}

	public ItemModifier unsetPiercingWeapon() {
		return this.unsetData(DataComponentTypes.PIERCING_WEAPON);
	}

	public ItemModifier resetPiercingWeapon() {
		return this.resetData(DataComponentTypes.PIERCING_WEAPON);
	}

	public ItemModifier setKineticWeapon(final KineticWeapon data) {
		return this.setData(DataComponentTypes.KINETIC_WEAPON, data);
	}

	public ItemModifier unsetKineticWeapon() {
		return this.unsetData(DataComponentTypes.KINETIC_WEAPON);
	}

	public ItemModifier resetKineticWeapon() {
		return this.resetData(DataComponentTypes.KINETIC_WEAPON);
	}

	public ItemModifier setAttackRange(final AttackRange data) {
		return this.setData(DataComponentTypes.ATTACK_RANGE, data);
	}

	public ItemModifier unsetAttackRange() {
		return this.unsetData(DataComponentTypes.ATTACK_RANGE);
	}

	public ItemModifier resetAttackRange() {
		return this.resetData(DataComponentTypes.ATTACK_RANGE);
	}

	public ItemModifier setSwingAnimation(final SwingAnimation data) {
		return this.setData(DataComponentTypes.SWING_ANIMATION, data);
	}

	public ItemModifier unsetSwingAnimation() {
		return this.unsetData(DataComponentTypes.SWING_ANIMATION);
	}

	public ItemModifier resetSwingAnimation() {
		return this.resetData(DataComponentTypes.SWING_ANIMATION);
	}

	public ItemModifier setStoredEnchantments(final ItemEnchantments data) {
		return this.setData(DataComponentTypes.STORED_ENCHANTMENTS, data);
	}

	public ItemModifier unsetStoredEnchantments() {
		return this.unsetData(DataComponentTypes.STORED_ENCHANTMENTS);
	}

	public ItemModifier resetStoredEnchantments() {
		return this.resetData(DataComponentTypes.STORED_ENCHANTMENTS);
	}

	public ItemModifier setDye(final DyeColor data) {
		return this.setData(DataComponentTypes.DYE, data);
	}

	public ItemModifier unsetDye() {
		return this.unsetData(DataComponentTypes.DYE);
	}

	public ItemModifier resetDye() {
		return this.resetData(DataComponentTypes.DYE);
	}

	public ItemModifier setDyedColor(final DyedItemColor data) {
		return this.setData(DataComponentTypes.DYED_COLOR, data);
	}

	public ItemModifier unsetDyedColor() {
		return this.unsetData(DataComponentTypes.DYED_COLOR);
	}

	public ItemModifier resetDyedColor() {
		return this.resetData(DataComponentTypes.DYED_COLOR);
	}

	public ItemModifier setMapColor(final MapItemColor data) {
		return this.setData(DataComponentTypes.MAP_COLOR, data);
	}

	public ItemModifier unsetMapColor() {
		return this.unsetData(DataComponentTypes.MAP_COLOR);
	}

	public ItemModifier resetMapColor() {
		return this.resetData(DataComponentTypes.MAP_COLOR);
	}

	public ItemModifier setMapId(final MapId data) {
		return this.setData(DataComponentTypes.MAP_ID, data);
	}

	public ItemModifier unsetMapId() {
		return this.unsetData(DataComponentTypes.MAP_ID);
	}

	public ItemModifier resetMapId() {
		return this.resetData(DataComponentTypes.MAP_ID);
	}

	public ItemModifier setMapDecorations(final MapDecorations data) {
		return this.setData(DataComponentTypes.MAP_DECORATIONS, data);
	}

	public ItemModifier unsetMapDecorations() {
		return this.unsetData(DataComponentTypes.MAP_DECORATIONS);
	}

	public ItemModifier resetMapDecorations() {
		return this.resetData(DataComponentTypes.MAP_DECORATIONS);
	}

	public ItemModifier setMapPostProcessing(final MapPostProcessing data) {
		return this.setData(DataComponentTypes.MAP_POST_PROCESSING, data);
	}

	public ItemModifier unsetMapPostProcessing() {
		return this.unsetData(DataComponentTypes.MAP_POST_PROCESSING);
	}

	public ItemModifier resetMapPostProcessing() {
		return this.resetData(DataComponentTypes.MAP_POST_PROCESSING);
	}

	public ItemModifier setChargedProjectiles(final ChargedProjectiles data) {
		return this.setData(DataComponentTypes.CHARGED_PROJECTILES, data);
	}

	public ItemModifier unsetChargedProjectiles() {
		return this.unsetData(DataComponentTypes.CHARGED_PROJECTILES);
	}

	public ItemModifier resetChargedProjectiles() {
		return this.resetData(DataComponentTypes.CHARGED_PROJECTILES);
	}

	public ItemModifier setBundleContents(final BundleContents data) {
		return this.setData(DataComponentTypes.BUNDLE_CONTENTS, data);
	}

	public ItemModifier unsetBundleContents() {
		return this.unsetData(DataComponentTypes.BUNDLE_CONTENTS);
	}

	public ItemModifier resetBundleContents() {
		return this.resetData(DataComponentTypes.BUNDLE_CONTENTS);
	}

	public ItemModifier setPotionContents(final PotionContents data) {
		return this.setData(DataComponentTypes.POTION_CONTENTS, data);
	}

	public ItemModifier unsetPotionContents() {
		return this.unsetData(DataComponentTypes.POTION_CONTENTS);
	}

	public ItemModifier resetPotionContents() {
		return this.resetData(DataComponentTypes.POTION_CONTENTS);
	}

	public ItemModifier setPotionDurationScale(final Float data) {
		return this.setData(DataComponentTypes.POTION_DURATION_SCALE, data);
	}

	public ItemModifier unsetPotionDurationScale() {
		return this.unsetData(DataComponentTypes.POTION_DURATION_SCALE);
	}

	public ItemModifier resetPotionDurationScale() {
		return this.resetData(DataComponentTypes.POTION_DURATION_SCALE);
	}

	public ItemModifier setSuspiciousStewEffects(final SuspiciousStewEffects data) {
		return this.setData(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, data);
	}

	public ItemModifier unsetSuspiciousStewEffects() {
		return this.unsetData(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS);
	}

	public ItemModifier resetSuspiciousStewEffects() {
		return this.resetData(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS);
	}

	public ItemModifier setWritableBookContent(final WritableBookContent data) {
		return this.setData(DataComponentTypes.WRITABLE_BOOK_CONTENT, data);
	}

	public ItemModifier unsetWritableBookContent() {
		return this.unsetData(DataComponentTypes.WRITABLE_BOOK_CONTENT);
	}

	public ItemModifier resetWritableBookContent() {
		return this.resetData(DataComponentTypes.WRITABLE_BOOK_CONTENT);
	}

	public ItemModifier setWrittenBookContent(final WrittenBookContent data) {
		return this.setData(DataComponentTypes.WRITTEN_BOOK_CONTENT, data);
	}

	public ItemModifier unsetWrittenBookContent() {
		return this.unsetData(DataComponentTypes.WRITTEN_BOOK_CONTENT);
	}

	public ItemModifier resetWrittenBookContent() {
		return this.resetData(DataComponentTypes.WRITTEN_BOOK_CONTENT);
	}

	public ItemModifier setTrim(final ItemArmorTrim data) {
		return this.setData(DataComponentTypes.TRIM, data);
	}

	public ItemModifier unsetTrim() {
		return this.unsetData(DataComponentTypes.TRIM);
	}

	public ItemModifier resetTrim() {
		return this.resetData(DataComponentTypes.TRIM);
	}

	public ItemModifier setInstrument(final MusicInstrument data) {
		return this.setData(DataComponentTypes.INSTRUMENT, data);
	}

	public ItemModifier unsetInstrument() {
		return this.unsetData(DataComponentTypes.INSTRUMENT);
	}

	public ItemModifier resetInstrument() {
		return this.resetData(DataComponentTypes.INSTRUMENT);
	}

	public ItemModifier setProvidesTrimMaterial(final TrimMaterial data) {
		return this.setData(DataComponentTypes.PROVIDES_TRIM_MATERIAL, data);
	}

	public ItemModifier unsetProvidesTrimMaterial() {
		return this.unsetData(DataComponentTypes.PROVIDES_TRIM_MATERIAL);
	}

	public ItemModifier resetProvidesTrimMaterial() {
		return this.resetData(DataComponentTypes.PROVIDES_TRIM_MATERIAL);
	}

	public ItemModifier setOminousBottleAmplifier(final OminousBottleAmplifier data) {
		return this.setData(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER, data);
	}

	public ItemModifier unsetOminousBottleAmplifier() {
		return this.unsetData(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER);
	}

	public ItemModifier resetOminousBottleAmplifier() {
		return this.resetData(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER);
	}

	public ItemModifier setJukeboxPlayable(final JukeboxPlayable data) {
		return this.setData(DataComponentTypes.JUKEBOX_PLAYABLE, data);
	}

	public ItemModifier unsetJukeboxPlayable() {
		return this.unsetData(DataComponentTypes.JUKEBOX_PLAYABLE);
	}

	public ItemModifier resetJukeboxPlayable() {
		return this.resetData(DataComponentTypes.JUKEBOX_PLAYABLE);
	}

	public ItemModifier setProvidesBannerPatterns(final RegistryKeySet<PatternType> data) {
		return this.setData(DataComponentTypes.PROVIDES_BANNER_PATTERNS, data);
	}

	public ItemModifier unsetProvidesBannerPatterns() {
		return this.unsetData(DataComponentTypes.PROVIDES_BANNER_PATTERNS);
	}

	public ItemModifier resetProvidesBannerPatterns() {
		return this.resetData(DataComponentTypes.PROVIDES_BANNER_PATTERNS);
	}

	public ItemModifier setRecipes(final List<Key> data) {
		return this.setData(DataComponentTypes.RECIPES, data);
	}

	public ItemModifier unsetRecipes() {
		return this.unsetData(DataComponentTypes.RECIPES);
	}

	public ItemModifier resetRecipes() {
		return this.resetData(DataComponentTypes.RECIPES);
	}

	public ItemModifier setLodestoneTracker(final LodestoneTracker data) {
		return this.setData(DataComponentTypes.LODESTONE_TRACKER, data);
	}

	public ItemModifier unsetLodestoneTracker() {
		return this.unsetData(DataComponentTypes.LODESTONE_TRACKER);
	}

	public ItemModifier resetLodestoneTracker() {
		return this.resetData(DataComponentTypes.LODESTONE_TRACKER);
	}

	public ItemModifier setFireworkExplosion(final FireworkEffect data) {
		return this.setData(DataComponentTypes.FIREWORK_EXPLOSION, data);
	}

	public ItemModifier unsetFireworkExplosion() {
		return this.unsetData(DataComponentTypes.FIREWORK_EXPLOSION);
	}

	public ItemModifier resetFireworkExplosion() {
		return this.resetData(DataComponentTypes.FIREWORK_EXPLOSION);
	}

	public ItemModifier setFireworks(final Fireworks data) {
		return this.setData(DataComponentTypes.FIREWORKS, data);
	}

	public ItemModifier unsetFireworks() {
		return this.unsetData(DataComponentTypes.FIREWORKS);
	}

	public ItemModifier resetFireworks() {
		return this.resetData(DataComponentTypes.FIREWORKS);
	}

	public ItemModifier setProfile(final ResolvableProfile data) {
		return this.setData(DataComponentTypes.PROFILE, data);
	}

	public ItemModifier unsetProfile() {
		return this.unsetData(DataComponentTypes.PROFILE);
	}

	public ItemModifier resetProfile() {
		return this.resetData(DataComponentTypes.PROFILE);
	}

	public ItemModifier setNoteBlockSound(final Key data) {
		return this.setData(DataComponentTypes.NOTE_BLOCK_SOUND, data);
	}

	public ItemModifier unsetNoteBlockSound() {
		return this.unsetData(DataComponentTypes.NOTE_BLOCK_SOUND);
	}

	public ItemModifier resetNoteBlockSound() {
		return this.resetData(DataComponentTypes.NOTE_BLOCK_SOUND);
	}

	public ItemModifier setBannerPatterns(final BannerPatternLayers data) {
		return this.setData(DataComponentTypes.BANNER_PATTERNS, data);
	}

	public ItemModifier unsetBannerPatterns() {
		return this.unsetData(DataComponentTypes.BANNER_PATTERNS);
	}

	public ItemModifier resetBannerPatterns() {
		return this.resetData(DataComponentTypes.BANNER_PATTERNS);
	}

	public ItemModifier setBaseColor(final DyeColor data) {
		return this.setData(DataComponentTypes.BASE_COLOR, data);
	}

	public ItemModifier unsetBaseColor() {
		return this.unsetData(DataComponentTypes.BASE_COLOR);
	}

	public ItemModifier resetBaseColor() {
		return this.resetData(DataComponentTypes.BASE_COLOR);
	}

	public ItemModifier setPotDecorations(final PotDecorations data) {
		return this.setData(DataComponentTypes.POT_DECORATIONS, data);
	}

	public ItemModifier unsetPotDecorations() {
		return this.unsetData(DataComponentTypes.POT_DECORATIONS);
	}

	public ItemModifier resetPotDecorations() {
		return this.resetData(DataComponentTypes.POT_DECORATIONS);
	}

	public ItemModifier setContainer(final ItemContainerContents data) {
		return this.setData(DataComponentTypes.CONTAINER, data);
	}

	public ItemModifier unsetContainer() {
		return this.unsetData(DataComponentTypes.CONTAINER);
	}

	public ItemModifier resetContainer() {
		return this.resetData(DataComponentTypes.CONTAINER);
	}

	public ItemModifier setBlockData(final BlockItemDataProperties data) {
		return this.setData(DataComponentTypes.BLOCK_DATA, data);
	}

	public ItemModifier unsetBlockData() {
		return this.unsetData(DataComponentTypes.BLOCK_DATA);
	}

	public ItemModifier resetBlockData() {
		return this.resetData(DataComponentTypes.BLOCK_DATA);
	}

	public ItemModifier setContainerLoot(final SeededContainerLoot data) {
		return this.setData(DataComponentTypes.CONTAINER_LOOT, data);
	}

	public ItemModifier unsetContainerLoot() {
		return this.unsetData(DataComponentTypes.CONTAINER_LOOT);
	}

	public ItemModifier resetContainerLoot() {
		return this.resetData(DataComponentTypes.CONTAINER_LOOT);
	}

	public ItemModifier setBreakSound(final Key data) {
		return this.setData(DataComponentTypes.BREAK_SOUND, data);
	}

	public ItemModifier unsetBreakSound() {
		return this.unsetData(DataComponentTypes.BREAK_SOUND);
	}

	public ItemModifier resetBreakSound() {
		return this.resetData(DataComponentTypes.BREAK_SOUND);
	}

	public ItemModifier setVillagerVariant(final Villager.Type data) {
		return this.setData(DataComponentTypes.VILLAGER_VARIANT, data);
	}

	public ItemModifier unsetVillagerVariant() {
		return this.unsetData(DataComponentTypes.VILLAGER_VARIANT);
	}

	public ItemModifier resetVillagerVariant() {
		return this.resetData(DataComponentTypes.VILLAGER_VARIANT);
	}

	public ItemModifier setWolfVariant(final Wolf.Variant data) {
		return this.setData(DataComponentTypes.WOLF_VARIANT, data);
	}

	public ItemModifier unsetWolfVariant() {
		return this.unsetData(DataComponentTypes.WOLF_VARIANT);
	}

	public ItemModifier resetWolfVariant() {
		return this.resetData(DataComponentTypes.WOLF_VARIANT);
	}

	public ItemModifier setWolfSoundVariant(final Wolf.SoundVariant data) {
		return this.setData(DataComponentTypes.WOLF_SOUND_VARIANT, data);
	}

	public ItemModifier unsetWolfSoundVariant() {
		return this.unsetData(DataComponentTypes.WOLF_SOUND_VARIANT);
	}

	public ItemModifier resetWolfSoundVariant() {
		return this.resetData(DataComponentTypes.WOLF_SOUND_VARIANT);
	}

	public ItemModifier setWolfCollar(final DyeColor data) {
		return this.setData(DataComponentTypes.WOLF_COLLAR, data);
	}

	public ItemModifier unsetWolfCollar() {
		return this.unsetData(DataComponentTypes.WOLF_COLLAR);
	}

	public ItemModifier resetWolfCollar() {
		return this.resetData(DataComponentTypes.WOLF_COLLAR);
	}

	public ItemModifier setFoxVariant(final Fox.Type data) {
		return this.setData(DataComponentTypes.FOX_VARIANT, data);
	}

	public ItemModifier unsetFoxVariant() {
		return this.unsetData(DataComponentTypes.FOX_VARIANT);
	}

	public ItemModifier resetFoxVariant() {
		return this.resetData(DataComponentTypes.FOX_VARIANT);
	}

	public ItemModifier setSalmonSize(final Salmon.Variant data) {
		return this.setData(DataComponentTypes.SALMON_SIZE, data);
	}

	public ItemModifier unsetSalmonSize() {
		return this.unsetData(DataComponentTypes.SALMON_SIZE);
	}

	public ItemModifier resetSalmonSize() {
		return this.resetData(DataComponentTypes.SALMON_SIZE);
	}

	public ItemModifier setParrotVariant(final Parrot.Variant data) {
		return this.setData(DataComponentTypes.PARROT_VARIANT, data);
	}

	public ItemModifier unsetParrotVariant() {
		return this.unsetData(DataComponentTypes.PARROT_VARIANT);
	}

	public ItemModifier resetParrotVariant() {
		return this.resetData(DataComponentTypes.PARROT_VARIANT);
	}

	public ItemModifier setTropicalFishPattern(final TropicalFish.Pattern data) {
		return this.setData(DataComponentTypes.TROPICAL_FISH_PATTERN, data);
	}

	public ItemModifier unsetTropicalFishPattern() {
		return this.unsetData(DataComponentTypes.TROPICAL_FISH_PATTERN);
	}

	public ItemModifier resetTropicalFishPattern() {
		return this.resetData(DataComponentTypes.TROPICAL_FISH_PATTERN);
	}

	public ItemModifier setTropicalFishBaseColor(final DyeColor data) {
		return this.setData(DataComponentTypes.TROPICAL_FISH_BASE_COLOR, data);
	}

	public ItemModifier unsetTropicalFishBaseColor() {
		return this.unsetData(DataComponentTypes.TROPICAL_FISH_BASE_COLOR);
	}

	public ItemModifier resetTropicalFishBaseColor() {
		return this.resetData(DataComponentTypes.TROPICAL_FISH_BASE_COLOR);
	}

	public ItemModifier setTropicalFishPatternColor(final DyeColor data) {
		return this.setData(DataComponentTypes.TROPICAL_FISH_PATTERN_COLOR, data);
	}

	public ItemModifier unsetTropicalFishPatternColor() {
		return this.unsetData(DataComponentTypes.TROPICAL_FISH_PATTERN_COLOR);
	}

	public ItemModifier resetTropicalFishPatternColor() {
		return this.resetData(DataComponentTypes.TROPICAL_FISH_PATTERN_COLOR);
	}

	public ItemModifier setMooshroomVariant(final MushroomCow.Variant data) {
		return this.setData(DataComponentTypes.MOOSHROOM_VARIANT, data);
	}

	public ItemModifier unsetMooshroomVariant() {
		return this.unsetData(DataComponentTypes.MOOSHROOM_VARIANT);
	}

	public ItemModifier resetMooshroomVariant() {
		return this.resetData(DataComponentTypes.MOOSHROOM_VARIANT);
	}

	public ItemModifier setRabbitVariant(final Rabbit.Type data) {
		return this.setData(DataComponentTypes.RABBIT_VARIANT, data);
	}

	public ItemModifier unsetRabbitVariant() {
		return this.unsetData(DataComponentTypes.RABBIT_VARIANT);
	}

	public ItemModifier resetRabbitVariant() {
		return this.resetData(DataComponentTypes.RABBIT_VARIANT);
	}

	public ItemModifier setPigVariant(final Pig.Variant data) {
		return this.setData(DataComponentTypes.PIG_VARIANT, data);
	}

	public ItemModifier unsetPigVariant() {
		return this.unsetData(DataComponentTypes.PIG_VARIANT);
	}

	public ItemModifier resetPigVariant() {
		return this.resetData(DataComponentTypes.PIG_VARIANT);
	}

	public ItemModifier setPigSoundVariant(final Pig.SoundVariant data) {
		return this.setData(DataComponentTypes.PIG_SOUND_VARIANT, data);
	}

	public ItemModifier unsetPigSoundVariant() {
		return this.unsetData(DataComponentTypes.PIG_SOUND_VARIANT);
	}

	public ItemModifier resetPigSoundVariant() {
		return this.resetData(DataComponentTypes.PIG_SOUND_VARIANT);
	}

	public ItemModifier setCowVariant(final Cow.Variant data) {
		return this.setData(DataComponentTypes.COW_VARIANT, data);
	}

	public ItemModifier unsetCowVariant() {
		return this.unsetData(DataComponentTypes.COW_VARIANT);
	}

	public ItemModifier resetCowVariant() {
		return this.resetData(DataComponentTypes.COW_VARIANT);
	}

	public ItemModifier setCowSoundVariant(final Cow.SoundVariant data) {
		return this.setData(DataComponentTypes.COW_SOUND_VARIANT, data);
	}

	public ItemModifier unsetCowSoundVariant() {
		return this.unsetData(DataComponentTypes.COW_SOUND_VARIANT);
	}

	public ItemModifier resetCowSoundVariant() {
		return this.resetData(DataComponentTypes.COW_SOUND_VARIANT);
	}

	public ItemModifier setChickenVariant(final Chicken.Variant data) {
		return this.setData(DataComponentTypes.CHICKEN_VARIANT, data);
	}

	public ItemModifier unsetChickenVariant() {
		return this.unsetData(DataComponentTypes.CHICKEN_VARIANT);
	}

	public ItemModifier resetChickenVariant() {
		return this.resetData(DataComponentTypes.CHICKEN_VARIANT);
	}

	public ItemModifier setChickenSoundVariant(final Chicken.SoundVariant data) {
		return this.setData(DataComponentTypes.CHICKEN_SOUND_VARIANT, data);
	}

	public ItemModifier unsetChickenSoundVariant() {
		return this.unsetData(DataComponentTypes.CHICKEN_SOUND_VARIANT);
	}

	public ItemModifier resetChickenSoundVariant() {
		return this.resetData(DataComponentTypes.CHICKEN_SOUND_VARIANT);
	}

	public ItemModifier setFrogVariant(final Frog.Variant data) {
		return this.setData(DataComponentTypes.FROG_VARIANT, data);
	}

	public ItemModifier unsetFrogVariant() {
		return this.unsetData(DataComponentTypes.FROG_VARIANT);
	}

	public ItemModifier resetFrogVariant() {
		return this.resetData(DataComponentTypes.FROG_VARIANT);
	}

	public ItemModifier setHorseVariant(final Horse.Color data) {
		return this.setData(DataComponentTypes.HORSE_VARIANT, data);
	}

	public ItemModifier unsetHorseVariant() {
		return this.unsetData(DataComponentTypes.HORSE_VARIANT);
	}

	public ItemModifier resetHorseVariant() {
		return this.resetData(DataComponentTypes.HORSE_VARIANT);
	}

	public ItemModifier setPaintingVariant(final Art data) {
		return this.setData(DataComponentTypes.PAINTING_VARIANT, data);
	}

	public ItemModifier unsetPaintingVariant() {
		return this.unsetData(DataComponentTypes.PAINTING_VARIANT);
	}

	public ItemModifier resetPaintingVariant() {
		return this.resetData(DataComponentTypes.PAINTING_VARIANT);
	}

	public ItemModifier setLlamaVariant(final Llama.Color data) {
		return this.setData(DataComponentTypes.LLAMA_VARIANT, data);
	}

	public ItemModifier unsetLlamaVariant() {
		return this.unsetData(DataComponentTypes.LLAMA_VARIANT);
	}

	public ItemModifier resetLlamaVariant() {
		return this.resetData(DataComponentTypes.LLAMA_VARIANT);
	}

	public ItemModifier setAxolotlVariant(final Axolotl.Variant data) {
		return this.setData(DataComponentTypes.AXOLOTL_VARIANT, data);
	}

	public ItemModifier unsetAxolotlVariant() {
		return this.unsetData(DataComponentTypes.AXOLOTL_VARIANT);
	}

	public ItemModifier resetAxolotlVariant() {
		return this.resetData(DataComponentTypes.AXOLOTL_VARIANT);
	}

	public ItemModifier setZombieNautilusVariant(final ZombieNautilus.Variant data) {
		return this.setData(DataComponentTypes.ZOMBIE_NAUTILUS_VARIANT, data);
	}

	public ItemModifier unsetZombieNautilusVariant() {
		return this.unsetData(DataComponentTypes.ZOMBIE_NAUTILUS_VARIANT);
	}

	public ItemModifier resetZombieNautilusVariant() {
		return this.resetData(DataComponentTypes.ZOMBIE_NAUTILUS_VARIANT);
	}

	public ItemModifier setCatVariant(final Cat.Type data) {
		return this.setData(DataComponentTypes.CAT_VARIANT, data);
	}

	public ItemModifier unsetCatVariant() {
		return this.unsetData(DataComponentTypes.CAT_VARIANT);
	}

	public ItemModifier resetCatVariant() {
		return this.resetData(DataComponentTypes.CAT_VARIANT);
	}

	public ItemModifier setCatSoundVariant(final Cat.SoundVariant data) {
		return this.setData(DataComponentTypes.CAT_SOUND_VARIANT, data);
	}

	public ItemModifier unsetCatSoundVariant() {
		return this.unsetData(DataComponentTypes.CAT_SOUND_VARIANT);
	}

	public ItemModifier resetCatSoundVariant() {
		return this.resetData(DataComponentTypes.CAT_SOUND_VARIANT);
	}

	public ItemModifier setCatCollar(final DyeColor data) {
		return this.setData(DataComponentTypes.CAT_COLLAR, data);
	}

	public ItemModifier unsetCatCollar() {
		return this.unsetData(DataComponentTypes.CAT_COLLAR);
	}

	public ItemModifier resetCatCollar() {
		return this.resetData(DataComponentTypes.CAT_COLLAR);
	}

	public ItemModifier setSheepColor(final DyeColor data) {
		return this.setData(DataComponentTypes.SHEEP_COLOR, data);
	}

	public ItemModifier unsetSheepColor() {
		return this.unsetData(DataComponentTypes.SHEEP_COLOR);
	}

	public ItemModifier resetSheepColor() {
		return this.resetData(DataComponentTypes.SHEEP_COLOR);
	}

	public ItemModifier setShulkerColor(final DyeColor data) {
		return this.setData(DataComponentTypes.SHULKER_COLOR, data);
	}

	public ItemModifier unsetShulkerColor() {
		return this.unsetData(DataComponentTypes.SHULKER_COLOR);
	}

	public ItemModifier resetShulkerColor() {
		return this.resetData(DataComponentTypes.SHULKER_COLOR);
	}

}
