package flaxbeard.automata.client.gui.codeblock;

import flaxbeard.automata.Automata;
import flaxbeard.automata.client.gui.GuiProgrammer;
import flaxbeard.automata.client.gui.codeblock.component.BlockSlot;
import flaxbeard.automata.client.gui.codeblock.component.Component;
import flaxbeard.automata.client.gui.codeblock.component.FollowingSlot;
import net.minecraft.util.ResourceLocation;
import scala.tools.nsc.doc.base.comment.Code;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CodeBlockInstance<T extends CodeBlock> {

    private static final ResourceLocation WHITE_PX =
            new ResourceLocation(Automata.MODID + ":textures/gui/whitepx.png");

    protected static final ResourceLocation METAL_TEXTURE =
            new ResourceLocation(Automata.MODID + ":textures/gui/metal2.png");

    private final Component[] components;
    private BlockSlot[] slots;
    private BlockSlot slotIn;

    private int height;
    private int width;
    private int renderHeight;
    private int renderWidth;

    private T codeBlock;

    private Map<String, Object> data;

    public CodeBlockInstance(T codeBlock) {
        data = new HashMap<>();
        this.codeBlock = codeBlock;
        this.components = codeBlock.getInstanceComponents();
        codeBlock.setup(this);

        int slotCount = 0;
        for (Component component : components) {
            component.setBlock(this);
            if (component instanceof BlockSlot) {
                slotCount++;
            }
        }

        slots = new BlockSlot[slotCount];

        int i = 0;
        for (Component component : components) {
            if (component instanceof BlockSlot) {
                slots[i] = (BlockSlot) component;
                i++;
            }
        }

        recalculateComponentPositions();
    }

    public void removeFromParent() {
        if (slotIn != null) {
            slotIn.removeContents();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRenderWidth() {
        return renderWidth;
    }

    public int getRenderHeight() {
        return renderHeight;
    }

    public void drawBackground(GuiProgrammer gui) {
        codeBlock.drawBackground(this, gui);
    }

    public void drawForeground(GuiProgrammer gui) {
        codeBlock.drawForeground(this, gui);
    }


    public CodeBlockInstance getHoveredBlock(int x, int y) {
        return codeBlock.getHoveredBlock(this, x, y);
    }

    public BlockSlot getHoveredSlot(int x, int y) {
        return codeBlock.getHoveredSlot(this, x, y);
    }

    public void recalculateComponentPositions() {
        codeBlock.recalculateComponentPositions(this);
    }

    public int getAbsoluteX() {
        if (slotIn == null) {
            return 0;
        }
        return slotIn.getStartX() + getParent().getAbsoluteX();
    }

    public int getAbsoluteY() {
        if (slotIn == null) {
            return 0;
        }
        return slotIn.getStartY() + getParent().getAbsoluteY();
    }

    public boolean hasParent() {
        return slotIn != null;
    }

    public CodeBlockInstance getParent() {
        if (hasParent()) {
            return slotIn.getBlock();
        }
        return null;
    }

    public void setSlotIn(BlockSlot slotIn) {
        this.slotIn = slotIn;
    }

    public Component[] getComponents() {
        return components;
    }

    public BlockSlot[] getSlots() {
        return slots;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setRenderHeight(int renderHeight) {
        this.renderHeight = renderHeight;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setRenderWidth(int renderWidth) {
        this.renderWidth = renderWidth;
    }

    public T getBlock() {
        return codeBlock;
    }

    public boolean instanceOf(Class cls) {
        return cls.isInstance(codeBlock);
    }

    public void setData(String key, Object info) {
        data.put(key, info);
    }

    public Object getData(String key) {
        if (data.containsKey(key)) {
            return data.get(key);
        }
        return null;
    }
}
