package com.flechazo.forge_connector.asm;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.coremod.ICoreModProvider;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ForgeConnectorTransformationService implements ITransformationService {
    
    public ForgeConnectorTransformationService() {
        // 在服务初始化时注册CoreMod
        System.out.println("[ForgeConnector DEBUG] ForgeConnectorTransformationService constructor called. Attempting to register CoreMod.");
        try {
            ICoreModProvider coreModProvider = FMLLoader.getCoreModProvider();
            if (coreModProvider != null) {
                System.out.println("[ForgeConnector DEBUG] CoreModProvider obtained successfully.");
                // 注册主CoreMod文件
                coreModProvider.addCoreMod(new ForgeConnectorCoreModFile());
                System.out.println("[ForgeConnector DEBUG] Main CoreMod file registered successfully.");
            } else {
                // 使用 System.err 来突出显示错误信息
                System.err.println("[ForgeConnector CRITICAL] Failed to get CoreModProvider. CoreMod will NOT be loaded. This is a critical issue.");
            }
        } catch (Exception e) {
            System.err.println("[ForgeConnector CRITICAL] Exception during CoreMod registration in ForgeConnectorTransformationService: " + e.getMessage());
            e.printStackTrace(System.err); // 打印完整的堆栈跟踪到错误流
        }
    }

    @Override
    public String name() {
        return "ForgeConnectorTransformationService";
    }

    @Override
    public void initialize(IEnvironment environment) {
        System.out.println("[ForgeConnector DEBUG] ForgeConnectorTransformationService.initialize called.");
    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException {
        System.out.println("[ForgeConnector DEBUG] ForgeConnectorTransformationService.onLoad called.");
    }

    @Override
    public List<ITransformer> transformers() {
        return Collections.emptyList(); // 我们不需要提供转换器，因为我们使用CoreMod
    }
}