package flaxbeard.automata.client.renderer;

import flaxbeard.automata.common.entity.EntityAutomaton;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderAutomaton extends RenderBiped<EntityAutomaton>
{
    private static final ResourceLocation ZOMBIE_TEXTURES = DefaultPlayerSkin.getDefaultSkinLegacy();

    public RenderAutomaton(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelBiped(0F, 0.0F, 64, 64), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityAutomaton entity) {
        return ZOMBIE_TEXTURES;
    }

    @Override
    protected void renderModel(EntityAutomaton entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        GlStateManager.scale(.5, .5, .5);
        GlStateManager.translate(0, entitylivingbaseIn.height * 1.5, 0);
        super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
    }
}
