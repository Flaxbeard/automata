package flaxbeard.automata.common.codeblock;

import flaxbeard.automata.common.codeblock.base.CodeBlockExpression;
import flaxbeard.automata.common.codeblock.component.StringComponent;
import flaxbeard.automata.common.program.ExprCurrentPos;
import flaxbeard.automata.common.program.base.Expression;
import net.minecraft.util.math.Vec3d;

public class CodeBlockCurrPos extends CodeBlockExpression {

    public CodeBlockCurrPos() {
        super(
                new StringComponent("current pos")
        );
    }

    @Override
    public boolean canTakeType(Type type) {
        return type == Type.POSITION;
    }

    @Override
    public <T> Expression<T> toExpression(Type<T> type) {
        if (type == Type.POSITION) {
            return (Expression<T>) new ExprCurrentPos();
        }
        return null;
    }
}
