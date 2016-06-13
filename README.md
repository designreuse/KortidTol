# Kortið tól
[![Release](https://jitpack.io/v/fiskurgit/KortidTol.svg)](https://jitpack.io/#fiskurgit/KortidTol) [![Build Status](https://travis-ci.org/fiskurgit/KortidTol.svg?branch=master)](https://travis-ci.org/fiskurgit/KortidTol) [![license](https://img.shields.io/github/license/mashape/apistatus.svg?maxAge=2592000)](https://github.com/fiskurgit/KortidTol/blob/master/LICENSE) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/a9f791f2fba04ae5905064680f5804a0)](https://www.codacy.com/app/fiskur/KortidTol?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=fiskurgit/KortidTol&amp;utm_campaign=Badge_Grade)

Map tools for GoogleMaps on Android, developed for the [Yorkshire 3 Peaks app](https://play.google.com/store/apps/details?id=eu.fiskur.yorkshirethreepeaks). Google translates 'map tool' into Icelandic as Kortið tól, used because I occasionally daydream about moving to Reykjavík.

##Dependency

Add jitpack.io to your root build.gradle, eg:

```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

then add the dependency to your project build.gradle:

```groovy
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.github.fiskurgit:KortidTol:1.0.1'
}
```
You can find the latest version in the releases tab above: https://github.com/fiskurgit/KortidTol/releases

More options at jitpack.io: https://jitpack.io/#fiskurgit/KortidTol

##Licence

Full licence here: https://github.com/fiskurgit/KortidTol/blob/master/LICENSE

In short:

> The MIT License is a permissive license that is short and to the point. It lets people do anything they want with your code as long as they provide attribution back to you and don’t hold you liable.

##Limit Bounds

Limit the map to displaying a specific area, prevents the user scrolling away, useful if your app is local/location specific:

```java
map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
  @Override public void onCameraChange(CameraPosition cameraPosition) {
    MapTools.limitBounds(map, cameraPosition, threePeaksBounds, MIN_ZOOM);
  }
});
```

##Create Bounds

Either provide four outlier coordinates or an array of LatLng pairs and return a bounding box LatLngBounds object:

```java
LatLngBounds threePeaksBounds = MapTools.createBounds(MIN_LAT, MAX_LAT, MIN_LON, MAX_LON);

//or

LatLng[] multipleCoords = ...
LatLngBounds threePeaksBounds = MapTools.createBounds(multipleCoords);
```

##Nearest Index

Find the nearest index in an array of coordinates to a point, for example when a user long-clicks on a map near a route, find the nearest point on the route:

```java
map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
  @Override public void onMapLongClick(LatLng latLng) {
    int nearestIndex = MapTools.nearestIndex(routeCoords, latLng);
    LatLng nearestRoutePoint = routeCoords.get(nearestIndex);
  }
});
```

##Long Click Helper and Draw Subsection

Manage long-click events with a map to help draw a route subsection.

```java
LongClickHelper longClickHelper = new LongClickHelper();

...

map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
  @Override public void onMapLongClick(LatLng latLng) {
    vibrator.vibrate(175);
    longClickHelper.addPoint(latLng);
    if(longClickHelper.isReady()){
      LatLng startClick = longClickHelper.getStart();
      LatLng endClick = longClickHelper.getEnd();
      MapTools.drawSubsection(map, routeCoords, startClick, endClick, Color.parseColor("#ff00cc"));
    }
  }
});
```

##Subsection Distance

Calculate the distance of a subsection of a route

```java
float meters = MapTools.subsectionDistance(routeCoords, startCoord, endCoord);
```
