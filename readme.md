Some notes about the source code

- lombok plugin require a particolar setup in the android studio environment.
- the api documentation has some typos, so I had to work reading the real data with a browser
- in a weather app we should read a city or the current location. I will start with preset values (Cork, IE)
then if I will have time, I will add other services for geolocation and reverse geocoding
- there is an official list of cities from the API. is a downloadable file in json format.
I will put in the res/raw folder for future use


things to do with more time
- a good class for exceptions
- progress bars
- manage current position from network or gps
- city selection with autocomplete
- setting view for selection of units (imperial/metric)
- we should read the current weather api and then compose the result with the forecast
- the floating icon is used to toggle between F and C
drawables are taken from https://materialdesignicons.com/
