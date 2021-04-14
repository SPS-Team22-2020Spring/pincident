// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */

const mymap = L.map('mapid').setView([0, 0], 2)
var GmapsID;
var placeInternal;

L.tileLayer(
    'https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}',
    {
      attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
      //maxZoom: 18,
      id: 'mapbox/streets-v11',
      tileSize: 512,
      zoomOffset: -1,
      accessToken: 'pk.eyJ1IjoidmFsZW50aW5hc3BzIiwiYSI6ImNrbXFseXo5OTBpaXQycHQ0b2diYWZka2kifQ.Z5CYmHqc3fzrf8z-wxZYrg'
    }).addTo(mymap)

var markers = L.markerClusterGroup();
// This example adds a search box to a map, using the Google Place Autocomplete
// feature. People can enter geographical searches. The search box will return a
// pick list containing a mix of places and predicted search terms.
// This example requires the Places library. Include the libraries=places
// parameter when you first load the API. For example:
// <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places">
function initAutocomplete() {
  // Create the search box and link it to the UI element.
  const input = document.getElementById("searchinput");
  //const searchBox = new google.maps.places.SearchBox(input);
  const autocomplete = new google.maps.places.Autocomplete(input);
  let tempMarker;
  // Listen for the event fired when the user selects a prediction and retrieve
  // more details for that place.
  autocomplete.addListener('place_changed', function () {
    let place = autocomplete.getPlace();
    placeInternal = autocomplete.getPlace();
    GmapsID = place.place_id;
    if (!place.geometry) {
      console.log("Returned place contains no geometry");
      return;
    }

    if (tempMarker) {
      mymap.removeLayer(tempMarker);
    }
    //if map has geometry fly to it
    let location = [place.geometry.location.lat(),
      place.geometry.location.lng()];
    mymap.flyTo(location, 12);
    tempMarker = L.marker(location).addTo(mymap);

  });
}

function submitForm() {
  let formName = request.getParameter("searchinput");
  let incidentType = request.getParameter("select-type-incident");
  let comment = request.getParameter("textarea");
}

async function savePlaceData(place) {

  const params = new URLSearchParams();
  params.append('longitude', place.geometry.location.lng());
  params.append('latitude', place.geometry.location.lat());
  params.append('visualidentifier', place.name);
  params.append('gmapsid', place.place_id);

  const response = await fetch('/Cl', {method: 'POST', body: params})
  if (response.status === 200) {
    return response.text();
  } else {
    throw 'Error adding location';
  }
}

async function saveIncidentData(locationID, typeReport, note) {

  const params = new URLSearchParams();
  params.append('locationID', locationID);
  params.append('typeReports', typeReport);
  params.append('note', note);

  const response = await fetch('/Cr', {method: 'POST', body: params})
  if (response.status === 200) {
    return response.text();
  } else {
    console.log(response.statusText);
    throw 'Error adding incident';

  }
}

$("#filter").submit(function (e) {
  e.preventDefault();
  $.ajax({
    url: 'ls',
    type: 'POST',
    data: $("#filter").serialize(),
    success: function (d) {
      markers.clearLayers(); //This line is to delete all the past markers
      putLocations(d);
    }
  })
});

async function returnIncidentsWhere(id) {
  const params = new URLSearchParams();
  params.append('id', id);
  await fetch('/Ris', {method: 'POST', body: params})
      .then(res => res.text())
      .then(body => {
        try {
          addIncidentsToModal(JSON.parse(body));
          // console.log(JSON.parse(body));
        } catch {
          throw Error(body);
        }
      })
}

async function returnAllLocations() {
  const params = new URLSearchParams();
  params.append('incidents', "all");
  await fetch('/ls', {method: 'POST', body: params})
      .then(res => res.text())
      .then(body => {
        try {
          putLocations(JSON.parse(body));
        } catch {
          throw Error(body);
        }
      })
}

function putLocations(data) {
  var locations = [];
  var locationIDs = [];
  var gmapsIDs = [];
  var visualidentifiers = [];
  var incidentmaps = [];
  for (var i = 0; i < data.length; i++) {
    var coords = [];
    for (const property in data[i]) {
      switch (property) {
        case "locationID":
          locationIDs.push(data[i][property]);
          break;
        case "latitude":
        case "longitude":
          coords.push(data[i][property]);
          break;
        case "gmapsid":
          gmapsIDs.push(data[i][property]);
          break;
        case "visualidentifier":
          visualidentifiers.push(data[i][property]);
          break;
        case "incidentmap":
          incidentmaps.push(data[i][property]);
          break;
        default:
          break;
      }
    }
    locations.push(coords);
  }
  for (let i = 0; i < locations.length; i++) {
    const marker = L.marker(locations[i]);
    marker.bindPopup(
        '<b>' + visualidentifiers[i] + '</b><br>' + locationIDs[i]).openPopup();

    marker.on('click', function () {
      returnIncidentsWhere(locationIDs[i])
      let modal = document.querySelector("#modal");
      let closeBtn = document.querySelector(".close-modal");
      document.getElementById("header-text").innerHTML = visualidentifiers[i];

      modal.style.display = "block";
      modal.style.visibility = "visible"
      modal.style.opacity = "1"
      closeBtn.onclick = function () {
        modal.style.display = "none";
      }

      window.onclick = function (e) {
        if (e.target == modal) {
          modal.style.display = "none";
        }
      }
    });

    markers.addLayer(marker);
  }
  mymap.addLayer(markers);
}

async function sendToPost(e) {
  var typeReports = $("#select-type-incident option:selected").text();
  var note = $('#textarea').val()
  const locationparams = new URLSearchParams();
  locationparams.append('gmapsid', GmapsID);
  var locationID;
  try {
    locationID = await getLocationID(placeInternal);
  } catch (e) {
    console.log(e);
  }

  console.log(locationID, typeReports, note);
  try {
    await saveIncidentData(locationID, typeReports, note);
    console.log("Added!")
  } catch (e) {
    throw e;
    console.log(e)
  }

}

async function getLocationID(placeInternal) {
  const response = await fetch(`/lid?gmapsid=${placeInternal.place_id}`)
  if (response.status === 200) {
    return response.text();
  } else {
    try {
      await savePlaceData(placeInternal);
    } catch (e) {
      throw e;
    }
    return getLocationID(placeInternal);
  }
}

$('#form').on('click', function (e) {
  e.preventDefault();
  $('#successMessage').show().delay(5000).fadeOut();
});

function addIncidentsToModal(data) { 
  var outerDiv = document.createElement("div");
  data.forEach(datum => {
    var innerDiv = document.createElement("div");
    const header = document.createTextNode(`Incident Type: ${datum.typeReports}`)
    const note = document.createTextNode(`Notes: ${datum.note}`)
    const date = document.createTextNode(`Date: ${datum.dateTime}`)
    innerDiv.append(header);
    innerDiv.appendChild(document.createElement("br"));    
    innerDiv.append(note);
    innerDiv.appendChild(document.createElement("br"));
    innerDiv.append(date);
    innerDiv.appendChild(document.createElement("br"));
    innerDiv.appendChild(document.createElement("br"));
    outerDiv.append(innerDiv);
  })
  const modalContent = document.getElementById("modal-content")
  modalContent.append(outerDiv);
}