package lol.zt8.AntiRevertAddon.modules;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;


public class AntiRevertModule extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    // Illegal items list
    private final List<String> illlegalItems = List.of(
        "barrier",
        "structure_void",
        "light",
        "command_block",
        "chain_command_block",
        "repeating_command_block",
        "structure_block",
        "jigsaw",
        "bedrock",
        "reinforced_deepslate",
        "end_portal_frame",
        "command_block_minecart"
        );

    /**
     * Example setting.
     * The {@code name} parameter should be in kebab-case.
     * If you want to access the setting from another class, simply make the setting {@code public}, and use
     * {@link meteordevelopment.meteorclient.systems.modules.Modules#get(Class)} to access the {@link Module} object.
     */
    private final Setting<Boolean> checkUnbreakebles = sgGeneral.add(new BoolSetting.Builder()
        .name("Check unbreakables")
        .description("Whether to interacting with items with the Unbreakable tag")
        .defaultValue(true)
        .build()
    );

    /**
     * The {@code name} parameter should be in kebab-case.
     */
    public AntiRevertModule() {
        super(lol.zt8.AntiRevertAddon.AntiRevertAddon.CATEGORY, "AntiRevert", "Prevents reverting superillegals.");
    }

    @EventHandler
    private void onPacketSend(PacketEvent.Send event) {
        if (!(event.packet instanceof ClickSlotC2SPacket)) {
            return;
        }
        ClickSlotC2SPacket packet = (ClickSlotC2SPacket)(event.packet);

        // Check the stack in getStack(), this is not always included in the iterator for whatever reason!
        ItemStack singleStack = packet.getStack();
        if (isIllegal(singleStack)) {
            ChatUtils.sendMsg(Text.of("Oops! [" + singleStack + "]"));
            event.cancel();
            assert mc.currentScreen != null;
            mc.currentScreen.close();
            return;
        }

        // I fucking hate java, bro. what is this. what the FUCK is an Int2ObjectMap supposed to be??? Why do I have to call THREE FUNCTIONs to get an iterator???
        Int2ObjectMap<ItemStack> stacksMap = packet.getModifiedStacks();
        ObjectCollection<ItemStack> stacksCollection = stacksMap.values();

        // Iterate over the ItemStacks in getModifiedStacks()
        for (ItemStack stack : stacksCollection) {
            if (isIllegal(stack)) {
                ChatUtils.sendMsg(Text.of("Oops!"));
                event.cancel();
                assert mc.currentScreen != null;
                mc.currentScreen.close();
                return;
            }
        }

    }
    private boolean isIllegal(ItemStack stack) {
        Item item = stack.getItem();
        String name = item.toString();

        // Compare to the strings in the illegalItems list
        if (this.illlegalItems.contains(name)) {
            return true;
        }

        // Spawn egg check
        if (name.contains("spawn_egg")) {
            return true;
        }

        // Overstacked check
        if (stack.getCount() > stack.getMaxCount()) {
            return true;
        }

        // These checks require the item to have NBT data, so we skip these checks for any items without NBT
        if (stack.hasNbt()) {

            // Over-enchant check
            Map<Enchantment, Integer> enchants = EnchantmentHelper.get(stack);
            for (Enchantment enchant : enchants.keySet()) {
                if (enchants.get(enchant) > enchant.getMaxLevel()) {
                    return true;
                }
            }

            NbtCompound compound = stack.getNbt();
            // Unbreakable check (toggleable)
            if (this.checkUnbreakebles.get()) {
                if (compound.contains("Unbreakable", 1)) {
                    return true;
                }
            }

            // Recurse into shulkers
            if (compound.contains("BlockEntityTag")) {
                NbtCompound tags = compound.getCompound("BlockEntityTag");
                if (tags.contains("Items")) {
                        NbtList items = tags.getList("Items", 10);
                        for (int i = 0; i < items.size(); i++) {
                            NbtCompound itemCompound = items.getCompound(i);
                            ItemStack newItem = ItemStack.fromNbt(itemCompound);
                            //ChatUtils.sendMsg(Text.of(newItem.toString()));
                            if (this.isIllegal(newItem)) {
                                return true;
                            }
                        }
                }
            }
        }

        // Potion check
        // Ill do this later.

        // We are all good!
        return false;
    }

}
