package io.github.a1qs.vaultadditions.client.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.a1qs.vaultadditions.util.UsernameProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LeaderboardMenu extends Screen {
    private final Map<UUID, Integer> leaderboard;


    public LeaderboardMenu(Component pTitle, Map<UUID, Integer> leaderboard) {
        super(pTitle);
        this.leaderboard = leaderboard;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        drawCenteredString(pPoseStack, this.font, this.title, this.width / 2, 10, 0xFFFFFF);
        renderLeaderboard(pPoseStack, this.width / 2, 30);

        int renderX = (int) (this.width * 0.85);
        int renderY = (int) (this.height * 0.6);
        int scale = Math.min(this.width, this.height) / 8;
        float mouseXOffset = renderX - pMouseX;
        float mouseYOffset = renderY - (pMouseY + scale);

        InventoryScreen.renderEntityInInventory(renderX, renderY, scale, mouseXOffset, mouseYOffset, this.minecraft.player);

        int positionY = renderY +10; // Add spacing below the player renderer
        renderPlayerPosition(pPoseStack, renderX, positionY);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }



    private void renderLeaderboard(PoseStack pPoseStack, int x, int startY) {
        // Create a sorted list of the leaderboard entries
        List<Map.Entry<UUID, Integer>> sortedLeaderboard = this.leaderboard.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                .toList();

        List<Map.Entry<UUID, Integer>> top10 = sortedLeaderboard.subList(0, Math.min(10, sortedLeaderboard.size()));

        // Render the top 10 entries
        int y = startY; // Start rendering leaderboard at the provided Y position
        int lineSpacing = 15;
        int placement = 1;

        for (Map.Entry<UUID, Integer> entry : top10) {
            UUID uuid = entry.getKey();
            int contributions = entry.getValue();
            String username = (uuid == this.minecraft.player.getUUID()) ? this.minecraft.player.getScoreboardName() : UsernameProvider.getUsernameFromUUID(uuid);

            MutableComponent textComponent = new TextComponent(String.format("#%d | ", placement))
                    .append(new TextComponent(username).withStyle(ChatFormatting.GREEN))
                    .append(new TextComponent(" | Contributions: ").withStyle(ChatFormatting.GRAY))
                    .append(new TextComponent(String.valueOf(contributions)).withStyle(ChatFormatting.GOLD));

            // Centered leaderboard text
            Minecraft.getInstance().font.draw(pPoseStack, textComponent, x - this.font.width(textComponent) / 2.0f, y, 0xFFFFFF);

            y += lineSpacing;
            placement++;
        }
    }


    private void renderPlayerPosition(PoseStack pPoseStack, int x, int y) {
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
            MutableComponent playerComponent = new TextComponent(String.format("#%d | ", playerPlacement))
                    .append(new TextComponent(UsernameProvider.getUsernameFromUUID(playerUUID)).withStyle(ChatFormatting.GREEN))
                    .append(new TextComponent(" | Contributions: ").withStyle(ChatFormatting.GRAY))
                    .append(new TextComponent(String.valueOf(playerContributions)).withStyle(ChatFormatting.GOLD));

            Minecraft.getInstance().font.draw(pPoseStack, playerComponent, x - this.font.width(playerComponent) / 2.0f, y + lineSpacing, 0xFFFFFF);
        } else {
            MutableComponent notFound = new TextComponent("Your position could not be found!").withStyle(ChatFormatting.RED);
            Minecraft.getInstance().font.draw(pPoseStack, notFound, x - this.font.width(notFound) / 2.0f, y + lineSpacing, 0xFFFFFF);
        }

        MutableComponent splitter = new TextComponent("--- Your Position ---").withStyle(ChatFormatting.YELLOW);
        Minecraft.getInstance().font.draw(pPoseStack, splitter, x - this.font.width(splitter) / 2.0f, y, 0xFFFFFF);
    }




//    ColorBlender colorBlender = new ColorBlender(1.0F);
//    Optional.ofNullable(clientCache.getGearColorComponents()).ifPresent((colors) -> colors.forEach((color) -> colorBlender.add(color, 60.0F)));
//    float time = (float) ClientScheduler.INSTANCE.getTick();
//    int color = colorBlender.getColor(time);
//    Optional<String> customName = Optional.ofNullable(clientCache.getGearName());
//    return (Component)customName.map((s) -> (new TextComponent(s)).setStyle(Style.EMPTY.withColor(color))).orElseGet(() -> defaultName.copy().setStyle(defaultName.getStyle().withColor(color)));


}
