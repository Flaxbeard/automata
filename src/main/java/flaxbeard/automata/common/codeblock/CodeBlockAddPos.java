package flaxbeard.automata.common.codeblock;

import flaxbeard.automata.common.codeblock.base.CodeBlockExpression;
import flaxbeard.automata.common.codeblock.component.ExpressionSlot;
import flaxbeard.automata.common.codeblock.component.StringComponent;
import flaxbeard.automata.common.program.ExprAddPos;
import flaxbeard.automata.common.program.base.Expression;
import net.minecraft.util.math.Vec3d;

public class CodeBlockAddPos extends CodeBlockExpression {

    public CodeBlockAddPos() {
        super(
                new ExpressionSlot(Type.POSITION),
                new StringComponent("+"),
                new ExpressionSlot(Type.POSITION)
                );
    }

    @Override
    public boolean canTakeType(Type type) {
        return type == Type.POSITION;
    }

    @Override
    public <T> Expression<T> toExpression(Type<T> type) {
        if (type == Type.POSITION) {
            Expression<Vec3d> addOne = (Expression<Vec3d>) ((CodeBlockExpression) slots[0].getContents()).toExpression(type);
            Expression<Vec3d> addTwo = (Expression<Vec3d>) ((CodeBlockExpression) slots[1].getContents()).toExpression(type);

            return (Expression<T>) new ExprAddPos(addOne, addTwo);
        }
        return null;
    }
}
