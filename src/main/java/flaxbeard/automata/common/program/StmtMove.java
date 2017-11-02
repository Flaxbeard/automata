package flaxbeard.automata.common.program;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.entity.EntityAutomaton;
import flaxbeard.automata.common.program.base.Expression;
import flaxbeard.automata.common.program.base.Statement;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class StmtMove extends Statement {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, StmtMove.class.getSimpleName());
        StatementRegistry.registerStatement(id, StmtMove.class);
    }

    private final Random r = new Random();
    private Expression<Vec3d> positionExpression;

    private double targetX;
    private double targetY;
    private double targetZ;

    public StmtMove() {}

    public StmtMove(Expression<Vec3d> positionExpression, Statement followingStatement) {
        super(followingStatement, positionExpression);
        this.positionExpression = positionExpression;
    }

    @Override
    public Statement execute(EntityAutomaton automaton) {

        double distance = automaton.getDistance(targetX, targetY, targetZ);
        boolean canPath = automaton.getNavigator().tryMoveToXYZ(targetX, targetY, targetZ, .5);


        if (canPath && distance > 0.5) {
            return this;
        }

        return followingStatement;
    }

    @Override
    public void startExecuting(EntityAutomaton automaton) {
        super.startExecuting(automaton);

        Vec3d vec = positionExpression.evaluate(automaton);


        targetX = vec.x;
        targetY = vec.y;
        targetZ = vec.z;
    }

    @Override
    protected NBTTagCompound saveData(NBTTagCompound compound) {
        compound = super.saveData(compound);
        compound.setDouble("x", targetX);
        compound.setDouble("y", targetY);
        compound.setDouble("z", targetZ);
        return compound;
    }

    @Override
    protected void loadData(NBTTagCompound compound) {
        super.loadData(compound);
        targetX = compound.getDouble("x");
        targetY = compound.getDouble("y");
        targetZ = compound.getDouble("z");
        positionExpression = expressions[0];
    }
}
