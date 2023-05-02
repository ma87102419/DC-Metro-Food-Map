import { createRouter as createVueRouter, createWebHistory, Router } from "vue-router";
import Home from "../views/Home.vue";
import Register from "../views/Register.vue";
import Login from "../views/Login.vue";
import Account from "../views/Account.vue";
import Favorite from "../views/Favorite.vue";
import Admin from "../views/Admin.vue";

import { App } from 'vue';

export function createRouter(app: App): Router {
  const router = createVueRouter({
    routes: [
      {
        path: "/",
        name: "home",
        component: Home
      },
      {
        path: "/register",
        name: "register",
        component: Register
      },
      {
        path: "/login",
        name: "login",
        component: Login
      },
      {
        path: "/account",
        name: "account",
        component: Account
      },
      {
        path: "/favorite",
        name: "favorite",
        component: Favorite
      },
      {
        path: "/admin",
        name: "admin",
        component: Admin,
      },
    ],
    history: createWebHistory()
  })

  // [API]
  router.beforeEach((to, from, next) => {
    const publicPages = ['/login', '/register', '/'];
    const authRequired = !publicPages.includes(to.path);
    
    const loggedIn = localStorage.getItem('user');
  
    // trying to access a restricted page + not logged in -> redirect to login page
    if (authRequired && !loggedIn) {
      next('/login');
    } else {
      next();
    }
  });

  return router;
}
