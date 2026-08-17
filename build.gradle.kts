buildscript {
    configurations.all {
        resolutionStrategy {
            force("org.apache.commons:commons-lang3:3.20.0")
            force("org.bitbucket.b_c:jose4j:0.9.6")
            force("org.bouncycastle:bcpkix-jdk18on:1.85")
            force("org.bouncycastle:bcprov-jdk18on:1.85.2")
            force("io.netty:netty-all:4.2.17.Final")
            force("io.netty:netty-common:4.2.17.Final")
            force("io.netty:netty-handler:4.2.17.Final")
            force("io.netty:netty-transport:4.2.17.Final")
            force("io.netty:netty-buffer:4.2.17.Final")
            force("io.netty:netty-codec:4.2.17.Final")
            force("io.netty:netty-resolver:4.2.17.Final")
        }
    }
    dependencies {
        // Override plugin transitive dependencies to resolve Dependabot alerts
        classpath("org.apache.commons:commons-lang3:3.20.0")
        classpath("org.bitbucket.b_c:jose4j:0.9.6")
        classpath("org.bouncycastle:bcpkix-jdk18on:1.85")
        classpath("org.bouncycastle:bcprov-jdk18on:1.85.2")
        classpath("io.netty:netty-all:4.2.17.Final")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.dagger.hilt) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
}