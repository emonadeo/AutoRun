package com.emonadeo.autorun;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class AutoRun implements ClientModInitializer {

    public static final String MODID = "autorun";
    public static final File CFG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "autorun.properties");

    private static KeyBinding keyBinding;
    private static Set<MovementDirection> toggled;
    private static long timeActivated;
    private static int delayBuffer;

    private static boolean originalAutoJumpSetting;
    private static boolean toggleAutoJump;

    @Override
    public void onInitializeClient() {
        AutoRun.toggled = new HashSet<>();
        AutoRun.timeActivated = -1;
        AutoRun.delayBuffer = 20;
        AutoRun.toggleAutoJump = true;
        AutoRun.keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autorun.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O, // Default to 'o'
                "key.categories.movement" // Append movement category
        ));

        loadConfig(CFG_FILE);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null && keyBinding.wasPressed()) {
                boolean activating = toggled.isEmpty();

                if (!activating) {
                    // Deactivating by pressing AutoRun key
                    toggled.clear();
                    restoreAutoJump(client);
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

                    timeActivated = client.world.getTime();
                    enableAutoJump(client);
                }
            }

            if (timeActivated != -1 && client.world != null
                    && client.world.getTime() - timeActivated >= delayBuffer) {
                x:
                for (MovementDirection dir : toggled) {
                    for (KeyBinding terminator : dir.getTerminators(client)) {
                        if (terminator.isPressed()) {
                            // Deactivating by pressing movement key
                            toggled.clear();
                            timeActivated = -1;
                            restoreAutoJump(client);
                            break x;
                        }
                    }
                }
            }
        });

        ClientEntityEvents.ENTITY_UNLOAD.register((entity, clientWorld) -> {
            if (entity instanceof ClientPlayerEntity) {
                restoreAutoJump(MinecraftClient.getInstance());
                toggled.clear();
            }
        });
    }

    public static void enableAutoJump(MinecraftClient client) {
        if (!toggleAutoJump)
            return;

        originalAutoJumpSetting = client.options.autoJump;

        client.options.autoJump = true;
        client.options.sendClientSettings();
    }

    public static void restoreAutoJump(MinecraftClient client) {
        if (!toggleAutoJump)
            return;

        client.options.autoJump = originalAutoJumpSetting;
        client.options.sendClientSettings();
    }

    public static void loadConfig(File file) {
        try {
            Properties cfg = new Properties();
            if (!file.exists()) {
                saveConfig(file);
            }
            cfg.load(new FileInputStream(file));
            delayBuffer = Integer.parseInt(cfg.getProperty("delayBuffer", "20"));
            toggleAutoJump = Boolean.parseBoolean(cfg.getProperty("toggleAutoJump", "true"));

            // Re-save so that new properties will appear in old config files
            saveConfig(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig(File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file, false);
            fos.write(("delayBuffer=" + delayBuffer + "\n").getBytes());
            fos.write(("toggleAutoJump=" + toggleAutoJump + "\n").getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<MovementDirection> getToggled() {
        return toggled;
    }

    public static int getDelayBuffer() {
        return delayBuffer;
    }

    public static void setDelayBuffer(int delayBuffer) {
        AutoRun.delayBuffer = delayBuffer;
    }
}
