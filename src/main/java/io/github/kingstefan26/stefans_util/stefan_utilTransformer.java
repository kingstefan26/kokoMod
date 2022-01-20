package io.github.kingstefan26.stefans_util;


import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;



public class stefan_utilTransformer implements IClassTransformer {

    private static final String GUI_SCREEN_CLASS_NAME = "net.minecraft.client.gui.GuiScreen";

    private static final String DRAW_WORLD_BAGKGROUND_METHOD = "drawWorldBackground";
    private static final String DRAW_WORLD_BAGKGROUND_METHOD_OBF = "func_146270_b";
    
    private static final String BULR_MODULE = "io/github/kingstefan26/stefans_util/module/render/blurClickGui";
    private static final String COLOR_HOOK_METHOD_NAME = "getBackgroundColor";
    private static final String COLOR_HOOK_METHOD_DESC = "(Z)I";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals(GUI_SCREEN_CLASS_NAME)) {
            System.out.println("Transforming Class [" + transformedName + "], Method [" + DRAW_WORLD_BAGKGROUND_METHOD + "]");

            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(basicClass);
            classReader.accept(classNode, 0);

            for (MethodNode m : classNode.methods) {
                if (m.name.equals(DRAW_WORLD_BAGKGROUND_METHOD) || m.name.equals(DRAW_WORLD_BAGKGROUND_METHOD_OBF)) {
                    for (int i = 0; i < m.instructions.size(); i++) {
                        AbstractInsnNode next = m.instructions.get(i);
                        if (next.getOpcode() == Opcodes.LDC) {
                            System.out.println("Modifying GUI background darkness... ");
                            AbstractInsnNode colorHook = new MethodInsnNode(Opcodes.INVOKESTATIC, BULR_MODULE, COLOR_HOOK_METHOD_NAME, COLOR_HOOK_METHOD_DESC, false);
                            AbstractInsnNode colorHook2 = colorHook.clone(null);

                            // Replace LDC with hooks
                            m.instructions.set(next, colorHook);
                            m.instructions.set(colorHook.getNext(), colorHook2);

                            // Load boolean constants for method param
                            m.instructions.insertBefore(colorHook, new InsnNode(Opcodes.ICONST_1));
                            m.instructions.insertBefore(colorHook2, new InsnNode(Opcodes.ICONST_0));
                            break;
                        }
                    }
                    break;
                }
            }
            
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(cw);
            System.out.println("Transforming " + transformedName + " Finished.");
            return cw.toByteArray();
        }
    
        return basicClass;
    }

}
