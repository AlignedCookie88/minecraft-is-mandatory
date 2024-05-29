package com.alignedcookie88.minecraft_is_mandatory.mixin;

import com.alignedcookie88.minecraft_is_mandatory.MinecraftIsMandatory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuMixin extends Screen {
    protected GameMenuMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        boolean drawTrollButton = !((GameMenuScreen) (Object) this).shouldShowMenu();

        if (drawTrollButton) {
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Secret exit"), (button) -> {
                button.setMessage(Text.literal("sike"));
                MinecraftIsMandatory.attemptsToClose++;
            }).dimensions(this.width - 104, this.height - 24, 100, 20).build());
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MinecraftIsMandatory.drawInfo(context, this.textRenderer);
    }
}
