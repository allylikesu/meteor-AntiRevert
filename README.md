# Meteor Addon: AntiRevert

1.20.4, built against Meteor 0.5.6.

Prevents reverting illegal items. Made for 0b0t.org

Comes with 1 module, AntiRevert, and 1 HUD element, AntiRevertWarning. The HUD element puts a warning on your screen
whenever the AntiRevert module isn't enabled.

### How to use

Download the .jar from releases, and put it in your mods folder.

### Config

There isn't a lot of config. There is a toggle for checking unbreakables, as the status of these in 0b's AntiIllegals 
config changes periodically. If you want to add/remove illegal blocks, you'll need to edit the `List` in 
`lol.zt8.AntiRevertAddon.modules.AntiRevertModule` and then recompile.

### What is illegal?

Superillegal blocks/items:
- Barrier
- Structure Void
- Light Block
- Command Block
- Chain Command Block
- Repeating Command Block
- Structure Block
- Jigsaw Block
- Bedrock
- Reinforced Deepslate
- End Portal Frame
- Spawner
- Command Block Minecart
- Debug Stick
- All spawn eggs

Overstacked items

Over-enchanted items (Checks if the enchant levels are greater than their maximum; For example, it WILL flag a 
Sharpness 6 sword, but NOT a Infinity + Mending bow)

Items with the Unbreakable tag (toggleable in module settings)

**And ANY shulker box with an illegal item inside!**

### How does it prevent reverting?

On 0b0t, the AntiIllegals is configured to not instantly revert illegals in a player's enderchest, only reverting if the
item is interacted with.

This addon prevents interacting (Left-clicking, right-clicking, shift-clicking, moving with number keys) with illegal 
items while they are in containers. It cancels the ClickSlotC2S packet, then closes the container gui to prevent de-syncing.
It does NOT prevent reverting illegals in your inventory (I believe there is an extra packet sent?) or reverting illegals 
in item frames (yet, keep an eye out). 

It must be noted that the act of opening a chest, placed shulker, furnace, dispenser, etc. will automatically revert 
any illegals inside, which this addon can do nothing to prevent, as placed containers do not send the data of their contents
until they are opened.


## License

This addon is available under the CC0 license.
