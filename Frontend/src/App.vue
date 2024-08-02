<template>
  <NavbarVue/>
  <router-view />
</template>

<script>
import NavbarVue from './components/Navbar.vue'
export default {
  components: { NavbarVue },
  setup() {
    
  },
}
</script>


<style>

</style>
