我要写一个类似Sinytra Connector（Sinytra Connector 是一个基于 Forge/NeoForge 的模组，旨在作为一个转译/兼容层来让 Forge/NeoForge 能够运行 Fabric 的模组，从而拉近两个平台间的距离，节省模组开发者多平台维护的时间和精力，并让玩家可以在一个整合包中畅玩所有模组。）的模组让不同forge版本的模组共存，它基于forge47.3.0/minecraftt1.20.1开发。
以下是forge47.3.0-47.4.0的更新日志：
47.4
====
 - 47.4.0 1.20.1 RB 4
          https://forums.minecraftforge.net/topic/154387-forge-474-minecraft-1201/

47.3
====
 - 47.3.39 Fix ForgeDev's test runs not working due to dead test mod (#10483)
 - 47.3.38 Cache this.useItem before running item break logic, Fixes #10344 (#10376)
 - 47.3.37 Speed up mod annotation scanning by ~30% (#10470)
           Co-authored-by: LexManos <LexManos@gmail.com>
 - 47.3.36 Add missed license headers (#10479)
 - 47.3.35 Add '#forge:chorus_additionally_grows_on' tag for similar mechanics to '#minecraft:azalea_grows_on' but for chorus (#10456)
 - 47.3.34 Fix cancelling ProjectileImpactEvent still firing onBlockHit (#10481)
 - 47.3.33 Honor attacker shield disabling status (#10321)
 - 47.3.32 Add fast graphics render type to block model jsons (#10393)
           Make modded leaves behave like vanilla leaves by default (Fixes #10389)
 - 47.3.31 Fix invalidly symlinked worlds crashing on level select (#10439)
 - 47.3.30 Backport even more future ResourceLocation methods (#10428)
 - 47.3.29 Ensure NetworkConstants is loaded before mod construction (#10407)
 - 47.3.28 Account for problematic mixins in VillagerTrades.EmeraldsForVillagerTypeItem (#10402)
 - 47.3.27 Fix incorrect method reference in TntBlock.explode()
 - 47.3.26 Fix issues in VillagerTrades.EmeraldsForVillagerTypeItem related to custom Villager Types (#10315)
           Add VillagerType#registerBiomeType
 - 47.3.25 Add clientSideOnly feature to mods.toml (#10085) (backport of #9804 to 1.20.1)
           Co-authored-by: Jonathing <me@jonathing.me>
 - 47.3.24 Fix non-passengers being tickable without checking canUpdate() (#10304)
 - 47.3.23 Fix finalizeSpawn's return value not being used correctly (#10301)
 - 47.3.22 Bump CoreMods to 5.2.4 (#10263)
 - 47.3.21 Allow mipmap lowering to be disabled (#10252)
 - 47.3.20 Add optional fix of use item duration, disabled by default (#10246)
 - 47.3.19 Backport some Vanilla 1.21 ResourceLocation methods (#10241)
           Co-authored-by: Paint_Ninja <PaintNinja@users.noreply.github.com>
 - 47.3.18 Simplify memory usage display on loading screen (#10233)
           Co-authored-by: Paint_Ninja <PaintNinja@users.noreply.github.com>
 - 47.3.17 Deprecate @ObjectHolder, add a couple of fast-paths (#10228)
           Co-authored-by: Paint_Ninja <PaintNinja@users.noreply.github.com>
 - 47.3.16 Skip Vanilla classes for the CapabilityTokenSubclass transformer (#10221)
           Co-authored-by: Paint_Ninja <PaintNinja@users.noreply.github.com>
 - 47.3.15 Skip Forge classes in the RuntimeEnumExtender transformer (#10216)
           Mod classes are still transformed as usual
           Co-authored-by: Paint_Ninja <PaintNinja@users.noreply.github.com>
 - 47.3.14 Skip processing Forge classes in RuntimeDistCleaner (#10208)
           Co-authored-by: Paint_Ninja <PaintNinja@users.noreply.github.com>
 - 47.3.13 Disable clean on TeamCity (#10258)
 - 47.3.12 Bump CoreMods to 5.2 (#10130)
           Full Changelog:
           https://gist.github.com/Jonathing/c3ad28b2a048ac839a7baba5417ee870
           The key features are:
           - ES6 language support
           - Thoroughly updated ASMAPI, with full documentation
           - Bug fixes (some optional for backwards-compatibility)
           - Partial internal code cleanup
           - Request CoreMods to not apply fix for ASMAPI.findFirstInstructionBefore by default
           - Updated ASM to 9.7.1
           - Updated Nashorn to 15.4
 - 47.3.11 Remove unneeded boat patch (backport of #10061 to 1.20.1) (#10096)
           Co-authored-by: andan42 <49289986+andan42@users.noreply.github.com>
 - 47.3.10 Optionally supply FMLJavaModLoadingContext as a param to mod constructors (backport of #10074 to 1.20.1) (#10100)
           Co-authored-by: RealMangoRage <andrew333awesome@outlook.com>
 - 47.3.9  Minor cleanup to ModListScreen and VersionChecker (backport of #9988 to 1.20.1) (#10095)
 - 47.3.8  Cleanup FML Bindings (backport of #10004 to 1.20.1) (#10094)
 - 47.3.7  Early display fixes/workarounds for buggy drivers. Backport of #9921 to 1.20.1 (#10073)
 - 47.3.6  Add a way to render tooltips from Formatted text and TooltipComponents elements (#10055)
           Backport of #10056 for 1.20.1
 - 47.3.5  Make HangingSignBlockEntity useable with custom BlockEntityTypes. #10038
 - 47.3.4  Unlock wrapped registries when firing register events. (#10035)
           Co-authored-by: LexManos <LexManos@gmail.com>
 - 47.3.3  Choose default JarJar mod file type based on parent JAR (#10023)
           Co-authored-by: thedarkcolour <30441001+thedarkcolour@users.noreply.github.com>
 - 47.3.2  Fixed falling block entities not rendering as moving blocks (#10006) (#10018)
           Co-authored-by: Ven <tudurap.com@gmail.com>
 - 47.3.1  Fix boat travel distance being incorrect. Closes #9997 #9999
 - 47.3.0  1.20.1 RB 3
           https://forums.minecraftforge.net/topic/139825-forge-473-minecraft-1201/