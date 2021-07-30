package com.emonadeo.autorun.mixin;

import com.emonadeo.autorun.AutoRun;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public abstract class AltSprintMixin {
    @Shadow public abstract String getTranslationKey();

    @Inject(method="isPressed", at = @At("HEAD"), cancellable = true)
    private void injectAltSprint(CallbackInfoReturnable<Boolean> cir) {
        if (this.getTranslationKey().equals("key.sprint")
                && AutoRun.altSprintKeybinding.isPressed()) {
            cir.setReturnValue(true);
        }
    }
}