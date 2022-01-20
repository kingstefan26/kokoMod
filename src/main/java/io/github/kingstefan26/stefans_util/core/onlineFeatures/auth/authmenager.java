package io.github.kingstefan26.stefans_util.core.onlineFeatures.auth;

import com.google.gson.Gson;
import io.github.kingstefan26.stefans_util.core.globals;
import io.github.kingstefan26.stefans_util.core.kokoMod;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static io.github.kingstefan26.stefans_util.main.connectedToKokoCLoud;

public class authmenager {

    private static authmenager instance;

    public static authmenager getInstance() {
        return instance == null ? instance = new authmenager() : instance;
    }

    Logger logger = LogManager.getLogger("authmenager");

    static final int[] deleyDurations = new int[]{5000, 10000, 15000, 60000};

    private static String cashedPlayerUuid;
    long recoocnectTimer;
    int recconectConter = 0;
    @Getter
    private authObject cashedAuthObject;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent ev) throws Throwable {
        if (!connectedToKokoCLoud) {
            if (System.currentTimeMillis() >= recoocnectTimer) {
                logger.info("trying to recconect to kokocloud");
                recconectConter++;

                cashedAuthObject = getAuth(Minecraft.getMinecraft().getSession().getProfile().getId().toString());

                if (!connectedToKokoCLoud) {
                    if (recconectConter == 1) {
                        recoocnectTimer = System.currentTimeMillis() + deleyDurations[0];
                    } else if (recconectConter == 2) {
                        recoocnectTimer = System.currentTimeMillis() + deleyDurations[1];
                    } else if (recconectConter == 3) {
                        recoocnectTimer = System.currentTimeMillis() + deleyDurations[2];
                    } else if (recconectConter > 3) {
                        recoocnectTimer = System.currentTimeMillis() + deleyDurations[3];
                    }
                }
            }
        }
    }

    public void start() {
//        cashedAuthObject = getAuth(Minecraft.getMinecraft().getSession().getProfile().getId().toString());
        // this is a hacky way to execute things on a separate thread and then get the results on the main
        CompletableFuture<authObject> future = CompletableFuture.supplyAsync(() -> getAuth(Minecraft.getMinecraft().getSession().getProfile().getId().toString()));

        future.thenApply(result -> {
            if (connectedToKokoCLoud) {
                cashedAuthObject = result;
                MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.connectedToKokoCloud());
            } else {
                recoocnectTimer = System.currentTimeMillis() + 5000;
            }
            return result;
        });
    }


    public authObject getAuth(String userUuid) {
        // here we check if the minecraft session is valid
        String serverId = UUID.randomUUID().toString().replace("-", "");
        try {
            Minecraft.getMinecraft().getSessionService().joinServer(Minecraft.getMinecraft().getSession().getProfile(), Minecraft.getMinecraft().getSession().getToken(), serverId);
        } catch (Exception var5) {
            return null;
        }

        authObject temp = null;
        cashedPlayerUuid = userUuid;

        String assambledGetReq = globals.authEndpoint + "?uuid=" + userUuid + "&type=login";

        Gson gson = new Gson();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(assambledGetReq);
        String response = null;
        try {
            HttpResponse httpresponse = httpclient.execute(httpget);
            response = IOUtils.toString(httpresponse.getEntity().getContent(), StandardCharsets.UTF_8);

        } catch (Exception e) {
            main.connectedToKokoCLoud = false;
            logger.error("there was a error logging to kokocloud", e);
            logger.info("there was a error logging to kokocloud, note that modules are delivered from kokocloud. trying again in few secs");
        }

        try{
            if(response != null) {
                if (response.equals("fool")) {
                    kokoMod.isAllowedToPlay = false;
                } else {
                    temp = gson.fromJson(response, authObject.class);
                }
                main.connectedToKokoCLoud = true;
            }
        } catch (Exception ignored) {
        }


        return temp;
    }

    public void saybey() throws IOException {
        HttpResponse httpresponse = HttpClients.createDefault().execute(new HttpGet(globals.authEndpoint + "?uuid=" + Minecraft.getMinecraft().getSession().getProfile().getId().toString() + "&type=logout"));
        logger.info("kokocloud said:" + IOUtils.toString(httpresponse.getEntity().getContent()));
    }
}
