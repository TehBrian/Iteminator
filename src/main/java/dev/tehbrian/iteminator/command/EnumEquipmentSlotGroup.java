package dev.tehbrian.iteminator.command;

import org.bukkit.inventory.EquipmentSlotGroup;

public enum EnumEquipmentSlotGroup {
	ANY(EquipmentSlotGroup.ANY),
	MAINHAND(EquipmentSlotGroup.MAINHAND),
	OFFHAND(EquipmentSlotGroup.OFFHAND),
	HAND(EquipmentSlotGroup.HAND),
	FEET(EquipmentSlotGroup.FEET),
	LEGS(EquipmentSlotGroup.LEGS),
	CHEST(EquipmentSlotGroup.CHEST),
	HEAD(EquipmentSlotGroup.HEAD),
	ARMOR(EquipmentSlotGroup.ARMOR),
	BODY(EquipmentSlotGroup.BODY),
	SADDLE(EquipmentSlotGroup.SADDLE);

	private final EquipmentSlotGroup type;

	EnumEquipmentSlotGroup(final EquipmentSlotGroup type) {
		this.type = type;
	}

	public EquipmentSlotGroup get() {
		return this.type;
	}
}
