import Vue from "vue";
import VueRouter from "vue-router";
import step1 from "../components/step1";
import step2 from "../components/step2";
import step3 from "../components/step3";
import step4 from "../components/step4";
import step4_1 from "../components/step4_1";
import step4_2 from "../components/step4_2";
import step5 from "../components/step5";
import step6 from "../components/step6";
import loading from "../components/loading";
import test from "../components/Test";


Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "loading",
    component: loading,
  },
  {
    path: "/test",
    name: "test",
    component: test,
  },
  {
    path: "/step1",
    name: "step1",
    component: step1,
  },
  {
    path: "/step2",
    name: "step2",
    component: step2,
  },
  {
    path: "/step3",
    name: "step3",
    component: step3,
  },
  // {
  //   path: "/step4",
  //   name: "step4",
  //   component: step4,
  // },
  {
    path: "/step4_1",
    name: "step4_1",
    component: step4_1,
  },
  {
    path: "/step4_2",
    name: "step4_2",
    component: step4_2,
  },
  {
    path: "/step5",
    name: "step5",
    component: step5,
  },
  {
    path: "/step6",
    name: "step6",
    component: step6,
  },
];

const router = new VueRouter({
  routes,
});

export default router;
