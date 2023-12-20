<div align="center">

# Resourceful Config

</div>

---

To add this library to your project, do the following:

```groovy
repositories {
  maven {
    // Location of the maven that hosts Team Resourceful's jars.
    name = "Resourceful Bees Maven"
    url = "https://nexus.resourcefulbees.com/repository/maven-public/"
  }
}
```

In an Architectury project, you would implement it like so:

Common
```groovy
dependencies {
  modImplementation "com.teamresourceful.resourcefulconfig:resourcefulconfig-common-1.20.2:2.2.2"
}
```

Fabric
```groovy
dependencies {
  modImplementation "com.teamresourceful.resourcefulconfig:resourcefulconfig-fabric-1.20.2:2.2.2"
}
```

Forge
```groovy
dependencies {
  modImplementation "com.teamresourceful.resourcefulconfig:resourcefulconfig-forge-1.20.2:2.2.2"
}
```

<div align="center">

<h3>You can either Jar-in-Jar this or depend on it on Curseforge or Modrinth.</h3>

<a href="https://www.curseforge.com/minecraft/mc-mods/resourceful-config"><img width=100 src="https://cdn.apexminecrafthosting.com/img/uploads/2021/05/21163117/curseforge-logo.png"/></a>
<a href="https://modrinth.com/mod/resourceful-config"><img width=100 src="https://github.com/modrinth/art/blob/main/Branding/Mark/mark-light__400x400.png"/></a>

</div>

---
