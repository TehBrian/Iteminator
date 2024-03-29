package dev.tehbrian.iteminator;

/**
 * Holds permission constants.
 */
public final class Permission {

  public static final String ROOT = "iteminator";
  public static final String RELOAD = ROOT + ".reload";

  public static final String FORMAT = ROOT + ".format";
  public static final String LEGACY = FORMAT + ".legacy";
  public static final String MINIMESSAGE = FORMAT + ".minimessage";

  public static final String AMOUNT = ROOT + ".amount";
  public static final String CUSTOM_MODEL_DATA = ROOT + ".custom-model-data";
  public static final String MATERIAL = ROOT + ".material";
  public static final String NAME = ROOT + ".name";
  public static final String UNBREAKABLE = ROOT + ".unbreakable";

  public static final String ATTRIBUTE = ROOT + ".attribute";
  public static final String ENCHANTMENT = ROOT + ".enchantment";
  public static final String FLAGS = ROOT + ".flags";
  public static final String LORE = ROOT + ".lore";

  public static final String SPECIAL = ROOT + ".special";
  public static final String ARMOR_STAND = SPECIAL + ".armor-stand";
  public static final String AXOLOTL_BUCKET = SPECIAL + ".axolotl-bucket";
  public static final String BANNER = SPECIAL + ".banner";
  public static final String BOOK = SPECIAL + ".book";
  public static final String DAMAGEABLE = SPECIAL + ".damageable";
  public static final String ENCHANTMENT_STORAGE = SPECIAL + ".enchantment-storage";
  public static final String FIREWORK = SPECIAL + ".firework";
  public static final String FIREWORK_EFFECT = SPECIAL + ".firework-effect";
  public static final String LEATHER_ARMOR = SPECIAL + ".leather-armor";
  public static final String MAP = SPECIAL + ".map";
  public static final String POTION = SPECIAL + ".potion";
  public static final String REPAIRABLE = SPECIAL + "repairable";
  public static final String SKULL = SPECIAL + ".skull";
  public static final String SUSPICIOUS_STEW = SPECIAL + ".suspicious-stew";
  public static final String TROPICAL_FISH_BUCKET = SPECIAL + ".tropical-fish-bucket";

  private Permission() {
  }

}
