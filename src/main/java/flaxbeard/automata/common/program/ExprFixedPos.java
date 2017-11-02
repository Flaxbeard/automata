package flaxbeard.automata.common.program;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.entity.EntityAutomaton;
import flaxbeard.automata.common.program.base.Expression;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class ExprFixedPos extends Expression<Vec3d> {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, ExprFixedPos.class.getSimpleName());
        ExpressionRegistry.registerExpression(id, ExprFixedPos.class);
    }

    private Vec3d vec;

    public ExprFixedPos() {}

    public ExprFixedPos(Vec3d vec) {
        this.vec = vec;
    }

    @Override
    public Vec3d evaluate(EntityAutomaton automaton) {
        return vec;
    }

    @Override
    protected NBTTagCompound saveData(NBTTagCompound compound) {
        compound = super.saveData(compound);
        compound.setDouble("x", vec.x);
        compound.setDouble("y", vec.y);
        compound.setDouble("z", vec.z);
        return compound;
    }

    @Override
    protected void loadData(NBTTagCompound compound) {
        super.loadData(compound);
        double x = compound.getDouble("x");
        double y = compound.getDouble("y");
        double z = compound.getDouble("z");
        vec = new Vec3d(x, y, z);
    }
}
