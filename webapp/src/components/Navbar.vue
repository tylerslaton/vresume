<template>
    <div class="navbar">
        <nav class="white">
            <div class="container valign-wrapper">
                <a
                    v-show="showSideNav"
                    href="#"
                    data-target="slide-out"
                    class="sidenav-trigger black-text"
                >
                    <i class="material-icons">menu</i>
                </a>
                <a class="brand-logo center">
                    <img
                        src="https://cdn.discordapp.com/attachments/636610599534723093/640305846621831228/cropped_black.png"
                        class="logo"
                        :class="{'logo-logged-in': showSideNav}"
                    />
                </a>

                <ul class="right">
                    <li v-if="!user">
                        <router-link :to="{ name: 'register' }">Register</router-link>
                    </li>
                    <li v-if="!user">
                        <router-link :to="{ name: 'login' }">Login</router-link>
                    </li>
                    <li v-if="user">
                        <a @click="logout">Logout</a>
                    </li>
                </ul>
            </div>
        </nav>
        <SideNavbar v-show="showSideNav" />
    </div>
</template>

<script>
import firebase from 'firebase/app';
import SideNavbar from '../components/SideNavbar.vue';

export default {
    name: 'Navbar',
    components: {
        SideNavbar
    },
    data() {
        return {
            user: null
        };
    },
    computed: {
        showSideNav() {
            return this.user && (this.$route.name !== 'login' && this.$route.name !== 'register');
        }
    },
    created() {
        firebase.auth().onAuthStateChanged(user => {
            this.user = user || null;
            M.AutoInit();
        });
    },
    mounted() {
        M.AutoInit();
    },
    methods: {
        async logout() {
            await firebase.auth().signOut();
            this.$router.push({ name: 'login' });
        }
    }
};
</script>

<style scoped>
.logo {
    width: 140px;
    height: 42px;
    display: flex;
}

.logo-logged-in {
    margin-left: 0;
}

ul {
    margin-left: auto;
}

li a {
    color: #000;
}

@media (min-width: 992px) {
    .sidenav-trigger {
        display: none;
    }

    .logo-logged-in {
        margin-left: 300px;
    }
}
</style>
