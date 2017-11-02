package flaxbeard.automata.common.program;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.entity.EntityAutomaton;
import flaxbeard.automata.common.program.base.Statement;
import net.minecraft.util.ResourceLocation;

public class StmtPrint extends Statement {

    static
    {
        ResourceLocation id = new ResourceLocation(Automata.MODID, StmtPrint.class.getSimpleName());
        StatementRegistry.registerStatement(id, StmtPrint.class);
    }

    public StmtPrint() {}

    public StmtPrint(Statement followingStatement) {
        super(followingStatement);
    }

    @Override
    public Statement execute(EntityAutomaton automaton) {
        System.out.println("TEST");
        return followingStatement;
    }

}
