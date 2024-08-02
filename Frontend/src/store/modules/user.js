import axios from "axios";

const state = {
  token: localStorage.getItem("token") || null,
  user: JSON.parse(localStorage.getItem("user")) || null,
  redirectUri: "http://localhost:8020/redirect",
  responseType: "code",
  storeData: JSON.parse(localStorage.getItem("storeData")) || null,
};

const mutations = {};

const actions = {
  setData({ state, rootGetters }) {
    state.user = rootGetters["auth/user"];
    state.storeData = rootGetters["auth/storeData"];
    state.token = rootGetters["auth/token"];
  },

  async checkPassword({ state }, payload) {
    console.log("Checking password");
    const url = "api/users/checkPassword/" + state.user.userId; // Replace with your API endpoint
    const token = state.token; // Replace with your actual Bearer token
    const passwordRequest = payload;
    try {
      const response = await axios.post(url, passwordRequest, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });
      if (response.status === 200) {
        return true;
      } else if (response.status === 401) {
        return false;
      }
    } catch (error) {
      console.error("There was an error : ", error);
      return false;
    }
  },

  async updateUser({ commit }, payload) {
    console.log("updating user");
    // const url = "api/users/" + state.user.userId; // Replace with your API endpoint
    const url = `/api/users/${state.user.userId}`;
    const token = state.token; // Replace with your actual Bearer token
    const userChangeRequest = payload;

    const formData = new FormData();

    formData.append("firstname", userChangeRequest.firstname);
    formData.append("lastname", userChangeRequest.lastname);
    formData.append("showName", userChangeRequest.showName);
    if (userChangeRequest.image) {
      formData.append("image", userChangeRequest.image);
    }

    for (const [key, value] of formData.entries()) {
      console.log("Key:", key, "Value:", value);
    }

    try {
      const response = await axios.put(url, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });
      const userDto = response.data;
      commit("auth/setUser", userDto, { root: true });
      return true;
    } catch (error) {
      console.error("There was an error : ", error);
      return false;
    }
  },

  async deleteUser({ state, dispatch }, payload) {
    console.log("deleting user");
    const url = "api/users/" + state.user.userId; // Replace with your API endpoint
    const token = state.token; // Replace with your actual Bearer token
    const password = encodeURIComponent(payload.password);
    try {
      const response = await axios.delete(url, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        params: {
          password: password,
        },
      });
      dispatch("auth/logout", null, { root: true });
      return true;
    } catch (error) {
      console.error("There was an error : ", error);
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
