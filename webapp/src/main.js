import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import firebase from 'firebase/app';
import './firebase/init';

Vue.config.productionTip = false;

let app = null;

firebase.auth().onAuthStateChanged(() => {
    if (!firebase.auth().currentUser) localStorage.clear();
    if (!app) {
        app = new Vue({
            router,
            store,
            render: h => h(App)
        }).$mount('#app');
    }
});
