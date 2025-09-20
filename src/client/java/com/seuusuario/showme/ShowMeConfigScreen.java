// ShowMeConfigScreen.java
package com.seuusuario.showme;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class ShowMeConfigScreen extends Screen {
    private final Screen parent;

    // espelhar valores enquanto edita
    private boolean showFps;
    private boolean showCoords;
    private boolean showBrightness;
    private boolean showDays;
    private boolean showBiome;

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

        int centerX = this.width / 2;
        int y = 60;

        addDrawableChild(ButtonWidget.builder(
                label("key.menu.showFps", showFps),
                b -> {
                    showFps = !showFps;
                    b.setMessage(label("key.menu.showFps", showFps));
                }).dimensions(centerX - 100, y, 200, 20).build());

        y += 24;

        addDrawableChild(ButtonWidget.builder(
                label("key.menu.showCoordinates", showCoords),
                b -> {
                    showCoords = !showCoords;
                    b.setMessage(label("key.menu.showCoordinates", showCoords));
                }).dimensions(centerX - 100, y, 200, 20).build());

        y += 24;

        addDrawableChild(ButtonWidget.builder(
                label("key.menu.showBrightness", showBrightness),
                b -> {
                    showBrightness = !showBrightness;
                    b.setMessage(label("key.menu.showBrightness", showBrightness));
                }).dimensions(centerX - 100, y, 200, 20).build());

        y += 24;

        addDrawableChild(ButtonWidget.builder(
                label("key.menu.showDays", showDays),
                b -> {
                    showDays = !showDays;
                    b.setMessage(label("key.menu.showDays", showDays));
                }).dimensions(centerX - 100, y, 200, 20).build());

        y += 24;

        addDrawableChild(ButtonWidget.builder(
                label("key.menu.showBiome", showBiome),
                b -> {
                    showBiome = !showBiome;
                    b.setMessage(label("key.menu.showBiome", showBiome));
                }).dimensions(centerX - 100, y, 200, 20).build());

        y += 32;

        addDrawableChild(ButtonWidget.builder(Text.translatable("key.save"), b -> {
            ShowMeClient.CONFIG.showFps = showFps;
            ShowMeClient.CONFIG.showCoords = showCoords;
            ShowMeClient.CONFIG.showBrightness = showBrightness;
            ShowMeClient.CONFIG.showDays = showDays;
            ShowMeClient.CONFIG.showBiome = showBiome;
            ShowMeConfig.save(ShowMeClient.CONFIG);
            close();
        }).dimensions(centerX - 100, y, 95, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.translatable("key.cancel"), b -> {
            close();
        }).dimensions(centerX + 5, y, 95, 20).build());
    }

    private Text label(String base, boolean v) {
        return Text.translatable(base).append(": ")
                .append(v ? Text.translatable("key.on") : Text.translatable("key.off"));
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
