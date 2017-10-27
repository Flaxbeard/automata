package flaxbeard.automata.client.gui;

public class CodeBlockText extends CodeBlock {

    public CodeBlockText(String text) {
        super(
                new StringComponent(text),
                new SlotComponent(),
                new FollowingComponent()
        );
    }

}
