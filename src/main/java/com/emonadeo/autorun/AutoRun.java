package com.emonadeo.autorun;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoRun implements ModInitializer {

    private static FabricKeyBinding keyBinding;
    private static Set<MovementDirection> toggled;

    @Override
    public void onInitialize() {
        AutoRun.toggled = new HashSet<>();
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
                boolean activating = toggled.isEmpty();

                if (!activating) {
                    // Deactivating
                    toggled.clear();
                } else {
                    // Activating
                    Set<MovementDirection> pressedDirections = Arrays.stream(MovementDirection.values())
                            .filter(dir -> dir.getKeyBinding(client).isPressed()).collect(Collectors.toSet());

                    if (!pressedDirections.isEmpty()) {
                        // Activate pressed directions
                        toggled.addAll(pressedDirections);
                    } else {
                        // Activate forward by default
                        toggled.add(MovementDirection.FORWARD);
                    }
                }
            }
        });
    }

    public static Set<MovementDirection> getToggled() {
        return toggled;
    }
}
