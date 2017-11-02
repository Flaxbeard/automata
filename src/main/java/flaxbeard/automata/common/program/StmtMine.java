package flaxbeard.automata.common.program;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.entity.EntityAutomaton;
import flaxbeard.automata.common.program.base.Expression;
import flaxbeard.automata.common.program.base.Statement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class StmtMine extends Statement {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, StmtMine.class.getSimpleName());
        StatementRegistry.registerStatement(id, StmtMine.class);
    }

    private final Random r = new Random();
    private Expression<Vec3d> positionExpression;

    private int targetX;
    private int targetY;
    private int targetZ;
    private int progress;

    public StmtMine() {}

    public StmtMine(Expression<Vec3d> positionExpression, Statement followingStatement) {
        super(followingStatement, positionExpression);
        this.positionExpression = positionExpression;
    }

    @Override
    public Statement execute(EntityAutomaton automaton) {

        BlockPos pos = new BlockPos(targetX, targetY, targetZ);
        IBlockState state = automaton.world.getBlockState(pos);
        if (state.getBlock().isAir(state, automaton.world, pos)) {
            return followingStatement;
        }


        double distance = automaton.getDistance(targetX, targetY, targetZ);
        boolean canPath = automaton.getNavigator().tryMoveToXYZ(targetX, targetY, targetZ, .5);

        if (canPath && distance > 0.5) {

            progress = 0;
            return this;
        }

        progress++;

        if (progress > 20) {
            automaton.world.destroyBlock(pos, true);
            return followingStatement;
        }


        return this;
    }

    @Override
    public void startExecuting(EntityAutomaton automaton) {
        super.startExecuting(automaton);

        Vec3d vec = positionExpression.evaluate(automaton);


        targetX = (int) Math.round(vec.x);
        targetY = (int) Math.round(vec.y);
        targetZ = (int) Math.round(vec.z);
        progress = 0;
    }

    @Override
    protected NBTTagCompound saveData(NBTTagCompound compound) {
        compound = super.saveData(compound);
        compound.setInteger("x", targetX);
        compound.setInteger("y", targetY);
        compound.setInteger("z", targetZ);
        return compound;
    }

    @Override
    protected void loadData(NBTTagCompound compound) {
        super.loadData(compound);
        targetX = compound.getInteger("x");
        targetY = compound.getInteger("y");
        targetZ = compound.getInteger("z");
        positionExpression = expressions[0];
    }
}
