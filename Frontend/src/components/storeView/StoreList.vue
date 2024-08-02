<template>
  <div class="border border-tertiary rounded-3 p-3">
    <!-- Navs -->
    <div class="px-3 pb-3">
      <ul class="nav nav-underline" id="myTab" role="tablist">
        <!-- Product Tab -->
        <li class="nav-item" role="presentation">
          <a
            class="nav-link active"
            id="product-tab"
            data-bs-toggle="tab"
            href="#product"
            role="tab"
            aria-controls="product"
            aria-selected="true"
            @click="setPage('products')"
            >Products</a
          >
        </li>

        <!-- User Tab  -->
        <li class="nav-item" role="presentation">
          <a
            class="nav-link"
            id="employees-tab"
            data-bs-toggle="tab"
            href="#employees"
            role="tab"
            aria-controls="employees"
            aria-selected="false"
            @click="setPage('users')"
            >Employees</a
          >
        </li>

        <!-- Transaction Histories Tab -->
        <li class="nav-item" role="presentation">
          <a
            class="nav-link"
            id="history-tab"
            data-bs-toggle="tab"
            href="#history"
            role="tab"
            aria-controls="history"
            aria-selected="false"
            @click="setPage('histories')"
            >Transaction Histories</a
          >
        </li>

        <!-- Buttons -->
        <li v-if="currentPage === 'products'" class="my-auto">
          <button
            v-if="isAdmin"
            class="btn btn-success"
            data-bs-toggle="modal"
            data-bs-target="#addNewProductModal"
          >
            Add New Product
          </button>
        </li>

        <li class="ms-2 ms-lg-auto my-auto">
          <div class="row">
            <!-- Counter -->
            <div class="col-auto my-auto">
              <p class="mb-0" v-if="currentPage === 'products'">
                Total Products : {{ filteredProducts.length }}
              </p>
              <p class="mb-0" v-if="currentPage === 'users'">
                Total Employees : {{ filteredUsers.length }}
              </p>
              <p class="mb-0" v-if="currentPage === 'history'">
                Total Histories : {{ filteredHistories.length }}
              </p>
            </div>

            <!-- Selections -->
            <div class="col-auto">
              <select
                v-if="currentPage === 'products'"
                class="form-select"
                v-model="productSelect"
              >
                <option value="---ALL---">All Categories</option>
                <option
                  v-for="(category, index) in storeData.categories"
                  :key="index"
                  :value="category"
                >
                  {{ category }}
                </option>
              </select>
              <select
                v-if="currentPage === 'users'"
                class="form-select"
                v-model="userSelect"
              >
                <option value="---ALL---">All Roles</option>
                <option value="ADMIN">Admin</option>
                <option value="STAFF">Staff</option>
              </select>
              <select
                v-if="currentPage === 'histories'"
                class="form-select"
                v-model="historySelect"
              >
                <option value="today">Today</option>
                <option value="week">This Week</option>
                <option value="month">This Month</option>
                <option value="year">This Year</option>
                <option value="all-time">All Time</option>
              </select>
            </div>

            <!-- Search Box -->
            <div class="col-auto">
              <form class="my-2 my-sm-0" role="search">
                <input
                  v-model="searchQuery"
                  class="form-control me-2"
                  type="search"
                  placeholder="Search"
                  aria-label="Search"
                />
              </form>
            </div>
          </div>
        </li>
      </ul>
      <hr class="pb-0 mb-0" />
    </div>

    <!-- Contents -->
    <div class="tab-content" id="myTabContent" style="height: 65vh">
      <!-- Product Content -->
      <div
        class="tab-pane fade show active m-3 mt-0"
        id="product"
        role="tabpanel"
        aria-labelledby="product-tab"
      >
        <!-- Headers -->
        <div class="row justify-content-center mx-2">
          <div class="d-none d-md-block col-md-1"></div>
          <div class="col-4 col-md-3" @click="changeProductSort('name')">
            Product Name
            <i
              v-if="productSort === 'nameAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="productSort === 'nameDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
          <div
            class="d-none d-md-block col-md-2"
            @click="changeProductSort('category')"
          >
            Category
            <i
              v-if="productSort === 'categoryAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="productSort === 'categoryDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
          <div
            class="d-none d-md-block col-md-2"
            @click="changeProductSort('price')"
          >
            Price
            <i
              v-if="productSort === 'priceAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="productSort === 'priceDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
          <div
            class="col-4 col-md-2 text-end text-sm-start"
            @click="changeProductSort('stock')"
          >
            Stock
            <i
              v-if="productSort === 'stockAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="productSort === 'stockDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
          <div
            class="col-4 col-md-2 text-end text-sm-start"
            @click="changeProductSort('sales')"
          >
            Sales
            <i
              v-if="productSort === 'salesAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="productSort === 'salesDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
        </div>
        <hr class="mb-2 mx-2" />

        <!-- Products -->
        <div
          v-if="products"
          style="overflow-y: auto; max-height: calc(65vh - 100px)"
        >
          <!-- No Product in store -->
          <div v-if="products.length === 0">
            <div class="container-fluid">
              <div
                class="row justify-content-center align-items-center"
                style="height: 50vh"
              >
                <div class="col-12 text-center">
                  <h4 class="m-3">It seems your store has no product.</h4>
                  <img
                    class="m-3"
                    src="@/assets/empty-icon.png"
                    alt="Empty"
                    style="height: 16vh"
                  />
                  <br />
                  <button
                    v-if="isAdmin"
                    class="btn btn-lg btn-success"
                    data-bs-toggle="modal"
                    data-bs-target="#addNewProductModal"
                  >
                    Add New Product
                  </button>
                </div>
              </div>
            </div>
          </div>

          <AddNewProductModal v-if="isAdmin" />

          <!-- Have Products & found -->
          <div class="accordion" id="productAccordion">
            <div v-for="(product, index) in filteredProducts" :key="index">
              <div class="accordion-item">
                <div class="accordion-header">
                  <div
                    class="border-bottom p-3 collapsed"
                    data-bs-toggle="collapse"
                    :data-bs-target="productAccordionTag(product.id)"
                    aria-expanded="false"
                    :aria-controls="productAccordionId(product.id)"
                  >
                    <div class="row justify-content-center">
                      <div class="d-none d-md-block col-md-1">
                        {{ index + 1 }}
                      </div>
                      <div class="col-4 col-md-3 overflow-data">
                        {{ product.name }}
                      </div>
                      <div
                        v-if="product.category"
                        class="d-none d-md-block col-md-2 overflow-data"
                      >
                        {{ product.category }}
                      </div>
                      <div v-else class="d-none d-md-block col-md-2">NONE</div>
                      <div class="d-none d-md-block col-md-2">
                        {{ product.price }}
                      </div>
                      <div
                        v-if="product.hasStock"
                        class="col-4 col-md-2 text-end text-sm-start overflow-data"
                      >
                        <span v-if="product.stock > 0">{{
                          product.stock
                        }}</span>
                        <span v-else class="text-danger">Out of Stock</span>
                      </div>
                      <div v-else class="col-4 col-md-2 text-end text-sm-start">
                        N/A
                      </div>
                      <div class="col-4 col-md-2 text-end text-sm-start">
                        N/A
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Product Accordian Content -->
                <div
                  :id="productAccordionId(product.id)"
                  class="accordion-collapse collapse"
                  data-bs-parent="#productAccordion"
                >
                  <div class="accordion-body">
                    <div class="row justify-content-center">
                      <div class="col-12 col-sm-6 col-md-3 text-center">
                        <img
                          v-if="product.imageUrl"
                          :src="product.imageUrl"
                          class="card-img-top fixed-img rounded-2"
                          :alt="product.name"
                        />
                        <img
                          v-else
                          src="@/assets/product.png"
                          class="card-img-top fixed-img rounded-2"
                          :alt="product.name"
                        />
                      </div>
                      <div
                        class="col-12 col-sm-6 col-md-9 mt-3 mt-sm-0 px-auto"
                      >
                        <div class="row justify-content-start">
                          <div class="col-auto col-md-6">
                            <h6>
                              <span class="fw-light">Name:</span>
                              {{ product.name }}
                            </h6>
                            <h6>
                              <span class="fw-light">Category:</span>
                              <span v-if="product.category">
                                {{ product.category }}</span
                              >
                              <span v-else> NONE</span>
                            </h6>
                            <h6>
                              <span class="fw-light">Description:</span>
                              {{ product.description }}
                            </h6>
                          </div>
                          <div class="col-auto col-md-6">
                            <h6>
                              <span class="fw-light">Price:</span>
                              {{ product.price }}
                            </h6>
                            <h6>
                              <span class="fw-light">Stock:</span>
                              <span v-if="product.hasStock">{{
                                product.stock
                              }}</span>
                              <span v-else>N/A</span>
                            </h6>

                            <button
                              v-if="isAdmin"
                              type="button"
                              class="btn btn-sm btn-outline-secondary btn-light"
                              @click="openEditProductModal(product)"
                            >
                              Edit Product
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <EditProductModal
              v-if="selectedProduct && isAdmin"
              id="editProductModal"
              :product="selectedProduct"
              @closeModal="closeEditProductModal"
            />
          </div>
          <!-- No Product Found -->
          <div v-if="filteredProducts.length === 0 && products.length !== 0">
            <div class="container-fluid">
              <div
                class="row justify-content-center align-items-center"
                style="height: 20vh"
              >
                <div class="col-12 text-center text-secondary">
                  <h1 class="display-6">No Product Found</h1>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="text-center">
          <h1 class="display-4 text-secondary">Loading...</h1>
        </div>
      </div>

      <!-- User Contents -->
      <div
        class="tab-pane fade m-3 mt-0"
        id="employees"
        role="tabpanel"
        aria-labelledby="employees-tab"
      >
        <!-- Headers -->
        <div class="row justify-content-center mx-2">
          <div class="d-none d-md-block col-md-1"></div>
          <div
            class="col-4 col-md-2 overflow-data"
            @click="changeUserSort('username')"
          >
            Username
            <i
              v-if="userSort === 'usernameAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="userSort === 'usernameDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
          <div
            class="d-none d-md-block col-md-2"
            @click="changeUserSort('firstname')"
          >
            Firstname
            <i
              v-if="userSort === 'firstnameAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="userSort === 'firstnameDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
          <div
            class="d-none d-md-block col-md-2"
            @click="changeUserSort('lastname')"
          >
            Lastname
            <i
              v-if="userSort === 'lastnameAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="userSort === 'lastnameDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
          <div
            class="col-5 col-md-3 text-start"
            @click="changeUserSort('email')"
          >
            Email
            <i
              v-if="userSort === 'emailAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="userSort === 'emailDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
          <div
            class="col-3 col-md-2 text-end text-sm-start"
            @click="changeUserSort('role')"
          >
            Role
            <i
              v-if="userSort === 'roleAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="userSort === 'roleDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
        </div>
        <hr class="mb-2" />

        <!-- Users -->
        <div
          v-if="users"
          style="overflow-y: auto; max-height: calc(65vh - 100px)"
        >
          <!-- Have Users & found -->
          <div class="accordion" id="userAccordion">
            <div v-for="(user, index) in filteredUsers" :key="index">
              <div class="accordion-item">
                <div class="accordion-header">
                  <div
                    class="border-bottom p-3 collapsed"
                    data-bs-toggle="collapse"
                    :data-bs-target="userAccordionTag(user.userId)"
                    aria-expanded="false"
                    :aria-controls="userAccordionId(user.userId)"
                  >
                    <div class="row justify-content-center">
                      <div class="d-none d-md-block col-md-1">
                        {{ index + 1 }}
                      </div>
                      <div class="col-4 col-md-2 overflow-data">
                        {{ user.showName }}
                      </div>
                      <div class="d-none d-md-block col-md-2 overflow-data">
                        {{ user.firstname }}
                      </div>
                      <div class="d-none d-md-block col-md-2 overflow-data">
                        {{ user.lastname }}
                      </div>
                      <div class="col-5 col-md-3 text-start overflow-data">
                        {{ user.email }}
                      </div>
                      <div class="col-3 col-md-2 text-end text-sm-start">
                        {{ user.role }}
                      </div>
                    </div>
                  </div>
                </div>

                <!-- User Accordian Content -->
                <div
                  :id="userAccordionId(user.userId)"
                  class="accordion-collapse collapse"
                  data-bs-parent="#userAccordion"
                >
                  <div class="accordion-body">
                    <div class="row justify-content-between me-0">
                      <div class="col-12 col-sm-6 col-md-3 text-center">
                        <img
                          v-if="user.imageUrl"
                          :src="user.imageUrl"
                          class="card-img-top fixed-img rounded-2"
                          :alt="user.showName"
                        />
                        <img
                          v-else
                          src="@/assets/null-user.png"
                          class="card-img-top fixed-img rounded-2"
                          :alt="user.showName"
                        />
                      </div>
                      <div
                        class="col-12 col-sm-6 col-md-9 mt-3 mt-sm-0 px-auto"
                      >
                        <div class="row justify-content-start">
                          <div class="col-auto col-md-6">
                            <h6>
                              <span class="fw-light">Userame:</span>
                              {{ user.showName }}
                            </h6>
                            <h6>
                              <span class="fw-light">Firstname:</span>
                              <span> {{ user.firstname }}</span>
                            </h6>
                            <h6>
                              <span class="fw-light">Lastname:</span>
                              {{ user.lastname }}
                            </h6>
                          </div>
                          <div class="col-auto col-md-6">
                            <h6>
                              <span class="fw-light">Email:</span>
                              {{ user.email }}
                            </h6>
                            <h6>
                              <span class="fw-light">Role:</span>
                              <span> {{ user.role }}</span>
                            </h6>

                            <!-- Make Admin -->
                            <button
                              v-if="
                                isAdmin &&
                                user.role !== 'ADMIN' &&
                                user.userId !== currentUser.userId
                              "
                              type="button"
                              class="btn btn-sm btn-primary m-1"
                              @click="openUserActionModal('makeAdmin', user)"
                            >
                              Make Admin
                            </button>

                            <!-- Remove As Admin -->
                            <button
                              v-if="isAdmin && user.role === 'ADMIN'"
                              type="button"
                              class="btn btn-sm btn-outline-primary m-1"
                              @click="
                                openUserActionModal('removeAsAdmin', user)
                              "
                            >
                              Reomve as Admin
                            </button>

                            <button
                              v-if="
                                isAdmin && user.userId !== currentUser.userId
                              "
                              type="button"
                              class="btn btn-sm btn-danger m-1"
                              @click="
                                openUserActionModal('removeFromStore', user)
                              "
                            >
                              Remove from Store
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- No user Found -->
          <div v-if="filteredUsers.length === 0">
            <div class="container-fluid">
              <div
                class="row justify-content-center align-items-center"
                style="height: 20vh"
              >
                <div class="col-12 text-center text-secondary">
                  <h1 class="display-6">No User Found</h1>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="text-center">
          <h1 class="display-4 text-secondary">Loading...</h1>
        </div>

        <UserActionModal
          v-if="selectedAction && selectedUser"
          :user="selectedUser"
          :action="selectedAction"
          @closeModal="closeUserActionModal"
        />
      </div>

      <!-- Transaction Histories -->
      <div
        class="tab-pane fade m-3 mt-0"
        id="history"
        role="tabpanel"
        aria-labelledby="history-tab"
      >
        <!-- Headers -->
        <div class="row justify-content-center mx-2">
          <div class="col-3 col-md-2" @click="changeHistorySort('id')">
            ID<i
              v-if="historySort === 'idAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="historySort === 'idDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
          <div
            class="d-none d-md-block col-md-2"
            @click="changeHistorySort('name')"
          >
            Merchant Name<i
              v-if="historySort === 'nameAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="historySort === 'nameDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
          <div class="col-6 col-md-4" @click="changeHistorySort('date')">
            Date & Time<i
              v-if="historySort === 'dateAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="historySort === 'dateDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
          <div
            class="d-none d-md-block col-md-2"
            @click="changeHistorySort('amount')"
          >
            Total Amount<i
              v-if="historySort === 'amountAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="historySort === 'amountDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
          <div
            class="col-3 col-md-2 text-end text-sm-start"
            @click="changeHistorySort('price')"
          >
            Total Price<i
              v-if="historySort === 'priceAscending'"
              class="bi bi-caret-down-fill"
            ></i>
            <i
              v-if="historySort === 'priceDescending'"
              class="bi bi-caret-up-fill"
            ></i>
          </div>
        </div>
        <hr class="mb-2 mx-2" />

        <!-- Histories -->
        <div
          v-if="histories"
          style="overflow-y: auto; max-height: calc(65vh - 100px)"
        >
          <!-- Have Histories & found -->
          <div class="accordion" id="historyAccordion">
            <div v-for="(history, index) in filteredHistories" :key="index">
              <div class="accordion-item">
                <div class="accordion-header">
                  <div
                    class="border-bottom p-3 collapsed"
                    data-bs-toggle="collapse"
                    :data-bs-target="historyAccordionTag(history.id)"
                    aria-expanded="false"
                    :aria-controls="historyAccordionId(history.id)"
                  >
                    <div class="row justify-content-center">
                      <div class="col-3 col-md-2">
                        {{ history.historyNumber }}
                      </div>
                      <div class="d-none d-md-block col-md-2">
                        {{ history.merchantName }}
                      </div>
                      <div class="col-6 col-md-4">
                        {{ formatDate(history.createdAt) }}
                      </div>
                      <div class="d-none d-md-block col-md-2">
                        {{ history.totalAmount }}
                      </div>
                      <div class="col-3 col-md-2 text-end text-sm-start">
                        {{ history.totalPrice }}
                      </div>
                    </div>
                  </div>
                </div>

                <!-- History Accordian Content -->
                <div
                  :id="historyAccordionId(history.id)"
                  class="accordion-collapse collapse"
                  data-bs-parent="#historyAccordion"
                >
                  <div class="accordion-body">
                    <div class="row justify-content-center">
                      <div class="col-12 col-sm-6 col-md-4 text-start">
                        <h6>
                          <span class="fw-light">ID:</span>
                          #{{ history.historyNumber }}
                        </h6>
                        <h6>
                          <span class="fw-light">Date:</span>
                          {{ getDate(history.createdAt) }}
                        </h6>
                        <h6>
                          <span class="fw-light">Time:</span>
                          {{ getTime(history.createdAt) }}
                        </h6>
                        <h6 class="mb-0">
                          <span class="fw-light">Completed By:</span>
                          {{ history.merchantName }}
                        </h6>
                      </div>
                      <div
                        class="col-12 col-sm-6 col-md-8 mt-3 mt-sm-0 px-auto"
                      >
                        <div class="row justify-content-start">
                          <div class="col-12 col-md-6">
                            <h6 class="mt-0">
                              <span class="fw-light"> Content: </span>

                              <div
                                v-for="(
                                  product, index
                                ) in history.orderedProducts"
                                :key="index"
                              >
                                <div
                                  class="row justify-content-start mx-1 mt-2"
                                >
                                  <div class="col-2">x{{ product.amount }}</div>
                                  <div class="col-7 overflow-data">
                                    {{ product.name }}
                                  </div>
                                  <div class="col-3 text-end">
                                    {{ product.singlePrice * product.amount }}
                                  </div>
                                </div>
                              </div>
                            </h6>
                          </div>
                          <div class="col-12 col-md-6">
                            <h6>
                              <div class="row me-1">
                                <div class="col-9">
                                  <span class="fw-light">Total Amount:</span>
                                </div>
                                <div class="col-3 text-end">
                                  {{ history.totalAmount }}
                                </div>
                              </div>
                            </h6>
                            <h6>
                              <div class="row me-1">
                                <div class="col-9">
                                  <span class="fw-light">Total Price:</span>
                                </div>
                                <div class="col-3 text-end">
                                  {{ history.totalPrice }}
                                </div>
                              </div>
                            </h6>

                            <button
                              class="btn btn-outline-primary"
                              @click="openHistoryModal(history)"
                            >
                              Detailed View
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- No history Found -->
          <div v-if="filteredHistories.length === 0">
            <div class="container-fluid">
              <div
                class="row justify-content-center align-items-center"
                style="height: 20vh"
              >
                <div class="col-12 text-center text-secondary">
                  <h1 class="display-6">No Transaction History Found</h1>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="text-center">
          <h1 class="display-4 text-secondary">Loading...</h1>
        </div>
      </div>

      <!-- Hostory Modal -->
      <TransactionHistoryModal
      v-if="selectedHistory"
        :history="selectedHistory"
        @closeModal="closeHistoryModal"
      />
    </div>
  </div>
