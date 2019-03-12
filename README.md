# Media-Style-Palette

The library uses system methods to get MediaStyle notification colors from Android Oreo.

![Alt Text](https://github.com/mkaflowski/Media-Style-Palette/blob/master/images/demo.gif?raw=true)

## Installation

Add JitPack in your root build.gradle at the end of repositories:
```
    allprojects {
        repositories {
            jcenter()
            maven { url "https://jitpack.io" }
        }
    }
```

Add it as a dependency in your app's build.gradle file:
```
    dependencies {
        compile 'com.github.mkaflowski:Media-Style-Palette:1.x' //CHANGE X TO CURRENT VERSION!!!
    }
```
	
## How to use

```java
MediaNotificationProcessor processor = new MediaNotificationProcessor(this, bitmap); // can use drawable
	
int backgroundColor = processor.getBackgroundColor();
int primaryTextColor = processor.getPrimaryTextColor();
int secondaryTextColor = processor.getSecondaryTextColor();
	
boolean isLight = processor.isLight();

// for async processing:
final MediaNotificationProcessor processor = new MediaNotificationProcessor(this);
processor.getPaletteAsync(onPaletteLoadedListener, bitmap);
```
