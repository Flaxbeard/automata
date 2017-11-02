package flaxbeard.automata.common.codeblock;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class Type<T> {

    private static List<Type> values = new ArrayList<>();

    public static final Type ANY = new Type<>(Object.class);
    public static final Type POSITION = new Type<>(Vec3d.class);
    public static final Type ENTITY = new Type<>(Entity.class);
    public static final Type NUMBER = new Type<>(Double.class);
    public static final Type BOOLEAN = new Type<>(Boolean.class);

    private Class<T> returnClass;

    public Type(Class<T> returnClass) {
        values.add(this);
        this.returnClass = returnClass;
    }

    public Class<T> getReturnClass() {
        return returnClass;
    }

    public static List<Type> values() {
        return values;
    }
}
