// ShowMeClient.java
package com.seuusuario.showme;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ShowMeClient implements ClientModInitializer {
    public static ShowMeConfig CONFIG = ShowMeConfig.load();

    private static boolean hudEnabled = true;
    private static KeyBinding toggleHudKey;
    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        System.out.println("[ShowMe] Inicializando mod - registrando teclas...");

        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.showme.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X,
                "key.categories.showme"));

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.showme.open_menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z,
                "key.categories.showme"));

        // Ajuste da mensagem: X alterna HUD e Z abre o menu
        System.out.println("[ShowMe] Teclas registradas: X (toggle HUD) e Z (menu)");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleHudKey.wasPressed()) {
                hudEnabled = !hudEnabled;
                toast(client, "HUD " + (hudEnabled ? "visível" : "oculto"));
                System.out.println("[ShowMe] Tecla x pressionada - HUD: " + hudEnabled);
            }
            while (openMenuKey.wasPressed()) {
                System.out.println("[ShowMe] Tecla z pressionada - Tentando abrir menu");
                if (client.currentScreen == null) {
                    System.out.println("[ShowMe] Abrindo tela de configuração");
                    client.setScreen(new ShowMeConfigScreen(client.currentScreen));
                } else {
                    System.out.println("[ShowMe] Não pode abrir - já há uma tela aberta: " + client.currentScreen.getClass().getSimpleName());
                }
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> renderHud(drawContext));
    }

    private static void renderHud(DrawContext ctx) {
        if (!hudEnabled) return;
        final var mc = MinecraftClient.getInstance();
        if (mc == null || mc.player == null || mc.world == null) return;

        var font = mc.textRenderer;
        List<String> lines = new ArrayList<>();

        if (CONFIG.showFps) {
            lines.add("FPS: " + mc.getCurrentFps());
        }
        if (CONFIG.showCoords) {
            BlockPos pos = mc.player.getBlockPos();
            lines.add(String.format("XYZ: %d / %d / %d", pos.getX(), pos.getY(), pos.getZ()));
        }
        if (CONFIG.showBrightness) {
            BlockPos pos = mc.player.getBlockPos();
            int blockLight = mc.world.getLightLevel(LightType.BLOCK, pos);
            int skyLight = mc.world.getLightLevel(LightType.SKY, pos);
            lines.add(String.format("Luz B/S: %d / %d", blockLight, skyLight));
        }

        if (lines.isEmpty()) return;

        int paddingX = 4;
        int paddingY = 3;
        int lineSpacing = 2;
        int x = 6;
        int y = 6;

        int maxWidth = 0;
        for (String l : lines) {
            int w = font.getWidth(l);
            if (w > maxWidth) maxWidth = w;
        }
        int totalHeight = lines.size() * font.fontHeight + (lines.size() - 1) * lineSpacing;

        ctx.fill(x - paddingX, y - paddingY, x + maxWidth + paddingX, y + totalHeight + paddingY, 0x88000000);

        int drawY = y;
        for (String l : lines) {
            ctx.drawTextWithShadow(font, l, x, drawY, 0xFFFFFFFF);
            drawY += font.fontHeight + lineSpacing;
        }
    }

    private static void toast(MinecraftClient mc, String msg) {
        if (mc != null && mc.player != null) {
            mc.player.sendMessage(Text.literal("[Show Me] " + msg), true);
        }
    }
}
