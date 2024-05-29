package com.alignedcookie88.minecraft_is_mandatory.mixin;

import com.alignedcookie88.minecraft_is_mandatory.MinecraftIsMandatory;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "stop", at = @At("HEAD"), cancellable = true)
    public void stop(CallbackInfo ci) {
        MinecraftIsMandatory.attemptsToClose++;
        ci.cancel();
    }

    @Inject(method = "scheduleStop", at = @At("HEAD"), cancellable = true)
    public void scheduleStop(CallbackInfo ci) {
        MinecraftIsMandatory.attemptsToClose++;
        ci.cancel();
    }

}
