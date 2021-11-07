package io.github.kingstefan26.stefans_util.core.newConfig.fileCache;

import io.github.kingstefan26.stefans_util.util.file;
import io.github.kingstefan26.stefans_util.util.handelers.APIHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class webCache {
    public static final Logger logger = LogManager.getLogger("webcache-kokomod");

    public static String downloadWithCaching(String url, String hash){
        logger.info(String.format("resutesd web cache url: %s hash: %s", url, hash));
        if(cacheManager.getInstance().isCachedByName(hash)){
            logger.info("Resource " + url + " was cashed");
            return cacheManager.getInstance().requestCacheByName(hash);
        }else{
            logger.info("the resourse was not in cashe downloading");
            try {
                System.out.println("Resource " + url+ " was not cashed downloading and cashing");
                String queryResult = APIHandler.downloadTextFromUrl(url);
                cacheManager.getInstance().cache(file.getMD5Hash(queryResult), queryResult);
                return queryResult;
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
