<template>
  <div
    class="modal modal-md fade modal-fullscreen-md-down"
    id="editProductModal"
    tabindex="-1"
    aria-labelledby="itemModalLabel"
    aria-hidden="true"
    ref="editProductModal"
  >
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <form @submit.prevent="updateProduct">
          <div class="modal-header">
            <h1 class="modal-title fs-5" id="editProductModalLabel">
              Editing {{ product.name }}
            </h1>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
              @click="clearModal"
            ></button>
          </div>
          <div class="modal-body" style="max-height: 75vh; overflow-y: auto">
            <div class="row justify-content-center">
              <div
                class="col-8 col-md-6 justify-content-center align-items-center"
              >
                <!-- Image Display with fixed frame -->
                <div class="image-container justify-content-center">
                  <div class="image-frame mx-auto">
                    <img
                      :src="editImageSrc"
                      class="rounded-4"
                      alt="Product Image"
                    />
                    <!-- <img
                      v-else
                      src="@/assets/product.png"
                      class="rounded-4"
                      alt="Product Image"
                    /> -->
                  </div>

                  <!-- Customized File Input -->
                  <div class="image-upload mt-3">
                    <label for="edit-image" class="btn btn-sm btn-success">
                      Upload Image
                      <input
                        type="file"
                        accept=".png, .jpg, .jpeg"
                        id="edit-image"
                        @change="handleEditFileUpload"
                        style="display: none"
                      />
                    </label>
                    <p v-if="editImageFile">{{ editImageFile.name }}</p>
                  </div>
                </div>
              </div>

              <div class="col-10 col-md-6">
                <!-- Product Name -->
                <div class="mb-3">
                  <label for="nameInput" class="form-label"
                    >Product Name:</label
                  >
                  <input
                    type="text"
                    class="form-control"
                    id="nameInput"
                    aria-describedby="nameError"
                    required
                    placeholder="Product Name"
                    v-model="productName"
                  />
                  <div id="nameError" class="form-text">{{ nameError }}</div>
                </div>

                <!-- Product Description -->
                <div class="mb-3">
                  <label for="desInput" class="form-label">Description:</label>
                  <textarea
                    class="form-control description-input"
                    id="desInput"
                    rows="2"
                    placeholder="Product Description"
                    v-model="description"
                  ></textarea>
                </div>

                <!-- Price -->
                <div class="mb-3">
                  <label for="priceInput" class="form-label">Price:</label>
                  <input
                    type="number"
                    class="form-control"
                    id="priceInput"
                    aria-describedby="priceError"
                    required
                    placeholder="00.00"
                    v-model="price"
                    step="0.01"
                  />
                  <div id="priceError" class="form-text">{{ priceError }}</div>
                </div>
              </div>
            </div>

            <!-- Under picture -->
            <div class="row justify-content-center">
              <!-- Additional Options -->
              <div class="mb-3">
                <label class="form-label mx-2">Additional Options:</label>
                <div
                  v-for="(option, oIndex) in additionalOptions"
                  :key="oIndex"
                  class="accordion m-0 p-0"
                >
                  <div class="accordion-item">
                    <h2 class="accordion-header" :id="'optionHeading' + oIndex">
                      <button
                        class="accordion-button"
                        type="button"
                        data-bs-toggle="collapse"
                        :data-bs-target="'#collapseOption' + oIndex"
                        aria-expanded="true"
                        :aria-controls="'collapseOption' + oIndex"
                      >
                        {{ option.name || "Option " + (oIndex + 1) }}
                      </button>
                    </h2>
                    <div
                      :id="'collapseOption' + oIndex"
                      class="accordion-collapse collapse show"
                      :aria-labelledby="'optionHeading' + oIndex"
                      data-bs-parent="#accordionExample"
                    >
                      <div class="accordion-body">
                        <div class="input-group">
                          <input
                            type="text"
                            class="form-control form-control-sm"
                            v-model="option.name"
                            placeholder="Option Name"
                            required
                          />
                          <button
                            class="btn btn-outline-danger btn-sm"
                            type="button"
                            @click="removeOption(oIndex)"
                          >
                            Remove
                          </button>
                        </div>

                        <div
                          class="row justify-content-center mx-0 my-2 px-0 align-items-center"
                        >
                          <!-- Required -->
                          <div class="col-3 p-0">
                            <div class="form-check my-2">
                              <input
                                class="form-check-input border-secondary"
                                type="checkbox"
                                id="optionEnabled"
                                v-model="option.required"
                                @change="updateMin(oIndex)"
                              />
                              <label
                                class="form-check-label"
                                for="optionEnabled"
                              >
                                Required
                              </label>
                            </div>
                          </div>

                          <!-- Min -->
                          <div class="col-4 p-0">
                            <div class="input-group p-2">
                              <span class="input-group-text">Min</span>
                              <input
                                type="number"
                                class="form-control"
                                v-model="option.min"
                                :disabled="!option.required"
                                :min="option.required ? 1 : 0"
                                :max="option.max"
                                step="1"
                              />
                            </div>
                          </div>

                          <!-- Max -->
                          <div class="col-4 p-0">
                            <div class="input-group p-0">
                              <span class="input-group-text">Max</span>
                              <input
                                type="number"
                                class="form-control"
                                v-model="option.max"
                                :disabled="option.choices.length === 0"
                                :min="1"
                                :max="option.choices.length"
                                step="1"
                              />
                            </div>
                          </div>
                        </div>
                        <div
                          v-for="(choice, cIndex) in option.choices"
                          :key="cIndex"
                          class="input-group mb-3 rounded-2"
                        >
                          <span class="fw-bold align-item-center mx-2"
                            ><i class="bi bi-dot"></i
                          ></span>
                          <input
                            type="text"
                            class="form-control form-control-sm"
                            :value="choice"
                            @input="
                              updateChoice(oIndex, cIndex, $event.target.value)
                            "
                            @blur="removeEmptyChoice(oIndex, cIndex)"
                            placeholder="Choice"
                            required
                          />
                          <input
                            type="number"
                            class="form-control"
                            required
                            placeholder="00.00"
                            @input="
                              updateAdditionalPrice(
                                oIndex,
                                cIndex,
                                $event.target.value
                              )
                            "
                            v-model="option.prices[cIndex]"
                            @blur="removeEmptyChoice(oIndex, cIndex)"
                            step="0.01"
                          />
                          <button
                            v-if="cIndex === option.choices.length - 1"
                            class="btn btn-outline-primary btn-sm"
                            type="button"
                            @click="addChoice(oIndex)"
                          >
                            Add Choice
                          </button>
                        </div>
                        <div v-if="option.choices.length === 0">
                          <button
                            class="btn btn-sm btn-primary"
                            type="button"
                            @click="addChoice(oIndex)"
                          >
                            Add Choice
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <button
                  type="button"
                  class="btn btn-sm btn-primary"
                  @click="addOption"
                >
                  Add Additional Option
                </button>
              </div>

              <!-- Category -->
              <div class="col-10 col-md-6">
                <label for="categorySelect" class="form-label">Category:</label>
                <select
                  class="form-select"
                  id="categorySelect"
                  aria-label="Default select example"
                  v-model="selectedCategory"
                >
                  <option value="---NONE---">NONE</option>
                  <option
                    v-for="(category, index) in storeData.categories"
                    :key="index"
                    :value="category"
                  >
                    {{ category }}
                  </option>
                  <option value="---NEW---">Add New Category</option>
                </select>
                <div v-if="selectedCategory === '---NEW---'" class="mt-2">
                  <input
                    type="text"
                    class="form-control"
                    placeholder="Enter New Category"
                    v-model="newCategory"
                    required
                  />
                </div>
              </div>

              <!-- Stock -->
              <div class="col-10 col-md-6">
                <div>
                  <input
                    class="form-check-input me-2"
                    type="checkbox"
                    id="flexCheckDefault"
                    v-model="isStockEnabled"
                  />
                  <label for="stockInput" class="form-label">Stock:</label>
                </div>
                <input
                  type="number"
                  class="form-control"
                  id="stockInput"
                  aria-describedby="stockError"
                  required
                  placeholder="0"
                  v-model="stock"
                  :disabled="!isStockEnabled"
                  step="1"
                />
                <div id="stockError" class="form-text">{{ stockError }}</div>
              </div>
            </div>
          </div>

          <!-- Footer -->
          <div class="modal-footer justify-content-between" style="height: 20%">
            <button
              :class="
                collapseDelete ? 'btn btn-secondary' : 'btn btn-outline-danger'
              "
              type="button"
              @click="toggleCollapseDelete"
              data-bs-toggle="collapse"
              data-bs-target="#deleteProductCollapse"
              aria-expanded="false"
              aria-controls="deleteProductCollapse"
            >
              {{
                collapseDelete ? "Cancel Deletion" : "Delete " + product.name
              }}
            </button>
            <div>
              <button
                type="button"
                class="btn btn-secondary mx-2"
                data-bs-dismiss="modal"
              >
                Cancel
              </button>
              <button type="submit" class="btn btn-success">
                Save Product
              </button>
            </div>
          </div>
          <div class="collapse" id="deleteProductCollapse">
            <div class="card card-body">
              <div>
                <h5>Deleting {{ product.name }}</h5>
              </div>
              <div class="row justify-content-between">
                <div class="col-6">
                  <p>
                    Are you sure? <br />
                    <span class="fw-light">This process cannot be undone.</span>
                  </p>
                </div>
                <div class="col-6 text-end">
                  <button class="btn btn-danger" @click.stop="deleteProduct">
                    Delete {{ product.name }}
                  </button>
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { computed, nextTick, onBeforeUnmount, onMounted, ref } from "vue";
import { useStore } from "vuex";
import bootstrapBundle from "bootstrap/dist/js/bootstrap.bundle";
export default {
  props: ["product"],
  emits: ["closeModal"],
  setup(props, { emit }) {
    const store = useStore();
    const storeData = computed(() => store.getters["auth/storeData"]);

    const product = props.product;

    const editProductModal = ref(null);
    const productName = ref(product.name);
    const nameError = ref("");
    const description = ref(product.description);
    const price = ref(product.price);
    const priceError = ref("");
    const selectedCategory = ref(product.category || "---NONE---");
    const newCategory = ref("");
    const isStockEnabled = ref(product.hasStock);
    const stock = ref(product.stock || 0);
    const stockError = ref("");
    const editImageFile = ref(null);
    const editImageSrc = ref(product.imageUrl);

    const handleEditFileUpload = (event) => {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        editImageSrc.value = e.target.result;
      };
      reader.readAsDataURL(file);
      editImageFile.value = file;
      event.target.value = null;
    };

    const additionalOptions = ref(product.additionalOptions);

    const addOption = () => {
      additionalOptions.value.push({
        name: "",
        required: false,
        choices: [""],
        prices: [0],
        min: 0,
        max: 1,
      });
    };

    const removeOption = (index) => {
      additionalOptions.value.splice(index, 1);
    };

    const additionalPrice = ref(0); // Reactive variable for additional price

    const addChoice = (optionIndex) => {
      const option = additionalOptions.value[optionIndex];
      option.choices.push("");
      option.prices.push(additionalPrice.value); // Use default additional price
      if (option.max === option.choices.length - 1 && !option.required)
        option.max = option.choices.length;
    };

    const updateChoice = (optionIndex, choiceIndex, value) => {
      additionalOptions.value[optionIndex].choices[choiceIndex] = value;
    };

    const updateAdditionalPrice = (optionIndex, choiceIndex, value) => {
      additionalOptions.value[optionIndex].prices[choiceIndex] = value;
    };

    const updateMin = (optionIndex) => {
      const option = additionalOptions.value[optionIndex];
      if (!option.required) {
        option.min = 0; // Set min to 0 if not required
      } else {
        option.min = 1;
      }
    };

    const removeEmptyChoice = (optionIndex, choiceIndex) => {
      const option = additionalOptions.value[optionIndex];
      const choices = option.choices;
      const prices = option.prices;
      if (!choices[choiceIndex] && option.choices.length > 1) {
        choices.splice(choiceIndex, 1);
        prices.splice(choiceIndex, 1);
      }
      if (option.max >= option.choices.length + 1) {
        option.max = option.choices.length;
      }
      if (!option.required) {
        option.min = 0; // Set min to 0 if not required
      } else {
        option.min = Math.min(option.min, option.choices.length);
      }
    };

    const clearModal = () => {
      productName.value = product.name;
      nameError.value = "";
      description.value = product.description;
      price.value = product.price;
      priceError.value = "";
      selectedCategory.value = product.category || "---NONE---";
      newCategory.value = "";
      isStockEnabled.value = product.hasStock;
      stock.value = product.stock;
      stockError.value = "";
      editImageSrc.value = product.imageUrl;
      editImageFile.value = null;
      additionalOptions.value = product.additionalOptions;

        emit("closeModal");

    };

    const closeModal = () => {
      clearModal();

      const modalElement = editProductModal.value;
      const modalInstance = bootstrapBundle.Modal.getInstance(modalElement);
      if (modalInstance) {
        modalInstance.hide();
      }
    };

    onMounted(() => {
      editProductModal.value.addEventListener("hide.bs.modal", clearModal);
    });

    onBeforeUnmount(() => {
      editProductModal.value.removeEventListener("hide.bs.modal", clearModal);
    });

    const updateProduct = async () => {
      var category =
        selectedCategory.value === "---NEW---"
          ? newCategory.value.trim()
          : selectedCategory.value;
      if (selectedCategory.value === "---NONE---") category = null;

      if (isStockEnabled.value && stock.value < 0) {
        stock.value = "";
        alert("Stock must be positive");
        return;
      }

      const saveStock = isStockEnabled.value ? stock.value : 0;

      const productDto = {
        id: product.id,
        name: productName.value.trim(),
        description: description.value.trim(),
        price: price.value,
        category: category,
        hasStock: isStockEnabled.value,
        stock: saveStock,
        image: editImageFile.value,
        additionalOptions: additionalOptions.value,
      };

      console.log(productDto);
      const validation = await store.dispatch("products/updateProduct", productDto);
      if (validation) {
        closeModal();
        location.reload();
      }
      
    };

    const collapseDelete = ref(false);

    const toggleCollapseDelete = () => {
      collapseDelete.value = !collapseDelete.value;
    };

    const deleteProduct = async (event) => {
      event.stopPropagation();
      event.preventDefault();
      await store.dispatch("products/deleteProduct", product);
      location.reload();
    };

    return {
      productName,
      nameError,
      description,
      price,
      priceError,
      storeData,
      selectedCategory,
      newCategory,
      isStockEnabled,
      stock,
      stockError,
      editImageFile,
      editImageSrc,
      clearModal,
      handleEditFileUpload,
      updateProduct,
      description,
      additionalOptions,
      addOption,
      removeOption,
      addChoice,
      updateChoice,
      removeEmptyChoice,
      additionalPrice,
      updateAdditionalPrice,
      editProductModal,
      updateMin,
      collapseDelete,
      toggleCollapseDelete,
      deleteProduct,
    };
  },
};
</script>

<style scoped>
.image-container {
  text-align: center;
}
.image-frame {
  width: 220px; /* Adjust as needed */
  height: 220px; /* Adjust as needed */
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}
.image-frame img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain; /* Adjust as needed */
}
.image-upload {
  margin-top: 1rem;
}
</style>
