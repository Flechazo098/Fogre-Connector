//package com.flechazo.forge_connector.asm;
//
//import com.flechazo.forge_connector.ForgeConnector;
//import org.objectweb.asm.Opcodes;
//import org.objectweb.asm.tree.*;
//
//import java.util.function.Function;
//
///**
// * ResourceLocation的ASM转换器
// * 用于在低版本Forge中动态添加高版本的API方法
// */
//public class ResourceLocationTransformer implements Function<ClassNode, ClassNode> {
//    private static final String RESOURCE_LOCATION_CLASS = "net/minecraft/resources/ResourceLocation";
//    private static final String COMPAT_CLASS = "com/flechazo/forge_connector/compat/ResourceLocationCompat";
//
//    @Override
//    public ClassNode apply(ClassNode classNode) {
//        // 只处理ResourceLocation类
//        if (!classNode.name.equals(RESOURCE_LOCATION_CLASS)) {
//            return classNode;
//        }
//
//        ForgeConnector.LOGGER.info("正在转换ResourceLocation类，添加47.4.0版本API...");
//
//        // 检查是否已经有这些方法，如果有就不添加了
//        if (hasMethod(classNode, "tryBySeparator")) {
//            ForgeConnector.LOGGER.info("检测到高版本API，跳过转换");
//            return classNode;
//        }
//
//        // 添加tryBySeparator静态方法
//        addTryBySeparatorMethod(classNode);
//
//        // 添加withDefaultNamespace静态方法
//        addWithDefaultNamespaceMethod(classNode);
//
//        // 添加compareNamespaced实例方法
//        addCompareNamespacedMethod(classNode);
//
//        ForgeConnector.LOGGER.info("ResourceLocation类转换完成");
//        return classNode;
//    }
//
//    /**
//     * 检查类是否已经有指定名称的方法
//     */
//    private boolean hasMethod(ClassNode classNode, String methodName) {
//        for (MethodNode method : classNode.methods) {
//            if (method.name.equals(methodName)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 添加tryBySeparator静态方法
//     */
//    private void addTryBySeparatorMethod(ClassNode classNode) {
//        MethodNode method = new MethodNode(
//                Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
//                "tryBySeparator",
//                "(Ljava/lang/String;C)Lnet/minecraft/resources/ResourceLocation;",
//                null,
//                null
//        );
//
//        InsnList instructions = method.instructions;
//
//        // 调用ResourceLocationCompat.tryBySeparator方法
//        instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // 加载第一个参数 (String location)
//        instructions.add(new VarInsnNode(Opcodes.ILOAD, 1)); // 加载第二个参数 (char separator)
//        instructions.add(new MethodInsnNode(
//                Opcodes.INVOKESTATIC,
//                COMPAT_CLASS,
//                "tryBySeparator",
//                "(Ljava/lang/String;C)Lnet/minecraft/resources/ResourceLocation;",
//                false
//        ));
//
//        instructions.add(new InsnNode(Opcodes.ARETURN)); // 返回结果
//
//        // 设置方法的局部变量和栈大小
//        method.maxLocals = 2;
//        method.maxStack = 2;
//
//        // 将方法添加到类中
//        classNode.methods.add(method);
//
//        ForgeConnector.LOGGER.info("已添加tryBySeparator方法");
//    }
//
//    /**
//     * 添加withDefaultNamespace静态方法
//     */
//    private void addWithDefaultNamespaceMethod(ClassNode classNode) {
//        MethodNode method = new MethodNode(
//                Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
//                "withDefaultNamespace",
//                "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
//                null,
//                null
//        );
//
//        InsnList instructions = method.instructions;
//
//        // 调用ResourceLocationCompat.withDefaultNamespace方法
//        instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // 加载参数 (String path)
//        instructions.add(new MethodInsnNode(
//                Opcodes.INVOKESTATIC,
//                COMPAT_CLASS,
//                "withDefaultNamespace",
//                "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
//                false
//        ));
//
//        instructions.add(new InsnNode(Opcodes.ARETURN)); // 返回结果
//
//        // 设置方法的局部变量和栈大小
//        method.maxLocals = 1;
//        method.maxStack = 1;
//
//        // 将方法添加到类中
//        classNode.methods.add(method);
//
//        ForgeConnector.LOGGER.info("已添加withDefaultNamespace方法");
//    }
//
//    /**
//     * 添加compareNamespaced实例方法
//     */
//    private void addCompareNamespacedMethod(ClassNode classNode) {
//        MethodNode method = new MethodNode(
//                Opcodes.ACC_PUBLIC,
//                "compareNamespaced",
//                "(Lnet/minecraft/resources/ResourceLocation;)I",
//                null,
//                null
//        );
//
//        InsnList instructions = method.instructions;
//
//        // 调用ResourceLocationCompat.compareNamespaced方法
//        instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // 加载this
//        instructions.add(new VarInsnNode(Opcodes.ALOAD, 1)); // 加载参数 (ResourceLocation other)
//        instructions.add(new MethodInsnNode(
//                Opcodes.INVOKESTATIC,
//                COMPAT_CLASS,
//                "compareNamespaced",
//                "(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;)I",
//                false
//        ));
//
//        instructions.add(new InsnNode(Opcodes.IRETURN)); // 返回结果
//
//        // 设置方法的局部变量和栈大小
//        method.maxLocals = 2;
//        method.maxStack = 2;
//
//        // 将方法添加到类中
//        classNode.methods.add(method);
//
//        ForgeConnector.LOGGER.info("已添加compareNamespaced方法");
//    }
//}