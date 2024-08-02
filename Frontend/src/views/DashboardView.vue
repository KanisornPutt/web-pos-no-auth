<template>
  <div v-if="user">
    <div class="row" style="width: 100%">
      <div class="container col-12 col-lg-9 col-xl-9">
        <div class="row mt-3">
          <div class="col-6">
            <h4 class="mx-5 my-2">{{ showName }}'s Dashboard</h4>
          </div>
          <div class="col-6 text-end">
            <h4 v-if="isLinkedToStore" @click="toStore" class="mx-5 my-2">
              {{ storeData.name }}
              <small class="text-secondary">{{ user.role }} </small>
            </h4>
          </div>
        </div>

        <hr class="mx-4" />

        <!-- User with no store -->
        <div v-if="!isLinkedToStore" class="container">
          <div
            class="row justify-content-center align-items-center"
            style="height: 50vh"
          >
            <div class="col-6 col-md-4 text-center">
              <h4>No Store Found</h4>
              <a class="btn btn-lg btn-orange text-light" href="/storeSetup"
                >Link to One Now</a
              >
            </div>
          </div>
        </div>

        <!-- For User's with Store id -->
        <div v-if="isLinkedToStore">
          <div class="container-fluid">
            <!-- Header -->
            <div
              class="row justify-content-center justify-content-sm-between align-items-center mx-4 my-1"
            >
              <div class="col-auto">
                <div
                  class="row justify-content-center justify-content-sm-start align-items-center"
                >
                  <h4 class="fw-light col-auto mb-0">All Items</h4>
                  <button
                    v-if="isAdmin"
                    class="btn btn-success col-auto"
                    data-bs-toggle="modal"
                    data-bs-target="#addNewProductModal"
                  >
                    Add New Product
                  </button>
                </div>
              </div>

              <form class="col-auto d-flex my-2 my-sm-0" role="search">
                <input
                  v-model="searchQuery"
                  class="form-control me-2"
                  type="search"
                  placeholder="Search"
                  aria-label="Search"
                />
              </form>
            </div>
          </div>

          <hr class="mx-4" />

          <AddNewProductModal />

          <!-- Products -->
          <!-- Loaded Product -->
          <div v-if="products">
            <!-- Have a product -->
            <div v-if="filteredProducts.length">
              <div class="row mx-1">
                <div
                  class="col-6 col-sm-4 col-md-3 col-lg-3 col-xl-2 mb-4"
                  v-for="(product, index) in filteredProducts"
                  :key="index"
                >
                  <ProductCard
                    :product="product"
                    @click="openViewProductModal(product)"
                  />
                </div>
              </div>

              <!-- Categories -->
              <div v-for="(category, index) in categories" :key="index">
                <Category
                  v-if="categoryProducts(category).length !== 0 "
                  :category="category"
                  :products="categoryProducts(category)"
                  @openViewProductModal="openViewProductModal"
                />
              </div>
            </div>

            <!-- No Product Found -->
            <div v-if="filteredProducts.length === 0 && products.length !== 0">
              <div class="container-fluid">
                <div
                  class="row justify-content-center align-items-center"
                  style="height: 20vh"
                >
                  <div class="col-12 text-center text-secondary">
                    <h1 class="display-6">No Product Found</h1>
                  </div>
                </div>
              </div>
            </div>

            <!-- No Product in store -->
            <div v-if="products.length === 0">
              <div class="container-fluid">
                <div
                  class="row justify-content-center align-items-center"
                  style="height: 50vh"
                >
                  <div class="col-12 text-center">
                    <h4 class="m-3">It seems your store has no product.</h4>
                    <img
                      class="m-3"
                      src="../assets/empty-icon.png"
                      alt="Empty"
                      style="height: 16vh"
                    />
                    <br />
                    <button
                      v-if="isAdmin"
                      class="btn btn-lg btn-success"
                      data-bs-toggle="modal"
                      data-bs-target="#addNewProductModal"
                    >
                      Add New Product
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Loading Product -->
          <div v-else>
            <div class="container-fluid">
              <div
                class="row justify-content-center align-items-center"
                style="height: 20vh"
              >
                <div class="col-12 text-center text-secondary">
                  <h1 class="display-6">Loading Product...</h1>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div
        v-if="isLinkedToStore"
        class="col-lg-3 col-xl-3 bg-body-tertiary cart"
      >
        <CartVue
          @openPaymentModal="openPaymentModal"
          @openClearCartModal="openClearCartModal"
        />
      </div>
    </div>
  </div>
  <div v-if="isLinkedToStore">
    <ProductModal
      v-if="selectedProduct"
      id="viewProductModal"
      :product="selectedProduct"
      @openEditModal="openEditProductModal"
      @closeModal="closeModal"
    />
    <EditProductModal
      v-if="selectedProduct && isAdmin"
      id="editProductModal"
      :product="selectedProduct"
      @closeModal="closeModal"
    />

    <!-- Qr-code Modal -->
    <PaymentModal
      v-if="paymentAmount"
      class="payment-modal"
      id="paymentModal"
      :reciever="reciever"
      :amount="parseFloat(paymentAmount)"
      :storeData="storeData"
    />

    <!-- Clear Cart Modal -->
    <ClearCartModal id="clearCartModal" />
  </div>
