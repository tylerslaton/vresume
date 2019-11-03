<template>
    <div id="app" :class="{'views': showSideNav }">
        <Navbar />
        <router-view />
    </div>
</template>

<script>
import Navbar from './components/Navbar.vue';

export default {
    name: 'App',
    components: {
        Navbar
    },
    computed: {
        showSideNav() {
            return this.$route && (this.$route.name !== 'login' && this.$route.name !== 'register');
        }
    }
};
</script>

<style>
html, body {
    height: 100%;
}

.views {
    padding-left: 0;
}

@media only screen and (min-width: 991px) {
    .views {
        padding-left: 300px;
    }
}
</style>