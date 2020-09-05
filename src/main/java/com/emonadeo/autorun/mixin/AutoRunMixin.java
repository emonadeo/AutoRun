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
        input.pressingForward = input.pressingForward || AutoRun.getToggled().contains(MovementDirection.FORWARD);
        return input.pressingForward;
    }

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "net/minecraft/client/input/KeyboardInput.pressingBack:Z", opcode = Opcodes.GETFIELD))
    private boolean onPressingBack(KeyboardInput input) {
        input.pressingBack = input.pressingBack || AutoRun.getToggled().contains(MovementDirection.BACK);
        return input.pressingBack;
    }

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "net/minecraft/client/input/KeyboardInput.pressingLeft:Z", opcode = Opcodes.GETFIELD))
    private boolean onPressingLeft(KeyboardInput input) {
        input.pressingLeft = input.pressingLeft || AutoRun.getToggled().contains(MovementDirection.LEFT);
        return input.pressingLeft;
    }

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "net/minecraft/client/input/KeyboardInput.pressingRight:Z", opcode = Opcodes.GETFIELD))
    private boolean onPressingRight(KeyboardInput input) {
        input.pressingRight = input.pressingRight || AutoRun.getToggled().contains(MovementDirection.RIGHT);
        return input.pressingRight;
    }
}
