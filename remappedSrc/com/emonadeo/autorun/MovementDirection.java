package com.emonadeo.autorun;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum MovementDirection {
    FORWARD,
    BACK,
    LEFT,
    RIGHT;

    public KeyBinding getKeyBinding(MinecraftClient client) {
        switch (this) {
            default:
                return client.options.keyForward;
            case BACK:
                return client.options.keyBack;
            case LEFT:
                return client.options.keyLeft;
            case RIGHT:
                return client.options.keyRight;
        }
    }

    public Set<KeyBinding> getTerminators(MinecraftClient client) {
        switch (this) {
            default:
                return Stream.of(
                        client.options.keyForward,
                        client.options.keyBack)
                        .collect(Collectors.toSet());
            case LEFT:
            case RIGHT:
                return Stream.of(
                        client.options.keyLeft,
                        client.options.keyRight)
                        .collect(Collectors.toSet());
        }
    }
}
