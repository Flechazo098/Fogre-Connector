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

/**
 * 实现ICoreModFile接口的类，用于注册CoreMod
 */
public class ForgeConnectorCoreModFile implements ICoreModFile {
    private final Path path;
    
    public ForgeConnectorCoreModFile() {
        // 使用相对路径，实际上这个路径并不重要，因为我们会在readCoreMod中直接返回配置
        this.path = Paths.get("forge_connector.coremod.json");
    }
    
    @Override
    public String getOwnerId() {
        return ForgeConnector.MODID;
    }
    
    @Override
    public Reader readCoreMod() throws IOException {
        // 从资源文件中读取CoreMod配置
        InputStream inputStream = ForgeConnector.class.getClassLoader()
                .getResourceAsStream("forge_connector.coremod.json");
        
        if (inputStream == null) {
            // 如果找不到配置文件，则返回一个内置的配置
            String json = "{\n" +
                    "  \"resourcelocation_transformer\": {\n" +
                    "    \"target\": {\n" +
                    "      \"type\": \"CLASS\",\n" +
                    "      \"name\": \"net.minecraft.resources.ResourceLocation\"\n" +
                    "    },\n" +
                    "    \"transformer\": \"com.flechazo.forge_connector.asm.ResourceLocationTransformer\"\n" +
                    "  },\n" +
                    "  \"mod_constructor_transformer\": {\n" +
                    "    \"target\": {\n" +
                    "      \"type\": \"CLASS\",\n" +
                    "      \"predicate\": {\n" +
                    "        \"type\": \"ANNOTATION\",\n" +
                    "        \"annotation\": \"net.minecraftforge.fml.common.Mod\"\n" +
                    "      }\n" +
                    "    },\n" +
                    "    \"transformer\": \"com.flechazo.forge_connector.asm.ModConstructorTransformer\"\n" +
                    "  }\n" +
                    "}";
            return new InputStreamReader(
                    new java.io.ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),
                    StandardCharsets.UTF_8
            );
        }
        
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }
    
    @Override
    public Path getPath() {
        return path;
    }
    
    @Override
    public Reader getAdditionalFile(String fileName) throws IOException {
        throw new IOException("不支持获取额外文件: " + fileName);
    }
}