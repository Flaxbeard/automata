package flaxbeard.automata.common.program.base;

import flaxbeard.automata.common.entity.EntityAutomaton;
import flaxbeard.automata.common.program.ExpressionRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

public abstract class Expression<T> {

    protected Expression[] expressions;

    public Expression(Expression... expressions) {
        this.expressions = expressions;
    }

    public abstract T evaluate(EntityAutomaton automaton);

    public final NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = saveData(compound);
        ResourceLocation id = ExpressionRegistry.getId(this);
        compound.setString(ExpressionRegistry.ID_STRING, id.toString());
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
    }
}
