package io.github.a1qs.vaultadditions.patreon;

import java.util.List;

public class PatreonConfig {
    private List<PatreonTransmog> PATREON_TRANSMOGS;

    public List<PatreonTransmog> getPatreonTransmogs() {
        return PATREON_TRANSMOGS;
    }

    public static class PatreonTransmog {
        private String uuid;
        private List<String> transmogs;

        public String getUuid() {
            return uuid;
        }

        public List<String> getTransmogs() {
            return transmogs;
        }
    }
}
