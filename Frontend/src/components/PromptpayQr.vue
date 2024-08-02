<template>
  <div class="fill" v-html="qrString"></div>
</template>

<script>
import generatePayload from "promptpay-qr";
import qr from "qrcode";

export default {
  props: {
    id: { type: String, required: true },
    amount: { type: Number, required: true },
  },
  data() {
    return {
      qrString: "",
    };
  },
  watch: {
    id(newId) {
      this.generateSvg();
    },
    amount(newAmount) {
      this.generateSvg();
    },
  },
  methods: {
    generateSvg() {
      const payload = generatePayload(this.id, { amount: this.amount });
      qr.toString(
        payload,
        { type: "svg", errorCorrectionLevel: "L" },
        (err, svg) => {
          if (err) {
            window.alert("Cannot generate QR code: " + String(err));
            return;
          }
          this.qrString = svg.replace(
            "<svg ",
            '<svg width="100%" height="100%" '
          );
        }
      );
    },
  },

  mounted() {
    this.generateSvg();
  },
};
</script>

<style scoped>
.fill {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
