package flaxbeard.automata.client.gui.codeblock;

import flaxbeard.automata.Automata;
import flaxbeard.automata.client.gui.codeblock.base.CodeBlockStatement;
import flaxbeard.automata.client.gui.codeblock.component.ExpressionSlot;
import flaxbeard.automata.client.gui.codeblock.component.StringComponent;
import net.minecraft.util.ResourceLocation;

public class CodeBlockMineArea extends CodeBlockStatement {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, "mineArea");
        CodeBlockRegistry.registerFactory(id, CodeBlockMineArea.class, (Void v) -> new CodeBlockMineArea());
    }

    public CodeBlockMineArea() {
        super(
                new StringComponent("Mine area"),
                new ExpressionSlot(Type.STRING)
        );
    }

}
