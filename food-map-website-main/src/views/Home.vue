<template>
  <div class="float-container" style="margin-top:-15px;width:100%; height:600px; margin-bottom:-25px;">
    <div class="accordion accordion-flush float-child" style="display:inline-block; width:20%; height: 600px;" id="accordionRestuarants">
      <div style="text-align:center; padding: 5px; border-bottom: 1px solid lightgrey;">
        <CascadeSelect 
        v-model="selectedStation" 
        :options="stationsOfLine" optionLabel="name" optionGroupLabel="lineCode"
        :optionGroupChildren="['stations']" 
        placeholder="Select a Station"
        id="selector"
        style="width: 185px;">
        </CascadeSelect>
        <label class="btn btn-primary btn-margin" style="margin-left:10px" @click="genMap">Go!</label>
      </div>
      <div v-if="restaurants">
        <div class="accordion-item" v-for="restaurant in restaurants">
          <h2 class="accordion-header" :id="`heading${restaurant.restaurantID}`">
            <button class="accordion-button collapsed" type="button"
            :id="`res${restaurant.restaurantID}`"
            data-bs-toggle="collapse" :data-bs-target="`#collapse${restaurant.restaurantID}`" 
            aria-expanded="false" :aria-controls="`collapse${restaurant.restaurantID}`" 
            @click="clickRestaurant(restaurant)" >
              {{ restaurant.name }}
            </button>
          </h2>
          <div :id="`collapse${restaurant.restaurantID}`"  class="accordion-collapse collapse" :aria-labelledby="`heading${restaurant.restaurantID}`" data-bs-parent="#accordionRestuarants">
            <div class="accordion-body" style="width:100%">
              <div class="float-left w-80">
                <Rating :modelValue="restaurant.starRating" readonly :cancel="false">
                  <template #onicon>
                    <i class="bi bi-star-fill" style="color: orange;"></i>
                  </template>
                  <template #officon>
                    <i class="bi bi-star"></i>
                  </template>
                </Rating>
              </div>
              <div class="float-right w-20" style="margin-top:-10px;">
                <button class="fav-btn" :id="`addfav${restaurant.restaurantID}`" style="margin-top:5px;font-size:22px; text-align:center;" @click="editFavorite(restaurant)">
                  <i v-if="favoritesID.includes(restaurant.restaurantID)" :id="`fav${restaurant.restaurantID}`" class="bi bi-bookmark-fill" style="color: orange;"></i>
                  <i v-else :id="`fav${restaurant.restaurantID}`" class="bi bi-bookmark"></i>
                </button>
              </div><br><br>
              <div class="w-100">
              <p>Hours: {{ restaurant.openTime }} - {{ restaurant.closeTime }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div id="reviewSection" class="float-child" style="border-left: 1px solid lightgrey; overflow-y: auto; display:none; background-color: #fff; width: 15%; height: 600px; position: absolute; z-index:999;">
      <DataView :value="reviews">
        <template #list="review">
          <div class="col-12">
            <div class="flex flex-column xl:flex-row xl:align-items-start py-2 px-4 gap-4">
              <div class="flex flex-column sm:flex-row justify-content-between align-items-center xl:align-items-start flex-1 gap-4">
                <div class="flex flex-column align-items-center sm:align-items-start">
                  <div class="font-bold" style="color: #000;font-size:18px;">{{ review.data.name }}</div>
                  <Rating :modelValue="review.data.starRating" readonly :cancel="false">
                    <template #onicon>
                      <i class="bi bi-star-fill" style="font-size:14px; color: orange;"></i>
                    </template>
                    <template #officon>
                      <i class="bi bi-star"></i>
                    </template>
                  </Rating>
                  <div style="font-size:14px; color:#000;">
                    {{ review.data.comment }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </template>
      </DataView>
    </div>
    <div class="float-child" style="display:inline-block; width:80%; height: 600px; z-index:1;">
      <div style="width:100%" id="map"></div>
    </div>
  </div><br>

</template>

<style>
#map {
  width: 100%;
  height: 100%;
  margin-left: auto;
  margin-right: auto;
}

.fav-btn {
  background-color: transparent;
  border: none; 
  padding: 0px;
  font-size: 16px;
}

.float-container {
    padding: 0px;
    width: 100%;
    height: 100%;
    margin-left: auto;
    margin-right: auto;
    border: none;
}

.float-child {
    border: none;
    float: left;
    padding: 0px;
}  

#selector:focus {
    outline: none;
}
</style>

