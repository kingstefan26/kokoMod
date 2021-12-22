package io.github.kingstefan26.stefans_util.core.newConfig.fileCacheing;

public class CacheObject {
    static class metaData{
        public String name;
        public String hash;
        public metaData(String name, String hash){
            this.name = name;
            this.hash = hash;
        }
    }
    static class cacheObject{
        private String content;
        private metaData metaData;


        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public CacheObject.metaData getMetaData() {
            return metaData;
        }

        public void setMetaData(CacheObject.metaData metaData) {
            this.metaData = metaData;
        }
    }

}
