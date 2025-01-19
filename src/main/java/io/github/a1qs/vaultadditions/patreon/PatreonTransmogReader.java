package io.github.a1qs.vaultadditions.patreon;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.github.a1qs.vaultadditions.VaultAdditions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PatreonTransmogReader {
    public static PatreonConfig fetchPatreonTransmogs(String url) {
        try {
            // Connect to the URL
            URL githubUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) githubUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();

            // Parse JSON using GSON
            Gson gson = new Gson();
            return gson.fromJson(json.toString(), PatreonConfig.class);

        } catch (JsonSyntaxException e) {
            VaultAdditions.LOGGER.error("Invalid JSON format: {}", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
