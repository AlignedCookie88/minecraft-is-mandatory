package com.alignedcookie88.minecraft_is_mandatory;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;

public class MinecraftIsMandatory implements ModInitializer {

    public static int attemptsToClose = 0;

    public static int ticksOpen = 0;

    public static Logger LOGGER = LoggerFactory.getLogger("minecraft is mandatory");

    private static int ticksUntilCrash = -1;

    boolean wasWorldLoaded = false;

    private int discordTimer = 0;

    private IPCClient discord;

    private boolean discordEnabled;

    private OffsetDateTime startTime;

    @Override
    public void onInitialize() {
        // Start time
        startTime = OffsetDateTime.now();

        // Discord RPC
        discordEnabled = false;

        try {
            setupDiscordRPC();
        } catch (NoDiscordClientException e) {
            LOGGER.info("Couldn't connect to discord, RPC is disabled.");
        }

        // Events
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Tick
            ticksOpen++;

            // Update discord
            if (discordEnabled) {
                if (discordTimer == 0) {
                    updateRPC();
                    discordTimer = 50;
                } else discordTimer--;
            }

            // Setup server TPS watcher
            if (MinecraftClient.getInstance().world == null) {
                ticksUntilCrash = -1;
                wasWorldLoaded = false;
            } else {
                if (!wasWorldLoaded)
                    ticksUntilCrash = 5;
                wasWorldLoaded = true;
            }
        });

        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            if (ticksUntilCrash == 0) {
                float tickdelta = ((float) server.getTickTimes()[server.getTicks() % 100]) / 1000 / 1000;
                if (tickdelta > 200) {
                    String message = "minecraft is mandatory detected low TPS, and closed the world to stop you from lagging the game.";
                    LOGGER.info(message);
                    attemptsToClose++;
                    throw new RuntimeException(message);
                }
            } else {
                ticksUntilCrash--;
            }
        });

    }


    public static void drawInfo(DrawContext context, TextRenderer textRenderer) {
        context.drawText(textRenderer, String.format("You have attempted to close Minecraft %s times.", attemptsToClose), 4, 4, 0xFFFFFF, true);
        context.drawText(textRenderer, String.format("You've been playing for %s.", TimeFormatter.fromTicks(ticksOpen).formatStandard()), 4, 16, 0xFFFFFF, true);
    }


    private void setupDiscordRPC() throws NoDiscordClientException {
        discord = new IPCClient(1245093002507194522L);
        discord.setListener(new IPCListener(){
            @Override
            public void onReady(IPCClient client) {
                discordEnabled = true;
                updateRPC();
            }
        });
        discord.connect();
    }

    private void updateRPC() {
        RichPresence.Builder builder = new RichPresence.Builder();
        builder.setState(String.format("Minecraft %d times.", attemptsToClose))
                .setDetails("Has attempted to close")
                .setStartTimestamp(startTime)
                .setLargeImage("cover", "minecraft is mandatory");
        discord.sendRichPresence(builder.build());
    }
}
