Some notes about the source code

video.mov is a demo of the app

it's an android studio project it should be run with no problems with the 2.2.2 version
be careful on lombok plugin it should be configured in the Default settings for annotation processing

- lombok plugin require a particolar setup in the android studio environment.
- the api documentation has some typos, so I had to work reading the real data with a browser
- in a weather app we should read a city or the current location. I will start with preset values (Cork, IE)
then if I will have time, I will add other services for geolocation and reverse geocoding (done)
- there is an official list of cities from the API. is a downloadable file in json format.
I will put in the res/raw folder for future use (done)

things to do with more time
- more unit tests
- unit test and roboelectric/moquito for testing ui
- clean dead code
- a good class for exceptions
- progress bars
- manage current position from network or gps
- city selection with autocomplete
-the forecasts should be displayed with no repeated dates- we should read the current weather api and then compose the result with the forecast api
- add placeholders for picasso image download
- methods as getCurrentCityBackground(), getCurrentCityID() should be based on
services that retrieve Observable Objects that should be combined for deliver the desired results
- the bar color should change color based on the weather
- the api call should be cached
-we should allow to change the city selecting from a dropdown list (or better, an autocomplete textbox)
 using the city_list.json resource in raw folder (or something better from the settings)
-allow multilingual support and then implement it in the api call (pay attention to the styles and text alignment)
-icon and splashscreen
-make the city list readable from remote or use google API for reverse geocoding
- i discovered http://survivingwithandroid.github.io/WeatherLib/android_weatherlib_search_city.html
maybe I could implement it and save time
- adding a geolocation service like this one to retrieve the coordinates of the current location
https://android-arsenal.com/details/1/1321
- using this lib for current location retrivial
https://android-arsenal.com/details/1/3291
- we can use these images for background using the weather
https://halgatewood.com/docs/plugins/awesome-weather-widget/creating-different-backgrounds-for-different-weather
-we need a service for retrieve cool images for the toolbar
-the toolbar title could be not easy to read sometimes. must be fixed



