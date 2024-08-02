<template>
  <div>
    <div
      data-bs-toggle="collapse"
      :data-bs-target="collapseId"
      :aria-controls="collapseName"
    >
      <div class="container-fluid btn p-1" @click="toggleCollapse">
        <!-- Header -->
        <div
          class="row justify-content-center justify-content-sm-between align-items-center mx-4 my-1"
        >
          <div class="col-auto">
            <div
              class="row justify-content-center justify-content-sm-start align-items-center"
            >
              <h4 class="fw-light col-auto">
                {{ category }}
              </h4>
            </div>
          </div>
        </div>
      </div>
    </div>
    <hr class="mx-4 mt-1 mb-3" />

    <!--  Product Cards -->
    <div v-if="products.length" class="collapse" ref="collapseElement">
      <div class="row mx-1">
        <div
          class="col-6 col-sm-3 col-md-3 col-lg-3 col-xl-2 mb-4"
          v-for="(product, index) in products"
          :key="index"
        >
          <ProductCard
            :product="product"
            @click="openViewProductModal(product)"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed, onMounted, ref, toRefs } from "vue";
import ProductCard from "./ProductCard.vue";
import bootstrap from "bootstrap/dist/js/bootstrap.bundle";

export default {
  props: ["category", "products"],
  emits: ["openViewProductModal"],
  components: { ProductCard },
  setup(props, { emit }) {
    const { category, products } = toRefs(props);
    const collapseElement = ref(null);
    const collapseName = props.category.replace(/\s+/g, "") + "collapse";
    const collapseId = "#" + collapseName;

    let categoryCollapse = null;

    const toggleCollapse = () => {
      if (categoryCollapse) categoryCollapse.toggle();
    };

    const openViewProductModal = (product) => {
      emit("openViewProductModal", product);
    };

    onMounted(() => {
      if (collapseElement.value) {
        categoryCollapse = new bootstrap.Collapse(collapseElement.value, {
          toggle: false,
        });
        categoryCollapse.show();
      }
    });

    return {
      collapseId,
      collapseName,
      collapseElement,
      toggleCollapse,
      openViewProductModal,
      products,
      category,
    };
  },
};
</script>

<style></style>
