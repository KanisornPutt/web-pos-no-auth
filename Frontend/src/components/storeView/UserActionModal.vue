<template>
  <!-- Modal -->
  <div
    class="modal fade"
    id="userActionModal"
    tabindex="-1"
    aria-labelledby="ModalLabel"
    aria-hidden="true"
  >
    <form
      class="modal-dialog modal-dialog-centered"
      @submit.prevent="handleContinue"
    >
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">
            <span v-if="action == 'makeAdmin'"
              >Make {{ user.showName }} Admin</span
            >
            <span v-if="action == 'removeAsAdmin'"
              >Remove {{ user.showName }} as Admin</span
            >
            <span v-if="action == 'removeFromStore'"
              >Remove {{ user.showName }} from Store</span
            >
          </h1>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
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
            Cancel
          </button>
          <button
            v-if="action == 'makeAdmin'"
            type="submit"
            class="btn btn-primary"
          >
            Make {{ user.showName }} Admin
          </button>
          <button
            v-if="action == 'removeAsAdmin'"
            type="submit"
            class="btn btn-outline-primary"
          >
            Remove {{ user.showName }} as Admin
          </button>
          <button
            v-if="action == 'removeFromStore'"
            type="submit"
            class="btn btn-danger"
          >
            Remove {{ user.showName }} from Store
          </button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import { onMounted, ref } from "vue";
import { useStore } from "vuex";
import { Alert } from 'bootstrap/dist/js/bootstrap.bundle';
export default {
  props: ["user", "action"],
  emits: ["closeModal"],
  setup(props, { emit }) {
    const password = ref("");
    const action = props.action;
    const user = props.user;
    const store = useStore();
    const errorMsg = ref("");

    const handleContinue = async () => {
      const payload = {
        adminPassword: password.value,
        user: user,
      };
      try {
        const validation = ref(false);

        switch (action) {
          case 'makeAdmin':
            await store.dispatch('store/makeAdmin', payload)
            break;
          case 'removeAsAdmin':
            await store.dispatch('store/removeAsAdmin', payload)
            break;
          case 'removeFromStore':
            await store.dispatch('store/removeFromStore', payload)
            break;
          default:
            validation.value = false;
            alert("An error has occured. Please try again")
            location.reload();
        }
        if (validation) {
          errorMsg.value = "";
          location.reload();
        } else {
          password.value = "";
          errorMsg.value = "Incorrect password";
        }
      } catch (error) {
        console.error("Error while checking password: ", error);
        errorMsg.value = "Error occurred while checking password";
      }
    };

    onMounted(() => {
      const modal = document.getElementById("userActionModal");
      modal.addEventListener("hide.bs.modal", () => {
        password.value = "";
        errorMsg.value = "";
        emit("closeModal");
      });
    });

    return {
      password,
      action,
      errorMsg,
      user,
      handleContinue,
    };
  },
};
</script>

<style></style>
