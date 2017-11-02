package flaxbeard.automata.common.program;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.entity.EntityAutomaton;
import flaxbeard.automata.common.program.base.Expression;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class ExprCurrentPos extends Expression<Vec3d> {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, ExprCurrentPos.class.getSimpleName());
        ExpressionRegistry.registerExpression(id, ExprCurrentPos.class);
    }

    public ExprCurrentPos() {}

    @Override
    public Vec3d evaluate(EntityAutomaton automaton) {
        return automaton.getPositionVector();
    }
}
