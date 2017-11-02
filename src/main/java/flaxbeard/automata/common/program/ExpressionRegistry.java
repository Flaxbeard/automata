package flaxbeard.automata.common.program;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.codeblock.CodeBlockAddPos;
import flaxbeard.automata.common.codeblock.CodeBlockRegistry;
import flaxbeard.automata.common.program.base.Expression;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ExpressionRegistry {

    public static final String ID_STRING = "id";

    private static Map<ResourceLocation, Constructor<? extends Expression>> idToFactoryRegistry = new HashMap<>();
    private static Map<Class, ResourceLocation> epxressionToIdRegistry = new HashMap<>();

    public static <T extends Expression> void registerExpression(ResourceLocation id, Class<T> cls) {
        if (idToFactoryRegistry.containsKey(id)) {
            throw new IllegalArgumentException("Expression id " + id + " already in use!");
        }
        try {
            Constructor<T> constructor = cls.getConstructor();
            idToFactoryRegistry.put(id, constructor);
            epxressionToIdRegistry.put(cls, id);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static Expression loadFromNBT(NBTTagCompound compound) {
        if (compound == null) {
            return null;
        }
        if (compound.hasKey(ID_STRING)) {
            ResourceLocation id = new ResourceLocation(compound.getString(ID_STRING));
            if (idToFactoryRegistry.containsKey(id)) {
                try {
                    Constructor<? extends Expression> factory = idToFactoryRegistry.get(id);
                    Expression epxression = factory.newInstance();
                    epxression.loadFromNBT(compound);
                    return epxression;
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static ResourceLocation getId(Expression expression) {
        Class<? extends Expression> cls = expression.getClass();
        if (epxressionToIdRegistry.containsKey(cls)) {
            return epxressionToIdRegistry.get(cls);
        }
        throw new IllegalArgumentException("No ID found for expression" + expression.getClass().getSimpleName());
    }
}
