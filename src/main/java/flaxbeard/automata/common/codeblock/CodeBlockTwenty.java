package flaxbeard.automata.common.codeblock;

import flaxbeard.automata.common.codeblock.base.CodeBlockExpression;
import flaxbeard.automata.common.codeblock.component.StringComponent;
import flaxbeard.automata.common.program.ExprFixedPos;
import flaxbeard.automata.common.program.base.Expression;
import net.minecraft.util.math.Vec3d;

public class CodeBlockTwenty extends CodeBlockExpression {

    public CodeBlockTwenty() {
        super(
                new StringComponent("5, 0, 0")
        );
    }

    @Override
    public boolean canTakeType(Type<T> type) {
        return type == Type.POSITION;
    }

    @Override
    public <T> Expression<T> toExpression(Type<T> type) {
        System.out.println("TE");
        if (type == Vec3d.class) {
            return (Expression<T>) new ExprFixedPos(new Vec3d(1, 0, 0));
        }
        return null;
    }
}
