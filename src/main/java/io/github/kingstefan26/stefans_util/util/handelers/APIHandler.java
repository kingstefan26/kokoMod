package io.github.kingstefan26.stefans_util.util.handelers;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
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
import java.util.Base64;
import java.util.Scanner;

import static io.github.kingstefan26.stefans_util.util.bin.getInternalnameFromNBT;
import static io.github.kingstefan26.stefans_util.util.bin.getLoreFromNBT;

public class APIHandler {
    public static final Gson gson = new Gson();
    public static final String HYPIXEL_API = "https://api.hypixel.net";
    static final Logger logger = LogManager.getLogger("APIHandler");


    private APIHandler() {
        throw new SecurityException("This class shouldn't be instantiated");
    }

    public static String downloadTextFromUrl(final String URL) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(URL);
            HttpResponse httpresponse = httpclient.execute(httpget);
            return IOUtils.toString(httpresponse.getEntity().getContent(), StandardCharsets.UTF_8);
        }
    }

    public static JsonObject getautionpage(int page) {
        if (page >= 11 || page < 0) return null;

        final String api = HYPIXEL_API + "/skyblock/auctions" + (page == 0 ? "" : "?page=" + page);

        try {
            URL url = new URL(api);

            return gson.fromJson(getgetresponse(url), JsonObject.class);
        } catch (IOException e) {
            logger.error("getautionpage failed ", e);
        }

        return null;
    }


    public static JsonObject getendedautions() {
        final String api = HYPIXEL_API + "/skyblock/auctions_ended";

        try {
            URL url = new URL(api);

            return gson.fromJson(getgetresponse(url), JsonObject.class);

        } catch (IOException e) {
            logger.error("failed ", e);
        }

        return null;
    }


    public static String getgetresponse(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String input;
            StringBuilder response = new StringBuilder();

            while ((input = in.readLine()) != null) {
                response.append(input);
            }
            in.close();

            return response.toString();
        }

        return null;
    }


    public static JsonObject getJsonFromItemBytes(String itemBytes) {
        try {
            NBTTagCompound tag = CompressedStreamTools.readCompressed(new ByteArrayInputStream(Base64.getDecoder().decode(itemBytes)));
            return getJsonFromNBT(tag);
        } catch (IOException e) {
            return null;
        }
    }

    public static String getINTERNAMEfromitembytes(String itemBytes) {
        String json = String.valueOf(APIHandler.getJsonFromItemBytes(itemBytes));
        JsonObject itemlore = gson.fromJson(json, JsonObject.class);

        String name = itemlore.get("internalname").getAsString().replaceAll(";[0-9]+", "");


        return name;
    }

    public static NBTTagCompound getNBTTagCompoundFromItemBytses(String itemBytes) {
        try {
            NBTTagCompound tag = CompressedStreamTools.readCompressed(new ByteArrayInputStream(Base64.getDecoder().decode(itemBytes)));
            return tag;
        } catch (IOException e) {
            return null;
        }
    }

    public static JsonObject getJsonFromNBT(NBTTagCompound tag) {
        return getJsonFromNBTEntry(tag.getTagList("i", 10).getCompoundTagAt(0));
    }

    public static JsonObject getJsonFromNBTEntry(NBTTagCompound tag) {
        if (tag.getKeySet().isEmpty()) return null;

        int id = tag.getShort("id");
        int damage = tag.getShort("Damage");
        int count = tag.getShort("Count");
        tag = tag.getCompoundTag("tag");

        if (id == 141) id = 391; //for some reason hypixel thinks carrots have id 141

        String internalname = getInternalnameFromNBT(tag);
        if (internalname == null) return null;

        NBTTagCompound display = tag.getCompoundTag("display");
        String[] lore = getLoreFromNBT(tag);

        Item itemMc = Item.getItemById(id);
        String itemid = "null";
        if (itemMc != null) {
            itemid = itemMc.getRegistryName();
        }
        String displayname = display.getString("Name");

        JsonObject item = new JsonObject();
        item.addProperty("internalname", internalname);
        item.addProperty("itemid", itemid);
        item.addProperty("displayname", displayname);

        if (tag.hasKey("ExtraAttributes", 10)) {
            NBTTagCompound ea = tag.getCompoundTag("ExtraAttributes");

            byte[] bytes = null;
            for (String key : ea.getKeySet()) {
                if (key.endsWith("backpack_data") || key.equals("new_year_cake_bag_data")) {
                    bytes = ea.getByteArray(key);
                    break;
                }
            }
            if (bytes != null) {
                JsonArray bytesArr = new JsonArray();
                for (byte b : bytes) {
                    bytesArr.add(new JsonPrimitive(b));
                }
                item.add("item_contents", bytesArr);
            }
            if (ea.hasKey("dungeon_item_level")) {
                item.addProperty("dungeon_item_level", ea.getInteger("dungeon_item_level"));
            }
        }

        if (lore != null && lore.length > 0) {
            JsonArray jsonLore = new JsonArray();
            for (String line : lore) {
                jsonLore.add(new JsonPrimitive(line));
            }
            item.add("lore", jsonLore);
        }

        item.addProperty("damage", damage);
        if (count > 1) item.addProperty("count", count);
        item.addProperty("nbttag", tag.toString());

        return item;
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

    public static JsonArray getArrayResponse(String urlString) {
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
                logger.error("Request failed. HTTP Error Code: {}", conn.getResponseCode());
            }
        } catch (IOException ex) {
            logger.error("An error has occured. See logs for more details.", ex);
        }

        return new JsonArray();
    }

    public static String getUUID(String username) {
        JsonObject uuidResponse = getResponse("https://api.mojang.com/users/profiles/minecraft/" + username);
        return uuidResponse.get("id").getAsString();
    }

    public static String tokentouuid(String token) throws IOException {
        final String urlString = "https://api.minecraftservices.com/minecraft/profile";

        String basicAuth = "Bearer " + token;

        String response;

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(urlString);
            httpget.setHeader("Authorization", basicAuth);
            HttpResponse httpresponse = httpclient.execute(httpget);
            response = IOUtils.toString(httpresponse.getEntity().getContent(), StandardCharsets.UTF_8);
        }

        JsonObject uuidResponse = gson.fromJson(response, JsonObject.class);

        // get the uuid and add -
        return uuidResponse.get("id").getAsString().replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                "$1-$2-$3-$4-$5"
        );
    }

    public static JsonObject tokentoprofile(String token) throws IOException {
        JsonObject uuidResponse = null;
        final String urlString = "https://api.minecraftservices.com/minecraft/profile";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        String basicAuth = "Bearer " + token;

        conn.setRequestProperty("Authorization", basicAuth);
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

            uuidResponse = gson.fromJson(response.toString(), JsonObject.class);
        }

        // get the uuid and add -
        return uuidResponse;
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