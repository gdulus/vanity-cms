grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

// uncomment (and adjust settings) to fork the JVM to isolate classpaths
//grails.project.fork = [
//   run: [maxMemory:1024, minMemory:64, debug:false, maxPerm:256]
//]

grails.project.dependency.resolution = {
    inherits("global") {}
    log "error"          // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true       // Whether to verify checksums on resolve
    legacyResolve false  // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true    // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    dependencies {
    }

    plugins {
        runtime ":jquery:1.8.3"
        runtime ":resources:1.1.6"
        runtime ":database-migration:1.2.1"
        build ":tomcat:$grailsVersion"
        compile ':cache:1.0.1'
        compile ":hibernate:2.2.1"
    }
}

grails.plugin.location.'vanity-core' = '../vanity-core'

