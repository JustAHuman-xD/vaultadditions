package io.github.a1qs.vaultadditions.util;

import iskallia.vault.client.util.ClientScheduler;
import iskallia.vault.item.tool.ColorBlender;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;

public class TextUtil {
    public static Component blendString(String text, float interval, String... hexStrings) {
        ColorBlender colorBlender = new ColorBlender(1.0F);

        for(String hexString : hexStrings) {
            colorBlender.add(TextColor.parseColor(hexString).getValue(), interval);
        }

        float time = (float) ClientScheduler.INSTANCE.getTick();
        int color = colorBlender.getColor(time);
        return new TextComponent(text).setStyle(Style.EMPTY.withColor(color));
    }
}
