package flaxbeard.automata.common.codeblock;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.codeblock.base.CodeBlockExpression;
import flaxbeard.automata.common.codeblock.component.StringComponent;
import net.minecraft.util.ResourceLocation;

public class CodeBlockPosition extends CodeBlockExpression {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, "position");
        CodeBlockRegistry.registerCodeBlock(id, CodeBlockPosition.class);
    }

    public CodeBlockPosition() {
        super(
                new StringComponent("Position")
                );
    }

    @Override
    public boolean canTakeType(Type type) {
        return true;
    }
}