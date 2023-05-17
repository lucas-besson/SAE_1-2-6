package boardifier.model.animation;

import java.util.HashMap;
import java.util.Map;

/* All animation type names must be defined with a strict format
   category/version/flavor

   The 2 first are mandatory

 */
public final class AnimationTypes {
    public static final String MOVETELEPORT_NAME = "move/teleport";
    public static final int MOVETELEPORT_VALUE = 10;
    public static final String MOVELINEARCST_NAME = "move/linearcst";
    public static final int MOVELINEARCST_VALUE = 11;
    public static final String MOVELINEARPROP_NAME = "move/linearprop";
    public static final int MOVELINEARPROP_VALUE = 12;
    public static final String LOOKSIMPLE_NAME = "look/simple";
    public static final int LOOKSIMPLE_VALUE = 20;
    public static final String LOOKSEQUENCE_NAME = "look/sequence";
    public static final int LOOKSEQUENCE_VALUE = 21;
    public static final String LOOKRANDOM_NAME = "look/random";
    public static final int LOOKRANDOM_VALUE = 22;
    public static Map<String,Integer> types;
    static {
        types = new HashMap<>();
        types.put("none",-1);
        types.put(MOVETELEPORT_NAME,MOVETELEPORT_VALUE);
        types.put(MOVELINEARCST_NAME,MOVELINEARCST_VALUE);
        types.put(MOVELINEARPROP_NAME,MOVELINEARPROP_VALUE);
        types.put(LOOKSEQUENCE_NAME, LOOKSIMPLE_VALUE);
        types.put(LOOKSEQUENCE_NAME, LOOKSEQUENCE_VALUE);
        types.put(LOOKRANDOM_NAME,LOOKRANDOM_VALUE);
    }

    private AnimationTypes() {
    }

    public static int getType(String name) {
        if (types.containsKey(name)) return types.get(name);
        return 0;
    }
    public static String getName(int type) {
        String name = types.entrySet().stream()
                .filter(e -> e.getValue() == type)
                .map(Map.Entry::getKey)
                .findAny()
                .orElse("");
        return name;
    }

    public static void register(String name, int typeNumber) throws IllegalArgumentException {
        if ((!types.containsKey(name)) && (!types.containsValue(typeNumber)) )  {
            types.put(name, typeNumber);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public static boolean isValid(int typeNumber) {
        return types.containsValue(typeNumber);
    }
    public static boolean isValid(String name) {
        return types.containsKey(name);
    }
}
