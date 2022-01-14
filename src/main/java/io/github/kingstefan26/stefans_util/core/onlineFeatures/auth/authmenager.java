package io.github.kingstefan26.stefans_util.core.onlineFeatures.auth;

import com.google.gson.Gson;
import io.github.kingstefan26.stefans_util.core.globals;
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

import java.nio.charset.StandardCharsets;

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
        cashedAuthObject = getAuth(Minecraft.getMinecraft().getSession().getProfile().getId().toString());

        if (connectedToKokoCLoud) {
            MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.connectedToKokoCloud());
        } else {
            recoocnectTimer = System.currentTimeMillis() + 5000;
        }
    }

    //https://auth.kingstefan26.workers.dev/?uuid=fe347f57-f382-40db-9b17-a8aa23736f88&type=login
    public authObject getAuth(String userUuid){
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
            if(response != null){
                temp = gson.fromJson(response, authObject.class);
                main.connectedToKokoCLoud = true;
            }
        }catch(Exception ignored){}



        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                HttpResponse httpresponse = HttpClients.createDefault().execute(new HttpGet(globals.authEndpoint + "?uuid=" + this.cashedPlayerUuid + "&type=logout"));
                logger.info("kokocloud said:" + IOUtils.toString(httpresponse.getEntity().getContent()));

            } catch (Exception e) {
                logger.error("there was a error sending the logout request", e);
            }
        }));

        return temp;
    }
}
