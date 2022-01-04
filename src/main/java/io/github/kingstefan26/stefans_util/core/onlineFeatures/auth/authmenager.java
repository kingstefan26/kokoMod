package io.github.kingstefan26.stefans_util.core.onlineFeatures.auth;

import com.google.gson.Gson;
import io.github.kingstefan26.stefans_util.core.globals;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

public class authmenager {

    private static authmenager instance;
    public static authmenager getInstance() {
        return instance == null ? instance = new authmenager() : instance;
    }


    Logger logger = LogManager.getLogger("authmenager");

    private authObject cashedAuthObject;
    private static String cashedPlayerUuid;

    public authObject getCashedAuthObject(){
        return cashedAuthObject;
    }

    public void start(){
        cashedAuthObject = authmenager.getInstance().getAuth(Minecraft.getMinecraft().getSession().getProfile().getId().toString());
    }

    //https://auth.kingstefan26.workers.dev/?uuid=fe347f57-f382-40db-9b17-a8aa23736f88&type=login
    public authObject getAuth(String userUuid){
        authObject temp = null;
        cashedPlayerUuid = userUuid;

        String assambledGetReq = globals.authEndpoint + "?uuid=" + userUuid + "&type=login";

        Gson gson = new Gson();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(assambledGetReq);
        try {
            HttpResponse httpresponse = httpclient.execute(httpget);

            String response = IOUtils.toString(httpresponse.getEntity().getContent(), StandardCharsets.UTF_8);

            temp = gson.fromJson(response, authObject.class);

            main.connectedToKokoCLoud = true;

        } catch (Exception e) {
            main.connectedToKokoCLoud = false;
            logger.error("there was a error logging to kokocloud", e);
            chatService.queueCleanChatMessage("there was a error logging to kokocloud, note that modules are delivered from kokocloud. trying again in few secs");
        }

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
