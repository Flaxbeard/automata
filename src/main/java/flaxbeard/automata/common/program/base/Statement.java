package flaxbeard.automata.common.program.base;

import flaxbeard.automata.common.entity.EntityAutomaton;
import flaxbeard.automata.common.program.ExpressionRegistry;
import flaxbeard.automata.common.program.StatementRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

public abstract class Statement {

    protected Statement followingStatement;
    protected Expression[] expressions;

    public Statement() {}

    public Statement(Statement followingStatement, Expression... expressions) {
        this.followingStatement = followingStatement;
        this.expressions = expressions;
    }

    public abstract Statement execute(EntityAutomaton automaton);

    public void startExecuting(EntityAutomaton automaton) {}

    public final NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = saveData(compound);
        ResourceLocation id = StatementRegistry.getId(this);
        compound.setString(StatementRegistry.ID_STRING, id.toString());
        return compound;
    }

    protected NBTTagCompound saveData(NBTTagCompound compound) {
        NBTTagList slotTagList = new NBTTagList();
        for (Expression expr : expressions) {
            NBTTagCompound exprTag = new NBTTagCompound();
            if (expr != null) {
                exprTag = expr.writeToNBT(exprTag);
            }
            slotTagList.appendTag(exprTag);
        }
        compound.setTag("expressions", slotTagList);

        NBTTagCompound stmtTag = new NBTTagCompound();
        if (followingStatement != null) {
            stmtTag = followingStatement.writeToNBT(stmtTag);
        }
        compound.setTag("followingStatement", stmtTag);


        return compound;
    }

    public final void loadFromNBT(NBTTagCompound compound) {
        loadData(compound);
    }

    protected void loadData(NBTTagCompound compound) {
        NBTTagList slotTagList = (NBTTagList) compound.getTag("expressions");

        expressions = new Expression[slotTagList.tagCount()];
        for (int i = 0; i < slotTagList.tagCount(); i++) {
            NBTTagCompound slotTag = slotTagList.getCompoundTagAt(i);
            Expression expr = ExpressionRegistry.loadFromNBT(slotTag);
            expressions[i] = expr;
        }

        followingStatement = StatementRegistry.loadFromNBT(compound.getCompoundTag("followingStatement"));
    }
}
