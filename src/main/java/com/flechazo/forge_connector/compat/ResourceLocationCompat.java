package com.flechazo.forge_connector.compat;

import com.flechazo.forge_connector.ForgeConnector;
import cpw.mods.modlauncher.api.INameMappingService;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeEntity;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ResourceLocation兼容层
 * 提供Forge 47.3.0到47.4.0之间的ResourceLocation API兼容
 * 在47.3.0版本中实现47.4.0版本的API
 */
public class ResourceLocationCompat {
    private static boolean isLowVersion = false;
    private static String DEFAULT_NAMESPACE = "minecraft";
    private static Constructor<?> dummyConstructor = null;

    public static void init() {
        ForgeConnector.LOGGER.info("初始化ResourceLocation兼容层");
        
        // 检测当前Forge版本是否为47.3.0（低版本）
        checkForgeVersion();

        // 初始化反射所需的字段和方法
        try {
            String fieldName = ObfuscationReflectionHelper.remapName(
                    INameMappingService.Domain.FIELD, "DEFAULT_NAMESPACE"
            );
            Field field = ResourceLocation.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            String namespace = (String) field.get(null);
            
//            ForgeConnector.LOGGER.info("ResourceLocation兼容层已初始化，当前模式：" + (isLowVersion ? "低版本(47.3.0)" : "高版本(47.4.0)"));
        } catch (Exception e) {
            // 如果获取字段失败，使用默认值"minecraft"
            DEFAULT_NAMESPACE = "minecraft";
            ForgeConnector.LOGGER.info("无法获取DEFAULT_NAMESPACE字段，使用默认值: " + DEFAULT_NAMESPACE);
//            ForgeConnector.LOGGER.info("ResourceLocation兼容层已初始化，当前模式：" + (isLowVersion ? "低版本(47.3.0)" : "高版本(47.4.0)"));
        }
    }

    /**
     * 检测当前Forge版本
     */
    private static void checkForgeVersion() {
        try {
            // 尝试调用47.4.0版本的方法，如果不存在则为低版本
            Method method = ResourceLocation.class.getMethod("bySeparator", String.class, char.class);
            isLowVersion = false;
        } catch (NoSuchMethodException e) {
            isLowVersion = true;
            ForgeConnector.LOGGER.info("检测到Forge低版本(47.3.0)，将提供47.4.0 API兼容");
        }
    }

    /**
     * 提供47.4.0版本的fromNamespaceAndPath方法
     */
    public static ResourceLocation fromNamespaceAndPath(String namespace, String path) {
        if (!isLowVersion) {
            // 高版本直接调用原生方法
            try {
                Method method = ResourceLocation.class.getMethod("fromNamespaceAndPath", String.class, String.class);
                return (ResourceLocation) method.invoke(null, namespace, path);
            } catch (Exception e) {
                ForgeConnector.LOGGER.error("调用ResourceLocation.fromNamespaceAndPath失败", e);
            }
        }
        
        // 低版本实现
        try {
            return new ResourceLocation(namespace, path);
        } catch (Exception e) {
            ForgeConnector.LOGGER.error("创建ResourceLocation失败", e);
            return null;
        }
    }

    /**
     * 提供47.4.0版本的parse方法
     */
    public static ResourceLocation parse(String location) {
        if (!isLowVersion) {
            // 高版本直接调用原生方法
            try {
                Method method = ResourceLocation.class.getMethod("parse", String.class);
                return (ResourceLocation) method.invoke(null, location);
            } catch (Exception e) {
                ForgeConnector.LOGGER.error("调用ResourceLocation.parse失败", e);
            }
        }
        
        // 低版本实现
        try {
            return new ResourceLocation(location);
        } catch (Exception e) {
            ForgeConnector.LOGGER.error("创建ResourceLocation失败", e);
            return null;
        }
    }

    /**
     * 提供47.4.0版本的bySeparator方法
     */
    public static ResourceLocation bySeparator(String location, char separator) {
        if (!isLowVersion) {
            // 高版本直接调用原生方法
            try {
                Method method = ResourceLocation.class.getMethod("bySeparator", String.class, char.class);
                return (ResourceLocation) method.invoke(null, location, separator);
            } catch (Exception e) {
                ForgeConnector.LOGGER.error("调用ResourceLocation.bySeparator失败", e);
            }
        }
        
        // 低版本实现 - 使用of方法
        try {
            Method method = ResourceLocation.class.getMethod("of", String.class, char.class);
            return (ResourceLocation) method.invoke(null, location, separator);
        } catch (Exception e) {
            ForgeConnector.LOGGER.error("调用ResourceLocation.of失败", e);
            // 手动实现
            int i = location.indexOf(separator);
            if (i >= 0) {
                String path = location.substring(i + 1);
                String namespace = i > 0 ? location.substring(0, i) : DEFAULT_NAMESPACE;
                return fromNamespaceAndPath(namespace, path);
            }
            return fromNamespaceAndPath(DEFAULT_NAMESPACE, location);
        }
    }

