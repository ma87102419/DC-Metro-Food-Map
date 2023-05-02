<template>
  <div class="nav-container mb-3">
    <nav class="navbar navbar-expand-md navbar-light bg-light">
      <div class="container">
        <div class="navbar-brand logo"></div>
        <button
          class="navbar-toggler"
          type="button"
          data-toggle="collapse"
          data-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item">
              <router-link to="/" class="nav-link">Home</router-link>
            </li>

            <li v-if="currentUser" class="nav-item" style="margin-left:30px;">
              <router-link to="/favorite" class="nav-link">Favorites</router-link>
            </li>
          </ul>

           <ul class="navbar-nav mr-auto">
              <div class="text-center hero">
                <h1 class="mb-4">Foodie Map!</h1>
              </div>
          </ul>

          <ul class="navbar-nav d-none d-md-block">
            <li v-if="!currentUser" class="nav-item">
              <router-link to="/login">
                <button class="btn btn-primary btn-margin">Login</button>
              </router-link>
            </li>

            <li class="nav-item dropdown" v-if="currentUser">
              <a
                class="nav-link dropdown-toggle"
                href="#"
                id="profileDropDown"
                data-toggle="dropdown"
              >
                <img
                  :src="currentUser.picture"
                  class="nav-user-profile rounded-circle"
                  width="50"
                />
              </a>
              <div class="dropdown-menu dropdown-menu-right">
                <div class="dropdown-header">{{ currentUser.username }}</div>
                <router-link to="/account" class="dropdown-item dropdown-profile">
                  <font-awesome-icon class="mr-3" icon="user" />Account
                </router-link>
                <a class="dropdown-item dropdown-profile" @click.prevent="logOut">
                  <font-awesome-icon class="mr-3" icon="sign-out-alt" /> LogOut
                </a>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  </div>
  
</template>

<script lang="ts">
export default {
  name: "NavBar",
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
  },
  methods: {
    logOut() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/login');
    }
  }
};
</script>

<style>
#mobileAuthNavBar {
  min-height: 125px;
  justify-content: space-between;
}
</style>
