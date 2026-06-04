# Iteminator Command Catalog

This is the proposed command tree for the Paper data component version of
Iteminator. Design rules, shared parsers, helper fragments, and extension
guidance live in [`command-design.md`](command-design.md).

The catalog is based on `io.papermc.paper.datacomponent.DataComponentTypes` from
the project's current `paper-api:26.1.2.build.+` dependency.

Data component commands are written relative to `/iteminator edit`.

Every data component below also has:

- `/iteminator edit <component> unset`
- `/iteminator edit <component> reset`

## Root Commands

- `/iteminator`
- `/iteminator help [query]`
- `/iteminator reload`
- `/iteminator format`
- `/iteminator format <formatting-type>`
- `/iteminator inspect [component]`
- `/iteminator components [filter]`
- `/iteminator defaults <component>`

## ItemStack Commands

- `/iteminator edit amount <amount>`
- `/iteminator edit type <item-type>`

## Simple Values

- `max-stack-size set <amount>`
- `max-damage set <damage>`
- `damage set <uses-consumed>`
- `custom-name set <name>`
- `minimum-attack-charge set <charge>`
- `damage-type set <damage-type>`
- `item-name set <name>`
- `item-model set <model>`
- `rarity set <rarity>`
- `repair-cost set <cost>`
- `enchantment-glint-override set <override>`
- `use-remainder set <item>`
- `damage-resistant set <damage-types>`
- `enchantable set <enchantability>`
- `repairable set <items>`
- `tooltip-style set <style>`
- `dye set <dye-color>`
- `dyed-color set <color>`
- `map-color set <color>`
- `map-id set <map-id>`
- `map-post-processing set <lock|scale>`
- `potion-duration-scale set <scale>`
- `trim set <trim-material> <trim-pattern>`
- `instrument set <instrument>`
- `provides-trim-material set <trim-material>`
- `ominous-bottle-amplifier set <amplifier>`
- `jukebox-playable set <jukebox-song>`
- `provides-banner-patterns set <patterns>`
- `firework-explosion set <firework-effect>`
- `note-block-sound set <sound>`
- `base-color set <dye-color>`
- `break-sound set <sound>`
- `villager-variant set <villager-type>`
- `wolf-variant set <wolf-variant>`
- `wolf-sound-variant set <wolf-sound-variant>`
- `wolf-collar set <dye-color>`
- `fox-variant set <fox-type>`
- `salmon-size set <salmon-variant>`
- `parrot-variant set <parrot-variant>`
- `tropical-fish-pattern set <fish-pattern>`
- `tropical-fish-base-color set <dye-color>`
- `tropical-fish-pattern-color set <dye-color>`
- `mooshroom-variant set <mooshroom-variant>`
- `rabbit-variant set <rabbit-type>`
- `pig-variant set <pig-variant>`
- `pig-sound-variant set <pig-sound-variant>`
- `cow-variant set <cow-variant>`
- `cow-sound-variant set <cow-sound-variant>`
- `chicken-variant set <chicken-variant>`
- `chicken-sound-variant set <chicken-sound-variant>`
- `frog-variant set <frog-variant>`
- `horse-variant set <horse-color>`
- `painting-variant set <painting-art>`
- `llama-variant set <llama-color>`
- `axolotl-variant set <axolotl-variant>`
- `zombie-nautilus-variant set <zombie-nautilus-variant>`
- `cat-variant set <cat-type>`
- `cat-sound-variant set <cat-sound-variant>`
- `cat-collar set <dye-color>`
- `sheep-color set <dye-color>`
- `shulker-color set <dye-color>`

## Non-Valued Flags

- `unbreakable set`
- `intangible-projectile set`
- `glider set`

## Lore

- `lore set <line...>`
- `lore line add [line]`
- `lore line insert <index> [line]`
- `lore line set <index> [line]`
- `lore line remove <index>`
- `lore line clear`

## Enchantments

- `enchantments set <enchantment> <level>`
- `enchantments add <enchantment> <level>`
- `enchantments remove <enchantment>`
- `enchantments clear`
- `stored-enchantments set <enchantment> <level>`
- `stored-enchantments add <enchantment> <level>`
- `stored-enchantments remove <enchantment>`
- `stored-enchantments clear`

## Adventure Predicates

