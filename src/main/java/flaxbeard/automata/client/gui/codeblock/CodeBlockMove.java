package flaxbeard.automata.client.gui.codeblock;

import flaxbeard.automata.Automata;
import flaxbeard.automata.client.gui.codeblock.base.CodeBlockStatement;
import flaxbeard.automata.client.gui.codeblock.component.ExpressionSlot;
import flaxbeard.automata.client.gui.codeblock.component.StringComponent;
import net.minecraft.util.ResourceLocation;

public class CodeBlockMove extends CodeBlockStatement {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, "move");
        CodeBlockRegistry.registerFactory(id, CodeBlockMove.class, (Void v) -> new CodeBlockMove());
    }

    public CodeBlockMove() {
        super(
                new StringComponent("Move to"),
                new ExpressionSlot(Type.STRING)
        );
    }

}
