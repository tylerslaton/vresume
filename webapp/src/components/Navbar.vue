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
                <img
                    src="https://cdn.discordapp.com/attachments/636610599534723093/640305846621831228/cropped_black.png"
                    class="logo"
                />
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
        <span v-show="showSideNav">
            <ul id="slide-out" class="sidenav sidenav-fixed">
                <div class="sidebar-header red lighten-1">
                    <img class="center"
                        src="https://cdn.discordapp.com/attachments/639928720274227231/640337425142644757/largeg.jpg"
                    />
                </div>
            </ul>
        </span>
    </div>
</template>

<script>
import firebase from 'firebase/app';

export default {
    name: 'Navbar',
    data() {
        return {
            user: null
        };
    },
    computed: {
        showSideNav() {
            return this.$route && (this.$route.name !== 'login' && this.$route.name !== 'register');
        }
    },
    created() {
        firebase.auth().onAuthStateChanged(user => {
            this.user = user || null;
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
}

ul {
    margin-left: auto;
}

li a {
    color: #000;
}

.sidebar-header {
    height: 350px;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.sidebar-header img {
    border-radius: 100px;
    margin-top: 50px;
    max-width: 55%;
    max-height: 200px;
}

@media (min-width: 992px) {
    .sidenav-trigger {
        display: none;
    }
}
</style>
