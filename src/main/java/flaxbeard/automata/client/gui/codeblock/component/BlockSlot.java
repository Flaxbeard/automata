package flaxbeard.automata.client.gui.codeblock.component;

import flaxbeard.automata.Automata;
import flaxbeard.automata.client.gui.GuiProgrammer;
import flaxbeard.automata.client.gui.codeblock.CodeBlock;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public abstract class BlockSlot extends Component {

    private static final ResourceLocation WHITE_PX =
            new ResourceLocation(Automata.MODID + ":textures/gui/whitepx.png");

    protected CodeBlock contents;
    protected boolean hovered = false;

    public CodeBlock getContents() {
        return contents;
    }

    public void removeContents() {
        if (contents != null) {
            contents.setSlotIn(null);
            contents = null;
            block.recalculateComponentPositions();
        }
    }

    public abstract boolean isBlockValid(CodeBlock block);

    public boolean setContents(CodeBlock newContents) {
        if (newContents == block || (newContents != null && newContents.hasParent())) {
            //System.out.println("RE");
            return false;
        }

        if (!isBlockValid(newContents)) {
            return false;
        }

        if (getContents() != null) {
            //System.out.println(getContents());
            return false;
        }

        if (newContents != null) {
            newContents.setSlotIn(this);
        }

        contents = newContents;
        block.recalculateComponentPositions();

        return true;
    }

    @Override
    public int getWidth() {
        if (contents == null) {
            return 11;
        }
        return contents.getWidth();
    }

    @Override
    public int getHeight() {
        if (contents == null) {
            return 9;
        }
        return contents.getHeight();
    }

    @Override
    public void drawBackground(GuiProgrammer gui) {
        if (contents == null) {
            GlStateManager.color(0, 0, 0, 0.5f);
            gui.mc.getTextureManager().bindTexture(WHITE_PX);
            gui.drawTexturedModalRect(2, 0, 0, 0, 7, 9);
            if (hovered) {
                GlStateManager.color(1, 1, 1, 0.5f);
                gui.drawTexturedModalRect(1, -1, 0, 0, 9, 1);
                gui.drawTexturedModalRect(1, 0, 0, 0, 1, 9);
                gui.drawTexturedModalRect(9, 0, 0, 0, 1, 9);
                gui.drawTexturedModalRect(1, 9, 0, 0, 9, 1);
                hovered = false;
            }
            GlStateManager.color(1, 1, 1, 1);
        } else {
            contents.drawBackground(gui);
            if (hovered) {
                gui.mc.getTextureManager().bindTexture(WHITE_PX);
                GlStateManager.color(1, 1, 1, 0.5f);
                gui.drawTexturedModalRect(-1, -1, 0, 0, getWidth(), 1);
                gui.drawTexturedModalRect(-1, 0, 0, 0, 1, getHeight());
                gui.drawTexturedModalRect(getWidth(), 0, 0, 0, 1, getHeight());
                gui.drawTexturedModalRect(-1, getHeight(), 0, 0,  getWidth(), 1);
                GlStateManager.color(1, 1, 1, 1);
                hovered = false;
            }
        }
    }

    @Override
    public void drawForeground(GuiProgrammer gui) {
        if (contents != null) {
            contents.drawForeground(gui);
        }
    }

    public void setHovered() {
        hovered = true;
    }
}