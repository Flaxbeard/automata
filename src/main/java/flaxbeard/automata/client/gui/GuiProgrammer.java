package flaxbeard.automata.client.gui;

import flaxbeard.automata.Automata;
import flaxbeard.automata.client.gui.codeblock.*;
import flaxbeard.automata.client.gui.codeblock.component.BlockSlot;
import flaxbeard.automata.client.gui.codeblock.component.ExpressionSlot;
import flaxbeard.automata.client.gui.codeblock.component.StringComponent;
import flaxbeard.automata.common.block.tile.TileEntityProgrammer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
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

        private final CodeBlockInstance block;

        private int x;
        private int y;

        public CodeBlockWrapper(CodeBlockInstance block, int x, int y) {
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

        public CodeBlockInstance getBlock() {
            return block;
        }
    }

    public GuiProgrammer(InventoryPlayer inventoryPlayer, TileEntityProgrammer programmer) {
        super(new ContainerProgrammer(inventoryPlayer, programmer));
        this.programmer = programmer;
        this.ySize = 114 + 6 * 18;

        codeBlockWrappers = new ArrayList<>();

        String[] texts = {
                "Go to position",
                "Attack",
                "Mine area"
        };

        codeBlockWrappers.add(new CodeBlockWrapper(
                new CodeBlockInstance(CodeBlocks.EXPR_ADD),
                10,
                10
        ));
        codeBlockWrappers.add(new CodeBlockWrapper(
                new CodeBlockInstance(CodeBlocks.EXPR_ADD),
                10,
                10
        ));
        codeBlockWrappers.add(new CodeBlockWrapper(
                new CodeBlockInstance(CodeBlocks.EXPR_CURR_POS),
                10,
                10
        ));
        codeBlockWrappers.add(new CodeBlockWrapper(
                new CodeBlockInstance(CodeBlocks.EXPR_CURR_POS),
                10,
                10
        ));
        codeBlockWrappers.add(new CodeBlockWrapper(
                new CodeBlockInstance(CodeBlocks.STMT_MINE),
                10,
                10
        ));
        codeBlockWrappers.add(new CodeBlockWrapper(
                new CodeBlockInstance(CodeBlocks.STMT_MINE),
                10,
                10
        ));
        codeBlockWrappers.add(new CodeBlockWrapper(
                new CodeBlockInstance(CodeBlocks.STMT_MOVE),
                10,
                10
        ));
        codeBlockWrappers.add(new CodeBlockWrapper(
                new CodeBlockInstance(CodeBlocks.STMT_MOVE),
                10,
                10
        ));
        codeBlockWrappers.add(new CodeBlockWrapper(
                new CodeBlockInstance(CodeBlocks.STMT_MOVE),
                10,
                10
        ));
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

            CodeBlockInstance draggingBlock = draggingWrapper.getBlock();

            if (tryToInsert(hoveredSlot, draggingBlock, mouseX, mouseY)
                    || tryToInsert(hoveredSlot2, draggingBlock, mouseX, mouseY)) {
                codeBlockWrappers.remove(draggingWrapper);
            }

            isDragging = false;
            draggingWrapper = null;
        }
    }

    private boolean tryToInsert(BlockSlot slot, CodeBlockInstance blockInstance, int x, int y) {
        if (slot == null) {
            return false;
        }

        if (blockInstance.instanceOf(CodeBlockStatement.class)) {
            // getBottomBlock takes a CodeBlockInstance<CodeBlockStatement>
            getBottomBlock(blockInstance);
        }

        if (blockInstance.instanceOf(CodeBlockStatement.class) &&
                slot.isBlockValid(blockInstance) && slot.getContents() != null) {
            CodeBlockInstance<CodeBlockStatement> bottomBlock = getBottomBlock(blockInstance);

            CodeBlockInstance toMove = slot.getContents();
            slot.removeContents();
            bottomBlock.getBlock().getFollowingSlot(bottomBlock).setContents(toMove);
            toMove.recalculateComponentPositions();
        }
        else if (slot.isBlockValid(blockInstance) && slot.getContents() != null) {
            removeBlockFromParent(x, y, slot.getContents());
        }
        if (slot.setContents(blockInstance)) {
            return true;
        }
        return false;
    }

    private CodeBlockInstance<CodeBlockStatement> getBottomBlock(CodeBlockInstance<CodeBlockStatement> blockInstance) {
        CodeBlockInstance next = blockInstance.getBlock().getFollowingSlot(blockInstance).getContents();
        if (next != null) {
            return getBottomBlock(next);
        }
        return blockInstance;
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
                CodeBlockInstance hoveredBlock = codeBlockWrapper.getBlock().getHoveredBlock(
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

    private CodeBlockWrapper removeBlockFromParent(int x, int y, CodeBlockInstance block) {
        CodeBlockWrapper newPos = new CodeBlockWrapper(block, x, y);
        block.removeFromParent();
        codeBlockWrappers.add(newPos);
        return newPos;
    }
}
