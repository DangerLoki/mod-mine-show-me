// ShowMeConfigScreen.java
package com.seuusuario.showme;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ShowMeConfigScreen extends Screen {
    private final Screen parent;

    // espelhar valores enquanto edita
    private boolean showFps;
    private boolean showCoords;
    private boolean showBrightness;
    private boolean showDays;

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

        int centerX = this.width / 2;
        int y = 60;

        addDrawableChild(ButtonWidget.builder(
                Text.literal(label("Mostrar FPS", showFps)),
                b -> {
                    showFps = !showFps;
                    b.setMessage(Text.literal(label("Mostrar FPS", showFps)));
                }).dimensions(centerX - 100, y, 200, 20).build());

        y += 24;

        addDrawableChild(ButtonWidget.builder(
                Text.literal(label("Mostrar Coordenadas", showCoords)),
                b -> {
                    showCoords = !showCoords;
                    b.setMessage(Text.literal(label("Mostrar Coordenadas", showCoords)));
                }).dimensions(centerX - 100, y, 200, 20).build());

        y += 24;

        addDrawableChild(ButtonWidget.builder(
                Text.literal(label("Mostrar Brilho", showBrightness)),
                b -> {
                    showBrightness = !showBrightness;
                    b.setMessage(Text.literal(label("Mostrar Brilho", showBrightness)));
                }).dimensions(centerX - 100, y, 200, 20).build());

        y += 24;

        addDrawableChild(ButtonWidget.builder(
                Text.literal(label("Mostrar dias", showDays)),
                b -> {
                    showDays = !showDays;
                    b.setMessage(Text.literal(label("Mostrar dias", showDays)));
                }).dimensions(centerX - 100, y, 200, 20).build());

        y += 32;

        addDrawableChild(ButtonWidget.builder(Text.literal("Salvar"), b -> {
            ShowMeClient.CONFIG.showFps = showFps;
            ShowMeClient.CONFIG.showCoords = showCoords;
            ShowMeClient.CONFIG.showBrightness = showBrightness;
            ShowMeClient.CONFIG.showDays = showDays;
            ShowMeConfig.save(ShowMeClient.CONFIG);
            close();
        }).dimensions(centerX - 100, y, 95, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Cancelar"), b -> {
            close();
        }).dimensions(centerX + 5, y, 95, 20).build());
    }

    private String label(String base, boolean v) {
        return base + ": " + (v ? "Ligado" : "Desligado");
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
