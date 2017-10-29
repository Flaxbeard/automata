package flaxbeard.automata.common.codeblock;

import flaxbeard.automata.common.codeblock.base.CodeBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CodeBlockRegistry {

    public static final String ID_STRING = "id";

    private static Map<ResourceLocation, Constructor<? extends CodeBlock>> idToFactoryRegistry = new HashMap<>();
    private static Map<Class, ResourceLocation> blockToIdRegistry = new HashMap<>();

    public static <T extends CodeBlock> void registerCodeBlock(ResourceLocation id, Class<T> cls) {
        try {
            Constructor<T> constructor = cls.getConstructor();
            idToFactoryRegistry.put(id, constructor);
            blockToIdRegistry.put(cls, id);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static CodeBlock loadFromNBT(NBTTagCompound compound) {
        if (compound.hasKey(ID_STRING)) {
            ResourceLocation id = new ResourceLocation(compound.getString(ID_STRING));
            if (idToFactoryRegistry.containsKey(id)) {
                try {
                    Constructor<? extends CodeBlock> factory = idToFactoryRegistry.get(id);
                    CodeBlock block = factory.newInstance();
                    block.loadFromNBT(compound);
                    return block;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
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
