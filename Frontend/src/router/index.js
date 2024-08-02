import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import store from "../store/index.js";
import { computed } from "vue";
import { useStore } from "vuex";

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: { name: "home" }, // Redirect to home page
  },
  {
    path: "/about",
    name: "about",
    component: () => import("../views/AboutView.vue"),
  },
  {
    path: "/login",
    name: "login",
    component: () => import("../views/auth/LoginView.vue"),
    meta: { requiresGuest: true },
  },
  {
    path: "/signup",
    name: "signup",
    component: () => import("../views/auth/SignupView.vue"),
    meta: { requiresGuest: true },
  },
  {
    path: "/dashboard",
    name: "dashboard",
    component: () => import("../views/DashboardView.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/redirect",
    name: "redirect",
    component: () => import("../views/auth/RedirectView.vue"),
    meta: { requiresAuth: false },
  },

  {
    path: "/profile",
    name: "profile",
    component: () => import("../views/ProfileView.vue"),
    meta: { requiresAuth: true },
  },

  {
    path: "/storesetup",
    name: "storeSetup",
    component: () => import("../views/StoreSetupView.vue"),
    meta: { requiresAuth: true, requireNoStore: true },
  },

  {
    path: "/store",
    name: "store",
    component: () => import("../views/StoreView.vue"),
    meta: { requiresAuth: true},
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

// Navigation guards
router.beforeEach(async (to, from, next) => {
  if (store.getters["auth/isAuthenticated"]) {
    try {
      await store.dispatch("auth/getUser");
    } catch (error) {
      console.error("Error fetching user:", error);
      // Handle error fetching user details, maybe redirect to login
      next({ name: "login" });
      return;
    }
  }

  // storeSetup
  if (to.meta.requireNoStore && to.meta.requiresAuth) {
    if (store.getters["store/isLinkedToStore"]) {
      next({ name: "dashboard" });
    } else {
      next();
    }
  }

  if (to.meta.requiresAuth) {
    if (!store.getters["auth/isAuthenticated"]) {
      next({ name: "login" });
    } else {
      next();
    }
  } else if (to.meta.requiresGuest) {
    if (store.getters["auth/isAuthenticated"]) {
      next({ name: "home" });
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
