# Iteminator Command Design

This document explains the command design behind the data-component version of
Iteminator. The companion command list lives in
[`command-catalog.md`](command-catalog.md).

The goal is to make item editing feel as powerful as using Paper's data
component API directly, while keeping command syntax predictable enough that
players can learn the system once and apply it to future Minecraft components.

## Core Model

- Iteminator edits the item in the player's main hand.
- Commands should be based on Paper data components, not `ItemMeta`.
- Components are named in kebab-case: `CUSTOM_NAME` becomes `custom-name`.
- Tab completion for `/iteminator edit <component>` should include every editable
  data component.
- Root support commands such as `inspect`, `components`, and `defaults` are kept
  because they make the system discoverable without bloating edit syntax.
- `inspect` and `defaults` should share item component printing logic so current
  values and default values render consistently.
- `defaults` should read defaults from Paper's item type API:
  `item.getType().getDefaultData(type)`, `hasDefaultData(type)`, and
  `getDefaultDataTypes()`. Do not depend on an external generated `items.json`
  report.
- `amount` and `type` are not data components, but they are fundamental item
  stack edits and belong beside the component editor.
- Map view edits are intentionally not part of the component catalog. The item
  stores a map identity with `MAP_ID`; center, scale, tracking, locking, world,
  and renderers live on the server's `MapView` state for that map ID and affect
  every item pointing at that map.
- If MapView editing is ever added, it should be a clearly separate non-component
  command group that edits the shared map object by map ID, warns that all items
  pointing at that map are affected, and requires an existing map identity.
- Iteminator should not reject component edits merely because the component does
  not affect the current item type. Data components can be attached to any item
  stack, and they may become meaningful after the item type changes.

## Component Operations

Every data component has these component-level commands:

- `/iteminator edit <component> unset`
- `/iteminator edit <component> reset`

Use these meanings everywhere:

- `unset`: remove the explicit component from the item.
- `reset`: restore the component to the default value for the item's type, if one
  exists.
- `set`: set a value or add a non-valued component.
- `clear`: clear a nullable subfield or collection contents.
- `remove`: remove a specific list, map, or set entry.

`unset` and `reset` are component-level operations only. Do not use them for
nested fields. For nested fields, use `clear`.

For top-level collection components, `clear` means "keep the component present
with an empty collection." This may behave like `unset` for some vanilla
components, but it is still the explicit-empty operation.

Command descriptions and help text should clearly distinguish these operations.
In particular, `clear` should explain its target: for nullable fields it sets the
field to null, and for collections it empties the collection while preserving the
containing component.

## Value Shape Rules

Use the component value's shape to choose the command tree:

- Non-valued components use `set`.
- Simple valued components use `set <role>`.
- Wrapped primitives should feel like simple values, even if Paper exposes a
  component object. For example, `enchantable set <enchantability>` and
  `map-id set <map-id>`.
- Compound components expose their API fields as short subcommands.
- Lists support `add`, `insert <index>`, `set <index>`, `remove <index>`, and
  `clear`.
- Maps support `put` or `set`, `remove <entry-key>`, and `clear`.
- Sets support `add`, `remove`, and `clear`.
- Field-level commands are the discoverable path. Whole-object `set` commands
  are the fast path when they are readable.

Numeric indices are zero-based, matching Java lists.

## Semantic Help Text

Command help should describe Minecraft semantics, not just storage types. For
example, `damage` is the number of durability uses consumed, not durability
remaining. A command may still be short, but its description should use the
precise unit.

If vanilla or Paper uses a sentinel value, accept a named literal in the same
argument position as the value it represents instead of making players remember
the magic value. Prefer `container-loot seed set random` over a separate
`container-loot seed random` verb.

Known current sentinel-style values:

- `container-loot seed set random`: writes the random-seed sentinel for container
  loot. In vanilla format this is represented by an omitted seed or `0`.
- Tool rule `correct-for-drops` accepts `not-set`, matching the API's tri-state
  "no opinion" value.
- Attribute modifier display accepts `default`, matching the API's reset/default
  display value.

