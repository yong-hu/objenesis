How to build and develop Objenesis.

# Maven #

I'm using Maven 3.0.5.

There is a settings.xml example (`settings-example.xml`) in the project root directory.

# Eclipse #

I'm using Kepler.

  1. Install the M2E plugin
  1. Instal Android Development Tools
  1. Instal Android Configurator for M2E
  1. Import the `objenesis-formatting.xml` file to get the right formatting.
  1. Import the Objenesis Maven projects.

# Build #

Use
```
mvn clean install -Pwebsite,release,android,benchmark
```
from the Objenesis root directory. This will build the following artifacts:

| **Artifact** | **Directory** | **Content** |
|:-------------|:--------------|:------------|
| objenesis-${project.version}-bin.zip | main/target | Binary, source and Javadoc |
| objenesis-tck-${project.version}-.jar | tck/target | Tck standalone jar |
| objenesis-tck-android-${project.version}-.apk | tck-android/target | Android Tck |
| objenesis-benchmark-${project.version}-.jar | benchmark/target | Performance benchmarks |
| website | website/target/xsite | Website deployed in the gh-pages branch |

## Build the Android TCK ##

Specifying the _android_ Maven profile will build the Android TCK. You need some specific configuration first:
  1. Set the `ANDROID_HOME` environment variable so that it points to the "sdk" directory in your Android SDK installation.
  1. Add `$ANDROID_HOME/tools` and `$ANDROID_HOME/platform-tools` to your `PATH` to for access to the Android command-line tools.

When the Android TCK is built, it will run during the Maven `integration-test` phase calling those commands
```
adb install -r target/objenesis-tck-android-${project.version}.apk
adb shell am instrument -w org.objenesis.tck.android/.TckInstrumentation
```

It can run on an actual device or the Android simulator.

For an actual device, just connect it via USB.

For the simulator:

  1. Run the Android SDK manager through Eclipse or by typing `android`
  1. Make sure the System Image for the version you want to test on is installed.
  1. Run the Virtual Device Manager (Tools -> AVD Manager) and click "New" to define a new virtual device
  1. Again, be sure to select the System Image you're interested in. (The choice of CPU and device really shouldn't impact the Objenesis TCK.)
  1. Make sure that your device has a little RAM and storage.
  1. Once it's created, you can start it by clicking "Start".
  1. Give it time to boot up and your good to go.

## Build the Benchmark ##

The benchmark project is using JMH which is still in a snapshot state.

So you first need to install JMH following the procedure explained [here](http://openjdk.java.net/projects/code-tools/jmh/). As soon as it's installed, your project should compile normally.

To run the benchmarks, look at the `launch.bat` file showing you how.

# Deliver a release #

Add the release notes and new acknowledgements to the website.

We use the maven release plugin for that. From a clean checkout, call
```
mvn clean release:prepare -DdryRun=true -Darguments=-Dgpg.passphrase=thephrase -Pfull,release,website,android,benchmark
```
from the Objenesis root directory. Answer the questions. You can press enter (to validate the default) for all of them. The GPG parameter is required due to a bug in the `maven-release-plugin` that prevents the GPG plugin to ask for a password interactively.

If everything goes well, do the real release (dryRun=true does a release simulation).
```
mvn clean release:prepare -Dresume=false -Darguments=-Dgpg.passphrase=thephrase -Pfull,release,website,android,benchmark
```
Give the same answers to the questions.

At this point, the tag has been created and the version upgraded in the poms.

If everything is fine, push everything to GitHub.
```
git push origin --tags
```
You are now ready to upload the artifacts to Sonatype.
```
mvn release:perform -Darguments=-Dgpg.passphrase=thephrase -Pfull,release,website,android,benchmark
```

Now just add the binaries to the [download section](http://code.google.com/p/objenesis/downloads/list) (in this order).

| **File** | **Summary** |
|:---------|:------------|
| objenesis-tck-android-2.x.apk | Objenesis 2.x Android TCK |
| objenesis-tck-2.x.jar | Objenesis 2.x TCK |
| objenesis-2.x-bin.zip | Objenesis 2.x Bundle |

Close and release the repository at [Sonatype](http://oss.sonatype.org).

Finally, check-in the website and javadoc in the gh-pages branch.

# Test #

Normal tests are done as usual through `mvn test`. To perform tests on a specific JVM, you can of course use the TCK. However, if you prefer using the Maven build, call `mvn -Pjvm-test -Dmy.jvm=$MY_JAVA_HOME/bin/java`.

# Upgrade the license #

Every year, the copyrights must be extended which means that all file headers must be updated. To perform this boring task, use to following Maven command:
```
mvn install -Plicense -Dmaven.test.skip=true -Dskip=true
```

# Checking plugins and dependencies version #
```
mvn versions:display-dependency-updates versions:display-plugin-updates -Pwebsite,android,benchmark
```