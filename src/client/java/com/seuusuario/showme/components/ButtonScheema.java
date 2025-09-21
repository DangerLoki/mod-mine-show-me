package com.seuusuario.showme.components;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.text.Text;

public class ButtonScheema {

    public interface LabeledEnum {
        String getLabel();
    }

    private Function<ButtonWidget, ButtonWidget> f;
    private int x;
    private int y;
    private int width;
    private int spacing;
    private int height;

    public ButtonScheema(Function<ButtonWidget, ButtonWidget> f) {
        this.f = f;
    }

    public ButtonScheema setX(int x) {
        this.x = x - this.width / 2;
        return this;

    }

    public ButtonScheema setY(int y) {
        this.y = y;
        return this;
    }

    public int getY() {
        return this.y;
    }

    public ButtonScheema setWidth(int width) {
        this.width = width;
        return this;
    }

    public ButtonScheema setHeight(int height) {
        this.height = height;
        return this;
    }

    public ButtonScheema setSpacing(int spacing) {
        this.spacing = spacing;
        return this;
    }

    public void newCustomButton(String label, PressAction a, int x, int y, int width, int height) {
        this.f.apply(
                ButtonWidget
                        .builder(Text.translatable(label), a)
                        .dimensions(x, y, width, height)
                        .build());
    }

    public void newSwitchButton(String label, Supplier<Boolean> value, Consumer<Boolean> setValue) {
        this.f.apply(
                ButtonWidget.builder(localizedSwitchLabel(label, value.get()), b -> {
                    setValue.accept(!value.get());
                    b.setMessage(localizedSwitchLabel(label, value.get()));
                }).dimensions(this.x - this.width / 2, this.y, this.width, this.height).build());
        this.y += this.spacing;
    }

    public <T extends Enum<T> & LabeledEnum> void newToggleButton(String label, Supplier<T> value,
            Consumer<T> setValue) {
        final T[] values = value.get().getDeclaringClass().getEnumConstants();

        this.f.apply(ButtonWidget.builder(
                Text.translatable(label, Text.translatable(value.get().getLabel())),
                b -> {
                    setValue.accept(values[(value.get().ordinal() + 1) % values.length]);
                    b.setMessage(Text.translatable(label, Text.translatable(value.get().getLabel())));
                })
                .dimensions(this.x - this.width / 2, this.y, this.width, this.height).build());
        this.y += this.spacing;
    }

    private Text localizedSwitchLabel(String base, boolean v) {
        return Text.translatable(base).append(": ")
                .append(v ? Text.translatable("key.on") : Text.translatable("key.off"));
    }
}
