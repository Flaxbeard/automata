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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class GuiProgrammer extends GuiContainer {

    private static final float BLOCK_SCALE = 0.5f;

    private static final ResourceLocation PROGRAMMER_TEXTURES =
            new ResourceLocation(Automata.MODID + ":textures/gui/programmer.png");

    private final TileEntityProgrammer programmer;
    private List<CodeBlockWrapper> codeBlockWrappers;

    public static class CodeBlockWrapper {

        private CodeBlock block;

        private int x;
        private int y;

        public CodeBlockWrapper(CodeBlock block, int x, int y) {
            this.block = block;
            this.x = x;
            this.y = y;
        }

        public void drawForeground(GuiProgrammer gui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 0);
            GlStateManager.scale(BLOCK_SCALE, BLOCK_SCALE, 0);
            block.drawForeground(gui);
            GlStateManager.popMatrix();
        }

        public void drawBackground(GuiProgrammer gui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 0);
            GlStateManager.scale(BLOCK_SCALE, BLOCK_SCALE, 0);
            block.drawBackground(gui);
            GlStateManager.popMatrix();
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public CodeBlockWrapper setPos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public int getHeight() {
            return (int) (block.getHeight() * BLOCK_SCALE);
        }

        public int getWidth() {
            return (int) (block.getWidth() * BLOCK_SCALE);
        }

        public CodeBlock getBlock() {
            return block;
        }

        public void readFromNBT(NBTTagCompound compound) {
            x = compound.getInteger("x");
            y = compound.getInteger("y");
            block = CodeBlockRegistry.loadFromNBT(compound.getCompoundTag("block"));
        }

        public NBTTagCompound writeToNBT(NBTTagCompound compound) {
            compound.setInteger("x", x);
            compound.setInteger("y", y);
            compound.setTag("block", block.writeToNBT(new NBTTagCompound()));
            return compound;
        }
    }

    public GuiProgrammer(InventoryPlayer inventoryPlayer, TileEntityProgrammer programmer) {
        super(new ContainerProgrammer(inventoryPlayer, programmer));
        this.programmer = programmer;
        this.ySize = 114 + 6 * 18;

        loadFromNBT(programmer.getData());
        if (codeBlockWrappers.size() == 0) {
            for (int i = 0; i < 10; i++) {
                codeBlockWrappers.add(
                        new CodeBlockWrapper(
                                new CodeBlockAdd(),
                                10,
                                10
                        )
                );
            }
            for (int i = 0; i < 10; i++) {
                codeBlockWrappers.add(
                        new CodeBlockWrapper(
                                new CodeBlockPosition(),
                                20,
                                20
                        )
                );
            }
            for (int i = 0; i < 10; i++) {
                codeBlockWrappers.add(
                        new CodeBlockWrapper(
                                new CodeBlockMineArea(),
                                30,
                                30
                        )
                );
            }
            for (int i = 0; i < 10; i++) {
                codeBlockWrappers.add(
                        new CodeBlockWrapper(
                                new CodeBlockMove(),
                                40,
                                40
                        )
                );
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
                codeBlockWrappers.add(wrapper);
            }
        }
    }
}
