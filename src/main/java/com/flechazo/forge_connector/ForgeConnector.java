package com.flechazo.forge_connector;

import com.flechazo.forge_connector.compat.ResourceLocationCompat;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ForgeConnector.MODID)
public class ForgeConnector {
    public static final String MODID = "forge_connector";

    public static final Logger LOGGER = LogUtils.getLogger();

    // 添加静态初始化块，尽早注册CoreMod
    static {
        LOGGER.info("ForgeConnector静态初始化块执行");
        try {
            // 手动触发服务加载
            Class.forName("com.flechazo.forge_connector.asm.ForgeConnectorTransformationService");
            LOGGER.info("已手动加载ForgeConnectorTransformationService");
        } catch (Exception e) {
            LOGGER.error("手动加载ForgeConnectorTransformationService失败", e);
        }
    }

    public ForgeConnector() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        LOGGER.info("Forge Connector 初始化中...");

        initCompatLayers();
    }

    private void initCompatLayers() {
        ResourceLocationCompat.init();
        LOGGER.info("所有兼容层已初始化，当前版本: (47.3.0)");
    }
    
    // 提供静态桥接方法，作为备用方案
    public static class ResourceLocationBridge {
        public static net.minecraft.resources.ResourceLocation tryBySeparator(String location, char separator) {
            return ResourceLocationCompat.tryBySeparator(location, separator);
        }
        
        public static net.minecraft.resources.ResourceLocation withDefaultNamespace(String path) {
            return ResourceLocationCompat.withDefaultNamespace(path);
        }
    }
}
