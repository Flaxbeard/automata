package flaxbeard.automata.client.gui.codeblock;

import flaxbeard.automata.Automata;
import flaxbeard.automata.client.gui.codeblock.base.CodeBlockExpression;
import flaxbeard.automata.client.gui.codeblock.component.StringComponent;
import net.minecraft.util.ResourceLocation;

public class CodeBlockPosition extends CodeBlockExpression {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, "position");
        CodeBlockRegistry.registerFactory(id, CodeBlockPosition.class, (Void v) -> new CodeBlockPosition());
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