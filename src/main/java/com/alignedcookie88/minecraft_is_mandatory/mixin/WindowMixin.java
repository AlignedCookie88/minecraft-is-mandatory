package com.alignedcookie88.minecraft_is_mandatory.mixin;

import com.alignedcookie88.minecraft_is_mandatory.MinecraftIsMandatory;
import com.mojang.blaze3d.platform.GLX;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Window.class)
public class WindowMixin {

    @Inject(method = "shouldClose", at = @At("HEAD"), cancellable = true)
    public void shouldClose(CallbackInfoReturnable<Boolean> cir) {
        if (GLX._shouldClose((Window) (Object) this)) {
            GLFW.glfwSetWindowShouldClose(((Window) (Object) this).getHandle(), false);
            MinecraftIsMandatory.attemptsToClose++;
        }
        cir.setReturnValue(false);
    }

}
