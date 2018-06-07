
# react-native-screen-recorder

## Getting started

`$ npm install react-native-screen-recorder --save`

### Mostly automatic installation

`$ react-native link react-native-screen-recorder`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-screen-recorder` and add `RNScreenRecorder.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNScreenRecorder.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNScreenRecorderPackage;` to the imports at the top of the file
  - Add `new RNScreenRecorderPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-screen-recorder'
  	project(':react-native-screen-recorder').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-screen-recorder/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-screen-recorder')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNScreenRecorder.sln` in `node_modules/react-native-screen-recorder/windows/RNScreenRecorder.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Screen.Recorder.RNScreenRecorder;` to the usings at the top of the file
  - Add `new RNScreenRecorderPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNScreenRecorder from 'react-native-screen-recorder';

// TODO: What to do with the module?
RNScreenRecorder;
```
  