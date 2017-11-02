package flaxbeard.automata.common.entity;

import flaxbeard.automata.common.program.base.Statement;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIFollowProgram extends EntityAIBase {

    private final EntityAutomaton automaton;

    public EntityAIFollowProgram(EntityAutomaton automaton) {
        this.automaton = automaton;
    }

    @Override
    public boolean shouldExecute() {
        return automaton.getProgram() != null;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
    }

    @Override
    public void resetTask() {
        super.resetTask();
    }

    @Override
    public void updateTask() {
        super.updateTask();

        if (automaton.getCurrentStep() == null) {
            automaton.setCurrentStep(automaton.getProgram());
            automaton.setNewStep(true);
        }

        if (automaton.getCurrentStep() != null) {
            if (automaton.isNewStep())  {
                automaton.getCurrentStep().startExecuting(automaton);
            }
            Statement lastStep = automaton.getCurrentStep();
            automaton.setCurrentStep(automaton.getCurrentStep().execute(automaton));
            if (automaton.getCurrentStep() != lastStep) {
                automaton.setNewStep(true);
            } else {
                automaton.setNewStep(false);
            }
        }


    }
}
