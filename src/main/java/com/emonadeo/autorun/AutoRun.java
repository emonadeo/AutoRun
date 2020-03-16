package com.emonadeo.autorun;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class AutoRun implements ModInitializer {

    private static FabricKeyBinding keyBinding;
    private static boolean toggled;

    @Override
    public void onInitialize() {
        AutoRun.toggled = false;
        AutoRun.keyBinding = FabricKeyBinding.Builder.create(
                new Identifier("autorun", "toggle"),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P, // Default to 'p'
                "key.categories.movement" // Append movement category
        ).build();

        // Register Keybinding
        KeyBindingRegistry.INSTANCE.register(keyBinding);

        ClientTickCallback.EVENT.register(client -> {
            if (keyBinding.wasPressed()) {
                toggled = !toggled;
            }
            else if (client.options.keyBack.wasPressed() || client.options.keyForward.wasPressed()) {
                toggled = false;
            }
        });
    }

    public static boolean isToggled() {
        return toggled;
    }
}
