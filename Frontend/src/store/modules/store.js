import axios from "axios";

const state = {
  storeData: JSON.parse(localStorage.getItem("storeData")) || null,
  token: localStorage.getItem("token") || "",
  user: JSON.parse(localStorage.getItem("user")) || null,
};

const mutations = {
  setStore(state, requestStore) {
    console.log("Setting Store");
    console.log(requestStore);
    state.storeData = requestStore;
    localStorage.setItem("storeData", JSON.stringify(requestStore));
  },
};

const actions = {
  setData({ state, rootGetters }) {
    state.user = rootGetters["auth/user"];
    state.storeData = rootGetters["auth/storeData"];
    state.token = rootGetters["auth/token"];
  },

  async createStore({ state, commit, dispatch }, payload) {
    console.log("Creating store");
    const url = "/api/stores/" + state.user.userId; // Replace with your API endpoint
    const token = state.token; // Replace with your actual Bearer token
    const storeData = payload;
    try {
      const response = await axios.post(url, storeData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json", // If you're sending JSON data
        },
      });
      commit("auth/setStore", response.data, { root: true });
      dispatch("auth/getUser", null, { root: true });
      return true;
    } catch (error) {
      console.error("There was an error : ", error);
      return false;
    }
  },

  async joinStore({ state, commit, dispatch }, payload) {
    console.log("Joining store");
    const url = "/api/stores/join/" + state.user.userId; // Replace with your API endpoint
    const token = state.token; // Replace with your actual Bearer token
    const joinRequest = payload;
    try {
      const response = await axios.put(url, joinRequest, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json", // If you're sending JSON data
        },
      });
      commit("auth/setStore", response.data, { root: true });
      dispatch("auth/getUser", null, { root: true });
      return true;
    } catch (error) {
      console.error("There was an error : ", error);
      return false;
    }
  },

  async leaveStore({ state, commit }) {
    console.log("Leaving store");
    const url = "/api/users/removeStore/" + state.user.userId;
    const token = state.token;
    try {
      const response = await axios.put(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      commit("auth/setUser", response.data, { root: true });

      return true;
    } catch (error) {
      console.error("There was an error : ", error);
      return false;
    }
  },

  async getAdminData({ state }) {
    // console.log("getting admin data");
    const user = state.user;
    const storeData = state.storeData;
    const token = state.token;
    const url = `/api/stores/admin/${storeData.id}/${user.userId}`;
    try {
      const response = await axios.get(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      // console.log(response.data);
      return response.data;
    } catch (error) {
      console.log("There was an error: ", error);
      return false;
    }
  },

  async getUsersFromStore({ state }, payload) {
    // console.log("getting uers data from store");
    const user = payload;
    const token = state.token;
    const url = `/api/stores/users/${user.storeId}/${user.userId}`;
    try {
      const response = await axios.get(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log(response.data);
      return response.data;
    } catch (error) {
      console.log(error);
      return false;
    }
  },

  async updateStore({ state }, payload) {
    console.log("Updating Store");
    const user = state.user;
    const storeData = state.storeData;
    const token = state.token;
    const adminDto = payload;
    const url = `api/stores/${storeData.id}/${user.userId}`;

    try {
      const response = await axios.put(url, adminDto, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json", // If you're sending JSON data
        },
      });
      console.log(response.data);
      return true;
    } catch (error) {
      console.log(error);
      return false;
    }
  },

  async resetPassword({ state }, payload) {
    console.log("Resting store password");
    const user = state.user;
    const token = state.token;
    const formData = new FormData();
    formData.append("userPassword", payload.userPassword);
    formData.append("newStorePassword", payload.newStorePassword);

    const url = `/api/stores/resetPassword/${user.storeId}/${user.userId}`; // Adjust with your API endpoint

    try {
      const response = await axios.put(url, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });
      console.log(response.data);
      return true;
    } catch (error) {
      console.log(error);
      return false;
    }
  },

  async deleteStore({ state }, payload) {
    console.log("Deleting Store");
    const user = state.user;
    const token = state.token;
    const userPassword = payload.userPassword;

    const url = `/api/stores/delete/${user.storeId}/${user.userId}`;

    try {
      const response = await axios.delete(url, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        params: {
          userPassword: userPassword,
        },
      });
      console.log(response.data);
      state.storeData = null;
      localStorage.removeItem("storeData");
      return true;
    } catch (error) {
      console.log(error);
      return false;
    }
  },

  async makeAdmin({ state }, payload) {
    console.log("making admin");
    const admin = state.user;
    const token = state.token;
    const adminPassword = payload.adminPassword;
    const user = payload.user;
    const url = `/api/stores/makeAdmin/${admin.storeId}/${admin.userId}/${user.userId}`;

    const formData = new FormData();
    formData.append("adminPassword", adminPassword);

    try {
      const response = await axios.put(url, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });
      console.log(response.data);
      return true;
    } catch (error) {
      console.log(error);
      return false;
    }
  },

  async removeAsAdmin({ state }, payload) {
    console.log("removing admin");
    const admin = state.user;
    const token = state.token;
    const adminPassword = payload.adminPassword;
    const user = payload.user;
    const url = `/api/stores/removeAsAdmin/${admin.storeId}/${admin.userId}/${user.userId}`;

    const formData = new FormData();
    formData.append("adminPassword", adminPassword);

    try {
      const response = await axios.put(url, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });
      console.log(response.data);
      return true;
    } catch (error) {
      console.log(error);
      return false;
    }
  },


  async removeFromStore({ state }, payload) {
    console.log("removing from store");
    const admin = state.user;
    const token = state.token;
    const adminPassword = payload.adminPassword;
    const user = payload.user;
    const url = `/api/stores/removeFromStore/${admin.storeId}/${admin.userId}/${user.userId}`;

    const formData = new FormData();
    formData.append("adminPassword", adminPassword);

    try {
      const response = await axios.put(url, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });
      console.log(response.data);
      return true;
    } catch (error) {
      console.log(error);
      return false;
    }
  },

};

const getters = {};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
};
