package kihira.tails.client.render;

import kihira.tails.client.PartRegistry;
import kihira.tails.common.PartInfo;
import kihira.tails.common.PartsData;
import kihira.tails.common.Tails;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerPart implements LayerRenderer<AbstractClientPlayer> {

    private final ModelRenderer modelRenderer;
    private final PartsData.PartType partType;

    public LayerPart(ModelRenderer modelRenderer, PartsData.PartType partType) {
        this.modelRenderer = modelRenderer;
        this.partType = partType;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer entity, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        if (Tails.proxy.hasPartsData(entity.getUniqueID())) {
            PartsData partsData = Tails.proxy.getPartsData(entity.getUniqueID());
            if (partsData.hasPartInfo(partType) && partsData.getPartInfo(partType).hasPart) {
                modelRenderer.postRender(0.0625F);
                PartInfo tailInfo = partsData.getPartInfo(partType);
                PartRegistry.getRenderPart(tailInfo.partType, tailInfo.typeid).render(entity, tailInfo, 0, 0, 0, partialTicks);
            }
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
