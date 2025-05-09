# Forge-Connector Mod

## Purpose

Forge-Connector is a Minecraft mod designed to solve API incompatibility issues between different versions of Forge, currently targeting only the differences between Forge 47.3.0 and Forge 47.4.0.

Its function is to allow mods developed for newer Forge versions (such as 47.4.0) to run on older Forge versions (such as 47.3.0).

## Implementation

This mod mainly achieves its compatibility goals through the following technical combination:

1.  **Compatibility Layer**:
    *   Contains a core compatibility logic class that can detect the currently running Forge version.
    *   **For newer Forge (47.3.0+)**: Directly calls the native, updated methods via Java reflection.
    *   **For older Forge (47.3.0)**: Provides compatible implementations for missing or inconsistent APIs.

2.  **Mixin**:
    *   Uses Mixin technology to modify the code of vanilla Minecraft or Forge at runtime.
    *   For example, it intercepts calls to old methods and redirects them to the logic implemented in the compatibility layer.

3.  **CoreMod and ASM Bytecode Transformation**:
    *   Registers a CoreMod so that it can intervene in the class loading process at an early stage of Minecraft startup.
    *   Uses the ASM (Abstract Syntax Tree Manipulation) library to dynamically modify bytecode.
    *   The ASM transformer checks whether the target class lacks the new API. If missing, it dynamically adds these methods to the class. The internal implementation of these dynamically added methods will call the corresponding compatibility methods in the compatibility layer.

4.  **Main Mod Class**:
    *   In addition to initialization, it also provides an optional static bridge class, allowing other code to directly and explicitly call the methods provided by the compatibility layer when needed.

## Currently Implemented Compatibility Features

1. **ResourceLocation API Compatibility**:
    * Adds high-version ResourceLocation API methods such as tryBySeparator and withDefaultNamespace to lower-version Forge.

2. **Mod Constructor Compatibility**:
    * Supports mod constructors with FMLJavaModLoadingContext parameter introduced in Forge 47.3.10.
    * Automatically creates a no-argument constructor for mod classes with parameterized constructors, allowing them to work properly on lower-version Forge.

## Summary

Forge-Connector effectively bridges the API differences between different Forge versions by simulating the changes of the new Forge API in the old Forge environment. This enables mods that depend on these newer APIs to be backward compatible and work properly in older Forge environments.