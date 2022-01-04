package io.github.kingstefan26.stefans_util.module.render;

import com.google.common.base.Throwables;
import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderNoDecimalSetting;
import io.github.kingstefan26.stefans_util.util.ShaderResourcePack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class blurClickGui extends basicModule {
    public static blurClickGui blurClickGui_;
    public static blurClickGui getBlurClickGui(){
        if(blurClickGui_ == null) blurClickGui_ = new blurClickGui();
        return blurClickGui_;
    }

    ArrayList<String> alowedGuiClasses = new ArrayList<>(Arrays.asList(ClickGui.class.getName()));

    @Nonnull
    private final ShaderResourcePack dummyPack = new ShaderResourcePack();

    @Override
    public void onLoad(){
        new SliderNoDecimalSetting("radius", this ,12, 0, 50, (newval) -> {
            radius = (int) newval;
        });

        new SliderNoDecimalSetting("fadetime", this, 150, 0, 1000, (newval) -> {
            fadeTime = (int) newval;
        });

        ((List<IResourcePack>) ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "field_110449_ao", "defaultResourcePacks")).add(dummyPack);
        ((SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(dummyPack);

        super.onLoad();
    }


    public blurClickGui(){
        super("blurClickGui", "adds nice blur to click gui", moduleManager.Category.RENDER, new presistanceDecorator());
        blurClickGui_ = this;
    }

    @Override
    public void onDisable(){
        super.onDisable();
        EntityRenderer er = Minecraft.getMinecraft().entityRenderer;
        er.stopUseShader();
    }

    private Field _listShaders;
    private long start;
    private int fadeTime = 150;

    public static int radius = 12;

    @SubscribeEvent
    public void onGuiChange(GuiOpenEvent event) {
        if (_listShaders == null) {
            _listShaders = ReflectionHelper.findField(ShaderGroup.class, "field_148031_d", "listShaders");
        }
        if (Minecraft.getMinecraft().theWorld != null) {
            EntityRenderer er = Minecraft.getMinecraft().entityRenderer;
//            if(event.gui != null){
//                this.logger.info("current GUI class: " + event.gui.getClass().getName());
//            }
            boolean excluded = event.gui == null || ! alowedGuiClasses.contains(event.gui.getClass().getName());
            //this.logger.info(excluded ? "current Gui is excuded" : "current Gui is incuded");

            if (!er.isShaderActive() && !excluded) {
                er.loadShader(new ResourceLocation("shaders/post/fade_in_blur.json"));
                start = System.currentTimeMillis();
            } else {
                er.stopUseShader();
            }
        }
    }

    private float getProgress() {
        return Math.min((System.currentTimeMillis() - start) / (float) fadeTime, 1);
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().entityRenderer.isShaderActive()) {
            ShaderGroup sg = Minecraft.getMinecraft().entityRenderer.getShaderGroup();
            try {
                @SuppressWarnings("unchecked")
                List<Shader> shaders = (List<Shader>) _listShaders.get(sg);
                for (Shader s : shaders) {
                    ShaderUniform su = s.getShaderManager().getShaderUniform("Progress");
                    if (su != null) {
                        su.set(getProgress());
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }
    }

    public static int getBackgroundColor(boolean second) {
        int color = 0x75000000;
        int a = color >>> 24;
        int r = (color >> 16) & 0xFF;
        int b = (color >> 8) & 0xFF;
        int g = color & 0xFF;
        float prog = getBlurClickGui().getProgress();
        a *= prog;
        r *= prog;
        g *= prog;
        b *= prog;
        return a << 24 | r << 16 | b << 8 | g;
    }
}
