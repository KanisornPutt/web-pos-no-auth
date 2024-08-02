<template>
  <form @submit.prevent="handleSubmit">
    <div class="mb-3">
      <label class="form-label">Email address</label>
      <input
        type="text"
        class="form-control"
        aria-describedby="emailHelp"
        v-model="email"
        placeholder="name@example.com"
        required
      />
    </div>
    <div class="mb-3">
      <label class="form-label">Password</label>
      <input
        type="password"
        class="form-control"
        v-model="password"
        placeholder="password"
        required
      />
    </div>

    <div class="row justify-content-center mx-3">
      <button type="submit" class="btn btn-primary col-6 m-3" >
        Login
      </button>
    </div>

    <div class="break-line mx-3 text-secondary"> <span class="mx-2">or</span> </div>

    <div class="row justify-content-center mt-3 mx-3">
      <button
        type="button"
        @click="loginWithOAuth"
        class="btn btn-primary col-6"
      >
        Continue with Google
      </button>
      <p class="fs-6 text-center mt-3">
        Don't have an account yet?
        <a href="/signup" class="text-decoration-none">Signup now</a>
      </p>
      <p v-show="errorMessage" class="fs-6 text-center text-danger">
        {{ errorMessage }}
      </p>
    </div>
  </form>
</template>

<script>
import { computed, ref } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
export default {
  setup() {
    const email = ref("");
    const password = ref("");

    const store = useStore();
    const router = useRouter();

    const errorMessage = ref("");

    const handleSubmit = async () => {
      try {
        errorMessage.value = "";
        const loginUser = {
          email: email.value.toLowerCase(),
          password: password.value,
        };
        await store.dispatch("auth/login", loginUser);
        await store.dispatch('auth/getUser');
        router.push("/dashboard");
      } catch (error) {
        console.error("Error during login:", error);
        errorMessage.value = "Wrong_UserName_Or_Password";
      }
    };

    const loginWithOAuth = () => {
      store.commit('auth/loginWithOAuth');
    };

    return { email, password, handleSubmit, loginWithOAuth, errorMessage };
  },
};
</script>

    <style>
        .break-line {
            display: flex;
            align-items: center;
            text-align: center;
        }
        .break-line::before,
        .break-line::after {
            content: '';
            flex: 1;
            border-bottom: 1px solid #ddd;
        }
        .break-line:not(:empty)::before {
            margin-right: .25em;
        }
        .break-line:not(:empty)::after {
            margin-left: .25em;
        }
    </style>