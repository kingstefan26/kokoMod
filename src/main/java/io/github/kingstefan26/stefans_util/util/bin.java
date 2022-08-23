/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;

public class bin {
    private static final int FAILS_BEFORE_SWITCH = 3;
    private final String[] myApiURLs = {"https://moulberry.codes/lowestbin.json.gz"};
    private final Integer[] myApiSuccesses = {0, 0, 0, 0};
    Gson gson = new Gson();
    private int currentUrl = 0;
    private long lastPrimaryUrl = 0;
    private ExecutorService es = Executors.newFixedThreadPool(3);

    private JsonObject lowestBins = null;

    public static String[] getLoreFromNBT(NBTTagCompound tag) {
        String[] lore = new String[0];
        NBTTagCompound display = tag.getCompoundTag("display");

        if (display.hasKey("Lore", 9)) {
            NBTTagList list = display.getTagList("Lore", 8);
            lore = new String[list.tagCount()];
            for (int k = 0; k < list.tagCount(); k++) {
                lore[k] = list.getStringTagAt(k);
            }
        }
        return lore;
    }

    public static String getInternalnameFromNBT(NBTTagCompound tag) {
        String internalname = null;
        if (tag != null && tag.hasKey("ExtraAttributes", 10)) {
            NBTTagCompound ea = tag.getCompoundTag("ExtraAttributes");

            if (ea.hasKey("id", 8)) {
                internalname = ea.getString("id").replaceAll(":", "-");
            } else {
                return null;
            }

            if ("PET".equals(internalname)) {
                String petInfo = ea.getString("petInfo");
                if (petInfo.length() > 0) {
                    JsonObject petInfoObject = (new Gson()).fromJson(petInfo, JsonObject.class);
                    internalname = petInfoObject.get("type").getAsString();
                    String tier = petInfoObject.get("tier").getAsString();
                    switch (tier) {
                        case "COMMON":
                            internalname += ";0";
                            break;
                        case "UNCOMMON":
                            internalname += ";1";
                            break;
                        case "RARE":
                            internalname += ";2";
                            break;
                        case "EPIC":
                            internalname += ";3";
                            break;
                        case "LEGENDARY":
                            internalname += ";4";
                            break;
                        case "MYTHIC":
                            internalname += ";5";
                            break;
                    }
                }
            }
            if ("ENCHANTED_BOOK".equals(internalname)) {
                NBTTagCompound enchants = ea.getCompoundTag("enchantments");

                for (String enchname : enchants.getKeySet()) {
                    internalname = enchname.toUpperCase() + ";" + enchants.getInteger(enchname);
                    break;
                }
            }
        }

        return internalname;
    }

    public JsonObject getApiGZIPSync(String urlS) throws IOException {
        URL url = new URL(urlS);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        String response = IOUtils.toString(new GZIPInputStream(connection.getInputStream()), StandardCharsets.UTF_8);

        JsonObject json = gson.fromJson(response, JsonObject.class);
        return json;
    }

    private String getMyApiURL() {
        if (currentUrl == 0) {
            lastPrimaryUrl = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - lastPrimaryUrl > 1000 * 60 * 30) { //Try switch back to main url after 30m
            currentUrl = 0;
        }

        myApiSuccesses[currentUrl] = Math.min(FAILS_BEFORE_SWITCH, myApiSuccesses[currentUrl] + 1);
        return myApiURLs[currentUrl];
    }

    public void getMyApiGZIPAsync(String urlS, Consumer<JsonObject> consumer, Runnable error) {
        es.submit(() -> {
            int current = currentUrl;
            try {
                consumer.accept(getApiGZIPSync(getMyApiURL() + urlS));
            } catch (Exception e) {
                myApiError(current);
                error.run();
            }
        });
    }

    private void myApiError(int index) {
        myApiSuccesses[index] = myApiSuccesses[index] - 2;

        if (myApiSuccesses[index] < 0) {
            myApiSuccesses[index] = 0;

            if (index == currentUrl) {
                currentUrl++;
                if (currentUrl >= myApiURLs.length) {
                    currentUrl = 0;
                }
            }
        }
    }

    public void updateLowestBin() {

        getMyApiGZIPAsync("lowestbin.json.gz", (jsonObject) -> {
            if (lowestBins == null) {
                lowestBins = new JsonObject();
            }
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                lowestBins.add(entry.getKey(), entry.getValue());
            }
        }, () -> {
        });
    }

    public int getLowestBin(String internalname) {
        if (lowestBins != null && lowestBins.has(internalname)) {
            JsonElement e = lowestBins.get(internalname);
            if (e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber()) {
                return e.getAsInt();
            }
        }
        return -1;
    }

    public String getInternalNameForItem(ItemStack stack) {
        if (stack == null) return null;
        NBTTagCompound tag = stack.getTagCompound();
        return getInternalnameFromNBT(tag);
    }
}
