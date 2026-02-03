plugins {
	`my-conventions`
}

repositories {
	maven("https://jitpack.io")
}

dependencies {
	compileOnly(project(":core"))
	compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")

	compileOnly("org.projectlombok:lombok:1.18.32")

	compileOnly("com.github.AstralLiteratureClub:MoreForJava:1.0.2")
	compileOnly("com.github.AstralLiteratureClub:MessageManager:2.4.1")
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 21
}
