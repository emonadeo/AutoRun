package com.emonadeo.autorun.mixin;

import com.emonadeo.autorun.AutoRun;
import com.emonadeo.autorun.MovementDirection;
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
    private boolean onPressingForward(KeyboardInput input) {
        return input.pressingForward || AutoRun.getToggled().contains(MovementDirection.FORWARD);
    }

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "net/minecraft/client/input/KeyboardInput.pressingBack:Z", opcode = Opcodes.GETFIELD))
    private boolean onPressingBack(KeyboardInput input) {
        return input.pressingBack || AutoRun.getToggled().contains(MovementDirection.BACK);
    }

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "net/minecraft/client/input/KeyboardInput.pressingLeft:Z", opcode = Opcodes.GETFIELD))
    private boolean onPressingLeft(KeyboardInput input) {
        return input.pressingLeft || AutoRun.getToggled().contains(MovementDirection.LEFT);
    }

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "net/minecraft/client/input/KeyboardInput.pressingRight:Z", opcode = Opcodes.GETFIELD))
    private boolean onPressingRight(KeyboardInput input) {
        return input.pressingRight || AutoRun.getToggled().contains(MovementDirection.RIGHT);
    }
}
