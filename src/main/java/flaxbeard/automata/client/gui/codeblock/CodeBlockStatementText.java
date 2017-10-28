package flaxbeard.automata.client.gui.codeblock;

import flaxbeard.automata.client.gui.codeblock.component.ExpressionSlot;
import flaxbeard.automata.client.gui.codeblock.component.StringComponent;

public class CodeBlockStatementText extends CodeBlockStatement {

    public CodeBlockStatementText(String text) {
        super(
                new StringComponent(text),
                new ExpressionSlot(Type.STRING)
        );
    }

}
