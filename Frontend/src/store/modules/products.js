import axios from "axios";

const state = {};

const mutations = {};

const actions = {

  async getProducts({ rootGetters }, user) {
    console.log("fetching products");
    if (!user.storeId) {
      console.log("User is not linked to a store");
      return null;
    }

    const url = `api/products/${user.storeId}/${user.userId}`; // Replace with your API endpoint
    const token = rootGetters["auth/token"];
    try {
      const response = await axios.get(url, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });
      const products = response.data;
      console.log(products);
      return products;
    } catch (error) {
      if (error.response) {
        console.error("Error response:", error.response.data);
        console.error("Error status:", error.response.status);
        console.error("Error headers:", error.response.headers);
      } else if (error.request) {
        console.error("Error request:", error.request);
      } else {
        console.error("Error message:", error.message);
      }
      console.error("Error config:", error.config);
      return null;
    }
  },

  async addProduct({ rootGetters }, productDto) {
    console.log("Adding product...");
    const user = rootGetters["auth/user"];
    
    if (!user.storeId) {
      console.log("User is not linked to a store");
      return null;
    }
  
    const formData = new FormData();
    formData.append("name", productDto.name);
    formData.append("description", productDto.description);
    formData.append("price", productDto.price);
    formData.append("hasStock", productDto.hasStock);
    formData.append("stock", productDto.stock);
    formData.append("additionalOptions", JSON.stringify(productDto.additionalOptions));

    if (productDto.category) {
        formData.append("category", productDto.category || null);
    }
    if (productDto.image) {
      formData.append("image", productDto.image);
    }

    for (const [key, value] of formData.entries()) {
        console.log("Key:", key, "Value:", value);
    }
  
    const url = `/api/products/${user.storeId}/${user.userId}`; // Adjust with your API endpoint
    const token = rootGetters["auth/token"];
  
    try {
      const response = await axios.post(url, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });
  
      const responseProduct = response.data;
      console.log("Product added successfully:", responseProduct);
      return true;
    } catch (error) {
      if (error.response) {
        console.error("Error response:", error.response.data);
        console.error("Error status:", error.response.status);
        console.error("Error headers:", error.response.headers);
      } else if (error.request) {
        console.error("Error request:", error.request);
      } else {
        console.error("Error message:", error.message);
      }
      console.error("Error config:", error.config);
      alert(error)
      return false;
    }
  },

  async updateProduct({ rootGetters }, productDto) {
    console.log("Updating product...");
    const user = rootGetters["auth/user"];
    
    if (!user.storeId) {
      console.log("User is not linked to a store");
      return null;
    }
  
    const formData = new FormData();

    formData.append("id", productDto.id);
    formData.append("name", productDto.name);
    formData.append("description", productDto.description);
    formData.append("price", productDto.price);
    formData.append("hasStock", productDto.hasStock);
    formData.append("stock", productDto.stock);
    formData.append("additionalOptions", JSON.stringify(productDto.additionalOptions));

    if (productDto.category) {
        formData.append("category", productDto.category || null);
    }
    if (productDto.image) {
      formData.append("image", productDto.image);
    }

    for (const [key, value] of formData.entries()) {
        console.log("Key:", key, "Value:", value);
    }
  
    const url = `/api/products/${user.storeId}/${user.userId}/${productDto.id}`; // Adjust with your API endpoint
    const token = rootGetters["auth/token"];
  
    try {
      const response = await axios.put(url, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });
  
      const responseProduct = response.data;
      console.log("Product updated successfully:", responseProduct);
      return true;
    } catch (error) {
      if (error.response) {
        console.error("Error response:", error.response.data);
        console.error("Error status:", error.response.status);
        console.error("Error headers:", error.response.headers);
      } else if (error.request) {
        console.error("Error request:", error.request);
      } else {
        console.error("Error message:", error.message);
      }
      console.error("Error config:", error.config);
      return false; // Handle error appropriately based on your application's needs
    }
  },

  async deleteProduct({ rootGetters }, product) {
    console.log("Deleting product...");
    console.log(product);
    const user = rootGetters["auth/user"];
    const productId = product.id;

    if (!user.storeId) {
      console.log("User is not linked to a store");
      return null;
    }

    const url = `/api/products/${user.storeId}/${user.userId}/${productId}`; // Adjust with your API endpoint
    const token = rootGetters["auth/token"];

    try {
      const response = await axios.delete(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
  

      console.log("Product deleted successfully:");
    } catch (error) {
      if (error.response) {
        console.error("Error response:", error.response.data);
        console.error("Error status:", error.response.status);
        console.error("Error headers:", error.response.headers);
      } else if (error.request) {
        console.error("Error request:", error.request);
      } else {
        console.error("Error message:", error.message);
      }
      console.error("Error config:", error.config);
      return null; // Handle error appropriately based on your application's needs
    }
  
  }
};

const getters = {};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
};