- `can-place-on add <blocks>`
- `can-place-on insert <index> <blocks>`
- `can-place-on set <index> <blocks>`
- `can-place-on remove <index>`
- `can-place-on clear`
- `can-break add <blocks>`
- `can-break insert <index> <blocks>`
- `can-break set <index> <blocks>`
- `can-break remove <index>`
- `can-break clear`

## Attribute Modifiers

- `attribute-modifiers add <attribute> <modifier-key> <modifier-amount> <operation> [slot-group]`
- `attribute-modifiers add <attribute> <modifier-key> <modifier-amount> <operation> [slot-group] display default`
- `attribute-modifiers add <attribute> <modifier-key> <modifier-amount> <operation> [slot-group] display hidden`
- `attribute-modifiers add <attribute> <modifier-key> <modifier-amount> <operation> [slot-group] display text <display-text>`
- `attribute-modifiers remove index <index>`
- `attribute-modifiers remove key <modifier-key>`
- `attribute-modifiers remove attribute <attribute>`
- `attribute-modifiers clear`
- `attribute-modifiers display set <index> default`
- `attribute-modifiers display set <index> hidden`
- `attribute-modifiers display set <index> text <display-text>`
- `attribute-modifiers slot set <index> <slot-group>`

## Custom Model Data

- `custom-model-data floats add <model-float>`
- `custom-model-data floats insert <index> <model-float>`
- `custom-model-data floats set <index> <model-float>`
- `custom-model-data floats remove <index>`
- `custom-model-data floats clear`
- `custom-model-data flags add <model-flag>`
- `custom-model-data flags insert <index> <model-flag>`
- `custom-model-data flags set <index> <model-flag>`
- `custom-model-data flags remove <index>`
- `custom-model-data flags clear`
- `custom-model-data strings add <model-string>`
- `custom-model-data strings insert <index> <model-string>`
- `custom-model-data strings set <index> <model-string>`
- `custom-model-data strings remove <index>`
- `custom-model-data strings clear`
- `custom-model-data colors add <model-color>`
- `custom-model-data colors insert <index> <model-color>`
- `custom-model-data colors set <index> <model-color>`
- `custom-model-data colors remove <index>`
- `custom-model-data colors clear`

## Tooltip Display

- `tooltip-display hide-tooltip set <hide-tooltip>`
- `tooltip-display hidden add <component>`
- `tooltip-display hidden remove <component>`
- `tooltip-display hidden clear`

## Food

- `food nutrition set <nutrition>`
- `food saturation set <saturation>`
- `food can-always-eat set <can-always-eat>`
- `food set <nutrition> <saturation> [can-always-eat]`

## Consumable

- `consumable consume-seconds set <seconds>`
- `consumable animation set <animation>`
- `consumable sound set <sound>`
- `consumable has-consume-particles set <has-particles>`
- `consumable effects add <consume-effect>`
- `consumable effects insert <index> <consume-effect>`
- `consumable effects set <index> <consume-effect>`
- `consumable effects remove <index>`
- `consumable effects clear`

## Use Effects

- `use-effects can-sprint set <can-sprint>`
- `use-effects interact-vibrations set <interact-vibrations>`
- `use-effects speed-multiplier set <multiplier>`
- `use-effects set <can-sprint> <interact-vibrations> <speed-multiplier>`

## Use Cooldown

- `use-cooldown seconds set <seconds>`
- `use-cooldown cooldown-group set <cooldown-group>`
- `use-cooldown cooldown-group clear`
- `use-cooldown set <seconds> [cooldown-group]`

## Tool

- `tool default-mining-speed set <speed>`
- `tool damage-per-block set <damage>`
- `tool can-destroy-blocks-in-creative set <can-destroy>`
- `tool rules add <blocks> [speed] [correct-for-drops]`
- `tool rules insert <index> <blocks> [speed] [correct-for-drops]`
- `tool rules set <index> <blocks> [speed] [correct-for-drops]`
- `tool rules remove <index>`
- `tool rules clear`

## Weapon

- `weapon item-damage-per-attack set <damage>`
- `weapon disable-blocking-for-seconds set <seconds>`
- `weapon set <item-damage-per-attack> <disable-blocking-for-seconds>`

## Equippable

