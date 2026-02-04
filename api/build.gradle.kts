plugins {
  `my-conventions`
}

repositories {
	maven("https://jitpack.io")
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 21
}
