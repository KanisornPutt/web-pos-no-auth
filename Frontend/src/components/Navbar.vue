<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
  <div class="container-fluid">
    <a class="navbar-brand" href="/">Web-Pos</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
      <div class="navbar-nav ms-auto me-5">
        <a class="nav-link" href="/">Home</a>

        <!-- For logged out user -->
        <a class="nav-link" v-if="!isLoggined" href="/login">Login</a>
        <div class="d-grid gap-2 d-sm-block my-auto">
          <a class=" btn btn-outline-success" v-if="!isLoggined" href="/signup">Join Now</a>
        </div>
        

        <!-- For logged in user -->
        <div class="nav-item dropdown" v-if="isLoggined">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            {{show_name}}
          </a>
          <ul class="dropdown-menu dropdown-menu-start ">
            <li><a class="dropdown-item" href="/profile">Profile</a></li>
            <li><a class="dropdown-item" href="/store">Store</a></li>
            <li><a class="dropdown-item" href="#">Settings</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="logout-dropdown dropdown-item" @click="logout">Logout</a></li>
          </ul>
        </div>
        <a class="nav-link" v-if="isLoggined" href="/dashboard">Dashboard</a>


      </div>
    </div>
  </div>
</nav>
</template>

<script>
import { computed, ref } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
export default {
  setup() {

    const store = useStore();
    const router = useRouter();
    const isLoggined = computed(() => store.getters['auth/isAuthenticated']);
    const show_name = computed(() => store.getters['auth/showName']);


    const logout = () => {
      store.dispatch('auth/logout').then(() => {
        router.push('/');
      });
    };

    return { logout, isLoggined, show_name }

  }
};
</script>

<style>

.navbar .dropdown-menu .logout-dropdown {
  color: crimson;
  border-radius: 5px;
}

.navbar .dropdown-menu .logout-dropdown:hover {
  color: rgb(209, 0, 0);
}

.navbar .dropdown-menu .logout-dropdown:active {
    background-color: crimson;
    color: white 
}

</style>
