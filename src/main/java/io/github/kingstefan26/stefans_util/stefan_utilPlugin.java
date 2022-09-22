package io.github.kingstefan26.stefans_util;

import com.google.common.collect.Sets;
import io.github.kingstefan26.stefans_util.core.Globals;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.FMLSecurityManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import sun.security.util.SecurityConstants;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.Set;

@IFMLLoadingPlugin.SortingIndex(Integer.MAX_VALUE)
@IFMLLoadingPlugin.MCVersion("1.8.9")
public class stefan_utilPlugin implements IFMLLoadingPlugin {

    public stefan_utilPlugin() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins." + Globals.MODID + ".json");
        MixinEnvironment.getCurrentEnvironment().setObfuscationContext("searge");
        overrideSecurityManager();
    }

    @Override
    public String[] getASMTransformerClass() {
//        return new String[] {"io.github.kingstefan26.stefans_util.stefan_utilTransformer"};
        return new String[0];
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

    // Bypass the FML security manager in order to set our own
    private void overrideSecurityManager() {
        try {
            SecurityManager s = new noobSkytilsSecurityManager();

            if (s.getClass().getClassLoader() != null) {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                    s.getClass().getProtectionDomain().implies
                            (SecurityConstants.ALL_PERMISSION);
                    return null;
                });
            }

            Method m = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
            m.setAccessible(true);
            Field[] fields = (Field[]) m.invoke(System.class, false);
            Method m2 = Class.class.getDeclaredMethod("searchFields", Field[].class, String.class);
            m2.setAccessible(true);

            Field field = (Field) m2.invoke(System.class, fields, "security");
            field.setAccessible(true);
            field.set(null, s);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static class noobSkytilsSecurityManager extends FMLSecurityManager {
        Set<String> badPaths = Sets.newHashSet(".ldb", ".leveldb", "launcher_accounts.json");

        @Override
        public void checkRead(String file) {
            for (String p : badPaths) {
                if (file.contains(p)) quitGame();
            }
            super.checkRead(file);
        }

        @Override
        public void checkRead(String file, Object context) {
            checkRead(file);
            super.checkRead(file, context);
        }

        public void checkPermission(Permission perm) {
            String permName = perm.getName() != null ? perm.getName() : "missing";
            if ("setSecurityManager".equals(permName)) {
                throw new SecurityException("Cannot replace the FML (Kokomod) security manager");
            }
        }

        private void quitGame() {
            try {
                Minecraft.getMinecraft().shutdownMinecraftApplet();
            } catch (Throwable t) {
                System.exit(0);
            }
        }
    }

}
