<template>
  <div class="container m-5 mx-auto">
    <div class="row justify-content-center">
      <div
        class="col-12 col-md-10 col-lg-8 p-5 border border-hide border-tertiary rounded-4"
      >
        <div>
          <form
            v-if="activeForm === 'createFrom'"
            @submit.prevent="createStore"
          >
            <!-- Store Creation -->
            <h2>Create New Store</h2>

            <!-- Store Name -->
            <div class="mb-3">
              <label for="storeName" class="form-label">Store Name</label>
              <input
                type="text"
                class="form-control"
                id="storeName"
                placeholder="Name of your store"
                required
                v-model="storeName"
              />
            </div>

            <!-- Store Code -->
            <div class="mb-3">
              <label for="createStoreCode" class="form-label">Store Code</label>
              <input
                type="text"
                class="form-control"
                id="createStoreCode"
                placeholder="Unique Code for your store, used for invitation"
                required
                v-model="createStoreCode"
              />
            </div>

            <!-- Store Password -->
            <div class="mb-3">
              <label for="createStorePassword" class="form-label"
                >Store Password</label
              >
              <input
                type="password"
                class="form-control"
                id="createStorePassword"
                placeholder="Password of at least 8 characters for invitaion"
                required
                v-model="createStorePassword"
              />
            </div>

            <!-- Promptpay Id -->
            <div class="mb-3">
              <label for=" promptpayId" class="form-label"
                >Phone Number or ID for Promptpay</label
              >
              <input
                type="text"
                class="form-control"
                id="promptpayId"
                pattern="\d*"
                placeholder="Phone Number or ID"
                v-model="promptpayId"
                required
                @input="validateNumber"
              />
              <p class="text-danger">{{ promptpayIdError }}</p>
            </div>
            <button type="submit" class="btn btn-primary my-2">
              Create Store
            </button>

            <hr />
            <p class="">Not an Owner?</p>
            <button
              type="button"
              class="btn btn-primary"
              @click="toggleForms('JoinForm')"
            >
              Join an Existing Store
            </button>
          </form>

          <form v-if="activeForm === 'JoinForm'" @submit.prevent="joinStore">
            <!-- Join Store -->
            <h2>Join an Existing Store</h2>

            <!-- Store Code -->
            <div class="mb-3">
              <label for="joinStoreCode" class="form-label">Store Code</label>
              <input
                type="text"
                class="form-control"
                id="joinStoreCode"
                placeholder="Store Code given by the store admin"
                required
                v-model="joinStoreCode"
              />
            </div>

            <!-- Store Password -->
            <div class="mb-3">
              <label for="joinStorePassword" class="form-label"
                >Store Password</label
              >
              <input
                type="password"
                class="form-control"
                id="joinStorePassword"
                placeholder="Store Password given by the store admin"
                required
                v-model="joinStorePassword"
              />
            </div>
            <button type="submit" class="btn btn-primary my-2">
              Join Store
            </button>
            <hr />
            <p class="">Creating a New Store?</p>
            <button
              type="button"
              class="btn btn-primary"
              @click="toggleForms('createFrom')"
            >
              Create a New Store
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
export default {
  setup() {
    const store = useStore();
    const router = useRouter();

    const activeForm = ref("createFrom");

    const toggleForms = (formName) => {
      clearForms();
      activeForm.value = formName;
    };

    const storeName = ref("");
    const createStoreCode = ref("");
    const createStorePassword = ref("");
    const promptpayId = ref("");
    const promptpayIdError = ref("");

    const joinStoreCode = ref("");
    const joinStorePassword = ref("");

    const clearForms = () => {
      storeName.value = "";
      createStoreCode.value = "";
      createStorePassword.value = "";
      promptpayId.value = "";
      promptpayIdError.value = "";
      joinStoreCode.value = "";
      joinStorePassword.value = "";
    };

    const validateNumber = () => {
      promptpayId.value = promptpayId.value.replace(/\D/g, "");
      if (promptpayId.value.length === 10 || promptpayId.value.length === 13) {
        promptpayIdError.value = "";
        return true;
      } else {
        promptpayIdError.value = "Please enter a valid phone number or ID";
        return false;
      }
    };

    const createStore = async () => {
      storeName.value = storeName.value.trim();
      createStoreCode.value = createStoreCode.value.trim();
      createStorePassword.value = createStorePassword.value.trim();
      
      if (
        storeName.value.length === 0 ||
        createStoreCode.value.length === 0 ||
        createStorePassword.value.length === 0
      )
        return;

      const createStoreRequest = {
        name: storeName.value,
        storeCode: createStoreCode.value,
        password: createStorePassword.value,
        promptpayId: promptpayId.value,
      };

      if (!validateNumber()) return;

      const validation = await store.dispatch(
        "store/createStore",
        createStoreRequest
      );
      if (validation) {
        router.push("/dashboard");
      } else {
        alert("Store code has already been used");
        createStoreCode.value = "";
        createStorePassword.value = "";
      }
    };

    const joinStore = async () => {
      const joinStoreRequest = {
        storeCode: joinStoreCode.value.trim(),
        password: joinStorePassword.value,
      };
      const validation = await store.dispatch(
        "store/joinStore",
        joinStoreRequest
      );
      if (validation) {
        router.push("/dashboard");
      } else {
        alert("Inccorect Store code or password");
        createStoreCode.value = "";
        createStorePassword.value = "";
      }
    };

    return {
      activeForm,
      toggleForms,
      storeName,
      createStoreCode,
      createStorePassword,
      promptpayId,
      promptpayIdError,
      validateNumber,
      joinStoreCode,
      joinStorePassword,
      createStore,
      joinStore,
    };
  },
};
</script>

<style scode>
@media (max-width: 768px) {
  .border-hide {
    border: none !important; /* Hide the border */
  }
}
</style>
