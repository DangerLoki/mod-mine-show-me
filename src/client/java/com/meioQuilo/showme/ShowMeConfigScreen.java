// ShowMeConfigScreen.java
package com.meioQuilo.showme;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
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
    private boolean showPing;
    private Position togglePosition;
    private boolean showMemory;

    private boolean showDebug;
    private boolean showClock; 
    private boolean showSeed;
    
    // Campos para rolagem
    private java.util.List<ClickableWidget> scrollWidgets = new java.util.ArrayList<>();
    private int scrollOffset = 0;
    private int contentHeight = 0;
    private int listLeft, listTop, listBottom, listWidth, availableHeight;
    private int buttonHeightPx;
    private int rowSpacing;
    private final int gapActionButtons = 8;

    private int thumbY, thumbH, scrollbarX, scrollbarW;
    private boolean draggingScrollbar = false;
    private int dragStartY;
    private int dragStartOffset;

    public ShowMeConfigScreen(Screen parent) {
        super(Text.literal("Show Me - Configurações"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        // limpar restos de inicializações anteriores
        this.clearChildren();
        this.scrollWidgets.clear();
        this.scrollOffset = 0;

        // Carregar valores
        this.showFps = ShowMeClient.CONFIG.showFps;
        this.showCoords = ShowMeClient.CONFIG.showCoords;
        this.showBrightness = ShowMeClient.CONFIG.showBrightness;
        this.showDays = ShowMeClient.CONFIG.showDays;
        this.showBiome = ShowMeClient.CONFIG.showBiome;
        this.showPing = ShowMeClient.CONFIG.showPing;
        this.togglePosition = ShowMeClient.CONFIG.togglePosition;
        this.showMemory = ShowMeClient.CONFIG.showMemory;
        this.showClock = ShowMeClient.CONFIG.showClock;
        this.showSeed = ShowMeClient.CONFIG.showSeed;
        this.showDebug = ShowMeClient.CONFIG.showDebug;

        // Config básicos (agora independentes do espaço vertical pois usamos scroll)
        int screenW = this.width;
        int screenH = this.height;

        this.buttonHeightPx = Math.max(20, Math.min(24, screenH / 18));
        this.rowSpacing = Math.max(6, screenH / 80);

        int horizontalPadding = Math.max(12, screenW / 20);
        this.listWidth = Math.max(220, screenW - horizontalPadding * 2);
        this.listLeft = (screenW - listWidth) / 2;
        this.listTop = Math.max(40, screenH / 14);
        
        int bottomMargin = Math.max(12, screenH / 40);
        this.listBottom = screenH - bottomMargin;
        this.availableHeight = listBottom - listTop;

        // Criar botões (posições serão ajustadas no render)
        ButtonScheema bs = new ButtonScheema(b -> {
            // b deve ser ButtonWidget ou subclasse; addDrawableChild retorna o mesmo objeto
            net.minecraft.client.gui.widget.ButtonWidget added = this.addDrawableChild(b);
            scrollWidgets.add(added);
            return added; // garante o tipo esperado por ButtonScheema
        })
                .setX(screenW / 2)
                .setY(listTop)          // temporário
                .setWidth(listWidth)
                .setHeight(buttonHeightPx)
                .setSpacing(rowSpacing);

        // 10 switches + enum (11 linhas)
        bs.newSwitchButton("key.menu.showFps", () -> showFps, v -> showFps = v);
        bs.newSwitchButton("key.menu.showCoordinates", () -> showCoords, v -> showCoords = v);
        bs.newSwitchButton("key.menu.showBrightness", () -> showBrightness, v -> showBrightness = v);
        bs.newSwitchButton("key.menu.showDays", () -> showDays, v -> showDays = v);
        bs.newSwitchButton("key.menu.showClock", () -> showClock, v -> showClock = v);
        bs.newSwitchButton("key.menu.showBiome", () -> showBiome, v -> showBiome = v);
        bs.newSwitchButton("key.menu.showPing", () -> showPing, v -> showPing = v);
        bs.newSwitchButton("key.menu.showDebug", () -> showDebug, v -> showDebug = v);
        bs.newSwitchButton("key.menu.showMemory", () -> showMemory, v -> showMemory = v);
        bs.newSwitchButton("key.menu.showSeed", () -> showSeed, v -> showSeed = v);
        bs.newToggleButton("key.menu.togglePosition", () -> togglePosition, v -> togglePosition = v);

        // A linha de ação (Save/Cancel) vira a última linha (12ª linha) da lista
        var saveBtn = net.minecraft.client.gui.widget.ButtonWidget
                .builder(Text.translatable("key.save"), b -> {
                    ShowMeClient.CONFIG.showFps = showFps;
                    ShowMeClient.CONFIG.showCoords = showCoords;
                    ShowMeClient.CONFIG.showBrightness = showBrightness;
                    ShowMeClient.CONFIG.showDays = showDays;
                    ShowMeClient.CONFIG.showBiome = showBiome;
                    ShowMeClient.CONFIG.togglePosition = togglePosition;
                    ShowMeClient.CONFIG.showPing = showPing;
                    ShowMeClient.CONFIG.showMemory = showMemory;
                    ShowMeClient.CONFIG.showDebug = showDebug;
                    ShowMeClient.CONFIG.showClock = showClock;
                    ShowMeClient.CONFIG.showSeed = showSeed;
                    ShowMeConfig.save(ShowMeClient.CONFIG);
                    close();
                }).dimensions(listLeft, listBottom - buttonHeightPx, (listWidth - gapActionButtons) / 2, buttonHeightPx).build();
        var cancelBtn = net.minecraft.client.gui.widget.ButtonWidget
                .builder(Text.translatable("key.cancel"), b -> close())
                .dimensions(listLeft + (listWidth + gapActionButtons) / 2, listBottom - buttonHeightPx, (listWidth - gapActionButtons) / 2, buttonHeightPx).build();

        addDrawableChild(saveBtn);
        addDrawableChild(cancelBtn);
        scrollWidgets.add(saveBtn);
        scrollWidgets.add(cancelBtn);

        // Calcular altura total do conteúdo: 11 linhas de toggle + 1 linha de ação
        int rows = 12;
        this.contentHeight = rows * buttonHeightPx + (rows - 1) * rowSpacing;
    }

    // Atualiza posições conforme scroll
    private void layoutScrollWidgets() {
        int rowsToggles = 11; // primeiras linhas
        int baseY = listTop - scrollOffset;
        // Reposicionar toggles e enum
        for (int i = 0; i < rowsToggles; i++) {
            ClickableWidget w = scrollWidgets.get(i);
            int y = baseY + i * (buttonHeightPx + rowSpacing);
            w.setX(listLeft);
            w.setY(y);
            w.setWidth(listWidth);
            w.setHeight(buttonHeightPx);
        }
        // Linha final (Save / Cancel)
        int actionRowY = baseY + (rowsToggles) * (buttonHeightPx + rowSpacing);
        int half = (listWidth - gapActionButtons) / 2;
        ClickableWidget save = scrollWidgets.get(scrollWidgets.size() - 2);
        ClickableWidget cancel = scrollWidgets.get(scrollWidgets.size() - 1);
        save.setX(listLeft);
        save.setY(actionRowY);
        save.setWidth(half);
        save.setHeight(buttonHeightPx);
        cancel.setX(listLeft + half + gapActionButtons);
        cancel.setY(actionRowY);
        cancel.setWidth(half);
        cancel.setHeight(buttonHeightPx);
    }

    private void updateScrollMetrics() {
        if (contentHeight <= availableHeight) return;
        this.scrollbarX = listLeft + listWidth + 4;
        this.scrollbarW = 5;
        int h = availableHeight;
        int maxScroll = contentHeight - availableHeight;
        this.thumbH = Math.max(12, (int) ((availableHeight / (float) contentHeight) * h));
        int trackFree = h - thumbH;
        if (maxScroll <= 0) {
            this.thumbY = listTop;
        } else {
            this.thumbY = listTop + (int) ((scrollOffset / (float) maxScroll) * trackFree);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (contentHeight > availableHeight) {
            int step = buttonHeightPx + rowSpacing;
            scrollOffset -= (int) (verticalAmount * step);
            scrollOffset = Math.max(0, Math.min(scrollOffset, contentHeight - availableHeight));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && contentHeight > availableHeight) {
            updateScrollMetrics();
            // Dentro do thumb
            if (mouseX >= scrollbarX && mouseX <= scrollbarX + scrollbarW &&
                mouseY >= thumbY && mouseY <= thumbY + thumbH) {
                draggingScrollbar = true;
                dragStartY = (int) mouseY;
                dragStartOffset = scrollOffset;
                return true;
            }
            // Clique no trilho (pula para posição proporcional)
            if (mouseX >= scrollbarX && mouseX <= scrollbarX + scrollbarW &&
                mouseY >= listTop && mouseY <= listBottom) {
                int h = availableHeight;
                int maxScroll = contentHeight - availableHeight;
                int trackFree = h - thumbH;
                int rel = (int) mouseY - listTop - thumbH / 2;
                rel = Math.max(0, Math.min(trackFree, rel));
                scrollOffset = (int) ((rel / (double) trackFree) * maxScroll);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingScrollbar && button == 0 && contentHeight > availableHeight) {
            int h = availableHeight;
            int maxScroll = contentHeight - availableHeight;
            int trackFree = h - thumbH;
            int dy = (int) mouseY - dragStartY;
            double ratio = trackFree == 0 ? 0 : (dy / (double) trackFree);
            scrollOffset = dragStartOffset + (int) Math.round(ratio * maxScroll);
            scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && draggingScrollbar) {
            draggingScrollbar = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fill(0, 0, this.width, this.height, 0x88000000);
        ctx.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        layoutScrollWidgets();

        ctx.enableScissor(listLeft, listTop, listLeft + listWidth, listBottom);
        for (ClickableWidget w : scrollWidgets) {
            int wy = w.getY();
            if (wy + buttonHeightPx >= listTop && wy <= listBottom) {
                w.render(ctx, mouseX, mouseY, delta);
            }
        }
        ctx.disableScissor();

        updateScrollMetrics();
        if (contentHeight > availableHeight) {
            ctx.fill(scrollbarX, listTop, scrollbarX + scrollbarW, listBottom, 0x55000000);      // trilho
            ctx.fill(scrollbarX, thumbY, scrollbarX + scrollbarW, thumbY + thumbH,
                    draggingScrollbar ? 0xFFFFFFFF : 0xFFAAAAAA); // thumb (claro ao arrastar)
        }
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }
}
