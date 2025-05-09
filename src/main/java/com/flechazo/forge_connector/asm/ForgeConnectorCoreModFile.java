package com.flechazo.forge_connector.asm;

import com.flechazo.forge_connector.ForgeConnector;
import net.minecraftforge.forgespi.coremod.ICoreModFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现ICoreModFile接口的类，用于注册CoreMod
 */
public class ForgeConnectorCoreModFile implements ICoreModFile {
    private final Path path;
    private final String jsFileName;
    private static final Map<String, String> JS_FILES = new HashMap<>();
    
    static {
        // 注册所有JavaScript文件
        JS_FILES.put("resourcelocation_transformer.js", "resourcelocation_transformer.js");
        JS_FILES.put("mod_constructor_transformer.js",   "mod_constructor_transformer.js");
    }
    
    public ForgeConnectorCoreModFile() {
        // 使用主JS文件路径
        this.path = Paths.get("javascript/forge_connector_coremod.js");
        this.jsFileName = null;
    }
    
    public ForgeConnectorCoreModFile(String jsFileName) {
        // 使用特定JS文件路径
        this.path = Paths.get(jsFileName);
        this.jsFileName = jsFileName;
    }
    
    @Override
    public String getOwnerId() {
        return ForgeConnector.MODID;
    }
    
    @Override
    public Reader readCoreMod() throws IOException {
        if (jsFileName != null && JS_FILES.containsKey(jsFileName)) {
            // 从资源文件中读取特定的JS文件
            InputStream inputStream = ForgeConnector.class.getClassLoader()
                    .getResourceAsStream(JS_FILES.get(jsFileName));
            
            if (inputStream == null) {
                throw new IOException("找不到JS文件: " + jsFileName);
            }
            
            return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } else {
            // 从资源文件中读取主JS文件
            InputStream inputStream = ForgeConnector.class.getClassLoader()
                    .getResourceAsStream("javascript/forge_connector_coremod.js");
            
            if (inputStream == null) {
                // 如果找不到JS文件，则返回一个内置的JS代码
                String js = "function initializeCoreMod() {\n" +
                        "    print('ForgeConnector CoreMod initializing...');\n" +
                        "    var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');\n" +
                        "    \n" +
                        "    // 加载其他JS文件\n" +
                        "    ASMAPI.loadFile('resourcelocation_transformer.js');\n" +
                        "    ASMAPI.loadFile('mod_constructor_transformer.js');\n" +
                        "    \n" +
                        "    return {};\n" +
                        "}";
                return new InputStreamReader(
                        new java.io.ByteArrayInputStream(js.getBytes(StandardCharsets.UTF_8)),
                        StandardCharsets.UTF_8
                );
            }
            
            return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        }
    }
    
    @Override
    public Path getPath() {
        return path;
    }
    
    @Override
    public Reader getAdditionalFile(String fileName) throws IOException {
        if (JS_FILES.containsKey(fileName)) {
            InputStream inputStream = ForgeConnector.class.getClassLoader()
                    .getResourceAsStream(JS_FILES.get(fileName));
            
            if (inputStream != null) {
                return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            }
        }
        
        throw new IOException("不支持获取额外文件: " + fileName);
    }
}