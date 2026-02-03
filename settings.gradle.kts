plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "guiman"

include("api")
include("core")
include("v1_17_1")
include("v1_20")
include("v1_20_5")
include("v1_21")
