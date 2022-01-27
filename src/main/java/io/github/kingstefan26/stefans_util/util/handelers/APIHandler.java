package io.github.kingstefan26.stefans_util.util.handelers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class APIHandler {
    static Logger logger;

    static {
        logger = LogManager.getLogger("APIHandler");
    }

    private APIHandler() {
    }

    public static String downloadTextFromUrl(final String URL) throws IOException {
        String response;

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(URL);
            HttpResponse httpresponse = httpclient.execute(httpget);
            response = IOUtils.toString(httpresponse.getEntity().getContent(), StandardCharsets.UTF_8);
        }


        return response;
    }


    public static String donloadAFileAndReturnPath(String urlString, String savePath) throws IOException {
        URL url = new URL(urlString);
        File file = new File(savePath);
        FileUtils.copyURLToFile(url, file);
        return file.getAbsolutePath();
    }


    public static JsonObject getResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String input;
                StringBuilder response = new StringBuilder();

                while ((input = in.readLine()) != null) {
                    response.append(input);
                }
                in.close();

                Gson gson = new Gson();

                return gson.fromJson(response.toString(), JsonObject.class);
            } else {
                if (urlString.startsWith("https://api.hypixel.net/")) {
                    InputStream errorStream = conn.getErrorStream();
                    try (Scanner scanner = new Scanner(errorStream)) {
                        scanner.useDelimiter("\\Z");
                        String error = scanner.next();

                        Gson gson = new Gson();
                        return gson.fromJson(error, JsonObject.class);
                    }
                } else if (urlString.startsWith("https://api.mojang.com/users/profiles/minecraft/") && conn.getResponseCode() == 204) {
                    logger.warn("Failed with reason: Player does not exist.");
                } else {
                    logger.warn("Request {} failed. HTTP Error Code: {}", urlString, conn.getResponseCode());
                }
            }
        } catch (IOException ex) {
            logger.warn("An error has occured. See logs for more details.");
            ex.printStackTrace();
        }

        return new JsonObject();
    }

    // Only used for UUID => Username
    public static JsonArray getArrayResponse(String urlString) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String input;
                StringBuilder response = new StringBuilder();

                while ((input = in.readLine()) != null) {
                    response.append(input);
                }
                in.close();

                Gson gson = new Gson();

                return gson.fromJson(response.toString(), JsonArray.class);
            } else {
                player.addChatMessage(new ChatComponentText("Request failed. HTTP Error Code: " + conn.getResponseCode()));
            }
        } catch (IOException ex) {
            player.addChatMessage(new ChatComponentText("An error has occured. See logs for more details."));
            ex.printStackTrace();
        }

        return new JsonArray();
    }

    public static String getUUID(String username) {
        JsonObject uuidResponse = getResponse("https://api.mojang.com/users/profiles/minecraft/" + username);
        return uuidResponse.get("id").getAsString();
    }

    public static String getLatestProfileID(String uuid, String key) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        // Get profiles
        logger.info("Fetching profiles...");

        JsonObject profilesResponse = getResponse("https://api.hypixel.net/skyblock/profiles?uuid=" + uuid + "&key=" + key);
        if (!profilesResponse.get("success").getAsBoolean()) {
            String reason = profilesResponse.get("cause").getAsString();
            player.addChatMessage(new ChatComponentText("Failed with reason: " + reason));
            return null;
        }
        if (profilesResponse.get("profiles").isJsonNull()) {
            player.addChatMessage(new ChatComponentText("This player doesn't appear to have played SkyBlock."));
            return null;
        }

        // Loop through profiles to find latest
        logger.info("Looping through profiles...");
        String latestProfile = "";
        long latestSave = 0;
        JsonArray profilesArray = profilesResponse.get("profiles").getAsJsonArray();

        for (JsonElement profile : profilesArray) {
            JsonObject profileJSON = profile.getAsJsonObject();
            long profileLastSave = 1;
            if (profileJSON.get("members").getAsJsonObject().get(uuid).getAsJsonObject().has("last_save")) {
                profileLastSave = profileJSON.get("members").getAsJsonObject().get(uuid).getAsJsonObject().get("last_save").getAsLong();
            }

            if (profileLastSave > latestSave) {
                latestProfile = profileJSON.get("profile_id").getAsString();
                latestSave = profileLastSave;
            }
        }

        return latestProfile;
    }
}