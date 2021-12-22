package io.github.kingstefan26.stefans_util.core.newConfig.fileCacheing;

import io.github.kingstefan26.stefans_util.util.cryptography;
import io.github.kingstefan26.stefans_util.util.handelers.APIHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

public class webCache {
    public static final Logger logger = LogManager.getLogger("webcache-kokomod");

    public static String downloadWithCaching(String url, String hash){
        logger.info(String.format("requested web caching url: %s hash: %s", url, hash));
        if(cacheManager.getInstance().isCachedByName(hash)){
            logger.info("Resource " + url + " was cashed");
            return cacheManager.getInstance().requestCacheByName(hash);
        }else{
            logger.info("the resource was not in cache downloading");
            try {
                System.out.println("Resource " + url+ " was not cashed downloading and cashing");
                String queryResult = APIHandler.downloadTextFromUrl(url);
                cacheManager.getInstance().cache(cryptography.getMD5Hash(queryResult), queryResult);
                return queryResult;
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * WARING USE THIS FOR STATIC CONTENT ONLY OTHERWISE IT CAN BECOME OUTDATED
     * @param url the url
     * @return the contents of the url
     */
    public static String downloadWithCaching(String url) throws UnsupportedEncodingException {
        logger.info("requested web caching url: " + url);
        String name = URLEncoder.encode(url, "UTF-8");



        if(cacheManager.getInstance().isCachedByName(name)){
            logger.info("Resource " + name + " was cashed");
            return cacheManager.getInstance().requestCacheByName(name);
        }else{
            logger.info("the resource was not in cache downloading");
            try {

                System.out.println("Resource " + name + " was not cashed downloading and cashing");
                String queryResult = APIHandler.downloadTextFromUrl(url);
                cacheManager.getInstance().cache(name, queryResult);
                return queryResult;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
