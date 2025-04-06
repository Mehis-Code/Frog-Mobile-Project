# FrogRadar. - Location-based Frog Sighting visualizer

FrogRadar uses the user's location to find frog sightings close to the user, or a chosen location. It can be used to see whether frogs have been spotted within an area, and their species. 

- The app is built using Android Studio, and utilizing Jetpack Compose <img src="https://developer.android.com/static/images/spot-icons/jetpack-compose.svg" height="28" /> and Kotlin <img src="https://download.logo.wine/logo/Kotlin_(programming_language)/Kotlin_(programming_language)-Logo.wine.png" height="28" marginLeft="5" />

- Frog sighting data is open information sourced from https://laji.fi/ under Creative Commons 4.0 license https://creativecommons.org/licenses/by/4.0/. Edits to the data include transforming the data to Json format for use in my project and removing unnecessary fields for my project. Data is displayed based either on the user location in Track mode, or input in Input mode. The app utilizes geocoding to figure out the user's closest location, or to find the coordinates for a location input. The coordinates are then compared to the frog sighting data, and filtered within around 0.09 of the coordinates (around ~10km, not an entirely accurate conversion, but good enough for this use case)

- A bar chart visualizer utilizing VicoChart displays the ratio of frogs sightings in the area. 
<p align="center">
<img src="https://github.com/user-attachments/assets/2ae93532-61c8-4006-a059-973196a9581a" width="300" height="560">
</p>

## The frogs.
<p align="center">
  <img src="https://github.com/user-attachments/assets/8fdc3ba8-cddc-4321-ae7e-19190424a2ec" width="100" />
  <img src="https://github.com/user-attachments/assets/3b9ad881-e29c-4db9-97a0-e4ea6a6b58af" width="100" /> 
  <img src="https://github.com/user-attachments/assets/ea10c593-51fe-423a-9207-acfdaf94d2e6" width="100" />
</p>
<p align="center">
  <b>Bufo Bufo, Rana Arvalis, Rana Temporaria</b>
</p>

Three frogs native to Finland are found in the app data. The custom assets including the app logo were built by me. I tried to stay faithful to the frog appearances but balance visual clarity.

## Acknowledge / limitations

- This was my first Android app, and some solutions could have been more optimal. My next app will have a better architecture, and UI.
- Only Finland was available for the data. This was because I could not find a good API to use for my vision. The best solution for this project's scale was to utilize Laji.fi's data as a Json download. It holds 11000 entries, which was a decent foundation, but is limited to Finland 🇫🇮. With these restrictions, I decided to narrow the scope.


