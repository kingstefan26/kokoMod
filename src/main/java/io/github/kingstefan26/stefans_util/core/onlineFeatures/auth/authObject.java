package io.github.kingstefan26.stefans_util.core.onlineFeatures.auth;

public class authObject {
//    {"uuid":"fe347f57-f382-40db-9b17-a8aa23736f88",
//            "configurl":"https://raw.githubusercontent.com/kingstefan26/cockmod-data/main/configs/dev.json",
//            "status":"dev"}
    public final String uuid;
    public final String configurl;
    public final String status;


    public authObject(String status, String uuid, String configurl) {
        this.status = status;
        this.uuid = uuid;
        this.configurl = configurl;
    }
}
