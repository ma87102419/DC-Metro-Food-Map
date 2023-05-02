<template>
  <div id="app" class="d-flex flex-column h-100">
    <nav-bar />
    <div>
      <div >
        <router-view />
      </div>
    </div>
    <footer class="bg-light text-center p-2" style="vertical-align:middle;">
      <p>
        Foodie Map! (2023)
      </p>
    </footer>
  </div>
</template>

<style>
  footer {
    margin-bottom:0px;
  }
  div#app {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
  }
</style>

<script lang="ts">
import NavBar from "./components/NavBar.vue";

export default {
  components: {
    NavBar,
  }
};
</script>
