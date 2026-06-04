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

	public static final String EDIT = ROOT + ".edit";
	public static final String AMOUNT = EDIT + ".amount";
	public static final String TYPE = EDIT + ".type";
	public static final String UNBREAKABLE = EDIT + ".unbreakable";
	public static final String CUSTOM_NAME = EDIT + ".custom-name";
	public static final String ITEM_NAME = EDIT + ".item-name";
	public static final String DAMAGE_TYPE = EDIT + ".damage-type";

	private Permission() {
	}

}
