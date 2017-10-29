package flaxbeard.automata.common.codeblock;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.codeblock.base.CodeBlockStatement;
import flaxbeard.automata.common.codeblock.component.ExpressionSlot;
import flaxbeard.automata.common.codeblock.component.StringComponent;
import net.minecraft.util.ResourceLocation;

public class CodeBlockMove extends CodeBlockStatement {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, "move");
        CodeBlockRegistry.registerCodeBlock(id, CodeBlockMove.class);
    }

    public CodeBlockMove() {
        super(
                new StringComponent("Move to"),
                new ExpressionSlot(Type.STRING)
        );
    }

}
