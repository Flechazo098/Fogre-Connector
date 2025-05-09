//package com.flechazo.forge_connector.asm;
//
//import com.flechazo.forge_connector.ForgeConnector;
//import org.objectweb.asm.Opcodes;
//import org.objectweb.asm.Type;
//import org.objectweb.asm.tree.*;
//
//import java.util.function.Function;
//
///**
// * 模组构造函数的ASM转换器
// * 用于处理47.3.10版本引入的带FMLJavaModLoadingContext参数的构造函数
// * 为带参数的构造函数创建无参版本，以兼容低版本Forge
// */
//public class ModConstructorTransformer implements Function<ClassNode, ClassNode> {
//    private static final String FML_CONTEXT_CLASS = "net/minecraftforge/fml/javafmlmod/FMLJavaModLoadingContext";
//    private static final String FML_CONTEXT_DESC = "Lnet/minecraftforge/fml/javafmlmod/FMLJavaModLoadingContext;";
//    private static final String MOD_ANNOTATION = "Lnet/minecraftforge/fml/common/Mod;";
//
//    @Override
//    public ClassNode apply(ClassNode classNode) {
//        // 检查类是否有@Mod注解
//        boolean isModClass = false;
//        if (classNode.visibleAnnotations != null) {
//            for (AnnotationNode annotation : classNode.visibleAnnotations) {
//                if (annotation.desc.equals(MOD_ANNOTATION)) {
//                    isModClass = true;
//                    break;
//                }
//            }
//        }
//
//        // 如果不是Mod类，直接返回
//        if (!isModClass) {
//            return classNode;
//        }
//
//        ForgeConnector.LOGGER.info("检测到Mod类: " + classNode.name);
//
//        // 查找带FMLJavaModLoadingContext参数的构造函数
//        MethodNode contextConstructor = null;
//        boolean hasDefaultConstructor = false;
//
//        for (MethodNode method : classNode.methods) {
//            if (method.name.equals("<init>")) {
//                Type[] argumentTypes = Type.getArgumentTypes(method.desc);
//
//                // 检查是否是无参构造函数
//                if (argumentTypes.length == 0) {
//                    hasDefaultConstructor = true;
//                }
//
//                // 检查是否是带FMLJavaModLoadingContext参数的构造函数
//                if (argumentTypes.length == 1 && argumentTypes[0].getDescriptor().equals(FML_CONTEXT_DESC)) {
//                    contextConstructor = method;
//                    ForgeConnector.LOGGER.info("找到带FMLJavaModLoadingContext参数的构造函数");
//                }
//            }
//        }
//
//        // 如果找到了带参数的构造函数，但没有无参构造函数，则创建一个
//        if (contextConstructor != null && !hasDefaultConstructor) {
//            ForgeConnector.LOGGER.info("为类 " + classNode.name + " 创建无参构造函数");
//            addDefaultConstructor(classNode, contextConstructor);
//        }
//
//        return classNode;
//    }
//
//    /**
//     * 添加无参构造函数，在其中调用带FMLJavaModLoadingContext参数的构造函数
//     */
//    private void addDefaultConstructor(ClassNode classNode, MethodNode contextConstructor) {
//        MethodNode defaultConstructor = new MethodNode(
//                Opcodes.ACC_PUBLIC,
//                "<init>",
//                "()V",
//                null,
//                null
//        );
//
//        InsnList instructions = defaultConstructor.instructions;
//
//        // 加载this引用
//        instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
//
//        // 获取FMLJavaModLoadingContext实例
//        instructions.add(new MethodInsnNode(
//                Opcodes.INVOKESTATIC,
//                FML_CONTEXT_CLASS,
//                "get",
//                "()Lnet/minecraftforge/fml/javafmlmod/FMLJavaModLoadingContext;",
//                false
//        ));
//
//        // 调用带参数的构造函数
//        instructions.add(new MethodInsnNode(
//                Opcodes.INVOKESPECIAL,
//                classNode.name,
//                "<init>",
//                "(Lnet/minecraftforge/fml/javafmlmod/FMLJavaModLoadingContext;)V",
//                false
//        ));
//
//        // 返回
//        instructions.add(new InsnNode(Opcodes.RETURN));
//
//        // 设置方法的局部变量和栈大小
//        defaultConstructor.maxLocals = 1;
//        defaultConstructor.maxStack = 2;
//
//        // 将方法添加到类中
//        classNode.methods.add(defaultConstructor);
//    }
//}