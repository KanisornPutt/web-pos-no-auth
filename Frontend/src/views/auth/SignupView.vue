<template>
  <div class="row justify-content-center m-3">
    <div class="col-md-8">
      <div class="d-md-block conatainer-md p-4 border rounded-4">
        <h1>Sign Up Page</h1>
        <SignupForm />
      </div>
    </div>
  </div>
</template>

<script>
import SignupForm from "@/components/auth/SignupForm.vue";
export default {
  components: { SignupForm },
};
</script>

<style></style>
