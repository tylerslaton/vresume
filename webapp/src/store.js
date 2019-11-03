import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
    scrict: true,
    state: {
        currentComponent: null
    },
    mutations: {
        setCurrentComponent: (state, payload) => {
            state.currentComponent = payload;
        }
    },
    actions: {
        setCurrentComponent: (context, payload) => {
            context.commit('setCurrentComponent', payload);
        }
    }
});
