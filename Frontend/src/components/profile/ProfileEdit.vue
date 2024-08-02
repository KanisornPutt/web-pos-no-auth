<template>
  <div class="row justify-content-center">
    <div
      class="col-6 col-md-4 m-4 d-flex justify-content-center align-items-center"
    >
      <div class="image-container justify-content-center">
        <div class="image-frame rounded-4">
          <img
            v-if="imageSrc"
            :src="imageSrc"
            class="rounded-4"
            alt="Product Image"
          />
          <img
            v-else
            src="@/assets/null-user.png"
            class="rounded-4"
            alt="Product Image"
          />
        </div>

        <!-- Customized File Input -->
        <div class="image-upload mt-3">
          <label for="image" class="btn btn-sm btn-success">
            Upload Image
            <input
              type="file"
              accept=".png, .jpg, .jpeg"
              id="image"
              @change="handleFileUpload"
              style="display: none"
            />
          </label>
          <p v-if="imageFile">{{ imageFile.name }}</p>
        </div>
      </div>
    </div>
    <div class="col-8 col-md-6 m-5 mb-0">
      <div class="row text-start">
        <div class="col-6">
          <h6 class="text-secondary">Firstname</h6>
          <input
            type="text"
            class="border border-secondary rounded-1"
            v-model="firstname"
            style="width: 95%"
          />
        </div>
        <div class="col-6">
          <h6 class="text-secondary">Lastname</h6>
          <input
            type="text"
            class="border border-secondary rounded-1"
            v-model="lastname"
            style="width: 95%"
          />
        </div>
      </div>
      <div class="text-start my-3">
        <h6 class="text-secondary">Username</h6>
        <input
          type="text"
          class="border border-secondary rounded-1"
          v-model="showName"
          style="width: 95%"
        />
      </div>
      <div class="text-start my-3">
        <h6 class="text-secondary">Email</h6>
        <h5>{{ user.email }}</h5>
      </div>
      <div class="text-start my-3">
        <h6 class="text-secondary">Store</h6>
        <div class="d-flex align-items-center" v-if="user.storeId">
          <h5 class="mb-0 me-3">{{ storeData.name }}</h5>
          <button
            type="button"
            class="btn btn-danger"
            data-bs-toggle="modal"
            data-bs-target="#leavingStoreModal"
          >
            Leave Store
          </button>
        </div>

        <!-- Leaving Store Modal -->
        <div
          class="modal fade"
          id="leavingStoreModal"
          tabindex="-1"
          aria-labelledby="leavingStoreLabel"
          aria-hidden="true"
        >
          <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
              <div class="modal-header">
                <h1 class="modal-title fs-5" id="leavingStoreLabel">
                  Leaving Store
                </h1>
                <button
                  type="button"
                  class="btn-close"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                ></button>
              </div>
              <div class="modal-body">
                <h4>Are you sure?</h4>
                <p>This process cannot be undone.</p>
              </div>
              <div class="modal-footer">
                <button
                  type="button"
                  class="btn btn-secondary"
                  data-bs-dismiss="modal"
                >
                  cancel
                </button>
                <button
                  type="button"
                  class="btn btn-danger"
                  data-bs-dismiss="modal"
                  @click="leaveStore"
                >
                  Yes, I'm sure.
                </button>
              </div>
            </div>
          </div>
        </div>

        <div v-if="!user.storeId">
          <h5>No Store</h5>
          <a class="btn btn-lg btn-orange text-light" href="/storeSetup"
            >Link to One Now</a
          >
        </div>
      </div>
    </div>
  </div>

  <!-- Footer -->
  <div class="row p-3">
    <div class="col-4 d-flex justify-content-start mb-4">
      <button
        class="btn btn-danger text-light me-3"
        style="width: 9rem; height: 3rem"
        type="button"
        data-bs-toggle="modal"
        data-bs-target="#deleteAccountModal"
      >
        Delete Account
      </button>

      <!-- Delete Account Modal -->
      <div
        class="modal fade"
        id="deleteAccountModal"
        tabindex="-1"
        aria-labelledby="deleteAccountModal"
        aria-hidden="true"
      >
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h1 class="modal-title fs-5" id="deleteAccountLabel">
                Deleting Account
              </h1>
              <button
                type="button"
                class="btn-close"
                data-bs-dismiss="modal"
                aria-label="Close"
              ></button>
            </div>
            <div class="modal-body">
              <h4>Are you sure?</h4>
              <p>This process cannot be undone.</p>
              <h5>Please Enter Your Password to Continue</h5>
              <input
                type="password"
                class="border border-secondary rounded-1"
                v-model="password"
                style="width: 95%; font-size: 20px"
                required
              />
              <p class="text-danger">{{ errorMsg }}</p>
            </div>
            <div class="modal-footer">
              <button
                type="button"
                class="btn btn-secondary"
                data-bs-dismiss="modal"
              >
                cancel
              </button>
              <button
                type="button"
                class="btn btn-danger"
                @click="deleteAccount"
              >
                Delete Account
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-8 d-flex justify-content-end mb-4">
      <!-- Move the button to a new row and align it to the right -->
      <button
        class="btn btn-secondary text-light me-3"
        style="width: 9rem; height: 3rem"
        @click="toggleContent"
      >
        Discard Changes
      </button>
      <button
        class="btn btn-success text-light me-3"
        style="width: 9rem; height: 3rem"
        @click="saveChanges"
      >
        Save Changes
      </button>
    </div>
  </div>
