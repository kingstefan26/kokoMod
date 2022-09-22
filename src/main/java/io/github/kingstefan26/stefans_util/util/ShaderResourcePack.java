package io.github.kingstefan26.stefans_util.util;

import com.google.common.collect.ImmutableSet;
import io.github.kingstefan26.stefans_util.Main;
import io.github.kingstefan26.stefans_util.module.render.blurClickGui;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ShaderResourcePack implements IResourcePack, IResourceManagerReloadListener {

	protected boolean validPath(ResourceLocation location) {
		return location.getResourceDomain().equals("minecraft") && location.getResourcePath().startsWith("shaders/");
	}

	private final Map<ResourceLocation, String> loadedData = new HashMap<>();

	@Override
	public InputStream getInputStream(ResourceLocation location) throws IOException {
        if (validPath(location)) {
			String s = loadedData.computeIfAbsent(location, loc -> {
				InputStream in = Main.class.getResourceAsStream("/" + location.getResourcePath());
				StringBuilder data = new StringBuilder();
				Scanner scan = new Scanner(in);
				try {
					while (scan.hasNextLine()) {
						data.append(scan.nextLine().replaceAll("@radius@", Integer.toString(blurClickGui.radius))).append('\n');
					}
				} finally {
					scan.close();
				}
				return data.toString();
			});

            return new ByteArrayInputStream(s.getBytes());
        }
        throw new FileNotFoundException(location.toString());
	}

	@Override
	public boolean resourceExists(ResourceLocation location) {
		return validPath(location) && Main.class.getResource("/" + location.getResourcePath()) != null;
	}

	@Override
	public Set<String> getResourceDomains() {
		return ImmutableSet.of("minecraft");
	}

	@SuppressWarnings({ "unchecked", "null" })
    @Override
	public <T extends IMetadataSection> T getPackMetadata(IMetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
	    if ("pack".equals(metadataSectionName)) {
	        return (T) new PackMetadataSection(new ChatComponentText("Blur's default shaders"), 3);
	    }
	    return null;
    }

	@Override
	public BufferedImage getPackImage() throws IOException {
		throw new FileNotFoundException("pack.png");
	}

	@Override
	public String getPackName() {
		return "Blur dummy resource pack";
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
	    loadedData.clear();
	}

}
