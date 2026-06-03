package dev.tehbrian.iteminator;

/**
 * Holds permission constants.
 */
// TODO: update permissions
public final class Permission {

	public static final String ROOT = "iteminator";
	public static final String RELOAD = ROOT + ".reload";

	public static final String FORMAT = ROOT + ".format";
	public static final String LEGACY = FORMAT + ".legacy";
	public static final String MINIMESSAGE = FORMAT + ".minimessage";

	public static final String ITEM = ROOT + ".item";
	public static final String AMOUNT = ITEM + ".amount";
	public static final String TYPE = ITEM + ".type";
	public static final String UNBREAKABLE = ITEM + ".unbreakable";
	public static final String CUSTOM_NAME = ITEM + ".custom-name";
	public static final String ITEM_NAME = ITEM + ".item-name";
	public static final String DAMAGE_TYPE = ITEM + ".damage-type";

	public static final String CUSTOM_MODEL_DATA = ITEM + ".custom-model-data";

	public static final String ATTRIBUTE = ITEM + ".attribute";
	public static final String ENCHANTMENT = ITEM + ".enchantment";
	public static final String FLAGS = ITEM + ".flags";
	public static final String LORE = ITEM + ".lore";

	public static final String ARMOR_STAND = ITEM + ".armor-stand";
	public static final String AXOLOTL_BUCKET = ITEM + ".axolotl-bucket";
	public static final String BANNER = ITEM + ".banner";
	public static final String BOOK = ITEM + ".book";
	public static final String DAMAGEABLE = ITEM + ".damageable";
	public static final String ENCHANTMENT_STORAGE = ITEM + ".enchantment-storage";
	public static final String FIREWORK = ITEM + ".firework";
	public static final String FIREWORK_EFFECT = ITEM + ".firework-effect";
	public static final String ITEM_FRAME = ITEM + ".item-frame";
	public static final String LEATHER_ARMOR = ITEM + ".leather-armor";
	public static final String MAP = ITEM + ".map";
	public static final String POTION = ITEM + ".potion";
	public static final String REPAIRABLE = ITEM + "repairable";
	public static final String SKULL = ITEM + ".skull";
	public static final String SUSPICIOUS_STEW = ITEM + ".suspicious-stew";
	public static final String TROPICAL_FISH_BUCKET = ITEM + ".tropical-fish-bucket";


	private Permission() {
	}

}
