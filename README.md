# dagger-doc
Generate documentation for your Dagger topology

[ ![Download](https://api.bintray.com/packages/oae/maven/dagger-doc-annotation-processor/images/download.svg) ](https://bintray.com/oae/maven/dagger-doc-annotation-processor/_latestVersion)

## News

- 2018/10/25 - 0.2 Release: @ModuleDoc annotation with category/layer
- 2018/10/12 - 0.1 Release: Show simple component/module relations

## Usage

### Gradle

- Define a new configuration named 'daggerDoc':
```
configurations {
    daggerDoc
}
```
- Setup the required dependencies:
```
dependencies {
    daggerDoc 'io.morethan.daggerdoc:dagger-doc-annotation-processor:0.2'
}
```
- Add the daggerDoc task:
```
TBD..
```

### Gradle KTS

- Define a new configuration named 'daggerDoc':
```
val daggerDoc by configurations.creating
```
- Setup the required dependencies:
```
dependencies {
    daggerDoc(group = "io.morethan.daggerdoc", name = "dagger-doc-annotation-processor", version = "0.2")
}
```
- Add the daggerDoc task:
```
task<JavaCompile>("daggerDoc") {
    source = sourceSets["main"].java
    classpath = sourceSets["main"].runtimeClasspath + configurations.annotationProcessor + configurations["daggerDoc"]
    options.compilerArgs.addAll(arrayOf("-proc:only", "-processor", "io.morethan.daggerdoc.DaggerDocProcessor"))
    destinationDir = file("build/doc")
}
```


## Project Build

### Release Project

- Tell gradle about the gpg key and sonatype/bintray/signing credentials, e.g. through ~/.gradle/gradle.properties
  - sonatypeUsername=$yourSonatypeUser
  - sonatypePassword=$yourSonatypePassword
  - bintrayUser=$yourBintrayUser
  - bintrayKey=$yourBintrayKey
  - signing.keyId=$yourKeyId
  - signing.password=$yourKeyPassphrase
  - signing.secretKeyRingFile=/Users/$username/.gnupg/secring.gpg

- Increase version in build.properties to the release version and commit
- Upload to Bintray repository: `./gradlew bintrayUpload`


## Resources

- Android clean architecture example: https://github.com/Teamwork/android-clean-architecture
- https://github.com/cjstehno/dependency-checker/blob/master/src/main/groovy/com/stehno/gradle/depchecker/CheckDependenciesTask.groovy