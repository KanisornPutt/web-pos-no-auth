<template>
  <div id="historyModal" class="modal fade modal-xl" tabindex="-1">
    <div
      class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-fullscreen-lg-down"
    >
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">
            Transaction History #{{ history.historyNumber }}
          </h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>

        <!-- Modal history -->
        <div class="modal-body mx-3">
          <!-- Header -->
          <div class="row justify-content-center">
            <div class="col-8 text-center mb-3">
              <h3>Transaction History ID #{{ history.historyNumber }}</h3>
              <hr />
            </div>
          </div>

          <!-- When Where Who -->
          <div class="row justify-content-between">
            <div class="col-auto mb-3">
              <h5>Date: {{ getDate(history.createdAt) }}</h5>
              <h5>Time: {{ getTime(history.createdAt) }}</h5>
            </div>
            <div class="col-auto mb-3">
              <h5>Store: {{ history.storeName }}</h5>
              <h5>By: {{ history.merchantName }}</h5>
            </div>
          </div>

          <!-- Contents -->
          <div>
            <h4>Contents</h4>

            <!-- Content Headers -->
            <hr />
            <div class="row">
              <div class="col-1"></div>
              <div class="col-6">Product Name</div>
              <div class="col-2">
                <span class="d-block d-sm-none">Qty.</span>
                <span class="d-none d-sm-block">Quantity</span>
              </div>
              <div class="col-2">
                <span class="d-block d-sm-none">Ttl.</span>
                <span class="d-none d-sm-block">Amount</span>
              </div>
            </div>
            <hr />

            <!-- Products -->
            <div
              v-for="(product, index) in history.orderedProducts"
              :key="index"
            >
              <div class="row">
                <div class="col-1">{{ index + 1 }}</div>
                <div class="col-6">
                  {{ product.name }}
                  <div class="fw-light">
                  <div v-for="(choice, s) in product.selectedChoices" :key="s">
                    - {{ choice }}
                  </div>
                  <div> - {{product.note}}</div>
                  <div v-if="product.addedPrice !== 0"> - Added price: {{product.addedPrice}}</div>
                </div>
                </div>
                <div class="col-2">
                  {{ product.amount }}
                </div>
                <div class="col-2">
                  {{ product.singlePrice * product.amount }}
                </div>
              </div>
            </div>

            <!-- Total -->
            <hr class="my-2" />
            <div class="row">
              <div class="col-1"></div>
              <div class="col-6">Total</div>
              <div class="col-2">
                {{ history.totalAmount }}
              </div>
              <div class="col-2">
                {{ history.totalPrice }}
              </div>
            </div>
            <hr class="mt-2 mb-0 pb-0"/>
            <hr class="mt-1 pt-1"/>
          </div>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Close
          </button>
          <!-- <button type="button" class="btn btn-primary">Save changes</button> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted } from "vue";
export default {
  props: ["history"],
  emits: ["closeModal"],
  setup(props, { emit }) {
    const getDate = (dateTime) => {
      function getDateWithSuffix(day) {
        if (day >= 11 && day <= 13) {
          return day + "th";
        }
        switch (day % 10) {
          case 1:
            return day + "st";
          case 2:
            return day + "nd";
          case 3:
            return day + "rd";
          default:
            return day + "th";
        }
      }

      const months = [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
      ];
      return `${getDateWithSuffix(dateTime.getDate())} ${
        months[dateTime.getMonth()]
      } ${dateTime.getFullYear()}`;
    };

    const getTime = (dateTime) => {
      const hours = dateTime.getHours();
      const minutes = dateTime.getMinutes();
      const seconds = dateTime.getSeconds();

      // Format the time with leading zeros if needed
      const formattedTime = `${hours.toString().padStart(2, "0")}:${minutes
        .toString()
        .padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`;

      return formattedTime;
    };

    onMounted(() => {
      const modal = document.getElementById("historyModal");
      modal.addEventListener("hide.bs.modal", () => {
        emit("closeModal");
      });
    });

    return {
      getDate,
      getTime,
    };
  },
};
</script>

<style></style>
