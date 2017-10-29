package flaxbeard.automata.common.codeblock;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.codeblock.base.CodeBlockStatement;
import flaxbeard.automata.common.codeblock.component.ExpressionSlot;
import flaxbeard.automata.common.codeblock.component.StringComponent;
import net.minecraft.util.ResourceLocation;

public class CodeBlockMineArea extends CodeBlockStatement {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, "mineArea");
        CodeBlockRegistry.registerCodeBlock(id, CodeBlockMineArea.class);
    }

    public CodeBlockMineArea() {
        super(
                new StringComponent("Mine area"),
                new ExpressionSlot(Type.STRING)
        );
    }

}
