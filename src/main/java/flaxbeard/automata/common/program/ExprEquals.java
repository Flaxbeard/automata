package flaxbeard.automata.common.program;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.entity.EntityAutomaton;
import flaxbeard.automata.common.program.base.Expression;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class ExprEquals extends Expression<Boolean> {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, ExprEquals.class.getSimpleName());
        ExpressionRegistry.registerExpression(id, ExprEquals.class);
    }

    private Expression<Object> o1;
    private Expression<Object> o2;

    public ExprEquals() {}

    public ExprEquals(Expression<Object> o1, Expression<Object> o2) {
        super(o1, o2);
        this.o1 = o1;
        this.o2 = o2;
    }

    @Override
    public Boolean evaluate(EntityAutomaton automaton) {
        Object obj1 = o1.evaluate(automaton);
        Object obj2 = o2.evaluate(automaton);

        return Objects.equals(obj1, obj2);
    }

    @Override
    protected void loadData(NBTTagCompound compound) {
        super.loadData(compound);
        o1 = expressions[0];
        o2 = expressions[1];
    }
}
