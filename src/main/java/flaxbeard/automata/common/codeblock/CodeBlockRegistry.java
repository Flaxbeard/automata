package flaxbeard.automata.common.codeblock;

import flaxbeard.automata.common.codeblock.base.CodeBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeBlockRegistry {

    public static final String ID_STRING = "id";

    private static Map<ResourceLocation, Constructor<? extends CodeBlock>> idToFactoryRegistry = new HashMap<>();
    private static Map<Class, ResourceLocation> blockToIdRegistry = new HashMap<>();

    public static <T extends CodeBlock> void registerCodeBlock(String modid, Class<T> cls) {
        ResourceLocation rl = new ResourceLocation(modid, cls.getSimpleName());
        registerCodeBlock(rl, cls);
    }

    public static <T extends CodeBlock> void registerCodeBlock(ResourceLocation id, Class<T> cls) {
        if (idToFactoryRegistry.containsKey(id)) {
            throw new IllegalArgumentException("Code block id " + id + " already in use!");
        }
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
            CodeBlock newBlock = createNew(id);
            if (newBlock != null) {
                newBlock.loadFromNBT(compound);
                return newBlock;
            }
        }
        return null;
    }

    public static CodeBlock createNew(CodeBlock template) {
        ResourceLocation id = getId(template);
        return createNew(id);
    }

    public static CodeBlock createNew(ResourceLocation id) {
        if (idToFactoryRegistry.containsKey(id)) {
            try {
                Constructor<? extends CodeBlock> factory = idToFactoryRegistry.get(id);
                CodeBlock block = factory.newInstance();
                return block;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
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

    public static List<CodeBlock> getAllCodeBlocks() {
        List<CodeBlock> codeBlockList = new ArrayList<>();
        for (ResourceLocation id : blockToIdRegistry.values()) {
            codeBlockList.add(createNew(id));
        }
        return codeBlockList;
    }
}
