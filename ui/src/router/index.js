import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/selectCard",
    name: "selectCard",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/selectCard.vue"),
  },
  {
    path: "/photo",
    name: "photo",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/photo.vue"),
  },
  {
    path: "/download",
    name: "download",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/download.vue"),
  },
];

const router = new VueRouter({
  routes,
});

export default router;
