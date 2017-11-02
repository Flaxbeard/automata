package flaxbeard.automata.common.codeblock;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.codeblock.base.CodeBlockExpression;
import flaxbeard.automata.common.codeblock.base.CodeBlockStatement;
import flaxbeard.automata.common.codeblock.component.ExpressionSlot;
import flaxbeard.automata.common.codeblock.component.StringComponent;
import flaxbeard.automata.common.program.StmtMine;
import flaxbeard.automata.common.program.StmtMove;
import flaxbeard.automata.common.program.base.Expression;
import flaxbeard.automata.common.program.base.Statement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class CodeBlockMine extends CodeBlockStatement {

    public CodeBlockMine() {
        super(
                new StringComponent("Mine block "),
                new ExpressionSlot(Type.POSITION)
        );
    }

    @Override
    public Statement toStatement() {
        Expression<Vec3d> expr = ((CodeBlockExpression) slots[0].getContents()).toExpression(Vec3d.class);
        return new StmtMine(expr, getFollowingStatement());
    }
}
