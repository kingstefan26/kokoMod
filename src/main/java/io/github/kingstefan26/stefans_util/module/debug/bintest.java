/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import com.google.common.collect.Iterables;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.kingstefan26.stefans_util.core.commands.SimpleCommand;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.prototypeModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.handelers.APIHandler;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.ClientCommandHandler;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.github.kingstefan26.stefans_util.module.render.macroSessionTracker.coolFormat;

public class bintest extends prototypeModule {
    public static ConcurrentMap<String, JsonObject> auctions = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, JsonObject> ended = new ConcurrentHashMap<>();
    static ConcurrentMap<String, Long> avragePrices = new ConcurrentHashMap<>();
    boolean isProcessing = false;
    boolean lock = false;

    public bintest() {
        super("bintest");
    }

    public static ConcurrentMap<String, JsonObject> reloadallbins(long nextpagestandby) throws InterruptedException {

        final ConcurrentHashMap<String, JsonObject> allauctions = new ConcurrentHashMap<>();
        final ConcurrentHashMap<String, JsonObject> bins = new ConcurrentHashMap<>();


        int counter = 0;
        while (counter <= 10) {
            JsonArray a = APIHandler.getautionpage(counter).get("auctions").getAsJsonArray();
            if (a != null) {
                for (JsonElement b : a) {

                    JsonObject e = b.getAsJsonObject();

                    allauctions.put(e.get("uuid").getAsString(), e);

                }
            }

            Thread.sleep(nextpagestandby);

            counter++;
        }

        for (Map.Entry<String, JsonObject> stringJsonObjectEntry : allauctions.entrySet()) {

            if (stringJsonObjectEntry.getValue().get("bin") != null) {
                if (stringJsonObjectEntry.getValue().get("bin").getAsBoolean()) {
                    bins.put(stringJsonObjectEntry.getKey(), stringJsonObjectEntry.getValue());
                }
            }
        }


        return bins;
    }

    public static ConcurrentMap<String, JsonObject> reloadendedbins() {

        final ConcurrentHashMap<String, JsonObject> allauctions = new ConcurrentHashMap<>();

        JsonObject thang = APIHandler.getendedautions();

        if (thang == null) return allauctions;
        if (thang.get("auctions") == null) return allauctions;

        JsonArray a = thang.get("auctions").getAsJsonArray();

        if (a != null) {
            for (JsonElement b : a) {
                JsonObject e = b.getAsJsonObject();
                JsonElement bin = e.get("bin");
                if (bin != null) {
                    if (bin.getAsBoolean()) {
                        allauctions.put(e.get("auction_id").getAsString(), e);
                    }
                }
            }
        }

        return allauctions;
    }

    @Override
    public void onLoad() {
        super.onLoad();


        ClientCommandHandler.instance.registerCommand(new SimpleCommand("reloadsmallestbins", new SimpleCommand.ProcessCommandRunnable() {
            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                chatService.queueCleanChatMessage("adding last 60s ended bins to memeory and calculating");
                ended.putAll(reloadendedbins());
                calculateAvrg(ended);
                chatService.queueCleanChatMessage("done...");

            }
        }));

