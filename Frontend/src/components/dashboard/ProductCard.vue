<template>
  <div style="cursor: pointer" class="hover-shadow rounded">
    <div class="card m-0 .w-100">
      <img
        v-if="product.imageUrl"
        :src="product.imageUrl"
        class="card-img-top fixed-img"
        :alt="product.name"
      />
      <img
        v-else
        src="@/assets/product.png"
        class="card-img-top fixed-img"
        :alt="product.name"
      />
      <div class="card-body pb-2">
        <div class="card-info" style="height: 80px; overflow: hidden">
          <h5 class="card-title" style="max-height: 50px; overflow: hidden">
            {{ product.name }}
          </h5>
          <p class="card-text">{{ product.price }} THB</p>
        </div>
      </div>
      <div v-if="product.hasStock && product.stock == 0" class="out-of-stock-overlay rounded">
        <p class="text-center">Out of Stock</p>
      </div>
    </div>
  </div>
</template>

<script>
import { toRefs } from "vue";
export default {
  props: ["product"],
  components: {},
  computed: {},
  setup(props) {
    const { product } = toRefs(props);


    return {
      product,
    };
  },
};
</script>

<style scoped>
.product-card:hover {
  background-color: #e0e0e0; /* Darker background color on hover */
}

.hover-shadow {
  transition: box-shadow 0.3s ease-in-out;
}

.hover-shadow:hover {
  box-shadow: 0 0.5rem 1rem rgba(79, 70, 56, 0.4);
}

.hover-shadow:focus {
  outline: none;
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.2),
    0 0 0 2px rgba(67, 63, 54, 0.369);
  border-color: rgba(66, 61, 52, 0.369);
}

.out-of-stock-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(128, 128, 128, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.5rem;
  font-weight: bold;
}

.fixed-img {
  width: 100%;
  height: 158px;
  object-fit: cover;
}
</style>
