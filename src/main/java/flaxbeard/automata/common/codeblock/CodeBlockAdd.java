package flaxbeard.automata.common.codeblock;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.codeblock.base.CodeBlockExpression;
import flaxbeard.automata.common.codeblock.component.ExpressionSlot;
import flaxbeard.automata.common.codeblock.component.StringComponent;
import net.minecraft.util.ResourceLocation;

public class CodeBlockAdd extends CodeBlockExpression {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, "add");
        CodeBlockRegistry.registerCodeBlock(id, CodeBlockAdd.class);
    }

    public CodeBlockAdd() {
        super(
                new ExpressionSlot(Type.STRING),
                new StringComponent("+"),
                new ExpressionSlot(Type.STRING)
                );
    }

    @Override
    public boolean canTakeType(Type type) {
        return true;
    }
}