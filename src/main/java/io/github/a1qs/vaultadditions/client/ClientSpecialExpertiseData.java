package io.github.a1qs.vaultadditions.client;

import io.github.a1qs.vaultadditions.network.KnownSpecialExpertiseMessage;
import io.github.a1qs.vaultadditions.vault.powermenu.SpecialExpertiseTree;
import iskallia.vault.core.net.ArrayBitBuffer;
import iskallia.vault.skill.base.TieredSkill;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClientSpecialExpertiseData {
    private static SpecialExpertiseTree SPECIAL_EXPERTISE = new SpecialExpertiseTree();

    public ClientSpecialExpertiseData() {
    }

    @Nonnull
    public static List<TieredSkill> getLearnedTalentNodes() {
        List<TieredSkill> talents = new ArrayList();
        SPECIAL_EXPERTISE.iterate(TieredSkill.class, (talent) -> {
            if (talent.isUnlocked()) {
                talents.add(talent);
            }

        });
        return talents;
    }

    @Nullable
    public static TieredSkill getLearnedTalentNode(String talentName) {
        Iterator var1 = getLearnedTalentNodes().iterator();

        TieredSkill node;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            node = (TieredSkill)var1.next();
        } while(!node.getId().equals(talentName));

        return node;
    }

    public static void updateTalents(KnownSpecialExpertiseMessage pkt) {
        ArrayBitBuffer buffer = ArrayBitBuffer.empty();
        pkt.getTree().writeBits(buffer);
        buffer.setPosition(0);
        SPECIAL_EXPERTISE.readBits(buffer);
    }
}
