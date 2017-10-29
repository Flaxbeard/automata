package flaxbeard.automata.common.codeblock.component;

import flaxbeard.automata.client.gui.GuiProgrammer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class StringComponent extends Component {
    private static FontRenderer font = Minecraft.getMinecraft().fontRenderer;
    public String text;

    public StringComponent(String text) {
        this.text = text;
    }

    @Override
    public int getWidth() {
        return font.getStringWidth(text) + 4;
    }

    @Override
    public int getHeight() {
        return font.FONT_HEIGHT + 2;
    }

    @Override
    public void drawForeground(GuiProgrammer gui) {
        font.drawString(text, 2, 1, 0);
    }
}
