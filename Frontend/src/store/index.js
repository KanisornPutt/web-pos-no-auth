import { createStore } from "vuex";
import axios from "axios";

import auth from './modules/auth'
import cart from "./modules/cart";
import store from "./modules/store";
import user from "./modules/user";
import products from "./modules/products";
import histories from "./modules/histories";

export default createStore({
  modules: {
    auth,
    store,
    cart,
    user,
    products,
    histories,
  },
});
