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
  modImplementation "com.teamresourceful.resourcefulconfig:resourcefulconfig-common-{minecraft_version}:{resourceful_config_version}"
}
```

Fabric
```groovy
dependencies {
  modImplementation "com.teamresourceful.resourcefulconfig:resourcefulconfig-fabric-{minecraft_version}:{resourceful_config_version}"
}
```

Forge
```groovy
dependencies {
  modImplementation "com.teamresourceful.resourcefulconfig:resourcefulconfig-forge-{minecraft_version}:{resourceful_config_version}"
}
```

<div align="center">

<h3>You can either Jar-in-Jar this or depend on it on Curseforge or Modrinth.</h3>

<a href="https://www.curseforge.com/minecraft/mc-mods/resourceful-config"><img width=100 src="https://cdn.apexminecrafthosting.com/img/uploads/2021/05/21163117/curseforge-logo.png"/></a>
<a href="https://modrinth.com/mod/resourceful-config"><img width=100 src="https://github.com/modrinth/art/blob/main/Branding/Mark/mark-light__400x400.png"/></a>

</div>

---

<div align="center">

![Common](https://img.shields.io/maven-metadata/v?label=Common%20Version&metadataUrl=https%3A%2F%2Fnexus.resourcefulbees.com%2Frepository%2Fmaven-public%2Fcom%2Fteamresourceful%2Fresourcefulconfig%2Fresourcefulconfig-common-1.19.2%2Fmaven-metadata.xml)
&nbsp;&nbsp;&nbsp;&nbsp;
![Fabric](https://img.shields.io/maven-metadata/v?label=Fabric%20Version&metadataUrl=https%3A%2F%2Fnexus.resourcefulbees.com%2Frepository%2Fmaven-public%2Fcom%2Fteamresourceful%2Fresourcefulconfig%2Fresourcefulconfig-fabric-1.19.2%2Fmaven-metadata.xml)
&nbsp;&nbsp;&nbsp;&nbsp;
![Forge](https://img.shields.io/maven-metadata/v?label=Forge%20Version&metadataUrl=https%3A%2F%2Fnexus.resourcefulbees.com%2Frepository%2Fmaven-public%2Fcom%2Fteamresourceful%2Fresourcefulconfig%2Fresourcefulconfig-forge-1.19.2%2Fmaven-metadata.xml)

</div>

---
