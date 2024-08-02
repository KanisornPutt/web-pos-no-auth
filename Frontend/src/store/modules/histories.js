import axios from "axios";

const state = {};

const mutations = {};

const actions = {
  async confirmPayment({ rootGetters }, payload) {
    console.log("making payment...");
    console.log(payload);

    const user = rootGetters["auth/user"];

    if (!user.storeId) {
      console.log("User is not linked to a store");
      return false;
    }

    const url = `/api/history/${user.storeId}/${user.userId}`;
    const token = rootGetters["auth/token"];

    try {
      const response = await axios.post(url, payload, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      const responseHistory = response.data;
      console.log("Transaction completed successfully:", responseHistory);
      return true;
    } catch (error) {
      if (error.response) {
        console.error("Error response:", error.response.data);
        console.error("Error status:", error.response.status);
        console.error("Error headers:", error.response.headers);
      } else if (error.request) {
        console.error("Error request:", error.request);
      } else {
        console.error("Error message:", error.message);
      }
      console.error("Error config:", error.config);
      alert(error);
      return false;
    }
  },

  async getHistories({ rootGetters }) {
    console.log("Getting histories");

    const user = rootGetters["auth/user"];

    if (!user.storeId) {
      console.log("User is not linked to a store");
      return false;
    }

    const url = `/api/history/${user.storeId}/${user.userId}`;
    const token = rootGetters["auth/token"];

    try {
      const response = await axios.get(url, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      const responseHistory = response.data.map((resHistory) => ({
        ...resHistory,
        createdAt: new Date(resHistory.createdAt),
      }));
      console.log("Transaction retrieved successfully:", responseHistory);
      return responseHistory;
    } catch (error) {
      if (error.response) {
        console.error("Error response:", error.response.data);
        console.error("Error status:", error.response.status);
        console.error("Error headers:", error.response.headers);
      } else if (error.request) {
        console.error("Error request:", error.request);
      } else {
        console.error("Error message:", error.message);
      }
      console.error("Error config:", error.config);
      alert(error);
      return null;
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
