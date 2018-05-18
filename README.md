# Media-Style-Palette
The library uses system methods to get MediaStyle notification colors from Android Oreo.

## Installation

To use the library, first include it your project using Gradle

    allprojects {
        repositories {
            jcenter()
            maven { url "https://jitpack.io" }
        }
    }

	dependencies {
	        compile 'com.github.mkaflowski:Media-Style-Palette:1.x'
	}
	
## How to use

```java
        MediaNotificationProcessor processor = new MediaNotificationProcessor(this, bitmap); // can use drawable
	
	int backgroundColor = processor.getBackgroundColor();
        int primaryTextColor = processor.getPrimaryTextColor();
        int secondaryTextColor = processor.getSecondaryTextColor();
	
	boolean isLight = processor.isLight();
```
