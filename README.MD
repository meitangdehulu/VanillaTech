# Vanilla Tech
This mod adds missing pieces to Minecraft.

# How to add to your development environment
Include following code into your build.gradle:

>repositories {
>		maven {
>				name = "APengu Maven"
>				url = "https://raw.githubusercontent.com/APengu/PenguLibs/gh-pages"
>		}
>}
>
>dependencies {
>		deobfCompile "apengu:SolarFluxReborn:MCVERSION-VTVERSION:deobf"
>}

Where "MCVERSION" is Minecraft Version and "VTVERSION" is Vanilla Tech Version
