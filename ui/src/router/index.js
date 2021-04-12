import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    redirect: "/step1",
  },
  {
    path: "/step1",
    name: "step1",
    component: () =>
        import("../components/step1.vue"),
  },
  {
    path: "/step2",
    name: "step2",
    component: () =>
        import("../components/step2.vue"),
  },
  {
    path: "/step3",
    name: "step3",
    component: () =>
        import("../components/step3.vue"),
  },
  {
    path: "/step4",
    name: "step4",
    component: () =>
        import("../components/step4.vue"),
  },
  {
    path: "/step5",
    name: "step5",
    component: () =>
        import("../components/step5.vue"),
  },
  {
    path: "/step6",
    name: "step6",
    component: () =>
        import("../components/step6.vue"),
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
