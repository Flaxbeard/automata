package flaxbeard.automata.client.gui.codeblock;

import flaxbeard.automata.client.gui.codeblock.component.Component;

public abstract class CodeBlockExpression extends CodeBlock {

    public CodeBlockExpression(Component... components) {
        super(components);
    }

    public abstract boolean canTakeType(CodeBlockInstance codeBlockInstance, Type type);
}
