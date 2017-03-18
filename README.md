# LuxAndroidPlugin
A gradle plugin for building Android apps with Lux

## Usage
The following should compile your lux code and install your app on a connected device.
```
gradle installDebug
```
See example/build.gradle for an example build file. 

When using in an Android app, make sure both the LuxGradlePlugin and this plugin (build it!) is in a directory that is specified in the `flatDir` closure in your app's __build.gradle__ file.

## Example 
#### Coming soon

## Building
```
gradle build
```

Make sure that the LuxGradlePlugin is in the current directory prior to building. This is only necessary until I place the plugin in a central repository. The LuxGradlePlugin can be found [here].

[here]: https://github.com/xran-deex/LuxGradlePlugin 
