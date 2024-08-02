<template>
  <h1>Redirecting....</h1>
</template>

<script>
import axios from "axios";
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

export default {
  setup() {
    const email = ref("");
    const router = useRouter();
    const store = useStore();

    const getAccessToken = async (code) => {
      try {
        const response = await axios.post("https://oauth2.googleapis.com/token", {
          code: code,
          client_id: "38181892421-dcmv3r9nvnkbalt85nskg16qtlc8e7n5.apps.googleusercontent.com",
          client_secret: "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",
          redirect_uri: "http://localhost:8020/redirect",
          grant_type: "authorization_code",
        });

        const accessToken = response.data.access_token;
        await getUserInfo(accessToken);
      } catch (error) {
        console.error("Failed to exchange authorization code for access token:", error);
      }
    };

    const getUserInfo = async (accessToken) => {
      try {
        const response = await axios.get("https://www.googleapis.com/oauth2/v1/userinfo?alt=json", {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });

        const userInfo = response.data;
        console.log("User Info:", userInfo);

        // Call the oauth action with the user info
        const registerUser = {
          firstname: userInfo.given_name,
          lastname: userInfo.family_name,
          show_name: userInfo.given_name,
          email: userInfo.email,
          password: "N/A",
        };
        await store.dispatch("auth/oauth", registerUser);

        router.push("/dashboard");
      } catch (error) {
        console.error("Failed to fetch user info:", error);
      }
    };

    onMounted(() => {
      const code = new URL(window.location.href).searchParams.get("code");
      const state = new URL(window.location.href).searchParams.get("state");

      if (code && state) {
        getAccessToken(code);
      } else {
        console.error("Authorization code or state missing.");
      }
    });

    return { email };
  },
};
</script>

<style></style>
