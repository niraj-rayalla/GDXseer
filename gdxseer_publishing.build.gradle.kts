apply<MavenPublishPlugin>()
apply<SigningPlugin>()

val libraryVersion: String by project

configure<JavaPluginExtension> {
    withJavadocJar()
    withSourcesJar()
}

configure<JavaPluginExtension> {
    withJavadocJar()
    withSourcesJar()
}

configure<PublishingExtension> {
    publications {
        this.create(project.name, MavenPublication::class.java) {
            groupId = "io.github.niraj-rayalla"
            artifactId = project.name
            version = libraryVersion

            from(project.components["java"])
            pom {
                name.set(project.name)
                this.description.set("The base GDXseer library which allows for Effekseer to be used in libGDX projects. This is used by all specific implementations for each platform/renderer.")
                url.set("https://github.com/niraj-rayalla/GDXseer")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("niraj-rayalla")
                        name.set("Niraj Rayalla")
                        email.set("mirage00055@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/niraj-rayalla/GDXseer.git")
                    developerConnection.set("scm:git:git@github.com:niraj-rayalla/GDXseer.git")
                    url.set("https://github.com/niraj-rayalla/GDXseer")
                }
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("GDXSEER_OSSRH_USER") ?: return@credentials
                password = System.getenv("GDXSEER_OSSRH_PASSWORD") ?: return@credentials
            }
        }
    }
}

configure<SigningExtension> {
    val key = System.getenv("MIRAGE_SIGNING_KEY")
    val password = System.getenv("MIRAGE_SIGNING_PASSWORD")

    useInMemoryPgpKeys(key, password)
    val publishing = (project as org.gradle.api.plugins.ExtensionAware).extensions.getByName("publishing") as org.gradle.api.publish.PublishingExtension
    sign(publishing.publications)
}