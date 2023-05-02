import App from "./App.vue";
import { createApp } from "vue";
import PrimeVue from 'primevue/config';
import { createRouter } from "./router";
import store from "./store";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faLink, faUser, faPowerOff, faHome, faUserPlus, faSignInAlt, faSignOutAlt,} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import hljs from 'highlight.js/lib/core';
import json from 'highlight.js/lib/languages/json';
import hljsVuePlugin from "@highlightjs/vue-plugin";
import "highlight.js/styles/github.css";
import 'bootstrap-icons/font/bootstrap-icons.css'

// For primevue
import "primevue/resources/themes/saga-blue/theme.css"; //theme
import "primevue/resources/primevue.min.css"; //core CSS
import "primeicons/primeicons.css"; //icons


hljs.registerLanguage('json', json);

const app = createApp(App);

library.add(faLink, faUser, faPowerOff, faHome, faUser, faUserPlus, faSignInAlt, faSignOutAlt);

app
  .use(hljsVuePlugin)
  .use(PrimeVue)
  .use(store)
  .use(createRouter(app))
  .component("font-awesome-icon", FontAwesomeIcon)
  .mount("#app");
