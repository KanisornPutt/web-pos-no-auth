<template>
  <!-- Modal -->
  <div
    class="modal modal-lg fade"
    tabindex="-1"
    aria-labelledby="paymentModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="paymentModalLabel">Payment</h1>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <div class="container text-center">
            <div class="qr-code">
              <PromptpayQr :id="reciever" :amount="parseFloat(amount)" />
            </div>
            <h2>To {{ storeData.name }}</h2>
            <h4>Amount {{ parseFloat(amount) }} THB</h4>
          </div>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Cancel
          </button>
          <button type="button" class="btn btn-primary" @click="confirmPayment">
            Confirm Payment
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed, onMounted } from "vue";
import PromptpayQr from "../PromptpayQr.vue";
import { useStore } from "vuex";
export default {
  props: ["reciever", "amount", "storeData"],
  components: { PromptpayQr },
  setup(props) {
    const store = useStore();
    const cart = computed(() => store.state.cart.cart);

    const totalAmount = computed(() => {
      if (cart.value.length === 0) {
        return 0;
      }
      return cart.value.reduce((acc, product) => acc + product.amount, 0);
    });

    const confirmPayment = async () => {
      const payload = {
        totalPrice: props.amount,
        totalAmount: totalAmount.value,
        orderedProducts: cart.value.map((product) => ({
          ...product,
          selectedChoices: product.selectedChoices.flat(),
          additionalPrices: product.additionalPrices.flat(),
        })),
      };
      const validation = await store.dispatch(
        "histories/confirmPayment",
        payload
      );

      if (validation) {
        alert("Payment Confirmed Successfully")
        location.reload()
      }
      else alert('An error has occured')
    };

    onMounted(() => {});

    return {
      confirmPayment,
    };
  },
};
</script>

<style scoped>
.qr-code {
  width: 100%;
  height: 50vh;
  object-fit: cover;
}
</style>
