<template>
  <div
    class="container mb-2"
    data-bs-toggle="collapse"
    :href="collapseHref"
    role="button"
    @click="updateCart"
    aria-expanded="false"
    :aria-controls="collapseHref"
  >
    <!-- Headers -->
    <div class="row align-items-center">
      <div class="col-3">
        <p class="mb-0">
          x <span>{{ product.amount }}</span>
        </p>
      </div>
      <div class="col-7 p-0">
        <p class="mb-0 overflow-data">{{ product.name }}</p>
      </div>
      <div class="col-2 p-0">
        <p class="mb-0">{{ product.singlePrice * product.amount }}</p>
      </div>
    </div>

    <!-- Subcontent -->
    <!-- Additional Options -->
    <div v-for="(choices, i) in product.selectedChoices" :key="i">
      <div v-for="(choice, j) in product.selectedChoices[i]" :key="j">
        <div class="row align-items-center">
          <div class="col-3"></div>
          <div class="col-7 p-0">
            <div v-if="choices.length > 0">
              <p
                class="fw-light my-0 fs-6 .overflow-data { overflow: hidden; white-space: nowrap; text-overflow: ellipsis; }"
              >
                - {{ choice }}
              </p>
            </div>
          </div>
          <div class="col-2 p-0">
            <div v-if="choices.length > 0">
              <p class="fw-light my-0 fs-6 mb-0 p-0" v-if="product.additionalPrices[i][j] != 0">
                <span v-if="product.additionalPrices[i][j] > 0">+</span
                >{{ product.additionalPrices[i][j] }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Subcontent 2 -->
    <!-- Notes -->
    <div class="row align-items-center">
      <div class="col-3"></div>
      <div class="col-9 p-0">
        <!-- Note -->
        <p class="fw-light my-0">{{ product.note }}</p>
      </div>
    </div>

    <!--dded Price -->
    <div class="row align-items-center">
      <div class="col-3"></div>
      <div class="col-7 p-0">
        <!-- Added Price -->
        <p v-if="product.addedPrice !== ''" class="fw-light my-0">- Added :</p>
      </div>
      <div class="col-2 p-0">
        <p class="fw-light my-0">
          <span v-if="product.addedPrice > 0">+</span> {{ product.addedPrice }}
        </p>
      </div>
    </div>
  </div>

  <div class="collapse" :id="collapseId">
    <div class="card card-body p-3 pt-0">
      <!-- Amount -->
      <p class="mt-2 mb-1">Amount</p>
      <div
        class="row justify-content-center align-items-center"
        style="width: 100%"
      >
        <div class="col-3 text-start">
          <button class="btn btn-outline-secondary" @click="setAmount(-1)">
            <i class="bi bi-dash"></i>
          </button>
        </div>
        <div class="col-6 text-center">
          <textarea
            type="text"
            class="form-control fw-bold ms-2"
            v-model="amount"
            style="height: 1rem"
            @input="handleAmountInput"
          ></textarea>
        </div>
        <div class="col-3 text-end">
          <button class="btn btn-outline-secondary" @click="setAmount(1)">
            <i class="bi bi-plus"></i>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed, ref } from "vue";
import { useStore } from "vuex";
export default {
  props: ["productId"],
  setup(props) {
    const store = useStore();
    const product = computed(() => {
      return store.getters["cart/getProductFromCartById"](props.productId);
    });

    const collapseId = ref("collapse-" + props.productId);
    const collapseHref = ref("#collapse-" + props.productId);
    const amount = ref(product.value.amount);

    const setAmount = (load) => {
      amount.value = amount.value + load;
      if (amount.value <= 0) amount.value = 0;
      if (product.value.stock) {
        if (amount.value >= product.value.stock)
          amount.value = product.value.stock;
      }
      product.value.amount = amount.value;
    };

    const handleAmountInput = (event) => {
      let inputValue = event.target.value;
      inputValue = inputValue.replace(/\D/g, "");
      amount.value = parseInt(inputValue);
      if (isNaN(amount.value)) amount.value = 0;
      setAmount(0);
    };

    const updateCart = () => {
      amount.value = product.value.amount;
      if (product.value.amount === 0) {
        store.commit("cart/removeProductFromCart", props.productId);
      }
    };

    return {
      collapseHref,
      collapseId,
      amount,
      setAmount,
      handleAmountInput,
      updateCart,
      product,
    };
  },
};
</script>

<style scoped>
.overflow-data {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
