package com.emonadeo.autorun.mixin;

import com.emonadeo.autorun.AutoRun;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.KeyboardInput;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(KeyboardInput.class)
public class AutoRunMixin {

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "net/minecraft/client/input/KeyboardInput.pressingForward:Z", opcode = Opcodes.GETFIELD))
    private boolean onPressingForwardInTick(KeyboardInput input) {
        return input.pressingForward || AutoRun.isToggled();
    }
}
