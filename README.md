# Kortið tól
[![Release](https://jitpack.io/v/fiskurgit/KortidTol.svg)](https://jitpack.io/#fiskurgit/KortidTol) [![Build Status](https://travis-ci.org/fiskurgit/KortidTol.svg?branch=master)](https://travis-ci.org/fiskurgit/KortidTol) [![license](https://img.shields.io/github/license/mashape/apistatus.svg?maxAge=2592000)](https://github.com/fiskurgit/KortidTol/blob/master/LICENSE)

Map tools for GoogleMaps on Android, developer for the [Yorkshire 3 Peaks app](https://play.google.com/store/apps/details?id=eu.fiskur.yorkshirethreepeaks). Google translates 'map tool' into Icelandic as Kortið tól, used because I occasionally daydream about moving to Reykjavík.

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
