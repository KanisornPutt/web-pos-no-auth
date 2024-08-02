<template>
  <!-- Clear Cart Modal -->
  <div
    class="modal fade"
    id="clearCartModal"
    tabindex="-1"
    aria-labelledby="clearCartModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="clearCartModalLabel">
            Clearing Cart
          </h1>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">Are You Sure?</div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Cancel
          </button>
          <button
            type="button"
            class="btn btn-danger"
            data-bs-dismiss="modal"
            @click="clearCart"
          >
            Clear Cart
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useStore } from 'vuex';
export default {
  setup() {
    const store = useStore();
    const clearCart = () => {
      store.commit("cart/clearCart");
    };


    return {
        clearCart,
    }
  },
};
</script>

<style></style>
