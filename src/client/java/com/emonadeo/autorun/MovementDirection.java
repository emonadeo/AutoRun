package com.emonadeo.autorun;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public enum MovementDirection {
	FORWARD, BACK, LEFT, RIGHT;

	public KeyBinding getKeyBinding(MinecraftClient client) {
		switch (this) {
			default :
				return client.options.forwardKey;
			case BACK :
				return client.options.backKey;
			case LEFT :
				return client.options.leftKey;
			case RIGHT :
				return client.options.rightKey;
		}
	}

	public Set<KeyBinding> getTerminators(MinecraftClient client) {
		switch (this) {
			default :
				return Stream.of(client.options.forwardKey, client.options.backKey).collect(Collectors.toSet());
			case LEFT :
			case RIGHT :
				return Stream.of(client.options.leftKey, client.options.rightKey).collect(Collectors.toSet());
		}
	}
}
