package io.github.kingstefan26.stefans_util.module.misc.amiTimedOut;

import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import net.minecraft.client.gui.GuiScreen;

public class amiTimedOutGui extends GuiScreen {

    private final drawCenterString drawCenterStringOBJ = drawCenterString.getdrawCenterString();
    @Override
	public void initGui() {
//        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - (this.height / 4) + 10, "Close"));

        super.initGui();
    }

    @Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
        drawCenterStringOBJ.drawCenterStringOnScreen(mc, "server stopped responding", "ff002f");
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
//    @Override
//	protected void actionPerformed(GuiButton button) throws IOException {
//		if (button.id == 0) {
//			mc.thePlayer.closeScreen();
//		}
//	}

    @Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}

//public class RemotecraftGui extends GuiScreen {
//
//	private String mIpAddress;
//
//	private GuiButton mButtonClose;
//	private GuiLabel mLabelIpAddress;
//
//	private IpAddressManager mIpAddressManager;
//	private QrCodeManager mQrCodeManager;
//
//	@Override
//	public void initGui() {
//		super.initGui();
//		this.buttonList.add(mButtonClose = new GuiButton(0, this.width / 2 - 100, this.height - (this.height / 4) + 10, "Close"));
//		this.labelList.add(mLabelIpAddress = new GuiLabel(fontRendererObj, 1, this.width / 2 - 20, this.height / 2 + 40, 300, 20, 0xFFFFFF));
//
//		mIpAddressManager = new IpAddressManager();
//		mIpAddress = mIpAddressManager.getIpAddress();
//		mQrCodeManager = new QrCodeManager(mIpAddress);
//
//		mLabelIpAddress.addLine(mIpAddress);
//	}
//
//	@Override
//	protected void actionPerformed(GuiButton button) throws IOException {
//		if (button == mButtonClose) {
//			mc.thePlayer.closeScreen();
//		}
//	}
//
//	@Override
//	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//		this.drawDefaultBackground();
//
//		if (mQrCodeManager != null) {
//			mQrCodeManager.generateQrCode();
//			mQrCodeManager.renderQrCode(this);
//		}
//
//		super.drawScreen(mouseX, mouseY, partialTicks);
//	}
//
//	@Override
//	public boolean doesGuiPauseGame() {
//		return true;
//	}
