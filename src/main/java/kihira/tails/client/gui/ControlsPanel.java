/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014
 *
 * See LICENSE for full License
 */
package kihira.tails.client.gui;

import kihira.foxlib.client.gui.GuiBaseScreen;
import kihira.foxlib.client.toast.ToastManager;
import kihira.tails.common.PartInfo;
import kihira.tails.common.PartsData;
import kihira.tails.common.Tails;
import kihira.tails.common.network.PlayerDataMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public class ControlsPanel extends Panel<GuiEditor> {

    private GuiButton partTypeButton;

    @SuppressWarnings("unchecked")
    public ControlsPanel(GuiEditor parent, int left, int top, int right, int bottom) {
        super(parent, left, top, right, bottom);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        //Reset/Save
        buttonList.add(new GuiButton(12, width - 83, height - 25, 40, 20, I18n.format("gui.button.reset")));
        buttonList.add(new GuiButton(13, width - 43, height - 25, 40, 20, I18n.format("gui.done")));

        //Export
        buttonList.add(new GuiBaseScreen.GuiButtonTooltip(14, (width / 2) - 20, height - 25, 40, 20, I18n.format("gui.button.export"),
                (width / 2) - 20, I18n.format("gui.button.export.0.tooltip")));

        //PartType Select
        buttonList.add(partTypeButton = new GuiButton(20, 3, height - 25, 40, 20, parent.getPartType().name()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, width, height, 0xDD000000);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        PartsData partsData = parent.getPartsData();
        //Reset All
        if (button.id == 12) {
            PartInfo partInfo = parent.originalPartInfo.deepCopy();
            parent.partsPanel.selectDefaultListEntry();
            parent.setCurrTintEdit(0);
            parent.refreshTintPane();
            parent.setPartsInfo(partInfo);
        }
        //Save All
        else if (button.id == 13) {
            //Update part info, set local and send it to the server
            Tails.setLocalPartsData(partsData);
            Tails.proxy.addPartsData(partsData.uuid, partsData);
            Tails.networkWrapper.sendToServer(new PlayerDataMessage(mc.getSession().func_148256_e().getId(), partsData, false));
            ToastManager.INSTANCE.createCenteredToast(parent.width / 2, parent.height - 40, 100, EnumChatFormatting.GREEN + "Saved!");
            this.mc.displayGuiScreen(null);
        }
        //Export
        else if (button.id == 14) {
            mc.displayGuiScreen(new GuiExport(parent, partsData));
        }
        //PartType
        else if (button.id == 20) {
            if (parent.getPartType().ordinal() + 1 >= PartsData.PartType.values().length) {
                parent.setPartType(PartsData.PartType.values()[0]);
            }
            else {
                parent.setPartType(PartsData.PartType.values()[parent.getPartType().ordinal() + 1]);
            }

            partTypeButton.displayString = parent.getPartType().name();
        }
    }
}
