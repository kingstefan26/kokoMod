package io.github.kingstefan26.stefans_util;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.SortingIndex(Integer.MAX_VALUE)
@IFMLLoadingPlugin.MCVersion("1.8.9")
public class stefan_utilPlugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {"io.github.kingstefan26.stefans_util.stefan_utilTransformer"};
//        return new String[] {"io.github.kingstefan26.stefans_util.core.transformers.NetHandlerTransformer"};
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
