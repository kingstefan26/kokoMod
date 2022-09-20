# kokoMod
this is kokomod
This site was built using [Sticks And Bones](http://x.com/).[![Gradle](https://github.com/kingstefan26/kokoMod/actions/workflows/gradle.yml/badge.svg)](https://github.com/kingstefan26/kokoMod/actions/workflows/gradle.yml)
```
this = null;
```
![This is an image](https://myoctocat.com/assets/images/base-octocat.svg)


problem:`probject:test: Could not find net.minecraftforge:forgeBin:1.8.9-11.15.1.2318-1.8.9-PROJECT(project).
Searched in the following locations:`

solution: run setupDecompWorkspace dummy


PROBLEAM: while decompworkspace `Execution failed for task ':decompileMc'. > Java heap space`
SOLUTiATION: Add `org.gradle.jvmargs=-Xmx2G` to gradle.properties in root folder of project


PROBLEMO: `Resolving dependency configuration 'runtimeOnly' is not allowed as it is defined as 'canBeResolved=false'. Instead, a resolvable ('canBeResolved=true') dependency configuration that extends 'runtimeOnly' should be resolved.`
SOLUTIONE: gen intelij runs and run via intelij little drop down saying "Minecdt Client"


PRABLEMINATO: While build `error: package org.jetbrains.annotations does not exist
import org.jetbrains.annotations.Nullable;`
SAlALUTINATO: add `compile 'org.jetbrains:annotations:16.0.2'` dependency to the build.gradle file.