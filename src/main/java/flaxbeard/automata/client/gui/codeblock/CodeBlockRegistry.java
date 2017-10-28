package flaxbeard.automata.client.gui.codeblock;

import flaxbeard.automata.client.gui.codeblock.base.CodeBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CodeBlockRegistry {

    public static final String ID_STRING = "id";

    private static Map<ResourceLocation, Function<Void, CodeBlock>> idToFactoryRegistry = new HashMap<>();
    private static Map<Class, ResourceLocation> blockToIdRegistry = new HashMap<>();

    public static <T extends CodeBlock> void registerFactory(ResourceLocation id, Class<T> cls, Function<Void, T> factory) {
        idToFactoryRegistry.put(id, (Function<Void, CodeBlock>) factory);
        blockToIdRegistry.put(cls, id);
    }

    public static CodeBlock loadFromNBT(NBTTagCompound compound) {
        if (compound.hasKey(ID_STRING)) {
            ResourceLocation id = new ResourceLocation(compound.getString(ID_STRING));
            if (idToFactoryRegistry.containsKey(id)) {
                Function<Void, CodeBlock> factory = idToFactoryRegistry.get(id);

                CodeBlock block = factory.apply(null);
                block.loadFromNBT(compound);
            }
        }
        return null;
    }

    public static ResourceLocation getId(CodeBlock codeBlock) {
        Class<? extends CodeBlock> cls = codeBlock.getClass();
        if (blockToIdRegistry.containsKey(cls)) {
            return blockToIdRegistry.get(cls);
        }
        return null;
    }
}
