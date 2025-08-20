// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

plugins {
  id("java")
  id("org.jetbrains.intellij.platform") version "2.6.0"
}

group = "go-playground"
version = "1.0.0"

repositories {
  mavenCentral()

  intellijPlatform {
    defaultRepositories()
  }
}

dependencies {
  //implementation("com.github.jsqlparser", "jsqlparser", "5.3")
  intellijPlatform {
    //intellijIdeaCommunity("2024.2.6")
    //pycharmCommunity("2024.2.6")
    goland("2024.1.1")
    //datagrip("2025.1.3")
    bundledPlugin("org.jetbrains.plugins.go")
  }
}

intellijPlatform {
  buildSearchableOptions = false

  pluginConfiguration {
    ideaVersion {
      sinceBuild = "241"
    }
  }
  pluginVerification  {
    ides {
      recommended()
    }
  }
}
