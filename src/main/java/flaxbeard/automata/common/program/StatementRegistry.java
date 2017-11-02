package flaxbeard.automata.common.program;

import flaxbeard.automata.common.program.base.Statement;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class StatementRegistry {

    public static final String ID_STRING = "id";

    private static Map<ResourceLocation, Constructor<? extends Statement>> idToFactoryRegistry = new HashMap<>();
    private static Map<Class, ResourceLocation> statementToIdRegistry = new HashMap<>();

    public static <T extends Statement> void registerStatement(ResourceLocation id, Class<T> cls) {
        if (idToFactoryRegistry.containsKey(id)) {
            throw new IllegalArgumentException("Statement id " + id + " already in use!");
        }
        try {
            Constructor<T> constructor = cls.getConstructor();
            idToFactoryRegistry.put(id, constructor);
            statementToIdRegistry.put(cls, id);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static Statement loadFromNBT(NBTTagCompound compound) {
        if (compound == null) {
            return null;
        }
        if (compound.hasKey(ID_STRING)) {
            ResourceLocation id = new ResourceLocation(compound.getString(ID_STRING));
            if (idToFactoryRegistry.containsKey(id)) {
                try {
                    Constructor<? extends Statement> factory = idToFactoryRegistry.get(id);
                    Statement statement = factory.newInstance();
                    statement.loadFromNBT(compound);
                    return statement;
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static ResourceLocation getId(Statement statement) {
        Class<? extends Statement> cls = statement.getClass();
        if (statementToIdRegistry.containsKey(cls)) {
            return statementToIdRegistry.get(cls);
        }
        throw new IllegalArgumentException("No ID found for statement" + statement.getClass().getSimpleName());
    }
}