Sentinel literals are not the same as `clear`. A sentinel is a concrete value in
the field's domain, such as tri-state `not-set`. `clear` removes a nullable
field value or empties a collection.

## Argument Names

Argument names should describe what the argument means in that command, not the
Java/API type. Prefer:

```text
custom-name set <name>
attribute-modifiers add <attribute> <modifier-key> <modifier-amount> ...
written-book-content pages add <page>
```

Avoid exposing implementation types as the user-facing argument name:

```text
custom-name set <component>
profile property add <name> <value>
```

Use type-like names only when the role is genuinely generic, such as:

```text
block-data property set <property> <value>
```

Text values should use the player's selected Iteminator formatting mode by
default. Name commands require a name; empty text should be represented by
unsetting the component, not by an optional empty `set` value.

## Shared Parsers

These parser fragments are reused by multiple command families.

- `<key>`: namespaced key, defaulting to `minecraft:` when omitted.
- `<color>`: `#rrggbb`, `rgb <r> <g> <b>`, or named `DyeColor` where applicable.
- `<component>`: data component type key or kebab-case Iteminator name.
- `<item>`: `offhand`, `slot <slot>`, or `item <item-type> [amount]`.
- `<registry-set>`: `tag <tag-key>` or `values <key...>`.
- `<container-loot-seed>`: `<long>` or `random`.
- `<tri-state>`: `true`, `false`, or `not-set`.
- `<potion-effect>`: `<effect-type> <duration-ticks> <amplifier> [--ambient] [--hide-particles] [--hide-icon]`.
- `<firework-effect>`: `<shape> --color <color...> [--fade <color...>] [--flicker] [--trail]`.
- `<profile-property>`: `<property-name> <property-value> [signature]`.
- `<filtered-string>`: `<raw-text> [--filtered <filtered-text>]`.
- `<filtered-component>`: `<raw-text> [--filtered <filtered-text>]`.

Use flags for optional effect attributes and variable-length lists. Keep the
identity and required numeric fields positional so common commands stay short.

The `<item>` parser intentionally does not include `hand`, because the main hand
is already the edit target, and it does not include `cursor`, because players
cannot type commands while holding an item on the cursor.

## Shared Compound Fragments

### Consume Effects

Used by `consumable effects` and `death-protection effects`.

- `effects add apply-status-effects <probability> <potion-effect...>`
- `effects add remove-status-effects <effects>`
- `effects add clear-all-status-effects`
- `effects add teleport-randomly <diameter>`
- `effects add play-sound <sound>`
- `effects insert <index> <consume-effect>`
- `effects set <index> <consume-effect>`
- `effects remove <index>`
- `effects clear`

### ItemStack Lists

Used by `charged-projectiles`, `bundle-contents`, and `container`.

- `add <item>`
- `insert <index> <item>`
- `set <index> <item>`
- `remove <index>`
- `clear`

## Implementation Principles

- Default-value creation should be centralized per compound component so
  `modify(previous -> rebuilt)` can work when `previous` is null.
- Most list, map, and set commands should share immutable reconstruction helpers.
- Prefer registry parsers for Bukkit/Paper registries and enum parsers only for
  true enums.
- Prefer semantic remove commands where values have stable keys, and index remove
  commands where they do not.
- Support commands that print component values should share formatting logic.
- Help text should be written from the player's perspective, not from the Java
  type hierarchy.

## Extension Workflow

When Minecraft or Paper adds a new data component:

1. Convert the component constant to kebab-case.
2. Add `unset` and `reset`.
3. Inspect the component value type.
4. If it is non-valued, add `set`.
5. If it is simple or a wrapped primitive, add `set <role>`.
6. If it is compound, add field subcommands.
7. Add `clear` for nullable fields.
8. Add standard list, map, or set verbs for collection fields.
9. Look for sentinel values or magic defaults and expose named literals in the
   same argument position as the value they represent.
10. Reuse shared parsers or add a new shared parser if the value shape appears in
   more than one component.
11. Re-run the component coverage check against `DataComponentTypes`.

Later minimal-letter aliases should be generated mechanically from the canonical
command tree rather than designed by hand.
