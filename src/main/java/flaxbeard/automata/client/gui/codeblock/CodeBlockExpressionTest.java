package flaxbeard.automata.client.gui.codeblock;

import flaxbeard.automata.client.gui.codeblock.component.Component;

public class CodeBlockExpressionTest extends CodeBlockExpression {

    public CodeBlockExpressionTest(Component... components) {
        super(components);
    }

    @Override
    public boolean canTakeType(CodeBlockInstance codeBlockInstance, Type type) {
        return true;
    }
}