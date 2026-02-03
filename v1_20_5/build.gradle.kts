plugins {
	`my-conventions`
	id("io.papermc.paperweight.userdev")
}
repositories {
	maven("https://jitpack.io")
}

dependencies {
	implementation(project(":api"))

	compileOnly("com.github.AstralLiteratureClub:MoreForJava:1.0.2")
	compileOnly("com.github.AstralLiteratureClub:MessageManager:2.4.1")

	paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
	// paperweight.foliaDevBundle("1.21.10-R0.1-SNAPSHOT")
	// paperweight.devBundle("com.example.paperfork", "1.21.10-R0.1-SNAPSHOT")
}

tasks.withType<JavaCompile>().configureEach {
	// Override release for newer MC
	options.release = 21
}
