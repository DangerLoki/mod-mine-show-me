// ShowMeConfigScreen.java
package com.meioQuilo.showme;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import com.meioQuilo.showme.ShowMeConfig.Position;
import com.meioQuilo.showme.components.ButtonScheema;

public class ShowMeConfigScreen extends Screen {
    private final Screen parent;

    // espelhar valores enquanto edita
    private boolean showFps;
    private boolean showCoords;
    private boolean showBrightness;
    private boolean showDays;
    private boolean showBiome;
    private Position togglePosition;

    private boolean showDebug;

    protected ShowMeConfigScreen(Screen parent) {
        super(Text.literal("Show Me - Configurações"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // carregar valores atuais
        this.showFps = ShowMeClient.CONFIG.showFps;
        this.showCoords = ShowMeClient.CONFIG.showCoords;
        this.showBrightness = ShowMeClient.CONFIG.showBrightness;
        this.showDays = ShowMeClient.CONFIG.showDays;
        this.showBiome = ShowMeClient.CONFIG.showBiome;
        this.togglePosition = ShowMeClient.CONFIG.togglePosition;
        this.showDebug = ShowMeClient.CONFIG.showDebug;

        int centerX = this.width / 2;

        ButtonScheema bs = new ButtonScheema(x -> addDrawableChild(x))
                .setX(centerX)
                .setY(60)
                .setSpacing(24)
                .setWidth(200)
                .setHeight(20);

        bs.newSwitchButton("key.menu.showFps", () -> showFps, v -> showFps = v);

        bs.newSwitchButton("key.menu.showCoordinates", () -> showCoords, v -> showCoords = v);

        bs.newSwitchButton("key.menu.showBrightness", () -> showBrightness, v -> showBrightness = v);

        bs.newSwitchButton("key.menu.showDays", () -> showDays, v -> showDays = v);

        bs.newSwitchButton("key.menu.showBiome", () -> showBiome, v -> showBiome = v);

        bs.newSwitchButton("key.menu.showDebug", () -> showDebug, v -> showDebug = v);

        bs.setSpacing(32); // Ultimo botão precisa ter um espaçamento maior
        bs.newToggleButton("key.menu.togglePosition", () -> togglePosition, v -> togglePosition = v);

        bs.newCustomButton("key.save", b -> {
            ShowMeClient.CONFIG.showFps = showFps;
            ShowMeClient.CONFIG.showCoords = showCoords;
            ShowMeClient.CONFIG.showBrightness = showBrightness;
            ShowMeClient.CONFIG.showDays = showDays;
            ShowMeClient.CONFIG.showBiome = showBiome;
            ShowMeClient.CONFIG.togglePosition = togglePosition;
            ShowMeClient.CONFIG.showDebug = showDebug;
            ShowMeConfig.save(ShowMeClient.CONFIG);
            close();
        }, centerX - 100, bs.getY(), 95, 20);

        bs.newCustomButton("key.cancel", b -> close(), centerX + 5, bs.getY(), 92, 20);

    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fill(0, 0, this.width, this.height, 0x88000000);
        ctx.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }
}
