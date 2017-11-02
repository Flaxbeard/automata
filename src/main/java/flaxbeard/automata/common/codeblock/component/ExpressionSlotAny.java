package flaxbeard.automata.common.codeblock.component;

import flaxbeard.automata.common.codeblock.Type;
import flaxbeard.automata.common.codeblock.base.CodeBlock;
import flaxbeard.automata.common.codeblock.base.CodeBlockExpression;

public class ExpressionSlotAny extends ExpressionSlot {

    public ExpressionSlotAny() {
        super(null);
    }
    
    @Override
    public boolean isBlockValid(CodeBlock block) {
        return (block instanceof CodeBlockExpression);
    }
}
