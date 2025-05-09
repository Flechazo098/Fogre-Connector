# Forge-Connector Mod

## 目的

Forge-Connector 是一个 Minecraft Mod，旨在解决 Forge 不同版本之间 API 不兼容的问题，目前只针对 Forge 47.3.0 和 Forge 47.4.0 版本之间的差异。

其功能是允许为较新 Forge 版本（例如 47.4.0）开发的 Mod 能够在较旧的 Forge 版本（例如 47.3.0）上运行。

## 实现方式

该 Mod 主要通过以下技术组合来实现其兼容性目标：

1.  **兼容层**:
    *   包含一个核心的兼容逻辑类，它能够检测当前运行的 Forge 版本。
    *   **对于新版 Forge (47.3.0+)**: 通过Java反射机制直接调用原生的、更新后的方法。
    *   **对于旧版 Forge (47.3.0)**: 提供缺失或行为不一致的 API 的兼容实现。

2.  **Mixin**:
    *   利用 Mixin 技术在运行时修改原版 Minecraft 或 Forge 的代码。
    *   例如，它会拦截对旧版方法的调用，并将其重定向到兼容层中实现的逻辑。

3.  **CoreMod 与 ASM 字节码转换**:
    *   注册一个 CoreMod，使其能够在 Minecraft 启动的早期阶段介入类加载过程。
    *   使用 ASM (Abstract Syntax Tree Manipulation) 库来动态修改字节码。
    *   ASM 转换器会检查目标类是否缺少新版 API。如果缺少，它会动态地向类中添加这些方法。这些动态添加的方法的内部实现会调用兼容层中对应的兼容方法。

4.  **主 Mod 类**:
    *   除初始化外还提供了可选的静态桥接类 ，允许其他代码在需要时直接、显式地调用兼容层提供的方法。

## 当前实现的兼容性功能

1. **ResourceLocation API 兼容**:
   * 为低版本 Forge 添加了高版本的 ResourceLocation API 方法，如 tryBySeparator、withDefaultNamespace 等。

2. **模组构造函数兼容**:
   * 支持 Forge 47.3.10 引入的带 FMLJavaModLoadingContext 参数的模组构造函数。
   * 自动为带参数构造函数的模组类创建无参构造函数，使其能在低版本 Forge 上正常工作。

## 总结

Forge-Connector 通过在旧版 Forge 环境中模拟实现新版 Forge 更改的 API，有效地弥合了不同 Forge 版本之间的 API 差异。这使得依赖这些较新 API 的 Mod 能够向后兼容，在旧版 Forge 环境中也能正常工作。
