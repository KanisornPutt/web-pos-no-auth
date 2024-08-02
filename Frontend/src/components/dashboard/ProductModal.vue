<template>
  <div
    class="modal modal-lg fade modal-fullscreen-md-down"
    tabindex="-1"
    aria-labelledby="productModalLabel"
    aria-hidden="true"
    ref="productModal"
  >
    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
      <div class="modal-content">
        <!-- Header -->
        <div class="modal-header">
          <h2 class="modal-title ms-2" id="productModalLabel">
            {{ product.name }}
            <span
              v-if="product.stock === 0 && product.hasStock"
              class="text-danger"
              >Out of Stock</span
            >
          </h2>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
            @click="closeModal"
          ></button>
        </div>

        <!-- Body -->
        <div class="modal-body">
          <div class="row justify-content-center">
            <!-- Img -->
            <div class="col-8 col-lg-6 mb-3">
              <img
                v-if="product.imageUrl"
                :src="product.imageUrl"
                class="card-img-top fixed-img rounded-2"
                :alt="product.name"
              />
              <img
                v-else
                src="@/assets/product.png"
                class="card-img-top fixed-img rounded-2"
                :alt="product.name"
              />
            </div>

            <!-- Body -->
            <div class="col-11 col-lg-6">
              <h4 class="mb-0">
                {{ product.name }}
                <span>
                  <small
                    v-if="product.category"
                    class="mt-0 mb-2 fw-light fs-5"
                    >{{ product.category }}</small
                  >
                  <small v-else class="mt-0 mb-2 fw-light fs-6"
                    >No Category</small
                  >
                </span>
              </h4>
              <hr class="my-0" />

              <p class="mt-0 mb-2">{{ product.description }}</p>

              <!-- Options -->
              <div v-if="product.additionalOptions.length !== 0">
                <p class="mt-2 mb-0">Additional Options</p>
                <hr class="my-0" />
                <div
                  v-for="(o, optionIndex) in product.additionalOptions"
                  :key="optionIndex"
                  :id="'option-' + optionIndex"
                >
                  <p class="m-0">
                    {{ o.name }}
                    <span class="fw-light" v-if="o.required"> required </span>
                    <span v-if="o.min == o.max" class="fw-light"
                      >pick: {{ o.min }}</span
                    >
                    <span v-if="o.min == 0" class="fw-light"
                      >optional, max {{ o.max }}</span
                    >
                    <span v-if="o.min != 0 && o.min != o.max" class="fw-light"
                      >choose from {{ o.min }} to {{ o.max }}</span
                    >
                  </p>

                  <p
                    v-if="invalidSelection[optionIndex]"
                    class="text-danger fs-6 mb-0"
                  >
                    select at least {{ o.min }}
                  </p>

                  <div
                    v-for="(choice, choiceIndex) in o.choices"
                    :key="choiceIndex"
                  >
                    <div class="form-check">
                      <input
                        class="form-check-input"
                        type="checkbox"
                        :id="'choice-' + o.id + '-' + choice"
                        :value="choice"
                        v-model="selectedChoices[optionIndex]"
                        @change="handleChoiceChange(optionIndex, choiceIndex)"
                      />
                      <label
                        class="form-check-label"
                        :for="'choice-' + o.id + '-' + choice"
                      >
                        {{ choice }}
                        <span
                          class="text-secondary"
                          v-if="o.prices[choiceIndex] !== 0"
                          >(<span v-if="o.prices[choiceIndex] > 0">+</span
                          >{{ o.prices[choiceIndex] }})</span
                        >
                      </label>
                    </div>
                  </div>
                </div>
              </div>

              <div class="mx-auto">
                <!-- Notes Textbox -->
                <div class="my-2">
                  <p class="mb-1">Notes</p>
                  <textarea
                    class="border border-secondary mt-0 mx-auto"
                    name="addons"
                    id="addons"
                    style="
                  height: 4rem;
                  width: 90%;
                  m-2
                "
                    v-model="note"
                  ></textarea>
                </div>

                <!-- Added Price -->
                <div class="mb-2">
                  <label for="addedPriceInput" class="form-label"
                    >Added Price:</label
                  >
                  <input
                    type="number"
                    class="form-control"
                    id="addedPriceInput"
                    aria-describedby="priceError"
                    placeholder="00.00"
                    v-model="addedPrice"
                    step="0.01"
                    style="width: 90%"
                  />
                </div>

                <!-- Amount -->
                <div>
                  <p class="mt-2 mb-0">Amount</p>
                  <p class="text-secondary mb-1" v-if="product.hasStock">
                    Stock : {{ product.stock }}
                  </p>
                  <p class="text-secondary mb-1" v-else>Stock : N/A</p>
                  <div
                    class="row justify-content-center align-products-center"
                    style="width: 100%"
                  >
                    <div class="col-3 text-start">
                      <button
                        class="btn btn-outline-secondary"
                        @click="setAmount(-1)"
                      >
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
                        id="amountInput"
                      ></textarea>
                    </div>
                    <div class="col-3 text-end">
                      <button
                        class="btn btn-outline-secondary"
                        @click="setAmount(1)"
                      >
                        <i class="bi bi-plus"></i>
                      </button>
                    </div>
                    <div>
                      <p class="text-center text-danger mt-0 mb-3">
                        {{ invalidAmountMsg }}
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Footer -->
        <div class="modal-footer justify-content-between">
          <h5 class="ms-0">Product Price : {{ totalPrice }}</h5>
          <div class="ms-auto">
            <button
              v-if="isAdmin"
              type="button"
              class="btn btn-outline-secondary"
              @click="handleEditProduct"
            >
              Edit Product
            </button>

            <button
              type="button"
              class="btn btn-secondary mx-2 px-3"
              @click="closeModal"
            >
              Cancel
            </button>
            <button
              type="button"
              @click="handleAddToCart"
              class="btn btn-primary"
            >
              Add to Cart
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  computed,
  nextTick,
  onBeforeUnmount,
  onMounted,
  ref,
  watch,
} from "vue";
import { useStore } from "vuex";
import bootstrapBundle from "bootstrap/dist/js/bootstrap.bundle";
import EditProductModal from "./EditProductModal.vue";
export default {
  props: ["product"],
  emits: ["openEditModal", "closeModal"],
  components: { EditProductModal },
  setup(props, { emit }) {
    const store = useStore();
    const productModal = ref(null);
    const note = ref("");
    const product = props.product;
    const amount = ref(1);

    if (product.hasStock && product.stock === 0) {
      amount.value = 0;
    }

    const invalidAmountMsg = ref("");
    const addedPrice = ref("");
    const addedPriceValue = ref(0);
    const singlePrice = ref(product.price);
    const totalPrice = ref(amount.value * singlePrice.value);
    const role = computed(() => store.getters["auth/role"]);
    const isAdmin = role.value === "ADMIN";

    // Initialize selected choices
    const selectedChoices = ref({});
    const additionalPrices = ref({});
    const invalidSelection = ref({});
    props.product.additionalOptions.forEach((option, index) => {
      selectedChoices.value[index] = [];
      additionalPrices.value[index] = [];
      invalidSelection.value[index] = false;
    });

    watch(addedPrice, (newValue, oldValue) => {
      if (addedPrice.value != "") addedPriceValue.value = parseFloat(newValue);
      else addedPriceValue.value = 0;
      updateTotalPrice(); // Update total price whenever singlePrice changes
    });

    const updateTotalPrice = () => {
      const flattenedArray = Object.values(additionalPrices.value).flat();
      const sumOfAdditionalPrices = flattenedArray.reduce(
        (accumulator, currentValue) => accumulator + currentValue,
        0
      );

      singlePrice.value =
        product.price + addedPriceValue.value + sumOfAdditionalPrices;

      totalPrice.value = amount.value * singlePrice.value;
    };

    const setAmount = (load) => {
      amount.value = amount.value + load;
      if (amount.value <= 0) amount.value = 0;
      if (product.hasStock) {
        if (amount.value >= product.stock) amount.value = product.stock;
      }
      updateTotalPrice();
    };

    const handleAmountInput = (event) => {
      let inputValue = event.target.value;
      inputValue = inputValue.replace(/\D/g, "");
      amount.value = parseFloat(inputValue);
      if (isNaN(amount.value)) amount.value = 0;
      setAmount(0);
    };

    const clearModal = () => {
      amount.value = 1;
      note.value = "";
      props.product.additionalOptions.forEach((option, index) => {
        selectedChoices.value[index] = [];
        additionalPrices.value[index] = [];
      });
      addedPrice.value = "";
      totalPrice.value = product.price;
      nextTick(() => {
        emit("closeModal");
      });
    };

    const closeModal = () => {
      clearModal();
      const modalElement = productModal.value;
      const modalInstance = bootstrapBundle.Modal.getInstance(modalElement);
      if (modalInstance) {
        modalInstance.hide();
      }
    };

    onMounted(() => {
      nextTick(() => {
        const modalElement = productModal.value;
        if (modalElement) {
          bootstrapBundle.Modal.getOrCreateInstance(modalElement);
          modalElement.addEventListener("hide.bs.modal", clearModal);
        }
      });
    });

    onBeforeUnmount(() => {
      const modalElement = productModal.value;
      if (modalElement) {
        modalElement.removeEventListener("hide.bs.modal", clearModal);
      }
    });

    const handleChoiceChange = (optionIndex, choiceIndex) => {
      const option = product.additionalOptions[optionIndex];
      const price = option.prices[choiceIndex];
      const choice = option.choices[choiceIndex];
      let selected = selectedChoices.value[optionIndex];
      let additionalPrice = additionalPrices.value[optionIndex];

      if (selected.includes(choice)) {
        additionalPrice.push(price);
      } else {
        const index = additionalPrice.indexOf(price);
        if (index !== -1) {
          additionalPrice.splice(index, 1);
        }
      }

      // Handle single selection case
      if (option.max === 1 && option.min === 1 && selected.length > 1) {
        selected.splice(0, 1);
        additionalPrice.splice(0, 1);
      }
      // Ensure we don't exceed the max number of selections
      if (selected.length > option.max) {
        const exceeded = selected.length - option.max;
        selected.splice(option.max, exceeded);
        additionalPrice.splice(option.max, exceeded);
      }

      updateTotalPrice();
    };

    const validateSelections = () => {
      let validation = true;
      for (let i = 0; i < product.additionalOptions.length; i++) {
        const option = product.additionalOptions[i];
        const selected = selectedChoices.value[i];

        if (selected.length < option.min) {
          invalidSelection.value[i] = true;
          validation = false; // Validation failed
        } else invalidSelection.value[i] = false;
      }
      return validation; // Validation passed
    };

    const handleAddToCart = () => {
      let validation = true;
      if (!validateSelections()) {
        validation = false;

        for (const [optionIndex, invalid] of Object.entries(
          invalidSelection.value
        )) {
          if (invalid) {
            console.log("invalid", optionIndex);
            const optionElement = document.getElementById(
              `option-${optionIndex}`
            );
            if (optionElement) {
              optionElement.scrollIntoView({ behavior: "smooth" });
            }
            break;
          }
        }
      }

      if (product.hasStock && product.stock === 0) {
        invalidAmountMsg.value = "Product is out of stock";
        validation = false;
        const amountElemet = document.getElementById(`amountInput`);
        if (amountElemet) {
          amountElemet.scrollIntoView({ behavior: "smooth" });
        }
      } else if (amount.value <= 0) {
        invalidAmountMsg.value = "Amount is currently 0";
        validation = false;
        const amountElemet = document.getElementById(`amountInput`);
        if (amountElemet) {
          amountElemet.scrollIntoView({ behavior: "smooth" });
        }
      }
      if (validation) {
        const productInCart = {
          name: product.name,
          productId: product.id,
          originalPrice: product.price,
          category: product.category,
          singlePrice: singlePrice.value,
          addedPrice: parseFloat(addedPrice.value),
          amount: amount.value,
          note: note.value.trim(),
          additionalPrices: Object.values(additionalPrices.value),
          selectedChoices: Object.values(selectedChoices.value),
        };
        store.commit("cart/addProductToCart", productInCart);
        closeModal();
      } else {
        return;
      }
    };

    const handleEditProduct = () => {
      closeModal();
      nextTick(() => {
        emit("openEditModal", product);
      });
    };

    return {
      handleAddToCart,
      closeModal,
      handleAmountInput,
      productModal,
      amount,
      setAmount,
      note,
      invalidAmountMsg,
      addedPrice,
      totalPrice,
      isAdmin,
      selectedChoices,
      handleChoiceChange,
      handleEditProduct,
      invalidSelection,
    };
  },
};
</script>

<style>
.fixed-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