- `equippable slot set <equipment-slot>`
- `equippable equip-sound set <sound>`
- `equippable asset-id set <asset>`
- `equippable asset-id clear`
- `equippable camera-overlay set <overlay>`
- `equippable camera-overlay clear`
- `equippable allowed-entities set <entities>`
- `equippable allowed-entities clear`
- `equippable dispensable set <dispensable>`
- `equippable swappable set <swappable>`
- `equippable damage-on-hurt set <damage-on-hurt>`
- `equippable equip-on-interact set <equip-on-interact>`
- `equippable can-be-sheared set <can-be-sheared>`
- `equippable shear-sound set <sound>`
- `equippable set <equipment-slot>`

## Death Protection

- `death-protection effects add <consume-effect>`
- `death-protection effects insert <index> <consume-effect>`
- `death-protection effects set <index> <consume-effect>`
- `death-protection effects remove <index>`
- `death-protection effects clear`

## Blocks Attacks

- `blocks-attacks block-delay-seconds set <seconds>`
- `blocks-attacks disable-cooldown-scale set <scale>`
- `blocks-attacks damage-reductions add <damage-types> <horizontal-angle> <base> <factor>`
- `blocks-attacks damage-reductions insert <index> <damage-types> <horizontal-angle> <base> <factor>`
- `blocks-attacks damage-reductions set <index> <damage-types> <horizontal-angle> <base> <factor>`
- `blocks-attacks damage-reductions remove <index>`
- `blocks-attacks damage-reductions clear`
- `blocks-attacks item-damage threshold set <threshold>`
- `blocks-attacks item-damage base set <base>`
- `blocks-attacks item-damage factor set <factor>`
- `blocks-attacks item-damage set <threshold> <base> <factor>`
- `blocks-attacks bypassed-by set <damage-types>`
- `blocks-attacks bypassed-by clear`
- `blocks-attacks block-sound set <sound>`
- `blocks-attacks block-sound clear`
- `blocks-attacks disable-sound set <sound>`
- `blocks-attacks disable-sound clear`

## Piercing Weapon

- `piercing-weapon deals-knockback set <deals-knockback>`
- `piercing-weapon dismounts set <dismounts>`
- `piercing-weapon sound set <sound>`
- `piercing-weapon sound clear`
- `piercing-weapon hit-sound set <sound>`
- `piercing-weapon hit-sound clear`

## Kinetic Weapon

- `kinetic-weapon contact-cooldown-ticks set <ticks>`
- `kinetic-weapon delay-ticks set <ticks>`
- `kinetic-weapon forward-movement set <movement>`
- `kinetic-weapon damage-multiplier set <multiplier>`
- `kinetic-weapon sound set <sound>`
- `kinetic-weapon sound clear`
- `kinetic-weapon hit-sound set <sound>`
- `kinetic-weapon hit-sound clear`
- `kinetic-weapon dismount-conditions set <max-duration-ticks> <min-speed> <min-relative-speed>`
- `kinetic-weapon dismount-conditions clear`
- `kinetic-weapon knockback-conditions set <max-duration-ticks> <min-speed> <min-relative-speed>`
- `kinetic-weapon knockback-conditions clear`
- `kinetic-weapon damage-conditions set <max-duration-ticks> <min-speed> <min-relative-speed>`
- `kinetic-weapon damage-conditions clear`

## Attack Range

- `attack-range min-reach set <reach>`
- `attack-range max-reach set <reach>`
- `attack-range min-creative-reach set <reach>`
- `attack-range max-creative-reach set <reach>`
- `attack-range hitbox-margin set <margin>`
- `attack-range mob-factor set <factor>`
- `attack-range set <min-reach> <max-reach> <min-creative-reach> <max-creative-reach> <hitbox-margin> <mob-factor>`

## Swing Animation

- `swing-animation type set <none|whack|stab>`
- `swing-animation duration set <ticks>`
- `swing-animation set <animation> <ticks>`

## Map Decorations

- `map-decorations put <decoration-id> <decoration-type> <x> <z> <rotation>`
- `map-decorations remove <decoration-id>`
- `map-decorations clear`
- `map-decorations type set <decoration-id> <decoration-type>`
- `map-decorations position set <decoration-id> <x> <z>`
- `map-decorations rotation set <decoration-id> <rotation>`

## ItemStack List Components

- `charged-projectiles add <item>`
- `charged-projectiles insert <index> <item>`
- `charged-projectiles set <index> <item>`
- `charged-projectiles remove <index>`
- `charged-projectiles clear`
- `bundle-contents add <item>`
- `bundle-contents insert <index> <item>`
- `bundle-contents set <index> <item>`
- `bundle-contents remove <index>`
- `bundle-contents clear`
- `container add <item>`
- `container insert <index> <item>`
- `container set <index> <item>`
- `container remove <index>`
- `container clear`