        ClientCommandHandler.instance.registerCommand(new SimpleCommand("findgooddeals", new SimpleCommand.ProcessCommandRunnable() {
            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                if (ended.isEmpty()) {
                    chatService.queueCleanChatMessage("you havent loaded auctions yet");
                    return;
                }
                if (args.length != 1) {
                    chatService.queueCleanChatMessage("too little arguments");
                    return;
                }


                clearexipredAH();
                TreeMap<Long, JsonObject> deals = processbins(auctions, ended, Integer.parseInt(args[0]));


                chatService.queueCleanChatMessage("Top 5 deals:");
                chatService.queueCleanChatMessage("-----------");
                for (Map.Entry<Long, JsonObject> longJsonObjectEntry : Iterables.limit(deals.descendingMap().entrySet(), 5)) {
                    chatService.queueCleanChatMessage(longJsonObjectEntry.getValue().get("item_name").getAsString());
                    chatService.queueCleanChatMessage("item price: " + coolFormat(longJsonObjectEntry.getValue().get("starting_bid").getAsLong()) + " avg price: " + coolFormat(longJsonObjectEntry.getKey()));
                    chatService.queueCleanChatMessage("-----------");

                }

//                ArrayList<JsonObject> d = Lists.newArrayList
//                        (Iterables.limit(deals.descendingMap().values(), 5));
//                chatService.queueCleanChatMessage("Top 5 deals:");
//                chatService.queueCleanChatMessage("-----------");
//                for (JsonObject jsonObject : d) {
//                    chatService.queueCleanChatMessage(jsonObject.get("item_name").getAsString());
//                    chatService.queueCleanChatMessage(coolFormat(jsonObject.get("starting_bid").getAsLong()));
//                    chatService.queueCleanChatMessage("-----------");
//                }
                chatService.queueCleanChatMessage("proccessed " + (auctions.size() + ended.size()) + " bins");

            }
        }));

    }

    private void calculateAvrg(ConcurrentMap<String, JsonObject> ended) {
        for (Map.Entry<String, JsonObject> stringJsonObjectEntry : ended.entrySet()) {
            String iternalname = APIHandler.getINTERNAMEfromitembytes(stringJsonObjectEntry.getValue().get("item_bytes").getAsString());

            long price = stringJsonObjectEntry.getValue().get("price").getAsLong();

            logger.info("calculating avrage price for {}", iternalname);

            if (avragePrices.containsKey(iternalname)) {

                long p = avragePrices.get(iternalname);

                avragePrices.put(iternalname, (p + price) / 2);

                logger.info("avg {}: {}", iternalname, iternalname, (p + price) / 2);

            } else {
                avragePrices.put(iternalname, price);
            }
        }
    }

    private TreeMap<Long, JsonObject> processbins(ConcurrentMap<String, JsonObject> auctions, ConcurrentMap<String, JsonObject> ended) {
        return processbins(auctions, ended, 0);
    }

    private TreeMap<Long, JsonObject> processbins(ConcurrentMap<String, JsonObject> auctions, ConcurrentMap<String, JsonObject> ended, int timeout) {

        TreeMap<Long, JsonObject> hits = new TreeMap<>();

        // calculate avrage prices

        logger.info("calculating scores");
        logger.info("----");
        for (Map.Entry<String, JsonObject> stringJsonObjectEntry : auctions.entrySet()) {


            String iternalname = APIHandler.getINTERNAMEfromitembytes(stringJsonObjectEntry.getValue().get("item_bytes").getAsString());

            if (avragePrices.containsKey(iternalname)) {
                long price = stringJsonObjectEntry.getValue().get("starting_bid").getAsLong();
                logger.info("processing auction {}", stringJsonObjectEntry.getKey());
                logger.info("item name: {} price: {}, avrage price: {}", iternalname, price, avragePrices.get(iternalname));
                hits.put(avragePrices.get(iternalname) - price, stringJsonObjectEntry.getValue());
                logger.info("----");
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return hits;
    }

    void clearexipredAH() {
        Iterator<JsonObject> iterator = auctions.values().iterator();
        while (iterator.hasNext()) {
            JsonObject value = iterator.next();
            long exiretime = value.get("end").getAsLong();

            if (exiretime <= System.currentTimeMillis()) iterator.remove();
        }
    }

    @Override
    protected void PROTOTYPETEST() {
        (new Thread(() -> {
            if (isProcessing) return;
            isProcessing = true;

            try {
                auctions = reloadallbins(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            chatService.queueCleanChatMessage("done processing");
            isProcessing = false;

        })).start();

        if (!isProcessing) {
//            chatService.queueCleanChatMessage("showing bins");
//            chatService.queueCleanChatMessage("size: " + auctions.size());
//
//            for (Map.Entry<String, JsonObject> stringJsonObjectEntry : auctions.entrySet()) {
//
//                logger.info(stringJsonObjectEntry.getValue().get("item_name").getAsString());
//                logger.info(coolFormat(stringJsonObjectEntry.getValue().get("starting_bid").getAsLong()));
//
//                String bytes = stringJsonObjectEntry.getValue().get("item_bytes").getAsString();
//                if (bytes != null) {
//                    logger.info(APIHandler.getJsonFromItemBytes(bytes));
//                }
//                logger.info("-----------");
//
//
//            }

        } else {
            chatService.queueClientChatMessage("processing...");
        }
    }


}
