import axios from "axios";
import router from '@/router'; 

const state = {
  token: localStorage.getItem("token") || null,
  status: "",
  user: JSON.parse(localStorage.getItem("user")) || null,
  clientId:
    "38181892421-dcmv3r9nvnkbalt85nskg16qtlc8e7n5.apps.googleusercontent.com",
  redirectUri: "http://localhost:8020/redirect",
  responseType: "code",
  scope: "email profile", // adjust scopes as needed
  state: "YOUR_STATE", // a random string to prevent CSRF attacks
  storeData: JSON.parse(localStorage.getItem("storeData")) || null,
};

const mutations = {
  auth_request(state) {
    state.status = "loading";
  },
  auth_success(state, payload) {
    state.status = "";
    state.token = payload.token;
    state.user = payload.user;
    localStorage.setItem("token", payload.token);
    localStorage.setItem("user", JSON.stringify(payload.user));
  },

  setUser(state, requestUser) {
    state.user = requestUser;
    localStorage.setItem("user", JSON.stringify(requestUser));
    if (!requestUser.storeId) {
      localStorage.removeItem("storeData");
    }
  },

  setStore(state, requestStore) {
    state.storeData = requestStore;
    state.user.storeId = requestStore.id;
    localStorage.setItem("storeData", JSON.stringify(requestStore));
  },

  auth_error(state) {
    state.status = "error";
  },
  logout(state) {
    state.status = "";
    state.token = "";
    state.user = null;
    state.storeData = null;
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    localStorage.removeItem("storeData");
  },
  loginWithOAuth() {
    console.log("oauth");
    const authUrl = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${state.clientId}&redirect_uri=${state.redirectUri}&response_type=${state.responseType}&scope=${state.scope}&state=${state.state}`;
    window.location.href = authUrl;
  },
};

const actions = {
  async register({ commit }, user) {
    try {
      commit("auth_request");
      const response = await axios.post("/api/v1/auth/register", user);
      const token = response.data.token;
      alert("Signed up successfully. Please login to continue")
      return response;
    } catch (error) {
      console.log(error);
      if (error.response.status !== 200) {
        commit("auth_error");
        throw new Error("This email is already registered.");
      } else {
        commit("auth_error");
        throw error;
      }
    }
  },

  async login({ commit, dispatch }, user) {
    try {
      commit("auth_request");
      const response = await axios.post("/api/v1/auth/authenticate", user);
      const token = response.data.token;
      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
      const userDetails = await axios.get("/api/users/" + user.email, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const userDto = userDetails.data;
      commit("auth_success", { token, user: userDto });
      return response;
    } catch (error) {
      commit("auth_error");
      localStorage.removeItem("token");
      delete axios.defaults.headers.common["Authorization"];
      throw error;
    }
  },

  async oauth({ commit, dispatch }, user) {
    try {
      commit("auth_request");
      const response = await axios.post("/api/v1/auth/oauth", user);
      const token = response.data.token;
      const userDetails = await axios.get("/api/users/" + user.email, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const userDto = userDetails.data;
      commit("auth_success", { token, user: userDto });

      return response;
    } catch (error) {
      console.log(error);
      if (error.response.status === 403) {
        commit("auth_error");
        throw new Error("oauth denied");
      } else {
        commit("auth_error");
        throw error;
      }
    }
  },

  async getUser({ state, commit, dispatch }) {
    const url = "/api/users/" + state.user.email; // Replace with your API endpoint
    const token = state.token; // Replace with your actual Bearer token
    const user = state.user;
    
    try {
      const response = await axios.get(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const userDto = response.data;
      commit("setUser", userDto);
      console.log(userDto);

      if (userDto.storeId) {
        const getStoreUrl = `/api/stores/${userDto.storeId}/${user.userId}`;

        try {
          const response = await axios.get(getStoreUrl, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          commit("setStore", response.data);
          dispatch("store/setData", null, {root : true});
          dispatch("user/setData", null, {root : true});
        } catch (error) {
          console.error("There was an error: ", error);
          if (error.response.status === 404) {
            console.log("Does not found store");
          }
        }
      } else {
        localStorage.removeItem("storeData");
      }
    } catch (error) {
      console.error("There was an error!", error);
      throw error;
    }
  },


  async logout({ commit }) {
    commit("logout");
    localStorage.removeItem("token");
    delete axios.defaults.headers.common["Authorization"];
    router.push("/")
  },
};

const getters = {
  isAuthenticated: (state) => !!state.token,
  authStatus: (state) => state.status,
  showName: (state) => state.user.showName,
  user: (state) => state.user,
  storeId: (state) => state.user.storeId,
  storeData: (state) => state.storeData,
  role: (state) => state.user.role,
  token: (state) => state.token,
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
};
