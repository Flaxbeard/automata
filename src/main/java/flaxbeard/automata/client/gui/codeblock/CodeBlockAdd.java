package flaxbeard.automata.client.gui.codeblock;

import flaxbeard.automata.Automata;
import flaxbeard.automata.client.gui.codeblock.base.CodeBlockExpression;
import flaxbeard.automata.client.gui.codeblock.component.ExpressionSlot;
import flaxbeard.automata.client.gui.codeblock.component.StringComponent;
import net.minecraft.util.ResourceLocation;

public class CodeBlockAdd extends CodeBlockExpression {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, "add");
        CodeBlockRegistry.registerFactory(id, CodeBlockAdd.class, (Void v) -> new CodeBlockAdd());
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