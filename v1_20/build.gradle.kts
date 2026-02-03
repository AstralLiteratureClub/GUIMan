plugins {
  `my-conventions`
  id("io.papermc.paperweight.userdev")
}

dependencies {
  implementation(project(":api"))

  paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
}
tasks.withType<JavaCompile>().configureEach {
	// Override release for newer MC
	options.release = 21
}
