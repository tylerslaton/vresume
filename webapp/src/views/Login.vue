<template>
    <div class="container">
        <form @submit.prevent="login" class="card-panel">
            <h3 class="center red-text darken-1">Login</h3>
            <div class="input-field">
                <label for="email">Email:</label>
                <input type="email" name="email" v-model="email" required />
            </div>
            <div class="input-field">
                <label for="password">Password:</label>
                <input type="password" name="password" v-model="password" required />
            </div>
            <p class="red-text" v-if="error">{{ error }}</p>
            <button class="btn blue ligthen-1">Submit</button>
        </form>
    </div>
</template>

<script>
import db from '@/firebase/init';
import firebase from 'firebase/app';
import { setCurrentUser, getCurrentUser } from '../services/users';

export default {
    name: 'Register',
    data() {
        return {
            email: null,
            password: null,
            error: null
        };
    },
    methods: {
        async login() {
            try {
                const cred = await firebase.auth().signInWithEmailAndPassword(this.email, this.password);
                const snapshot = await getCurrentUser();

                snapshot.forEach(doc => {
                    setCurrentUser(doc.data());
                    this.$router.push({ name: `${doc.data().role}-home` });
                });
            } catch (error) {
                this.error = error.message || error;
            }
        }
    }
};
</script>

<style scoped>
.container {
    max-width: 500px;
    margin-top: 60px;
}

h3 {
    font-size: 2.4em;
    margin-top: 0;
    margin-bottom: 35px;
}

.input-field {
    margin-bottom: 16px;
}
</style>
