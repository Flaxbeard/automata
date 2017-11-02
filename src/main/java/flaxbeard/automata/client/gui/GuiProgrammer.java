package flaxbeard.automata.client.gui;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.codeblock.*;
import flaxbeard.automata.common.codeblock.base.CodeBlock;
import flaxbeard.automata.common.codeblock.base.CodeBlockStatement;
import flaxbeard.automata.common.codeblock.component.BlockSlot;
import flaxbeard.automata.common.block.tile.TileEntityProgrammer;
import flaxbeard.automata.common.network.PacketHandler;
import flaxbeard.automata.common.network.UpdateProgrammerPacket;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class GuiProgrammer extends GuiContainer {

    protected static final float BLOCK_SCALE = 0.5f;

    private static final ResourceLocation PROGRAMMER_TEXTURES =
            new ResourceLocation(Automata.MODID + ":textures/gui/programmer.png");

    private final TileEntityProgrammer programmer;
    private List<CodeBlockWrapper> codeBlockWrappers;
    private List<CodeBlockTemplate> codeBlockTemplates;

    private int tab;

    public GuiProgrammer(InventoryPlayer inventoryPlayer, TileEntityProgrammer programmer) {
        super(new ContainerProgrammer(inventoryPlayer, programmer));
        this.programmer = programmer;
        this.ySize = 114 + 6 * 18;

        tab = 0;

        loadFromNBT(programmer.getData());
        if (codeBlockWrappers.size() == 0) {
            codeBlockWrappers.add(new CodeBlockWrapper(
                    new CodeBlockHead(),
                    100,
                    0
            ));
        }

        int x = 0;
        int y = 0;
        codeBlockTemplates = new ArrayList<>();
        for (CodeBlock codeBlockType : CodeBlockRegistry.getAllCodeBlocks()) {
            if (!(codeBlockType instanceof CodeBlockHead)) {
                CodeBlockTemplate template = new CodeBlockTemplate(codeBlockType, x, y);
                y += template.getHeight() + 2;
                codeBlockTemplates.add(template);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(PROGRAMMER_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        updateDrag(Mouse.isButtonDown(0), mouseX - i, mouseY - j);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);


        for (CodeBlockWrapper block : codeBlockWrappers) {
            block.drawBackground(this);
            block.drawForeground(this);
            RenderHelper.disableStandardItemLighting();
        }

        for (CodeBlockTemplate block : codeBlockTemplates) {
            block.drawBackground(this);
            block.drawForeground(this);
            RenderHelper.disableStandardItemLighting();
        }

        if (draggingWrapper != null) {
            draggingWrapper.drawBackground(this);
            draggingWrapper.drawForeground(this);
        }
    }

    private boolean isDragging = false;
    private CodeBlockWrapper draggingWrapper = null;

    private int offsetX = 0;
    private int offsetY = 0;

    private void updateDrag(boolean mouseDown, int mouseX, int mouseY) {

        if (mouseDown) {
            if (!isDragging) {
                CodeBlockWrapper hoveredBlock = getHoveredBlock(mouseX, mouseY);
                if (hoveredBlock != null) {
                    isDragging = true;
                    draggingWrapper = hoveredBlock;
                    offsetX = mouseX - hoveredBlock.getX();
                    offsetY = mouseY - hoveredBlock.getY();
                }

                CodeBlockTemplate hoveredTemplate = getHoveredTemplate(mouseX, mouseY);
                if (hoveredTemplate != null) {
                    isDragging = true;
                    draggingWrapper = hoveredTemplate.getCopy();
                    offsetX = mouseX - hoveredTemplate.getX();
                    offsetY = mouseY - hoveredTemplate.getY();
                }
            }
            if (isDragging) {
                int posX = Math.max(mouseX - offsetX, -99992);
                //posX = Math.min(posX, xSize - draggingWrapper.getWidth() - 3);
                int posY = Math.max(mouseY - offsetY, -99992);
                //posY = Math.min(posY, 128 - draggingWrapper.getHeight() - 3);

                draggingWrapper.setPos(posX, posY);

                BlockSlot hoveredSlot = getHoveredSlot(draggingWrapper.getX(), draggingWrapper.getY() + draggingWrapper.getHeight() / 2);
                if (hoveredSlot != null && hoveredSlot.isBlockValid(draggingWrapper.getBlock())) {
                    hoveredSlot.setHovered();
                }
            }
        } else if (isDragging) {

            codeBlockWrappers.remove(draggingWrapper);
            codeBlockWrappers.add(draggingWrapper);

            //BlockSlot hoveredSlot = getHoveredSlot(mouseX, mouseY);
            BlockSlot hoveredSlot = getHoveredSlot(draggingWrapper.getX(), draggingWrapper.getY() + draggingWrapper.getHeight() / 2);
            BlockSlot hoveredSlot2 = getHoveredSlot(draggingWrapper.getX(), draggingWrapper.getY());

            CodeBlock draggingBlock = draggingWrapper.getBlock();

            if (tryToInsert(hoveredSlot, draggingBlock, mouseX, mouseY)
                    || tryToInsert(hoveredSlot2, draggingBlock, mouseX, mouseY)) {
                codeBlockWrappers.remove(draggingWrapper);
            }

            isDragging = false;
            draggingWrapper = null;
        }
    }

    private boolean tryToInsert(BlockSlot slot, CodeBlock block, int x, int y) {
        if (slot == null) {
            return false;
        }

        if (block instanceof CodeBlockStatement &&
                slot.isBlockValid(block) && slot.getContents() != null) {
            CodeBlockStatement bottomBlock = getBottomBlock((CodeBlockStatement) block);

            CodeBlock toMove = slot.getContents();
            slot.removeContents();
            bottomBlock.getFollowingSlot().setContents(toMove);
            toMove.recalculateComponentPositions();
        }
        else if (slot.isBlockValid(block) && slot.getContents() != null) {
            removeBlockFromParent(x, y, slot.getContents());
        }
        if (slot.setContents(block)) {
            return true;
        }
        return false;
    }

    private CodeBlockStatement getBottomBlock(CodeBlockStatement block) {
        CodeBlockStatement next = (CodeBlockStatement) block.getFollowingSlot().getContents();
        if (next != null) {
            return getBottomBlock(next);
        }
        return block;
    }

    private BlockSlot getHoveredSlot(int mouseX, int mouseY) {
        for (int i = codeBlockWrappers.size() - 1; i >= 0; i--) {
            CodeBlockWrapper codeBlockWrapper = codeBlockWrappers.get(i);

            if (codeBlockWrapper == draggingWrapper) continue;

            boolean hovering = (mouseX >= codeBlockWrapper.getX() && mouseX <= codeBlockWrapper.getX() + codeBlockWrapper.getWidth())
                    && (mouseY >= codeBlockWrapper.getY() && mouseY <= codeBlockWrapper.getY() + codeBlockWrapper.getHeight());

            if (hovering) {
                return codeBlockWrapper.getBlock().getHoveredSlot(
                        (int) ((mouseX - codeBlockWrapper.getX()) / BLOCK_SCALE),
                        (int) ((mouseY - codeBlockWrapper.getY()) / BLOCK_SCALE)
                );
            }
        }
        return null;
    }

    private CodeBlockWrapper getHoveredBlock(int mouseX, int mouseY) {
        for (int i = codeBlockWrappers.size() - 1; i >= 0; i--) {
            CodeBlockWrapper codeBlockWrapper = codeBlockWrappers.get(i);

            boolean hovering = (mouseX >= codeBlockWrapper.getX() && mouseX <= codeBlockWrapper.getX() + codeBlockWrapper.getWidth())
                    && (mouseY >= codeBlockWrapper.getY() && mouseY <= codeBlockWrapper.getY() + codeBlockWrapper.getHeight());

            if (hovering) {
                CodeBlock hoveredBlock = codeBlockWrapper.getBlock().getHoveredBlock(
                        (int) ((mouseX - codeBlockWrapper.getX()) / BLOCK_SCALE),
                        (int) ((mouseY - codeBlockWrapper.getY()) / BLOCK_SCALE)
                );
                if (hoveredBlock.hasParent()) {
                    int x = hoveredBlock.getAbsoluteX();
                    int y = hoveredBlock.getAbsoluteY();

                    x = (int) (x * BLOCK_SCALE) + codeBlockWrapper.getX();
                    y = (int) (y * BLOCK_SCALE) + codeBlockWrapper.getY();

                    return removeBlockFromParent(x, y, hoveredBlock);
                } else {
                    return codeBlockWrapper;
                }
            }
        }
        return null;
    }

    private CodeBlockTemplate getHoveredTemplate(int mouseX, int mouseY) {
        for (int i = codeBlockTemplates.size() - 1; i >= 0; i--) {
            CodeBlockTemplate codeBlockTemplate = codeBlockTemplates.get(i);

            boolean hovering = (mouseX >= codeBlockTemplate.getX() && mouseX <= codeBlockTemplate.getX() + codeBlockTemplate.getWidth())
                    && (mouseY >= codeBlockTemplate.getY() && mouseY <= codeBlockTemplate.getY() + codeBlockTemplate.getHeight());

            if (hovering) {
                return codeBlockTemplate;
            }
        }
        return null;
    }

    private CodeBlockWrapper removeBlockFromParent(int x, int y, CodeBlock block) {
        CodeBlockWrapper newPos = new CodeBlockWrapper(block, x, y);
        block.removeFromParent();
        codeBlockWrappers.add(newPos);
        return newPos;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        programmer.setData(writeToNBT(new NBTTagCompound()));
        PacketHandler.INSTANCE.sendToServer(new UpdateProgrammerPacket(programmer, writeToNBT(new NBTTagCompound())));
    }

    private NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList wrapperTagList = new NBTTagList();
        for (int i = 0; i < codeBlockWrappers.size(); i++) {
            NBTTagCompound wrapperTag = new NBTTagCompound();
            codeBlockWrappers.get(i).writeToNBT(wrapperTag);
            wrapperTagList.appendTag(wrapperTag);

            if (codeBlockWrappers.get(i).getBlock() instanceof CodeBlockHead) {
                compound.setTag("head", codeBlockWrappers.get(i).getBlock().writeToNBT(new NBTTagCompound()));
            }
        }
        compound.setTag("wrappers", wrapperTagList);
        return compound;
    }


    private void loadFromNBT(NBTTagCompound compound) {
        codeBlockWrappers = new ArrayList<>();
        if (compound.hasKey("wrappers")) {
            NBTTagList wrapperTagList = (NBTTagList) compound.getTag("wrappers");
            for (int i = 0; i < wrapperTagList.tagCount(); i++) {
                NBTTagCompound wrapperTag = wrapperTagList.getCompoundTagAt(i);

                CodeBlockWrapper wrapper = new CodeBlockWrapper(null, 0, 0);
                wrapper.readFromNBT(wrapperTag);
                if (wrapper.getBlock() != null) {
                    codeBlockWrappers.add(wrapper);
                }
            }
        }
    }
}
