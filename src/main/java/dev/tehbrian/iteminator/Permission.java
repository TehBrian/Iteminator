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

	private Permission() {
	}

}
