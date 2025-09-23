package com.meioQuilo.showme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meioQuilo.showme.components.ButtonScheema.LabeledEnum;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ShowMeConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("show-me.json");

    public enum Position implements LabeledEnum {
        TOP_LEFT("key.menu.left"),
        TOP_RIGHT("key.menu.right"),
        BOTTOM_RIGHT("key.menu.bottomRight"),
        BOTTOM_LEFT("key.menu.bottomLeft");

        private final String label;

        Position(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    // Configurações do HUD
    public boolean showFps = true;
    public boolean showCoords = false;
    public boolean showBiome = false;
    public boolean showPing = false;
    public boolean showTime = false;
    public boolean showBrightness = false; // novo campo para brilho do bloco/céu
    public boolean showDays = false;
    public Position togglePosition = Position.TOP_LEFT;

    public boolean showDebug = false;

    public static ShowMeConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                return GSON.fromJson(json, ShowMeConfig.class);
            } catch (IOException e) {
                System.err.println("Erro ao carregar configuração do Show Me: " + e.getMessage());
            }
        }
        return new ShowMeConfig();
    }

    public static void save(ShowMeConfig config) {
        try {
            String json = GSON.toJson(config);
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, json);
        } catch (IOException e) {
            System.err.println("Erro ao salvar configuração do Show Me: " + e.getMessage());
        }
    }
}
