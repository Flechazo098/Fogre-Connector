function initializeCoreMod() {
    print("ForgeConnector CoreMod initializing...");
    var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    var ForgeConnector = Java.type('com.flechazo.forge_connector.ForgeConnector');

    // 加载其他JS文件
    ASMAPI.loadFile('resourcelocation_transformer.js');
    ASMAPI.loadFile('mod_constructor_transformer.js');

    ForgeConnector.LOGGER.info("ForgeConnector CoreMod 已初始化");

    // 主文件不需要返回转换器，它们由加载的JS文件提供
    return {};
}