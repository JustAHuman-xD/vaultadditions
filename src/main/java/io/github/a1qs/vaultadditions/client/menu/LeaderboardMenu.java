package io.github.a1qs.vaultadditions.client.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.a1qs.vaultadditions.util.UsernameProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
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
        renderLeaderboard(pPoseStack);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    private void renderLeaderboard(PoseStack pPoseStack) {
        int x = this.width / 2;
        int y = 30;

        // Create a sorted list of the leaderboard entries
        List<Map.Entry<UUID, Integer>> sortedLeaderboard = this.leaderboard.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                .toList();

        List<Map.Entry<UUID, Integer>> top10 = sortedLeaderboard.subList(0, Math.min(10, sortedLeaderboard.size()));

        int placement = 1; // Start with the 1st place
        for (Map.Entry<UUID, Integer> entry : top10) {
            UUID uuid = entry.getKey();
            int contributions = entry.getValue();
            String username = (uuid == this.minecraft.player.getUUID()) ? this.minecraft.player.getScoreboardName() : UsernameProvider.getUsernameFromUUID(uuid);

            MutableComponent textComponent = new TextComponent(String.format("#%d | ", placement))
                    .append(new TextComponent(username).withStyle(ChatFormatting.GREEN))
                    .append(new TextComponent(" | Contributions: ").withStyle(ChatFormatting.GRAY))
                    .append(new TextComponent(String.valueOf(contributions)).withStyle(ChatFormatting.GOLD))
                    .append(new TextComponent(" " + uuid).withStyle(ChatFormatting.GRAY));

            Minecraft.getInstance().font.draw(pPoseStack, textComponent, x - this.font.width(textComponent) / 2.0f, y, 0xFFFFFF);

            y += 15; // Move down for the next entry
            placement++;
        }


        MutableComponent splitter = new TextComponent("--- Your Position ---").withStyle(ChatFormatting.YELLOW);
        y += 15; // Add space before the splitter
        Minecraft.getInstance().font.draw(pPoseStack, splitter, x - this.font.width(splitter) / 2.0f, y, 0xFFFFFF);

        y += 15; // Move down after the splitter

        // Find the player's placement and display it
        UUID playerUUID = Minecraft.getInstance().player.getUUID();
        int playerPlacement = -1;
        int playerContributions = 0;

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

            Minecraft.getInstance().font.draw(pPoseStack, playerComponent, x - this.font.width(playerComponent) / 2.0f, y, 0xFFFFFF);
        } else {
            MutableComponent notFound = new TextComponent("Your position could not be found!").withStyle(ChatFormatting.RED);
            Minecraft.getInstance().font.draw(pPoseStack, notFound, x - this.font.width(notFound) / 2.0f, y, 0xFFFFFF);
        }
    }

//    ColorBlender colorBlender = new ColorBlender(1.0F);
//    Optional.ofNullable(clientCache.getGearColorComponents()).ifPresent((colors) -> colors.forEach((color) -> colorBlender.add(color, 60.0F)));
//    float time = (float) ClientScheduler.INSTANCE.getTick();
//    int color = colorBlender.getColor(time);
//    Optional<String> customName = Optional.ofNullable(clientCache.getGearName());
//    return (Component)customName.map((s) -> (new TextComponent(s)).setStyle(Style.EMPTY.withColor(color))).orElseGet(() -> defaultName.copy().setStyle(defaultName.getStyle().withColor(color)));


}
