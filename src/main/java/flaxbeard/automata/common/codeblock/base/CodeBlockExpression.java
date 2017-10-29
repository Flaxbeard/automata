package flaxbeard.automata.common.codeblock.base;

import flaxbeard.automata.common.codeblock.Type;
import flaxbeard.automata.common.codeblock.component.Component;

public abstract class CodeBlockExpression extends CodeBlock {

    public CodeBlockExpression(Component... components) {
        super(components);
    }

    public abstract boolean canTakeType(Type type);
}