</template>

<script>
import { computed, onMounted, ref } from "vue";
import { useStore } from "vuex";
import AddNewProductModal from "../dashboard/AddNewProductModal.vue";
import EditProductModal from "../dashboard/EditProductModal.vue";
import bootstrapBundle from "bootstrap/dist/js/bootstrap.bundle";
import UserActionModal from "./UserActionModal.vue";
import TransactionHistoryModal from "./TransactionHistoryModal.vue";
export default {
  components: {
    AddNewProductModal,
    EditProductModal,
    UserActionModal,
    TransactionHistoryModal,
  },
  setup() {
    const store = useStore();
    const currentUser = computed(() => store.getters["auth/user"]);
    const storeData = computed(() => store.getters["auth/storeData"]);
    const role = computed(() => store.getters["auth/role"]);
    const isAdmin = role.value === "ADMIN";
    const currentPage = ref("products");

    const setPage = (value) => {
      currentPage.value = value;
    };

    const searchQuery = ref("");

    // Products

    const products = ref(null);

    const productSelect = ref("---ALL---");

    const productSort = ref("nameAscending");

    const changeProductSort = (value) => {
      if (value === "name") {
        if (productSort.value == "nameAscending")
          productSort.value = "nameDescending";
        else productSort.value = "nameAscending";
      } else if (value === "category") {
        if (productSort.value == "categoryAscending")
          productSort.value = "categoryDescending";
        else productSort.value = "categoryAscending";
      } else if (value === "price") {
        if (productSort.value == "priceAscending")
          productSort.value = "priceDescending";
        else productSort.value = "priceAscending";
      } else if (value === "stock") {
        if (productSort.value == "stockAscending")
          productSort.value = "stockDescending";
        else productSort.value = "stockAscending";
      } else if (value === "sales") {
        if (productSort.value == "salesAscending")
          productSort.value = "salesDescending";
        else productSort.value = "salesAscending";
      }
    };

    const filteredProducts = computed(() => {
      if (!products.value) return [];
      const searchedProduct = products.value
        .filter((product) => {
          const matchesCategory =
            productSelect.value === "---ALL---" ||
            product.category === productSelect.value;
          const matchesName =
            product.name
              .toLowerCase()
              .includes(searchQuery.value.toLowerCase()) ||
            (product.category
              ? product.category
                  .toLowerCase()
                  .includes(searchQuery.value.toLowerCase())
              : false);
          return matchesCategory && matchesName;
        })
        .map((product) => {
          return product;
        });

      switch (productSort.value) {
        case "nameAscending":
          return searchedProduct.sort((a, b) => a.name.localeCompare(b.name));
        case "nameDescending":
          return searchedProduct.sort((a, b) => b.name.localeCompare(a.name));
        case "categoryAscending":
          return searchedProduct.sort((a, b) => {
            if (a.category && !b.category) return -1;
            if (!a.category && b.category) return 1;

            if (a.category === b.category) {
              return a.name.localeCompare(b.name);
            }

            return a.category.localeCompare(b.category);
          });
        case "categoryDescending":
          return searchedProduct.sort((a, b) => {
            if (a.category && !b.category) return -1;
            if (!a.category && b.category) return 1;

            if (a.category === b.category) {
              return a.name.localeCompare(b.name);
            }

            return b.category.localeCompare(a.category);
          });
        case "priceAscending":
          return searchedProduct.sort((a, b) => {
            if (a.price === b.price) {
              return a.name.localeCompare(b.name);
            }
            return a.price - b.price;
          });
        case "priceDescending":
          return searchedProduct.sort((a, b) => {
            if (a.price === b.price) {
              return a.name.localeCompare(b.name);
            }
            return b.price - a.price;
          });
        case "stockAscending":
          return searchedProduct.sort((a, b) => {
            if (a.hasStock && !b.hasStock) return -1;
            if (!a.hasStock && b.hasStock) return 1;

            if (a.stock === b.stock) {
              return a.name.localeCompare(b.name);
            }

            return a.stock - b.stock;
          });
        case "stockDescending":
          return searchedProduct.sort((a, b) => {
            if (a.hasStock && !b.hasStock) return -1;
            if (!a.hasStock && b.hasStock) return 1;

            if (a.stock === b.stock) {
              return a.name.localeCompare(b.name);
            }

            return b.stock - a.stock;
          });

        default:
          return searchedProduct.sort((a, b) => a.name.localeCompare(b.name));
      }
    });

    const getProducts = async (user) => {
      const products = await store.dispatch("products/getProducts", user);
      return products;
    };

    const productAccordionId = (productId) => {
      return "productAccordion" + productId;
    };

    const productAccordionTag = (productId) => {
      return "#productAccordion" + productId;
    };

    const selectedProduct = ref(null);

    const openEditProductModal = (product) => {
      console.log("opening edit modal");
      selectedProduct.value = product;
      console.log(selectedProduct.value.name);
      setTimeout(() => {
        const editProductElement = document.getElementById(`editProductModal`);
        if (editProductElement) {
          const editProductModalInstance =
            bootstrapBundle.Modal.getOrCreateInstance(editProductElement);
          editProductModalInstance.show();
        }
      }, 100); // Adjust the delay if needed
    };

    const closeEditProductModal = () => {
      console.log("closing modal");
      setTimeout(() => {
        selectedProduct.value = null;
      }, 100);
    };

    // Users
    const users = ref(null);

    const getUsersFromStore = async (user) => {
      const users = await store.dispatch("store/getUsersFromStore", user);
      return users;
    };

    const userSelect = ref("---ALL---");

    const userSort = ref("roleAscending");

    const changeUserSort = (value) => {
      if (value === "username") {
        if (userSort.value == "usernameAscending")
          userSort.value = "usernameDescending";
        else userSort.value = "usernameAscending";
      } else if (value === "firstname") {
        if (userSort.value == "firstnameAscending")
          userSort.value = "firstnameDescending";
        else userSort.value = "firstnameAscending";
      } else if (value === "lastname") {
        if (userSort.value == "lastnameAscending")
          userSort.value = "lastnameDescending";
        else userSort.value = "lastnameAscending";
      } else if (value === "email") {
        if (userSort.value == "emailAscending")
          userSort.value = "emailDescending";
        else userSort.value = "emailAscending";
      } else if (value === "role") {
        if (userSort.value == "roleAscending")
          userSort.value = "roleDescending";
        else userSort.value = "roleAscending";
      }
    };

    const filteredUsers = computed(() => {
      if (!users.value) return [];
      const searchedUser = users.value
        .filter((user) => {
          const matchesName =
            user.showName
              .toLowerCase()
              .includes(searchQuery.value.toLowerCase()) ||
            user.firstname
              .toLowerCase()
              .includes(searchQuery.value.toLowerCase()) ||
            user.lastname
              .toLowerCase()
              .includes(searchQuery.value.toLowerCase()) ||
            user.email.toLowerCase().includes(searchQuery.value.toLowerCase());
          const matchRoles =
            userSelect.value === "---ALL---" || userSelect.value == user.role;
          return matchesName && matchRoles;
        })
        .map((user) => {
          return user;
        });

      switch (userSort.value) {
        case "usernameAscending":
          return searchedUser.sort((a, b) => {
            if (a.showName === b.showName)
              return a.email.localeCompare(b.email);
            return a.showName.localeCompare(b.showName);
          });
        case "usernameDescending":
          return searchedUser.sort((a, b) => {
            if (a.showName === b.showName)
              return a.email.localeCompare(b.email);
            return b.showName.localeCompare(a.showName);
          });

        case "roleAscending":
          return searchedUser.sort((a, b) => {
            if (a.role === b.role) return a.showName.localeCompare(b.showName);
            return a.role.localeCompare(b.role);
          });
        case "roleDescending":
          return searchedUser.sort((a, b) => {
            if (a.role === b.role) return a.showName.localeCompare(b.showName);
            return b.role.localeCompare(a.role);
          });

        case "firstnameAscending":
          return searchedUser.sort((a, b) => {
            if (a.firstname === b.firstname)
              return a.showName.localeCompare(b.showName);
            return a.firstname.localeCompare(b.firstname);
          });
        case "firstnameDescending":
          return searchedUser.sort((a, b) => {
            if (a.firstname === b.showNfirstnameame)
              return a.showName.localeCompare(b.showName);
            return b.firstname.localeCompare(a.firstname);
          });

        case "lastnameAscending":
          return searchedUser.sort((a, b) => {
            if (a.lastname === b.lastname)
              return a.showName.localeCompare(b.showName);
            return a.lastname.localeCompare(b.lastname);
          });
        case "lastnameDescending":
          return searchedUser.sort((a, b) => {
            if (a.lastname === b.lastname)
              return a.showName.localeCompare(b.showName);
            return b.lastname.localeCompare(a.lastname);
          });

        case "emailAscending":
          return searchedUser.sort((a, b) => {
            return a.email.localeCompare(b.email);
          });
        case "emailDescending":
          return searchedUser.sort((a, b) => {
            return b.email.localeCompare(a.email);
          });

        default:
          return searchedUser.sort((a, b) => {
            if (a.role === b.role) return a.email.localeCompare(b.email);
            return a.role.localeCompare(b.role);
          });
      }
    });

    const userAccordionId = (userId) => {
      return "userAccordion" + userId;
    };

    const userAccordionTag = (userId) => {
      return "#userAccordion" + userId;
    };

    const selectedAction = ref(null);
    const selectedUser = ref(null);

    const openUserActionModal = (action, user) => {
      selectedUser.value = user;
      selectedAction.value = action;
      setTimeout(() => {
        const viewProductElement = document.getElementById(`userActionModal`);
        if (viewProductElement) {
          const viewProductModalInstance =
            bootstrapBundle.Modal.getOrCreateInstance(viewProductElement);
          viewProductModalInstance.show();
        }
      }, 100); // Adjust the delay if needed
    };

    const closeUserActionModal = () => {
      console.log("Closing user action modal");
      setTimeout(() => {
        selectedUser.value = null;
        selectedAction.value = null;
      }, 100);
    };

    // Transaction History

    const histories = ref(null);

    const getHistories = async () => {
      const histories = await store.dispatch("histories/getHistories");
      return histories;
    };

    const historyAccordionId = (historyId) => {
      return "historyAccordion" + historyId;
    };

    const historyAccordionTag = (historyId) => {
      return "#historyAccordion" + historyId;
    };

    const formatDate = (dateTime) => {
      // const date = new Date(dateTime);
      const formattedDate = dateTime.toLocaleString("en-GB", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
        hour12: false,
      });
      return formattedDate;
    };

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

    const historySort = ref("idDescending");

    const historySelect = ref("today");

    const changeHistorySort = (value) => {
      if (value === "date") {
        if (historySort.value == "dateAscending")
          historySort.value = "dateDescending";
        else historySort.value = "dateAscending";
      } else if (value === "id") {
        if (historySort.value == "idAscending")
          historySort.value = "idDescending";
        else historySort.value = "idAscending";
      } else if (value === "name") {
        if (historySort.value == "nameAscending")
          historySort.value = "nameDescending";
        else historySort.value = "nameAscending";
      } else if (value === "amount") {
        if (historySort.value == "amountAscending")
          historySort.value = "amountDescending";
        else historySort.value = "amountAscending";
      } else if (value === "price") {
        if (historySort.value == "priceAscending")
          historySort.value = "priceDescending";
        else historySort.value = "priceAscending";
      }
    };

    const getTimeRange = (range) => {
      const now = new Date();
      let startDate, endDate;
      switch (range) {
        case "today":
          startDate = new Date(now.setHours(0, 0, 0, 0));
          endDate = new Date(now.setHours(23, 59, 59, 999));
          break;
        case "week":
          const firstDayOfWeek = now.getDate() - now.getDay();
          startDate = new Date(now.setDate(firstDayOfWeek));
          startDate.setHours(0, 0, 0, 0);
          endDate = new Date(startDate);
          endDate.setDate(startDate.getDate() + 6);
          endDate.setHours(23, 59, 59, 999);
          break;
        case "month":
          startDate = new Date(now.getFullYear(), now.getMonth(), 1);
          endDate = new Date(now.getFullYear(), now.getMonth() + 1, 0);
          endDate.setHours(23, 59, 59, 999);
          break;
        case "year":
          startDate = new Date(now.getFullYear(), 0, 1);
          endDate = new Date(now.getFullYear() + 1, 0, 0);
          endDate.setHours(23, 59, 59, 999);
          break;
        case "all-time":
          startDate = new Date(0); // Epoch time
          endDate = new Date(); // Current time
          break;
        default:
          startDate = new Date(0); // Epoch time
          endDate = new Date(); // Current time
          break;
      }
      return { startDate, endDate };
    };

    const filteredHistories = computed(() => {
      if (!histories.value) return [];

      const { startDate, endDate } = getTimeRange(historySelect.value);

      const searchedHistories = histories.value
        .filter((history) => {
          const matchSearch =
            history.merchantName
              .toLowerCase()
              .includes(searchQuery.value.toLowerCase()) ||
            getDate(history.createdAt)
              .toLowerCase()
              .includes(searchQuery.value.toLowerCase()) ||
            getTime(history.createdAt)
              .toLowerCase()
              .includes(searchQuery.value.toLowerCase());
          const historyDate = new Date(history.createdAt);
          const matchesDateRange =
            historyDate >= startDate && historyDate <= endDate;
          // || historySelect.value === "all-time";
          return matchSearch && matchesDateRange;
        })
        .map((user) => {
          return user;
        });

      switch (historySort.value) {
        case "idDescending":
          return searchedHistories.sort(
            (a, b) => b.historyNumber - a.historyNumber
          );
        case "idAscending":
          return searchedHistories.sort(
            (a, b) => a.historyNumber - b.historyNumber
          );
        case "dateAscending":
          return searchedHistories.sort((a, b) => {
            if (a.createdAt == b.createdAt)
              return b.historyNumber - a.historyNumber;
            return a.createdAt - b.createdAt;
          });
        case "dateDescending":
          return searchedHistories.sort((a, b) => {
            if (a.createdAt == b.createdAt)
              return b.historyNumber - a.historyNumber;
            return b.createdAt - a.createdAt;
          });
        case "nameAscending":
          return searchedHistories.sort((a, b) => {
            if (a.merchantName == b.merchantName)
              return b.historyNumber - a.historyNumber;
            return a.merchantName.localeCompare(b.merchantName);
          });
        case "nameDescending":
          return searchedHistories.sort((a, b) => {
            if (a.merchantName == b.merchantName)
              return b.historyNumber - a.historyNumber;
            return b.merchantName.localeCompare(a.merchantName);
          });
        case "amountAscending":
          return searchedHistories.sort((a, b) => {
            if (a.totalAmount == b.totalAmount)
              return b.historyNumber - a.historyNumber;
            return a.totalAmount - b.totalAmount;
          });
        case "amountDescending":
          return searchedHistories.sort((a, b) => {
            if (a.totalAmount == b.totalAmount)
              return b.historyNumber - a.historyNumber;
            return b.totalAmount - a.totalAmount;
          });
        case "priceAscending":
          return searchedHistories.sort((a, b) => {
            if (a.totalPrice == b.totalPrice)
              return b.historyNumber - a.historyNumber;
            return a.totalPrice - b.totalPrice;
          });
        case "priceDescending":
          return searchedHistories.sort((a, b) => {
            if (a.totalPrice == b.totalPrice)
              return b.historyNumber - a.historyNumber;
            return b.totalPrice - a.totalPrice;
          });
        default:
          return searchedHistories.sort(
            (a, b) => b.historyNumber - a.historyNumber
          );
      }
    });

    const selectedHistory = ref(null);

    const openHistoryModal = (history) => {
      selectedHistory.value = history;
      setTimeout(() => {
        const historyModalElement = document.getElementById(`historyModal`);
        if (historyModalElement) {
          const historyModalInstance =
            bootstrapBundle.Modal.getOrCreateInstance(historyModalElement);
          historyModalInstance.show();
        }
      }, 100); // Adjust the delay if needed
    };

    const closeHistoryModal = () => {
      setTimeout(() => {
        selectedHistory.value = null;
      }, 100);
    };

    onMounted(async () => {
      products.value = await getProducts(currentUser.value);
      // console.log(products.value);
      // if (products.value) console.log(products.value.length);

      users.value = await getUsersFromStore(currentUser.value);
      // console.log(users.value);

      histories.value = await getHistories();
    });

    return {
      storeData,
      currentUser,
      isAdmin,
      currentPage,
      setPage,
      searchQuery,
      products,
      productSelect,
      filteredProducts,
      productSort,
      changeProductSort,
      productAccordionId,
      productAccordionTag,
      selectedProduct,
      openEditProductModal,
      closeEditProductModal,
      users,
      filteredUsers,
      userSort,
      userSelect,
      changeUserSort,
      userAccordionId,
      userAccordionTag,
      openUserActionModal,
      selectedUser,
      selectedAction,
      closeUserActionModal,
      histories,
      historyAccordionId,
      historyAccordionTag,
      formatDate,
      getDate,
      getTime,
      filteredHistories,
      changeHistorySort,
      historySort,
      historySelect,
      openHistoryModal,
      selectedHistory,
      closeHistoryModal,
    };
  },
};
</script>

<style>
.fixed-img {
  width: 14vh;
  min-width: 100px;
  height: 14vh;
  min-height: 100px;
  object-fit: cover;
}

.overflow-data {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
