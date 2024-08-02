<template>
  <!-- Modal -->
  <div
    class="modal fade"
    :id="modalId"
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
            {{ modalTitle }}
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
          <button type="submit" class="btn btn-primary">Continue</button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import { onMounted, ref } from "vue";
import { useStore } from "vuex";
export default {
  props: ["modalId", "modelTitle"],
  emits: ["continue"],
  setup(props, { emit }) {
    const password = ref("");
    const modalTitle = props.modelTitle;
    const store = useStore();
    const errorMsg = ref("");

    onMounted(() => {
      const modal = document.getElementById(props.modalId);
      modal.addEventListener("hide.bs.modal", () => {
        password.value = "";
        errorMsg.value = "";
      });
    });

    const handleContinue = async () => {
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
          emit("continue");
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
      password,
      modalTitle,
      errorMsg,
      handleContinue,
    };
  },
};
</script>

<style></style>