    /**
     * 提供47.4.0版本的tryBySeparator方法
     */
    public static @Nullable ResourceLocation tryBySeparator(String location, char separator) {
        if (!isLowVersion) {
            // 高版本直接调用原生方法
            try {
                Method method = ResourceLocation.class.getMethod("tryBySeparator", String.class, char.class);
                return (ResourceLocation) method.invoke(null, location, separator);
            } catch (Exception e) {
                ForgeConnector.LOGGER.error("调用ResourceLocation.tryBySeparator失败", e);
            }
        }
        
        // 低版本实现
        try {
            int i = location.indexOf(separator);
            if (i >= 0) {
                String path = location.substring(i + 1);
                if (!isValidPath(path)) {
                    return null;
                }
                if (i != 0) {
                    String namespace = location.substring(0, i);
                    if (!isValidNamespace(namespace)) {
                        return null;
                    }
                    return fromNamespaceAndPath(namespace, path);
                } else {
                    return fromNamespaceAndPath(DEFAULT_NAMESPACE, path);
                }
            } else {
                if (!isValidPath(location)) {
                    return null;
                }
                return fromNamespaceAndPath(DEFAULT_NAMESPACE, location);
            }
        } catch (Exception e) {
            ForgeConnector.LOGGER.error("调用ResourceLocation.tryBySeparator失败", e);
            return null;
        }
    }

    /**
     * 提供47.4.0版本的withDefaultNamespace方法
     */
    public static ResourceLocation withDefaultNamespace(String path) {
        if (!isLowVersion) {
            // 高版本直接调用原生方法
            try {
                Method method = ResourceLocation.class.getMethod("withDefaultNamespace", String.class);
                return (ResourceLocation) method.invoke(null, path);
            } catch (Exception e) {
                ForgeConnector.LOGGER.error("调用ResourceLocation.withDefaultNamespace失败", e);
            }
        }
        
        // 低版本实现
        return fromNamespaceAndPath(DEFAULT_NAMESPACE, path);
    }

    /**
     * 为ResourceLocation实例提供47.4.0版本的compareNamespaced方法
     */
    public static int compareNamespaced(ResourceLocation a, ResourceLocation b) {
        if (!isLowVersion) {
            // 高版本直接调用原生方法
            try {
                Method method = a.getClass().getMethod("compareNamespaced", ResourceLocation.class);
                return (int) method.invoke(a, b);
            } catch (Exception e) {
                ForgeConnector.LOGGER.error("调用ResourceLocation.compareNamespaced失败", e);
            }
        }
        
        // 低版本实现 - 先比较命名空间，再比较路径
        try {
            Field namespaceField = ResourceLocation.class.getDeclaredField("namespace");
            Field pathField = ResourceLocation.class.getDeclaredField("path");
            namespaceField.setAccessible(true);
            pathField.setAccessible(true);
            
            String namespaceA = (String) namespaceField.get(a);
            String namespaceB = (String) namespaceField.get(b);
            
            int ret = namespaceA.compareTo(namespaceB);
            if (ret != 0) {
                return ret;
            }
            
            String pathA = (String) pathField.get(a);
            String pathB = (String) pathField.get(b);
            return pathA.compareTo(pathB);
        } catch (Exception e) {
            ForgeConnector.LOGGER.error("调用ResourceLocation.compareNamespaced失败", e);
            // 退化为普通比较
            return a.compareTo(b);
        }
    }
    
    /**
     * 检查命名空间是否有效
     */
    private static boolean isValidNamespace(String namespace) {
        try {
            Method method = ResourceLocation.class.getMethod("isValidNamespace", String.class);
            return (boolean) method.invoke(null, namespace);
        } catch (Exception e) {
            // 手动实现验证逻辑
            for (int i = 0; i < namespace.length(); i++) {
                char c = namespace.charAt(i);
                if (c == '_' || c == '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.') {
                    continue;
                }
                return false;
            }
            return true;
        }
    }
    
    /**
     * 检查路径是否有效
     */
    private static boolean isValidPath(String path) {
        try {
            Method method = ResourceLocation.class.getMethod("isValidPath", String.class);
            return (boolean) method.invoke(null, path);
        } catch (Exception e) {
            // 手动实现验证逻辑
            for (int i = 0; i < path.length(); i++) {
                char c = path.charAt(i);
                if (c == '_' || c == '-' || c == '/' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.' || c == ':') {
                    continue;
                }
                return false;
            }
            return true;
        }
    }
}