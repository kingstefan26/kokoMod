package io.github.kingstefan26.stefans_util;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.core.Logger;

import java.util.Map;

@IFMLLoadingPlugin.SortingIndex(Integer.MAX_VALUE)
@IFMLLoadingPlugin.MCVersion("1.8.9")
public class stefan_utilPlugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {"io.github.kingstefan26.stefans_util.stefan_utilTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