</template>

<script>
import { computed, onMounted, ref } from "vue";
import { useStore } from "vuex";
import bootstrapBundle from "bootstrap/dist/js/bootstrap.bundle";
import router from '@/router';
export default {
  emits: ["toggleContent"],
  setup(props, { emit }) {
    const store = useStore();
    const user = computed(() => store.getters["auth/user"]);
    const storeData = computed(() => store.getters["auth/storeData"]);
    const firstname = ref(user.value.firstname);
    const lastname = ref(user.value.lastname);
    const showName = ref(user.value.showName);
    const password = ref("");
    const errorMsg = ref("");
    const imageFile = ref(null);
    const imageSrc = ref(user.value.imageUrl);

    const handleFileUpload = (event) => {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        imageSrc.value = e.target.result;
      };
      reader.readAsDataURL(file);
      imageFile.value = file; // Assign the raw file object
      event.target.value = null;
    };

    const toggleContent = () => {
      emit("toggleContent");
    };

    const saveChanges = () => {
      const userChangeRequest = {
        firstname: firstname.value,
        lastname: lastname.value,
        showName: showName.value,
        image: imageFile.value,
      };
      store.dispatch("user/updateUser", userChangeRequest);
      location.reload();
      emit("toggleContent");
    };

    const leaveStore = () => {
      store.dispatch("store/leaveStore");
    };

    const deleteAccount = async () => {
      const passwordRequest = {
        password: password.value,
      };
      try {
        const validation = await store.dispatch(
          "user/checkPassword",
          passwordRequest
        );
        if (validation) {
          errorMsg.value = "";
          const modalElement = document.getElementById("deleteAccountModal");
          const modal = bootstrapBundle.Modal.getInstance(modalElement);
          modal.hide();
          store.dispatch("user/deleteUser", passwordRequest);
        } else {
          password.value = "";
          errorMsg.value = "Incorrect password";
        }
      } catch (error) {
        console.error("Error while checking password: ", error);
        errorMsg.value = "Error occurred while checking password";
      }
    };

    return {
      user,
      storeData,
      toggleContent,
      firstname,
      lastname,
      showName,
      saveChanges,
      leaveStore,
      password,
      deleteAccount,
      errorMsg,
      imageFile,
      imageSrc,
      handleFileUpload,
    };
  },
};
</script>

<style scoped>
input {
  font-size: 20px;
}

.image-container {
  text-align: center;
}
.image-frame {
  width: 225px; /* Adjust as needed */
  height: 225px; /* Adjust as needed */
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}
.image-frame img {
  max-width: 100%;
  /* max-height: 100%; */
  object-fit: contain; /* Adjust as needed */
}
.image-upload {
  margin-top: 1rem;
}
</style>
