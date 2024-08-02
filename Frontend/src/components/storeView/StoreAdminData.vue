<template>
  <div v-if="isAdmin" class="accordion mx-4" id="adminData">
    <div class="accordion-item">
      <h2 class="accordion-header">
        <button
          class="accordion-button collapsed"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#collapseOne"
          aria-expanded="true"
          aria-controls="collapseOne"
          @click="toggleEdit(false)"
        >
          <h4>Store Data</h4>
        </button>
      </h2>
      <div
        id="collapseOne"
        class="accordion-collapse collapse"
        data-bs-parent="#accordionExample"
      >
        <div class="accordion-body">
          <div v-if="adminData">
            <!-- Viewing Data -->
            <div v-if="!isEditing">
              <div class="row my-3">
                <!-- Viewing Promptpay Id -->
                <div class="col-10 col-md-6">
                  <h4 class="">
                    <span class="fw-light">Promptpay ID: </span>
                    {{ adminData.promptpayId }}
                  </h4>
                </div>

                <!-- Viewing Store Code -->
                <div class="col-10 col-md-6">
                  <h4 class="">
                    <span class="fw-light">Store Code: </span>
                    {{ adminData.storeCode }}
                  </h4>
                </div>
              </div>

              <div class="row justify-content-between">
                <div class="col-auto">
                  <a
                    href=""
                    data-bs-toggle="modal"
                    data-bs-target="#resetPasswordModal"
                    >create new store password.</a
                  >
                </div>
                <div class="col-auto">
                  <button
                    class="btn btn-secondary"
                    data-bs-toggle="modal"
                    data-bs-target="#checkPasswordModal"
                  >
                    Edit Store Data
                  </button>
                </div>
                <CheckPasswordModal
                  modalId="checkPasswordModal"
                  modelTitle="Edit Store Data"
                  @continue="toggleEdit(true)"
                />
              </div>
            </div>

            <!-- Editing Data -->
            <div v-else>
              <div class="row mt-3">
                <!-- Editing Store Name -->
                <div class="col-12 mb-4">
                  <h4 class="fw-light">Store Name:</h4>
                  <input
                    type="text"
                    class="form-control"
                    id="storeName"
                    placeholder="Name of your store"
                    required
                    v-model="storeName"
                  />
                </div>

                <!-- Editing Promptpay Id -->
                <div class="col-12 col-md-6 mb-4">
                  <h4 class="fw-light">Promptpay ID:</h4>
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
                  <p class="text-danger mb-0">{{ promptpayIdError }}</p>
                </div>

                <!-- Editing Store Code -->
                <div class="col-12 col-md-6 mb-2">
                  <h4 class="fw-light">Store Code:</h4>
                  <input
                    type="text"
                    class="form-control"
                    id="createStoreCode"
                    placeholder="Unique Code for your store, used for invitation"
                    required
                    v-model="storeCode"
                  />
                </div>
                <a
                  href=""
                  class="mb-3"
                  data-bs-toggle="modal"
                  data-bs-target="#resetPasswordModal"
                  >create new store password.</a
                >
              </div>

              <!-- Footer -->
              <div class="row justify-content-between">
                <div class="col-auto">
                  <button
                    class="btn btn-danger"
                    data-bs-toggle="modal"
                    data-bs-target="#deleteStoreModal"
                  >
                    Delete Store
                  </button>
                </div>
                <div class="col-auto d-flex justify-content-end mb-4">
                  <!-- Move the button to a new row and align it to the right -->
                  <button
                    class="btn btn-secondary text-light me-3"
                    @click="toggleEdit(false)"
                  >
                    Discard Changes
                  </button>
                  <button
                    class="btn btn-success text-light me-3"
                    @click="saveChanges"
                  >
                    Save Changes
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div v-else>
            <h3 class="fw-light">loading Data..</h3>
          </div>
        </div>

        <!-- Reset Password Modal -->
        <div
          class="modal fade"
          id="resetPasswordModal"
          tabindex="-1"
          aria-labelledby="resetPasswordModalLabel"
          aria-hidden="true"
        >
          <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
              <div class="modal-header">
                <h1 class="modal-title fs-5" id="resetPasswordModalLabel">
                  Reset Store Password
                </h1>
                <button
                  type="button"
                  class="btn-close"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                  @click="closeResetPasswordModal"
                ></button>
              </div>
              <div class="modal-body">
                <h6>
                  Please Enter Your
                  <span class="fw-bold"><u>User</u></span> Password
                </h6>
                <input
                  type="password"
                  class="border border-secondary rounded-1 mb-3"
                  v-model="userPassword"
                  style="width: 95%"
                  required
                />
                <h6>
                  Enter Your New
                  <span class="fw-bold"><u>Store</u></span> Password
                </h6>
                <input
                  type="password"
                  class="border border-secondary rounded-1 mb-3"
                  v-model="newStorePassword"
                  style="width: 95%"
                  required
                />
              </div>
              <div class="modal-footer">
                <button
                  type="button"
                  class="btn btn-secondary"
                  data-bs-dismiss="modal"
                  @click="closeResetPasswordModal"
                >
                  Close
                </button>
                <button
                  type="button"
                  class="btn btn-primary"
                  @click="resetPassword"
                >
                  Save New Store Password
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Delete Store Modal -->
        <div
          class="modal fade"
          id="deleteStoreModal"
          tabindex="-1"
          aria-labelledby="deleteStoreLabel"
          aria-hidden="true"
        >
          <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
              <div class="modal-header">
                <h1 class="modal-title fs-5" id="deleteStoreLabel">
                  Deleting Store
                </h1>
                <button
                  type="button"
                  class="btn-close"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                  @click="closeDeleteStoreModal"
                ></button>
              </div>
              <div class="modal-body">
                <h4>Are you sure?</h4>
                <p>This process cannot be undone.</p>
                <div class="mb-3">
                  <h5>Please Enter Your User Password</h5>
                  <input
                    type="password"
                    class="form-control"
                    v-model="deleteStoreUserPassword"
                    placeholder="Account Password"
                  />
                </div>
                <hr />
                <h5 class="fw-light">
                  Please enter the phrase below to continue
                </h5>
                <h4>{{ deleteStorePhrase }}</h4>
                <input
                  type="text"
                  class="border border-secondary rounded-1"
                  v-model="deleteStoreConfirmation"
                  style="font-size: 20px"
                  required
                />
                <p class="text-danger">{{ deleteStoreConfirmationError }}</p>
              </div>
              <div class="modal-footer">
                <button
                  type="button"
                  class="btn btn-secondary"
                  data-bs-dismiss="modal"
                  @click="closeDeleteStoreModal"
                >
                  cancel
                </button>
                <button
                  type="button"
                  class="btn btn-danger"
                  @click="deleteStore"
                >
                  Delete {{ storeData.name }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed, ref } from "vue";
