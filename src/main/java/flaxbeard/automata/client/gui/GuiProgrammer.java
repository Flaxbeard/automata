package flaxbeard.automata.client.gui;

import flaxbeard.automata.Automata;
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
    private List<CodeBlockPosition> codeBlockPositions;

    public static class CodeBlockPosition {

        private final CodeBlock block;

        private int x;
        private int y;

        public CodeBlockPosition(CodeBlock block, int x, int y) {
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

        public CodeBlockPosition setPos(int x, int y) {
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
    }

    public GuiProgrammer(InventoryPlayer inventoryPlayer, TileEntityProgrammer programmer) {
        super(new ContainerProgrammer(inventoryPlayer, programmer));
        this.programmer = programmer;
        this.ySize = 114 + 6 * 18;

        codeBlockPositions = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            codeBlockPositions.add(
                    new CodeBlockPosition(
                            new CodeBlockText("Number " + i),
                            i * 2,
                            i * 3
                    )
            );
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


        for (CodeBlockPosition block : codeBlockPositions) {
            block.drawBackground(this);
            block.drawForeground(this);
            RenderHelper.disableStandardItemLighting();
        }

        if (draggingBlockPos != null) {
            draggingBlockPos.drawBackground(this);
            draggingBlockPos.drawForeground(this);
        }
    }

    private boolean isDragging = false;
    private CodeBlockPosition draggingBlockPos = null;

    private int offsetX = 0;
    private int offsetY = 0;

    private void updateDrag(boolean mouseDown, int mouseX, int mouseY) {

        if (mouseDown) {
            if (!isDragging) {
                CodeBlockPosition hoveredBlock = getHoveredBlock(mouseX, mouseY);
                if (hoveredBlock != null) {
                    isDragging = true;
                    draggingBlockPos = hoveredBlock;
                    offsetX = mouseX - hoveredBlock.getX();
                    offsetY = mouseY - hoveredBlock.getY();
                }
            }
            if (isDragging) {
                int posX = Math.max(mouseX - offsetX, -99992);
                //posX = Math.min(posX, xSize - draggingBlockPos.getWidth() - 3);
                int posY = Math.max(mouseY - offsetY, -99992);
                //posY = Math.min(posY, 128 - draggingBlockPos.getHeight() - 3);

                draggingBlockPos.setPos(posX, posY);
            }
        } else if (isDragging) {

            CodeBlock.BlockSlot hoveredSlot = getHoveredSlot(mouseX, mouseY);
            CodeBlock.BlockSlot hoveredSlot2 = getHoveredSlot(draggingBlockPos.getX(), draggingBlockPos.getY());

            if (hoveredSlot != null) {
                if (hoveredSlot.getContents() != null) {
                    removeBlockFromParent(mouseX, mouseY, hoveredSlot.getContents());
                }
                if (hoveredSlot.setContents(draggingBlockPos.getBlock())) {
                    codeBlockPositions.remove(draggingBlockPos);
                }
            } else if (hoveredSlot2 != null) {
                if (hoveredSlot2.getContents() != null) {
                    removeBlockFromParent(mouseX, mouseY, hoveredSlot2.getContents());
                }
                if (hoveredSlot2.setContents(draggingBlockPos.getBlock())) {
                    codeBlockPositions.remove(draggingBlockPos);
                }
            }

            isDragging = false;
            draggingBlockPos = null;
        }
    }

    private CodeBlock.BlockSlot getHoveredSlot(int mouseX, int mouseY) {
        for (CodeBlockPosition blockPos : codeBlockPositions) {
            if (blockPos == draggingBlockPos) continue;

            boolean hovering = (mouseX >= blockPos.getX() && mouseX <= blockPos.getX() + blockPos.getWidth())
                    && (mouseY >= blockPos.getY() && mouseY <= blockPos.getY() + blockPos.getHeight());

            if (hovering) {
                return blockPos.getBlock().getHoveredSlot(
                        (int) ((mouseX - blockPos.getX()) / BLOCK_SCALE),
                        (int) ((mouseY - blockPos.getY()) / BLOCK_SCALE)
                );
            }
        }
        return null;
    }

    private CodeBlockPosition getHoveredBlock(int mouseX, int mouseY) {
        for (CodeBlockPosition blockPos : codeBlockPositions) {

            boolean hovering = (mouseX >= blockPos.getX() && mouseX <= blockPos.getX() + blockPos.getWidth())
                    && (mouseY >= blockPos.getY() && mouseY <= blockPos.getY() + blockPos.getHeight());

            if (hovering) {
                CodeBlock hoveredBlock = blockPos.getBlock().getHoveredBlock(
                        (int) ((mouseX - blockPos.getX()) / BLOCK_SCALE),
                        (int) ((mouseY - blockPos.getY()) / BLOCK_SCALE)
                );
                if (hoveredBlock.hasParent()) {
                    int x = hoveredBlock.getAbsoluteX();
                    int y = hoveredBlock.getAbsoluteY();

                    x = (int) (x * BLOCK_SCALE) + blockPos.getX();
                    y = (int) (y * BLOCK_SCALE) + blockPos.getY();

                    return removeBlockFromParent(x, y, hoveredBlock);
                } else {
                    return blockPos;
                }
            }
        }
        return null;
    }

    private CodeBlockPosition removeBlockFromParent(int x, int y, CodeBlock block) {
        CodeBlockPosition newPos = new CodeBlockPosition(block, x, y);
        block.removeFromParent();
        codeBlockPositions.add(newPos);
        return newPos;
    }
}
