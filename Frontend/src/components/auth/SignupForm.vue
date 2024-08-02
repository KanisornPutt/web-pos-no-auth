<template>
  <form @submit.prevent="handleRegister">
    <div class="row mt-4">
      <div class="col-6">
        <label class="form-label">firstname</label>
        <input
          type="text"
          class="form-control"
          v-model="firstname"
          placeholder="firstname"
          required
        />
      </div>
      <div class="col-6">
        <label class="form-label">lastname</label>
        <input
          type="text"
          class="form-control"
          v-model="lastname"
          placeholder="lastname"
          required
        />
      </div>
    </div>

    <div class="my-3">
      <label class="form-label">Username</label>
      <input
        type="text"
        class="form-control"
        v-model="show_name"
        placeholder="username"
        required
      />
    </div>
    <div class="mb-3">
      <label class="form-label">Email address</label>
      <input
        type="email"
        class="form-control"
        aria-describedby="emailHelp"
        placeholder="name@example.com"
        v-model="email"
        required
      />
    </div>
    <div class="mb-3">
      <label class="form-label">Password</label>
      <input
        type="password"
        class="form-control"
        v-model="password"
        placeholder="password of 8 charactors or more"
        required
      />
      <ul>
        <li
          v-for="message in passwordStrengthMessage"
          :key="message"
          class="text-danger"
        >
          {{ message }}
        </li>
      </ul>
    </div>
    <div class="my-3 form-check">
      <input type="checkbox" class="form-check-input" v-model="checkbox" />
      <label class="form-check-label">
        <a href="#terms">Accept conditions & terms</a></label
      >
    </div>



    <div class="row justify-content-center m-3 mt-3 mb-2">
      <button type="submit" class="btn btn-primary col-6">Sign Up</button>
    </div>

    <div class="break-line mx-3 text-secondary"> <span class="mx-2">or</span> </div>

    <div class="row justify-content-center mt-2 mx-3">
      <button
        type="button"
        @click="loginWithOAuth"
        class="btn btn-primary col-6"
      >
        Continue with Google
      </button>

      <p class="fs-6 text-center">
        Already have an account?
        <a href="/login" class="text-decoration-none col-12">Login now</a>
      </p>
      <p v-show="errorMessage" class="fs-6 text-center text-danger">
        {{ errorMessage }}
      </p>
    </div>
  </form>
</template>

<script>
import { ref, watch } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

export default {
  setup() {
    const firstname = ref("");
    const lastname = ref("");
    const show_name = ref("");
    const email = ref("");
    const password = ref("");
    const username = ref("");
    const checkbox = ref(false);
    const errorMessage = ref("");
    const passwordStrengthMessage = ref([]);

    const router = useRouter();
    const store = useStore();

    const checkPasswordStrength = () => {
      const passwordValue = password.value;
      passwordStrengthMessage.value = [];
      if (passwordValue.length < 8) {
        passwordStrengthMessage.value.push(
          "Password must be at least 8 characters long."
        );
      }
      if (!/[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/? ]+/.test(passwordValue)) {
        passwordStrengthMessage.value.push(
          "Password must have special character."
        );
      }
      if (!/\d+/.test(passwordValue)) {
        passwordStrengthMessage.value.push("Password must have number.");
      }
      if (!/[A-Z]+/.test(passwordValue)) {
        passwordStrengthMessage.value.push(
          "Password must have capital letter."
        );
      }
    };

    watch(password, checkPasswordStrength);

    const handleRegister = async () => {
      try {
        errorMessage.value = "";
        const registerUser = {
          firstname: firstname.value.trim(),
          lastname: lastname.value.trim(),
          show_name: show_name.value.trim(),
          email: email.value.trim().toLowerCase(),
          password: password.value,
        };
        await store.dispatch("auth/register", registerUser);

        router.push("/login");

        // Handle successful registration (redirect, show success message, etc.)
      } catch (error) {
        errorMessage.value = error.message;
      }
    };

    const loginWithOAuth = () => {
      store.commit('auth/loginWithOAuth');
    };

    return {
      firstname,
      lastname,
      show_name,
      email,
      password,
      checkbox,
      username,
      handleRegister,
      errorMessage,
      checkPasswordStrength,
      passwordStrengthMessage,
      loginWithOAuth,
    };
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
