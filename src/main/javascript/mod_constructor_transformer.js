function initializeCoreMod() {
    print("Mod Constructor Transformer initializing...");

    var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var Type = Java.type('org.objectweb.asm.Type');
    var ForgeConnector = Java.type('com.flechazo.forge_connector.ForgeConnector');

    var FML_CONTEXT_CLASS = "net/minecraftforge/fml/javafmlmod/FMLJavaModLoadingContext";
    var FML_CONTEXT_DESC = "Lnet/minecraftforge/fml/javafmlmod/FMLJavaModLoadingContext;";
    var MOD_ANNOTATION = "Lnet/minecraftforge/fml/common/Mod;";

    return {
        'mod_constructor_transformer': {
            'target': {
                'type': 'CLASS',
                'predicate': {
                    'type': 'ANNOTATION',
                    'annotation': 'net.minecraftforge.fml.common.Mod'
                }
            },
            'transformer': function(classNode) {
                ForgeConnector.LOGGER.info("检测到Mod类: " + classNode.name);

                // 查找带FMLJavaModLoadingContext参数的构造函数
                var contextConstructor = null;
                var hasDefaultConstructor = false;

                for (var i = 0; i < classNode.methods.size(); i++) {
                    var method = classNode.methods.get(i);
                    if (method.name.equals("<init>")) {
                        var methodDesc = method.desc;
                        var argumentTypes = Type.getArgumentTypes(methodDesc);

                        // 检查是否是无参构造函数
                        if (argumentTypes.length == 0) {
                            hasDefaultConstructor = true;
                        }

                        // 检查是否是带FMLJavaModLoadingContext参数的构造函数
                        if (argumentTypes.length == 1 && argumentTypes[0].getDescriptor().equals(FML_CONTEXT_DESC)) {
                            contextConstructor = method;
                            ForgeConnector.LOGGER.info("找到带FMLJavaModLoadingContext参数的构造函数");
                        }
                    }
                }

                // 如果找到了带参数的构造函数，但没有无参构造函数，则创建一个
                if (contextConstructor != null && !hasDefaultConstructor) {
                    ForgeConnector.LOGGER.info("为类 " + classNode.name + " 创建无参构造函数");
                    addDefaultConstructor(classNode, contextConstructor);
                }

                return classNode;
            }
        }
    };

    // 添加无参构造函数，在其中调用带FMLJavaModLoadingContext参数的构造函数
    function addDefaultConstructor(classNode, contextConstructor) {
        var methodNode = ASMAPI.getMethodNode();
        methodNode.access = Opcodes.ACC_PUBLIC;
        methodNode.name = "<init>";
        methodNode.desc = "()V";

        var instructions = methodNode.instructions;

        // 加载this引用
        instructions.add(ASMAPI.buildVarInsn(Opcodes.ALOAD, 0));

        // 获取FMLJavaModLoadingContext实例
        instructions.add(ASMAPI.buildMethodCall(
            FML_CONTEXT_CLASS,
            "get",
            "()Lnet/minecraftforge/fml/javafmlmod/FMLJavaModLoadingContext;",
            ASMAPI.MethodType.STATIC
        ));

        // 调用带参数的构造函数
        instructions.add(ASMAPI.buildMethodCall(
            classNode.name,
            "<init>",
            "(Lnet/minecraftforge/fml/javafmlmod/FMLJavaModLoadingContext;)V",
            ASMAPI.MethodType.SPECIAL
        ));

        // 返回
        instructions.add(ASMAPI.buildInsn(Opcodes.RETURN));

        // 设置方法的局部变量和栈大小
        methodNode.maxLocals = 1;
        methodNode.maxStack = 2;

        // 将方法添加到类中
        classNode.methods.add(methodNode);
    }
}