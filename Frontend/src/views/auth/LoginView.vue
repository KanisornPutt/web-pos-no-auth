<template>
  <div class="row justify-content-center m-3">
    <div class="col-md-8">
      <div class=" d-md-block conatainer-md p-4 border rounded-4">
        <h1>Login Page</h1>
        <LoginForm />
      </div>
    </div>
  </div>
</template>

<script>
import LoginForm from "@/components/auth/LoginForm.vue";
export default {
  components: { LoginForm },
};
</script>

<style></style>
