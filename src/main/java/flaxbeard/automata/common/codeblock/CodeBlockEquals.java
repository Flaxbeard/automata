package flaxbeard.automata.common.codeblock;

import flaxbeard.automata.common.codeblock.base.CodeBlockExpression;
import flaxbeard.automata.common.codeblock.component.ExpressionSlotAny;
import flaxbeard.automata.common.codeblock.component.StringComponent;
import flaxbeard.automata.common.program.ExprEquals;
import flaxbeard.automata.common.program.base.Expression;

public class CodeBlockEquals extends CodeBlockExpression {

    public CodeBlockEquals() {
        super(
                new ExpressionSlotAny(),
                new StringComponent("="),
                new ExpressionSlotAny()
        );
    }

    @Override
    public boolean canTakeType(Type type) {
        return type == Type.BOOLEAN;
    }

    @Override
    public <T> Expression<T> toExpression(Type<T> type) {
        if (type == Type.BOOLEAN || type == type.ANY) {
            Expression<Object> getOne = ((CodeBlockExpression) slots[0].getContents()).toExpression(Type.ANY);
            Expression<Object> getTwo = ((CodeBlockExpression) slots[1].getContents()).toExpression(Type.ANY);

            return (Expression<T>) new ExprEquals(getOne, getTwo);
        }
        return null;
    }
}
