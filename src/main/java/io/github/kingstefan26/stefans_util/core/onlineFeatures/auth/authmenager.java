package io.github.kingstefan26.stefans_util.core.onlineFeatures.auth;

import com.google.gson.Gson;
import io.github.kingstefan26.stefans_util.core.globals;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.nio.charset.StandardCharsets;

public class authmenager {

    private static authmenager instance;
    public static authmenager getInstance() {
        return instance == null ? instance = new authmenager() : instance;
    }

    private authObject cashedAuthObject;

    public authObject getCashedAuthObject(){
        return cashedAuthObject;
    }

    public void start(){
        cashedAuthObject = authmenager.getInstance().getAuth(Minecraft.getMinecraft().getSession().getProfile().getId().toString());
    }

    //https://auth.kingstefan26.workers.dev/?uuid=fe347f57-f382-40db-9b17-a8aa23736f88
    public authObject getAuth(String userUuid){
        authObject temp = null;
        String assambledGetReq = globals.authEndpoint + "?uuid=" + userUuid;

        Gson gson = new Gson();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(assambledGetReq);
        try {
            HttpResponse httpresponse = httpclient.execute(httpget);

            String response = IOUtils.toString(httpresponse.getEntity().getContent(), StandardCharsets.UTF_8);

            temp = gson.fromJson(response, authObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
}
