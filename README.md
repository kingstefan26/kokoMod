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
EXPLANATION: if you do code fiddling bad things happen

PROBLEM: while decompworkspace `Execution failed for task ':decompileMc'. > Java heap space`
SOLUTION: Add `org.gradle.jvmargs=-Xmx2G` to gradle.properties in root folder of project
EXPLANATION: java just likes ram ok

PROBLEM: while runClient `Resolving dependency configuration 'runtimeOnly' is not allowed as it is defined as 'canBeResolved=false'. Instead, a resolvable ('canBeResolved=true') dependency configuration that extends 'runtimeOnly' should be resolved.`
SOLUTION: gen IntelliJ runs and run via IntelliJ little drop down saying "Minecraft Client"
EXPLANATION: haven't figured this one yet, but I assume 2015 projects trying to run in 2022 ide 's

PARABOLA: While build `error: package org.jetbrains.annotations does not exist
import org.jetbrains.annotations.Nullable;`
SAlALUTINATO: add `compile 'org.jetbrains:annotations:16.0.2'` dependency to the build.gradle file.
EXPLANATION: you cant use a library which you don't have


PROBLEM: while decompworkspace`Execution failed for task ':fixMcSources'.> com.cloudbees.diff.PatchException: Cannot find hunk target`
SOLUTION: use java 8, run cleanCache gradle cache then: settings (alt+ctrl+s) | Build, Execution, Deployment | Build Tools | Jradle JVM and set that to java 8
EXPLANATION: com.cloudbees just doesn't like >java 8  you know

PROBLEM: when running client from IntelliJ run `Exception in thread "main" java.lang.NoClassDefFoundError: com/mojang/authlib/exceptions/AuthenticationException`
SOLUTION: to into the gradle tool window on right side and click that "Reload All gradle projects" at the top left
EXPLANATION: for some reason sometimes half of the libs are missing 