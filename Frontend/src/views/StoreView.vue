<template>
  <div class="my-3 mx-4">
    <!-- Linked to a Store  -->
    <div v-if="isLinkedToStore">
      <!-- Store Name -->
      <h1 class="mx-5">{{ storeData.name }}</h1>
      <hr class="m-3" />

      <!-- Infomation for Admin -->
      <StoreAdminData v-if="isAdmin" />

      <!-- Store members  -->
      <div>
        <div class=" mx-4 mt-3">

        <StoreList/>
        </div>

      </div>
    </div>

    <!-- Not Linked to a Store -->
    <div v-else>
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
  </div>
</template>

<script>
import { computed, ref } from "vue";
import { useStore } from "vuex";
import StoreAdminData from "@/components/storeView/StoreAdminData.vue";
import StoreList from '@/components/storeView/StoreList.vue';
export default {
  components: { StoreAdminData, StoreList },
  setup() {
    const store = useStore();
    const storeData = computed(() => store.getters["auth/storeData"]);
    const isLinkedToStore = computed(() => store.getters["auth/storeData"]);
    const role = computed(() => store.getters["auth/role"]);
    const isAdmin = role.value === "ADMIN";
    
    return {
      isLinkedToStore,
      storeData,
      isAdmin,
    };
  },
};
</script>

<style></style>
