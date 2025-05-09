function initializeCoreMod() {
    print("ResourceLocation Transformer initializing...");

    var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var ForgeConnector = Java.type('com.flechazo.forge_connector.ForgeConnector');

    return {
        'resourcelocation_transformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.resources.ResourceLocation'
            },
            'transformer': function(classNode) {
                ForgeConnector.LOGGER.info("正在转换ResourceLocation类，添加47.4.0版本API...");

                // 检查是否已经有这些方法，如果有就不添加了
                if (hasMethod(classNode, "tryBySeparator")) {
                    ForgeConnector.LOGGER.info("检测到高版本API，跳过转换");
                    return classNode;
                }

                // 添加tryBySeparator静态方法
                addTryBySeparatorMethod(classNode);

                // 添加withDefaultNamespace静态方法
                addWithDefaultNamespaceMethod(classNode);

                // 添加compareNamespaced实例方法
                addCompareNamespacedMethod(classNode);

                ForgeConnector.LOGGER.info("ResourceLocation类转换完成");
                return classNode;
            }
        }
    };

    // 检查类是否已经有指定名称的方法
    function hasMethod(classNode, methodName) {
        for (var i = 0; i < classNode.methods.size(); i++) {
            var method = classNode.methods.get(i);
            if (method.name.equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    // 添加tryBySeparator静态方法
    function addTryBySeparatorMethod(classNode) {
        var methodNode = ASMAPI.getMethodNode();
        methodNode.access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC;
        methodNode.name = "tryBySeparator";
        methodNode.desc = "(Ljava/lang/String;C)Lnet/minecraft/resources/ResourceLocation;";

        var instructions = methodNode.instructions;

        // 调用ResourceLocationCompat.tryBySeparator方法
        instructions.add(ASMAPI.buildVarInsn(Opcodes.ALOAD, 0)); // 加载第一个参数 (String location)
        instructions.add(ASMAPI.buildVarInsn(Opcodes.ILOAD, 1)); // 加载第二个参数 (char separator)
        instructions.add(ASMAPI.buildMethodCall(
            "com/flechazo/forge_connector/compat/ResourceLocationCompat",
            "tryBySeparator",
            "(Ljava/lang/String;C)Lnet/minecraft/resources/ResourceLocation;",
            ASMAPI.MethodType.STATIC
        ));

        instructions.add(ASMAPI.buildInsn(Opcodes.ARETURN)); // 返回结果

        // 设置方法的局部变量和栈大小
        methodNode.maxLocals = 2;
        methodNode.maxStack = 2;

        // 将方法添加到类中
        classNode.methods.add(methodNode);

        ForgeConnector.LOGGER.info("已添加tryBySeparator方法");
    }

    // 添加withDefaultNamespace静态方法
    function addWithDefaultNamespaceMethod(classNode) {
        var methodNode = ASMAPI.getMethodNode();
        methodNode.access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC;
        methodNode.name = "withDefaultNamespace";
        methodNode.desc = "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;";

        var instructions = methodNode.instructions;

        // 调用ResourceLocationCompat.withDefaultNamespace方法
        instructions.add(ASMAPI.buildVarInsn(Opcodes.ALOAD, 0)); // 加载参数 (String path)
        instructions.add(ASMAPI.buildMethodCall(
            "com/flechazo/forge_connector/compat/ResourceLocationCompat",
            "withDefaultNamespace",
            "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
            ASMAPI.MethodType.STATIC
        ));

        instructions.add(ASMAPI.buildInsn(Opcodes.ARETURN)); // 返回结果

        // 设置方法的局部变量和栈大小
        methodNode.maxLocals = 1;
        methodNode.maxStack = 1;

        // 将方法添加到类中
        classNode.methods.add(methodNode);

        ForgeConnector.LOGGER.info("已添加withDefaultNamespace方法");
    }

    // 添加compareNamespaced实例方法
    function addCompareNamespacedMethod(classNode) {
        var methodNode = ASMAPI.getMethodNode();
        methodNode.access = Opcodes.ACC_PUBLIC;
        methodNode.name = "compareNamespaced";
        methodNode.desc = "(Lnet/minecraft/resources/ResourceLocation;)I";

        var instructions = methodNode.instructions;

        // 调用ResourceLocationCompat.compareNamespaced方法
        instructions.add(ASMAPI.buildVarInsn(Opcodes.ALOAD, 0)); // 加载this
        instructions.add(ASMAPI.buildVarInsn(Opcodes.ALOAD, 1)); // 加载参数 (ResourceLocation other)
        instructions.add(ASMAPI.buildMethodCall(
            "com/flechazo/forge_connector/compat/ResourceLocationCompat",
            "compareNamespaced",
            "(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;)I",
            ASMAPI.MethodType.STATIC
        ));

        instructions.add(ASMAPI.buildInsn(Opcodes.IRETURN)); // 返回结果

        // 设置方法的局部变量和栈大小
        methodNode.maxLocals = 2;
        methodNode.maxStack = 2;

        // 将方法添加到类中
        classNode.methods.add(methodNode);

        ForgeConnector.LOGGER.info("已添加compareNamespaced方法");
    }
}