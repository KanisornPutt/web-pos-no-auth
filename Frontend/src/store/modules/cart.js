import { v4 as uuidv4 } from 'uuid';


const state = {
    cart: [],
  };
  
  const mutations = {
    clearCart(state) {
        state.cart = []
        console.log("Cart cleared");
      },
  
      addProductToCart(state, productInCart) {
        productInCart.cartId = uuidv4(); // Assign a unique ID to the Product
        state.cart.push(productInCart);
        console.log("Product added to cart:", productInCart);
      },
  
      removeProductFromCart(state, cartId) {
        console.log("Removing Product from Cart");
        const filteredCartProducts = state.cart.filter(productInCart => productInCart.cartId === cartId);
        if (filteredCartProducts.length > 0) {
          console.log(filteredCartProducts[0]);
        } else {
          console.log("Product Not Found");
        }
        state.cart = state.cart.filter(productInCart => productInCart.cartId !== cartId);
      },
  };
  
  const actions = {
  };
  
  const getters = {
    getProductFromCartById: (state) => (ProductId) => {
        return state.cart.find(Product => Product.cartId === ProductId);
      }
  };
  
  export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters,
  };
  