package flaxbeard.automata.client.gui.codeblock;

import flaxbeard.automata.client.gui.codeblock.component.ExpressionSlot;
import flaxbeard.automata.client.gui.codeblock.component.StringComponent;

import java.util.ArrayList;
import java.util.List;

public class CodeBlocks {

    public static List<CodeBlock> codeBlockList = new ArrayList<>();

    public static CodeBlock EXPR_ADD;
    public static CodeBlock EXPR_CURR_POS;

    public static CodeBlock STMT_MOVE;
    public static CodeBlock STMT_MINE;


    public static void preInit() {
        EXPR_ADD = new CodeBlockExpressionTest(
                new ExpressionSlot(Type.STRING),
                new StringComponent("+"),
                new ExpressionSlot(Type.STRING)
        );

        EXPR_CURR_POS = new CodeBlockExpressionTest(
                new StringComponent("Current position")
        );

        STMT_MOVE = new CodeBlockStatementText("Move to");
        STMT_MINE = new CodeBlockStatementText("Mine area");
    }
}