<script>
  import CascadeSelect from 'primevue/cascadeselect';
  import Accordion from 'primevue/accordion';
  import AccordionTab from 'primevue/accordiontab';
  import DataView from 'primevue/dataview';
  import DataViewLayoutOptions from 'primevue/dataviewlayoutoptions';
  import Rating from 'primevue/rating';
  import 'primeflex/primeflex.css';

  export default {
    components: { Accordion, AccordionTab, CascadeSelect, DataView, DataViewLayoutOptions, Rating },
    data() {
      return {
        selectedStation: null,
        lines: null,

        // [API]
        favorites: null,
        favoritesID: [],

        // [API]
        stationsOfLine: null,

        loading: false,
        location: "",
        access_token: "pk.eyJ1IjoiYW1hbmRhY3RsIiwiYSI6ImNsZjZjZHdlczFnNzMzcmxtNjZ2bW8za3gifQ.t3GXNEirFScqu7WHBn7OEg",
        map: {},
        marker: null,

        // [API]
        restaurants: null,
        restaurantsOnMap: [],
        reviews: null,
      }
    },
    computed: {
      currentUser() {
        return this.$store.state.auth.user;
      }
    },
    mounted() {
      navigator.geolocation.getCurrentPosition(
        position => {
          this.createMap(position.coords.longitude, position.coords.latitude)
        },
        error => {
          console.log(error.message);
        },
      )

      // [API]
      fetch('https://localhost:8080/api/lines')
      .then(response => response.json())
      .then(data => {
        this.lines.value = data._embedded.lines;
        for(var i=0; i<this.lines.length; i++){
          fetch(`https://localhost:8080/api/lines/${this.lines[i].lineCode}`)
          .then(response => response.json())
          .then(data => {
            this.stationsOfLine.append(data);
          });
        }
      });
      fetch(`https://localhost:8080/api/accounts/${this.currentUser.username}/favorites`)
      .then(response => response.json())
      .then(data => {
        this.favorites.value = data._embedded.restaurants;
      });
      
      for(var i=0; i<this.favorites.length; i++){
        this.favoritesID.push(this.favorites[i].restaurantID);
      }

    },
    methods: {
      createMap(long, lat) {
        try {
          mapboxgl.accessToken = this.access_token;
          this.map = new mapboxgl.Map({
            container: "map",
            style: "mapbox://styles/mapbox/streets-v12",
            center: [long, lat],
            zoom: 15,
          });

          this.marker = new mapboxgl.Marker({ color: "#D80739" })
            .setLngLat([long, lat])
            .addTo(this.map);
          
          this.center = [long, lat];
            
          // Add the control to the map.
          let geocoder = new MapboxGeocoder({
              accessToken: this.access_token,
              localGeocoder: [long, lat],
              mapboxgl: mapboxgl,
              placeholder: "Search",
              marker: true
            })
          this.map.addControl(geocoder);
          geocoder.on("result", (e) => {
            const marker = new mapboxgl.Marker({
              draggable: true,
              color: "#D80739",
            })
            .setLngLat(e.result.center)
            .addTo(this.map);
            this.center = e.result.center;
            marker.on("dragend", (e) => {
              this.center = Object.values(e.target.getLngLat());
            });
          });
          
          this.map.addControl(new mapboxgl.NavigationControl());

        } catch (err) {
          console.log("map error", err);
        }

        this.map.on('load', () => {
          this.map.addSource('places', {
            // This GeoJSON contains features that include an "icon" property. The value of the "icon" property corresponds
            // to an image in the Mapbox Streets style's sprite.
            'type': 'geojson',
            'data': {
              'type': 'FeatureCollection',
              'features': this.restaurantsOnMap,
            }
          })

          // Add a layer showing the places.
          this.map.addLayer({
            'id': 'places',
            'type': 'circle',
            'source': 'places',
            'paint': {
              'circle-color': '#4264fb',
              'circle-radius': 6,
              'circle-stroke-width': 2,
              'circle-stroke-color': '#ffffff'
            }
          });
        });
      },
      editFavorite(restaurant) {
        if (!this.currentUser) {
          alert("Please login first!");
        }
        if(!this.favoritesID.includes(restaurant.restaurantID)){
          document.getElementById(`fav${restaurant.restaurantID}`).classList="bi bi-bookmark-fill";
          document.getElementById(`fav${restaurant.restaurantID}`).style.color = "orange";

          // [API]
          const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ restaurantID: restaurantID })
          };
         fetch(`https://localhost:8080/api/accounts/${this.currentUser.username}/favorites`, requestOptions)
          .then(async response => {
            const data = await response.json();
            if (!response.ok) {
              const error = (data && data.message) || response.status;
              return Promise.reject(error);
            }
          })
          .catch(error => {
            this.errorMessage = error;
            console.error('There was an error!', error);
          });

          this.favoritesID.push(restaurant.restaurantID);
        }
        else{
          document.getElementById(`fav${restaurant.restaurantID}`).classList="bi bi-bookmark";
          document.getElementById(`fav${restaurant.restaurantID}`).style.color = "black";

          // [API]
          fetch(`https://localhost:8080/api/accounts/${this.currentUser.username}/favorites/${restaurant.restaurantID}`, { method: 'DELETE' })
          .then(() => this.status = 'Delete successful');

          this.favoritesID.pop(restaurant.restaurantID);
        }
      },
      genMap() {
        this.marker.remove();
        this.marker = new mapboxgl.Marker({ color: "#D80739" })
          .setLngLat([this.selectedStation.longitude, this.selectedStation.latitude])
          .addTo(this.map);

        this.map.flyTo({
          center: [this.selectedStation.longitude, this.selectedStation.latitude],
          duration: 3000,
          essential: true // this animation is considered essential with respect to prefers-reduced-motion
        });

        // [API]
        this.restaurants = null;
        this.restaurantsOnMap = [];
        fetch(`https://localhost:8080/api/stations/${this.selectedStation.stationID}/restaurants`)
        .then(response => response.json())
        .then(data => {
          for(var i=0; i<data._embedded.restaurants.length; i++){
            this.restaurantsOnMap.push({
              type: "Feature",
              id: data._embedded.restaurants[i].restaurantID,
              geometry: {
                type: 'Point',
                coordinates: [data._embedded.restaurants[i].longitude, data._embedded.restaurants[i].latitude]
              }
            })
            this.restaurants.value = data._embedded.restaurants;
          };
        });
        
      },
      clickRestaurant(restaurant) {

        // [API]
        this.reviews = null;
        fetch(`https://localhost:8080/api/restaurants/${restaurant.restaurantID}/reviews`)
        .then(response => response.json())
        .then(data => {
          this.reviews.value = data._embedded.reviews;
        });

        this.marker.remove();
        this.marker = new mapboxgl.Marker({ color: "#D80739" })
          .setLngLat([restaurant.longitude, restaurant.latitude])
          .addTo(this.map);

        this.map.flyTo({
          center: [restaurant.longitude, restaurant.latitude],
          duration: 2000,
          essential: true // this animation is considered essential with respect to prefers-reduced-motion
        });

        var opened = document.getElementById(`res${restaurant.restaurantID}`).getAttribute('aria-expanded');
        var reviewSection = document.getElementById('reviewSection');

        reviewSection.style.display = "none";

        if(opened == "true"){
          var coordinates = new mapboxgl.LngLat(restaurant.longitude, restaurant.latitude);
          if(this.reviews){ 
            reviewSection.style.display = "inline-block";
          }
        }
        else{
          reviewSection.style.display = "none";
        }
      }
    }
  }
</script>