import { useStore } from "vuex";
import CheckPasswordModal from "@/components/CheckPasswordModal.vue";
import bootstrapBundle from "bootstrap/dist/js/bootstrap.bundle";
import { useRouter } from "vue-router";
export default {
  components: { CheckPasswordModal },
  setup() {
    const store = useStore();

    const storeData = computed(() => store.getters["auth/storeData"]);

    const role = computed(() => store.getters["auth/role"]);
    const isAdmin = role.value === "ADMIN";
    const adminData = ref(null);
    const isEditing = ref(false);

    const storeName = ref("");
    const promptpayId = ref("");
    const promptpayIdError = ref("");
    const storeCode = ref("");

    const userPassword = ref("");
    const newStorePassword = ref("");

    const getAdminData = async () => {
      adminData.value = await store.dispatch("store/getAdminData");
      storeName.value = adminData.value.name;
      promptpayId.value = adminData.value.promptpayId;
      storeCode.value = adminData.value.storeCode;
    };

    getAdminData();

    const toggleEdit = (value) => {
      const modalElement = document.getElementById("checkPasswordModal");
      const modal = bootstrapBundle.Modal.getInstance(modalElement);
      if (modal) modal.hide();
      isEditing.value = value;
      getAdminData();
    };

    const clearModal = () => {
      userPassword.value = "";
      newStorePassword.value = "";
      deleteStoreConfirmation.value = "";
      deleteStoreUserPassword.value = "";
    };

    const closeResetPasswordModal = () => {
      const modalElement = document.getElementById("resetPasswordModal");
      const modal = bootstrapBundle.Modal.getInstance(modalElement);
      if (modal) modal.hide();
      clearModal();
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

    const saveChanges = async () => {
      storeName.value = storeName.value.trim();
      storeCode.value = storeCode.value.trim();

      if (!validateNumber) return;

      if (storeName.value.length === 0 || storeCode.value.length === 0) return;

      const adminDto = {
        name: storeName.value.trim(),
        storeCode: storeCode.value.trim(),
        promptpayId: promptpayId.value,
      };

      const valid = await store.dispatch("store/updateStore", adminDto);
      if (valid) {
        location.reload();
      } else {
        alert("The Store code is already taken. Please use another.");
      }
    };

    const resetPassword = async () => {
      const resetPasswordRequest = {
        userPassword: userPassword.value,
        newStorePassword: newStorePassword.value,
      };

      const valid = await store.dispatch(
        "store/resetPassword",
        resetPasswordRequest
      );

      if (valid) location.reload();
      else alert("Error Setting New Password");
    };

    const deleteStorePhrase = "Delete " + storeData.value.name;
    const deleteStoreUserPassword = ref("");
    const deleteStoreConfirmation = ref("");
    const deleteStoreConfirmationError = ref("");

    const closeDeleteStoreModal = () => {
      const modalElement = document.getElementById("deleteStoreModal");
      const modal = bootstrapBundle.Modal.getInstance(modalElement);
      if (modal) modal.hide();
      clearModal();
    };

    const deleteStore = async () => {
      console.log(deleteStoreConfirmation.value);

      if (deleteStoreConfirmation.value !== deleteStorePhrase) {
        deleteStoreConfirmation.value = "";
        deleteStoreConfirmationError.value = "The Phrase Does Not Match";
        return;
      } else {
        deleteStoreConfirmationError.value = "";
        const payload = {
          userPassword: deleteStoreUserPassword.value
        }
        const valid = await store.dispatch("store/deleteStore", payload);

        if (valid)
          location.reload();
        else {
          alert("Incorect Password")
          clearModal();
        }
      }
    };

    return {
      storeData,
      isAdmin,
      adminData,
      isEditing,
      toggleEdit,
      storeName,
      storeCode,
      promptpayId,
      promptpayIdError,
      validateNumber,
      saveChanges,
      closeResetPasswordModal,
      userPassword,
      newStorePassword,
      resetPassword,
      deleteStorePhrase,
      deleteStoreUserPassword,
      deleteStoreConfirmation,
      deleteStoreConfirmationError,
      closeDeleteStoreModal,
      deleteStore,
    };
  },
};
</script>

<style></style>
