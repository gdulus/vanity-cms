grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.7
grails.project.source.level = 1.7
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    inherits("global") {}
    log "warn"          // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true       // Whether to verify checksums on resolve
    legacyResolve false
    // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true    // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenLocal()
        mavenCentral()
        mavenRepo "http://repo.grails.org/grails/plugins"
        mavenRepo "http://repo.grails.org/grails/core"
        mavenRepo "http://repo.spring.io/milestone/"
    }

    dependencies {
        compile 'postgresql:postgresql:9.1-901.jdbc4'
        compile 'org.codehaus.gpars:gpars:1.1.0'
        compile 'org.codehaus.jsr166-mirror:jsr166y:1.7.0'
    }

    plugins {
        build ":tomcat:7.0.52.1"
        runtime ":jquery:1.8.3"
        runtime ":resources:1.1.6"
        runtime ":hibernate:3.6.10.10"
        compile ":quartz:1.0.2"
        compile ":quartz:1.0-RC5"
        compile ':cache:1.0.1'
        compile ":spring-security-core:2.0-RC2"
    }
}

grails.plugin.location.'burning-config' = '../burning-config'
grails.plugin.location.'vanity-core' = '../vanity-core'
grails.plugin.location.'vanity-search' = '../vanity-search'

