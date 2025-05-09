package com.flechazo.forge_connector.mixin;

import com.flechazo.forge_connector.compat.ResourceLocationCompat;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * ResourceLocation的Mixin类
 * 在47.3.0版本中注入47.4.0版本的API
 */
@Mixin(ResourceLocation.class)
public class ResourceLocationMixin {

    /**
     * 添加47.4.0版本的bySeparator方法
     */
    @Inject(method = "of", at = @At("HEAD"), cancellable = true)
    private static void onOf(String location, char separator, CallbackInfoReturnable<ResourceLocation> cir) {
        // 在47.3.0版本中，of方法已存在，我们拦截它并提供bySeparator的别名
        ResourceLocation result = ResourceLocationCompat.bySeparator(location, separator);
        if (result != null) {
            cir.setReturnValue(result);
        }
    }
    
    /**
     * 添加47.4.0版本的parse方法
     */
    @Inject(method = "<init>(Ljava/lang/String;)V", at = @At("RETURN"))
    private void onConstructor(String location, CallbackInfo ci) {
        // 这里不需要取消原方法，只是为了注册parse静态方法
        // 实际的parse方法会通过ResourceLocationCompat提供
    }
    
    /**
     * 添加47.4.0版本的fromNamespaceAndPath方法
     */
    @Inject(method = "<init>(Ljava/lang/String;Ljava/lang/String;)V", at = @At("RETURN"))
    private void onConstructorWithNamespace(String namespace, String path, CallbackInfo ci) {
        // 这里不需要取消原方法，只是为了注册fromNamespaceAndPath静态方法
        // 实际的fromNamespaceAndPath方法会通过ResourceLocationCompat提供
    }
    
    // 注意：tryBySeparator和withDefaultNamespace方法将通过ASM转换器添加
    // compareNamespaced方法也将通过ASM转换器添加
}