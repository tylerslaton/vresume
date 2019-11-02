<template>
    <div class="navbar">
        <nav class="blue lighten-1">
            <div class="container">
                <span class="brand-logo left">VResume</span>
                <ul class="right">
                    <li v-if="!user">
                        <router-link :to="{ name: 'register' }">Register</router-link>
                    </li>
                    <li v-if="!user">
                        <router-link :to="{ name: 'login' }">Login</router-link>
                    </li>
                    <li v-if="user">
                        <a href="#">{{ user.email }}</a>
                    </li>
                    <li v-if="user">
                        <a @click="logout">Logout</a>
                    </li>
                </ul>
            </div>
        </nav>
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
    methods: {
        async logout() {
            await firebase.auth().signOut();
            this.$router.push({ name: 'login' });
        }
    },
    created() {
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                const { uid, email } = user;
                localStorage.setItem('user', JSON.stringify({ uid, email }));
            } else {
                localStorage.clear();
            }
            this.user = user || null;
        });
    }
};
</script>
