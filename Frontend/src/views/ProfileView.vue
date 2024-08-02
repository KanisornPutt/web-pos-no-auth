<template>
  <div class="container m-5 mx-auto border border-hide border-tertiary rounded-4" v-if="isLoggined">
    <ProfileData v-if="!isEditing" @toggleContent="toggleContent"/>
    <ProfileEdit v-if="isEditing" @toggleContent="toggleContent"/>
  </div>
</template>

<script>
import ProfileData from "@/components/profile/ProfileData.vue";
import { computed, ref } from 'vue';
import { useStore } from 'vuex';
import ProfileEdit from '@/components/profile/ProfileEdit.vue';
export default {
  components: { ProfileData, ProfileEdit },
  setup() {
    const store = useStore();
    const isLoggined = computed(() => store.getters['auth/isAuthenticated']);

    const isEditing = ref(false) 

    const toggleContent = () => {
      isEditing.value = !isEditing.value;
    }

    return {
      isLoggined,
      toggleContent,
      isEditing,

    }
  }
};
</script>

<style scode>
@media (max-width: 768px) {
  .border-hide {
    border: none !important; /* Hide the border */
  }
}
</style>
