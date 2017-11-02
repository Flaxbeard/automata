package flaxbeard.automata.common.program;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.entity.EntityAutomaton;
import flaxbeard.automata.common.program.base.Statement;
import net.minecraft.util.ResourceLocation;

public class StmtHead extends Statement {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, StmtHead.class.getSimpleName());
        StatementRegistry.registerStatement(id, StmtHead.class);
    }

    public StmtHead() {}

    public StmtHead(Statement followingStatement) {
        super(followingStatement);
    }

    @Override
    public Statement execute(EntityAutomaton automaton) {
        if (followingStatement != null) {
            followingStatement.startExecuting(automaton);
            return followingStatement.execute(automaton);
        }
        return null;
    }

}