</template>

<script>
import { computed, onMounted, ref } from "vue";
import { useStore } from "vuex";
import ProductCard from "@/components/dashboard/ProductCard.vue";
import CartVue from "@/components/dashboard/Cart.vue";
import AddNewProductModal from "@/components/dashboard/AddNewProductModal.vue";
import ProductModal from "@/components/dashboard/ProductModal.vue";
import EditProductModal from "@/components/dashboard/EditProductModal.vue";
import Category from "@/components/dashboard/Category.vue";
import bootstrapBundle from "bootstrap/dist/js/bootstrap.bundle";
import router from "@/router";
import PaymentModal from "@/components/dashboard/PaymentModal.vue";
import ClearCartModal from "@/components/dashboard/ClearCartModal.vue";
export default {
  components: {
    ProductCard,
    CartVue,
    AddNewProductModal,
    Category,
    ProductModal,
    EditProductModal,
    PaymentModal,
    ClearCartModal,
  },
  setup() {
    const store = useStore();
    const user = computed(() => store.getters["auth/user"]);
    const showName = computed(() => store.getters["auth/showName"]);
    const storeId = computed(() => store.getters["auth/storeId"]);
    const storeData = computed(() => store.getters["auth/storeData"]);
    const categories = storeData.value ? storeData.value.categories : null;
    const isLinkedToStore = computed(() => store.getters["auth/storeData"]);
    const cart = computed(() => store.state.cart.cart);
    const role = computed(() => store.getters["auth/role"]);
    const isAdmin = role.value === "ADMIN";
    const products = ref(null);
    const selectedProduct = ref(null);
    const searchQuery = ref("");

    const toStore = () => {
      router.push("/store");
    };

    const getProducts = async (user) => {
      const result = await store.dispatch("products/getProducts", user);
      return result;
    };

    const filteredProducts = computed(() => {
      if (!products.value) return [];
      return products.value
        .filter((product) =>
          product.name.toLowerCase().includes(searchQuery.value.toLowerCase())
        )
        .map((product) => {
          if (!product.additionalOptions) {
            product.additionalOptions = [];
          }
          return product;
        })
        .sort((a, b) => {
          if (a.hasStock && a.stock == 0)
            return (1);
          else if (b.hasStock && b.stock == 0)
            return (-1);
          return a.name.localeCompare(b.name)});
    });

    const openViewProductModal = (product) => {
      selectedProduct.value = product;

      // console.log("selectedProduct: ", selectedProduct.value);

      setTimeout(() => {
        const viewProductElement = document.getElementById(`viewProductModal`);
        if (viewProductElement) {
          const viewProductModalInstance =
            bootstrapBundle.Modal.getOrCreateInstance(viewProductElement);
          viewProductModalInstance.show();
        }
      }, 100); // Adjust the delay if needed
    };

    const openEditProductModal = (product) => {
      // console.log("opening edit modal");
      selectedProduct.value = product;
      // console.log(selectedProduct.value.name);
      setTimeout(() => {
        const editProductElement = document.getElementById(`editProductModal`);
        if (editProductElement) {
          const editProductModalInstance =
            bootstrapBundle.Modal.getOrCreateInstance(editProductElement);
          editProductModalInstance.show();
        }
      }, 100); // Adjust the delay if needed
    };

    const closeModal = () => {
      // console.log("closing modal");
      selectedProduct.value = null;
    };

    const reciever = ref(null);

    if (isLinkedToStore.value) reciever.value = storeData.value.promptpayId;

    const paymentAmount = ref(null);

    const openPaymentModal = (amount) => {
      // console.log("opening payment modal", "amount : ", amount);
      paymentAmount.value = amount;
      setTimeout(() => {
        const paymenyElement = document.getElementById(`paymentModal`);
        if (paymenyElement) {
          const paymentModalInstance =
            bootstrapBundle.Modal.getOrCreateInstance(paymenyElement);
          paymentModalInstance.show();
        }
      }, 100); // Adjust the delay if needed
    };

    const openClearCartModal = () => {
      const clearCartModalElement = document.getElementById(`clearCartModal`);
      if (clearCartModalElement) {
        const pclearCartModalInstance =
          bootstrapBundle.Modal.getOrCreateInstance(clearCartModalElement);
        pclearCartModalInstance.show();
      }
    };

    const categoryProducts = (category) => {
      return filteredProducts.value.filter(
        (product) => product.category === category
      );
    };

    onMounted(async () => {
      products.value = await getProducts(user.value);
    });

    return {
      user,
      cart,
      storeId,
      showName,
      isLinkedToStore,
      storeData,
      toStore,
      products,
      isAdmin,
      searchQuery,
      filteredProducts,
      categories,
      selectedProduct,
      openEditProductModal,
      openViewProductModal,
      closeModal,
      reciever,
      openPaymentModal,
      paymentAmount,
      openClearCartModal,
      categoryProducts,
    };
  },
};
</script>

<style scoped>
.cart {
  /* Set default height */
  height: 100rem; /* Height from lg to xxl */
  position: relative;

  /* Media query for sm to md */
  @media (max-width: 992px) {
    height: 0vh; /* Height at xl to md */
    position: fixed;
  }
}
</style>
