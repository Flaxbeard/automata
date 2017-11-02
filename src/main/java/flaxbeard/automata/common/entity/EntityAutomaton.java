package flaxbeard.automata.common.entity;

import flaxbeard.automata.common.AutomataContent;
import flaxbeard.automata.common.codeblock.CodeBlockHead;
import flaxbeard.automata.common.codeblock.CodeBlockRegistry;
import flaxbeard.automata.common.program.StatementRegistry;
import flaxbeard.automata.common.program.base.Statement;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityAutomaton extends EntityGolem {

    private static final DataParameter<ItemStack> HELD_CODE =
            EntityDataManager.createKey(EntityAutomaton.class, DataSerializers.ITEM_STACK);

    private Statement program;
    private Statement currentStep;
    private boolean isNewStep;

    public EntityAutomaton(World worldIn) {
        super(worldIn);
        this.setSize(0.6F * .5F, 1.95F * .5F);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HELD_CODE, ItemStack.EMPTY);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIFollowProgram(this));
    }

    public void setProgramItem(ItemStack program) {
        dataManager.set(HELD_CODE, program);
    }

    public ItemStack getProgramItem() {
        return dataManager.get(HELD_CODE);
    }

    public Statement getProgram() {
        if (program != null) {
            return program;
        }

        if (getProgramItem() == null) {
            return null;
        }

        NBTTagCompound data = AutomataContent.GIZMO.getData(getProgramItem());
        program = StatementRegistry.loadFromNBT(data);

        return program;
    }

    public Statement getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Statement step) {
        currentStep = step;
    }

    public boolean isNewStep() {
        return isNewStep;
    }

    public void setNewStep(boolean isNewStep) {
        this.isNewStep = isNewStep;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        NBTTagCompound currentStepTag = new NBTTagCompound();
        if (currentStep != null) {
            currentStep.writeToNBT(currentStepTag);
        }
        compound.setTag("currentStep", currentStepTag);

        compound.setBoolean("isNewStep", isNewStep);

        compound.setTag("heldCode", getProgramItem().writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        NBTTagCompound currentStepTag = compound.getCompoundTag("currentStep");
        currentStep = StatementRegistry.loadFromNBT(currentStepTag);

        isNewStep = compound.getBoolean("isNewStep");

        setProgramItem(new ItemStack(compound.getCompoundTag("heldCode")));
    }
}