## Potion Contents

- `potion-contents potion set <potion-type>`
- `potion-contents potion clear`
- `potion-contents custom-color set <color>`
- `potion-contents custom-color clear`
- `potion-contents custom-name set <custom-name>`
- `potion-contents custom-name clear`
- `potion-contents custom-effects add <potion-effect>`
- `potion-contents custom-effects insert <index> <potion-effect>`
- `potion-contents custom-effects set <index> <potion-effect>`
- `potion-contents custom-effects remove <index>`
- `potion-contents custom-effects clear`

## Suspicious Stew Effects

- `suspicious-stew-effects add <effect-type> <duration-ticks>`
- `suspicious-stew-effects insert <index> <effect-type> <duration-ticks>`
- `suspicious-stew-effects set <index> <effect-type> <duration-ticks>`
- `suspicious-stew-effects remove <index>`
- `suspicious-stew-effects clear`

## Book Content

- `writable-book-content pages add <page>`
- `writable-book-content pages add-filtered <page> <filtered-page>`
- `writable-book-content pages insert <index> <page>`
- `writable-book-content pages set <index> <page>`
- `writable-book-content pages remove <index>`
- `writable-book-content pages clear`
- `written-book-content title set <title>`
- `written-book-content title set-filtered <title> <filtered-title>`
- `written-book-content author set <author>`
- `written-book-content generation set <generation>`
- `written-book-content resolved set <resolved>`
- `written-book-content pages add <page>`
- `written-book-content pages add-filtered <page> <filtered-page>`
- `written-book-content pages insert <index> <page>`
- `written-book-content pages set <index> <page>`
- `written-book-content pages remove <index>`
- `written-book-content pages clear`

## Fireworks

- `fireworks flight-duration set <duration>`
- `fireworks effects add <firework-effect>`
- `fireworks effects insert <index> <firework-effect>`
- `fireworks effects set <index> <firework-effect>`
- `fireworks effects remove <index>`
- `fireworks effects clear`

## Recipes

- `recipes add <recipe-key>`
- `recipes insert <index> <recipe-key>`
- `recipes set <index> <recipe-key>`
- `recipes remove <index>`
- `recipes clear`

## Lodestone Tracker

- `lodestone-tracker location set <world> <x> <y> <z>`
- `lodestone-tracker location here`
- `lodestone-tracker location clear`
- `lodestone-tracker tracked set <tracked>`
- `lodestone-tracker set <world> <x> <y> <z> <tracked>`

## Profile

- `profile name set <profile-name>`
- `profile name clear`
- `profile uuid set <uuid>`
- `profile uuid clear`
- `profile property add <property-name> <property-value> [signature]`
- `profile property remove <property-name>`
- `profile property clear`
- `profile skin-patch body set <body-texture>`
- `profile skin-patch body clear`
- `profile skin-patch cape set <cape-texture>`
- `profile skin-patch cape clear`
- `profile skin-patch elytra set <elytra-texture>`
- `profile skin-patch elytra clear`
- `profile skin-patch model set <classic|slim>`
- `profile skin-patch model clear`
- `profile skin-patch clear`
- `profile resolve`
- `profile from-player <player>`

## Banner Patterns

- `banner-patterns add <pattern-type> <dye-color>`
- `banner-patterns insert <index> <pattern-type> <dye-color>`
- `banner-patterns set <index> <pattern-type> <dye-color>`
- `banner-patterns remove <index>`
- `banner-patterns clear`

## Pot Decorations

- `pot-decorations back set <item-type>`
- `pot-decorations back clear`
- `pot-decorations left set <item-type>`
- `pot-decorations left clear`
- `pot-decorations right set <item-type>`
- `pot-decorations right clear`
- `pot-decorations front set <item-type>`
- `pot-decorations front clear`
- `pot-decorations set <back> <left> <right> <front>`

## Block Data

- `block-data set <block-data-string>`
- `block-data property set <property> <value>`
- `block-data property remove <property>`
- `block-data property clear`

## Container Loot

- `container-loot loot-table set <loot-table>`
- `container-loot seed set <container-loot-seed>`
- `container-loot set <loot-table> [container-loot-seed]`
