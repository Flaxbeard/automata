package flaxbeard.automata.common.program;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.entity.EntityAutomaton;
import flaxbeard.automata.common.program.base.Expression;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class ExprAddPos extends Expression<Vec3d> {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, ExprAddPos.class.getSimpleName());
        ExpressionRegistry.registerExpression(id, ExprAddPos.class);
    }

    private Expression<Vec3d> pos2;
    private Expression<Vec3d> pos1;

    public ExprAddPos() {}

    public ExprAddPos(Expression<Vec3d> pos1, Expression<Vec3d> pos2) {
        super(pos1, pos2);
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    @Override
    public Vec3d evaluate(EntityAutomaton automaton) {
        Vec3d vec1 = pos1.evaluate(automaton);
        Vec3d vec2 = pos2.evaluate(automaton);

        return vec1.add(vec2);
    }

    @Override
    protected void loadData(NBTTagCompound compound) {
        super.loadData(compound);
        pos1 = expressions[0];
        pos2 = expressions[1];
    }
}
