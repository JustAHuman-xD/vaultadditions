package io.github.a1qs.vaultadditions.client.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.a1qs.vaultadditions.util.TimeUtil;
import io.github.a1qs.vaultadditions.util.UsernameProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.*;

public class LeaderboardMenu extends Screen {
    private final Map<UUID, Integer> leaderboard;
    private final String nextScheduledEvent;


    public LeaderboardMenu(Component pTitle, Map<UUID, Integer> leaderboard, String nextScheduledEvent) {
        super(pTitle);
        this.leaderboard = leaderboard;
        this.nextScheduledEvent = nextScheduledEvent;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        int renderX = (int) (this.width * 0.85);
        int renderY = (int) (this.height * 0.6);
        int scale = Math.min(this.width, this.height) / 8;

        renderBackgroundAndTitle(pPoseStack);
        renderLeaderboard(pPoseStack, this.width / 2, 30);
        renderEventInfo(pPoseStack, (int) (this.width * 0.11F), this.height / 2);
        renderPlayerPosition(pPoseStack, renderX, renderY + 10);
        renderPlayerModel(pMouseX, pMouseY, renderX, renderY, scale);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    /* Actual render methods */
    private void renderLeaderboard(PoseStack pPoseStack, int x, int startY) {
        List<Map.Entry<UUID, Integer>> sortedLeaderboard = this.leaderboard.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                .toList();

        List<Map.Entry<UUID, Integer>> top10 = new ArrayList<>(sortedLeaderboard.subList(0, Math.min(10, sortedLeaderboard.size())));

        // Ensure the list contains exactly 10 entries by padding it
        for (int i = top10.size(); i < 10; i++) {
            top10.add(new AbstractMap.SimpleEntry<>(null, -1));
        }

        int y = startY;
        int lineSpacing = 15;

        for (int placement = 1; placement <= top10.size(); placement++) {
            Map.Entry<UUID, Integer> entry = top10.get(placement - 1);

            String user = (entry.getKey() != null)
                    ? (entry.getKey().equals(this.minecraft.player.getUUID())
                    ? this.minecraft.player.getScoreboardName()
                    : UsernameProvider.getUsernameFromUUID(entry.getKey()))
                    : I18n.get("text.leaderboard.missing_user");
            String contribution = entry.getValue() != -1 ? String.valueOf(entry.getValue()) : "0";

            MutableComponent userComponent = new TextComponent(user).withStyle(ChatFormatting.GREEN);
            MutableComponent contributionComponent = new TextComponent(contribution).withStyle(ChatFormatting.GOLD);

            MutableComponent textComponent = new TranslatableComponent("text.leaderboard.entry", placement, userComponent, contributionComponent);

            // Centered leaderboard text
            Minecraft.getInstance().font.draw(pPoseStack, textComponent, x - this.font.width(textComponent) / 2.0f, y, 0xFFFFFF);

            y += lineSpacing;
        }
    }


    private void renderPlayerPosition(PoseStack pPoseStack, int x, int y) {
        MutableComponent splitter = new TranslatableComponent("text.leaderboard.own_placement");
        Minecraft.getInstance().font.draw(pPoseStack, splitter, x - this.font.width(splitter) / 2.0f, y, 0xFFFFFF);

        UUID playerUUID = Minecraft.getInstance().player.getUUID();
        List<Map.Entry<UUID, Integer>> sortedLeaderboard = this.leaderboard.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                .toList();

        int playerPlacement = -1;
        int playerContributions = 0;
        int lineSpacing = 15;

        // Find player's position
        for (int i = 0; i < sortedLeaderboard.size(); i++) {
            if (sortedLeaderboard.get(i).getKey().equals(playerUUID)) {
                playerPlacement = i + 1; // Placement is index + 1
                playerContributions = sortedLeaderboard.get(i).getValue();
                break;
            }
        }

        if (playerPlacement != -1) {
            MutableComponent userComponent = new TextComponent(UsernameProvider.getUsernameFromUUID(playerUUID)).withStyle(ChatFormatting.GREEN);
            MutableComponent contributionComponent = new TextComponent(String.valueOf(playerContributions)).withStyle(ChatFormatting.GOLD);
            MutableComponent textComponent = new TranslatableComponent("text.leaderboard.entry", playerPlacement, userComponent, contributionComponent);

            Minecraft.getInstance().font.draw(pPoseStack, textComponent, x - this.font.width(textComponent) / 2.0f, y + lineSpacing, 0xFFFFFF);
        } else {


            MutableComponent userComponent = new TextComponent(UsernameProvider.getUsernameFromUUID(playerUUID)).withStyle(ChatFormatting.GREEN);
            MutableComponent contributionComponent = new TextComponent("N/A").withStyle(ChatFormatting.RED);
            MutableComponent textComponent = new TranslatableComponent("text.leaderboard.entry", "???", userComponent, contributionComponent);


            Minecraft.getInstance().font.draw(pPoseStack, textComponent, x - this.font.width(textComponent) / 2.0f, y + lineSpacing, 0xFFFFFF);
        }
    }

    private void renderEventInfo(PoseStack pPoseStack, int x, int y) {
        MutableComponent splitter = new TranslatableComponent("text.leaderboard.event_info");
        Minecraft.getInstance().font.draw(pPoseStack, splitter, x - this.font.width(splitter) / 2.0f, y, 0xFFFFFF);

        int lineSpacing = 15;
        MutableComponent textComponent;
        textComponent = new TranslatableComponent("text.leaderboard.no_event_scheduled");

        if (TimeUtil.untilTimestamp(nextScheduledEvent) != null) {
            long[] time = TimeUtil.untilTimestamp(nextScheduledEvent);
            String timeString = String.format("%dd %dh %dm %ds", time[3], time[2], time[1], time[0]);
            textComponent = new TranslatableComponent("text.leaderboard.scheduled_event", timeString);
        }

        Minecraft.getInstance().font.draw(pPoseStack, textComponent, x - this.font.width(textComponent) / 2.0f, y + lineSpacing, 0xFFFFFF);
    }



    private void renderBackgroundAndTitle(PoseStack pPoseStack) {
        this.renderBackground(pPoseStack);
        drawCenteredString(pPoseStack, this.font, this.title, this.width / 2, 10, 0xFFFFFF);
    }

    private void renderPlayerModel(int pMouseX, int pMouseY, int pRenderX, int pRenderY, int pScale) {
        float mouseXOffset = pRenderX - pMouseX;
        float mouseYOffset = pRenderY - (pMouseY + pScale);
        InventoryScreen.renderEntityInInventory(pRenderX, pRenderY, pScale, mouseXOffset, mouseYOffset, this.minecraft.player);
    }





//    ColorBlender colorBlender = new ColorBlender(1.0F);
//    Optional.ofNullable(clientCache.getGearColorComponents()).ifPresent((colors) -> colors.forEach((color) -> colorBlender.add(color, 60.0F)));
//    float time = (float) ClientScheduler.INSTANCE.getTick();
//    int color = colorBlender.getColor(time);
//    Optional<String> customName = Optional.ofNullable(clientCache.getGearName());
//    return (Component)customName.map((s) -> (new TextComponent(s)).setStyle(Style.EMPTY.withColor(color))).orElseGet(() -> defaultName.copy().setStyle(defaultName.getStyle().withColor(color)));


}
