<template>
    <div class="container">
        <form @submit.prevent="login" class="card-panel">
            <h5 class="center">Login</h5>
            <div class="input-field">
                <label for="email">Email:</label>
                <input type="email" name="email" v-model="email" required />
            </div>
            <div class="input-field">
                <label for="password">Password:</label>
                <input type="password" name="password" v-model="password" required />
            </div>
            <p class="red-text" v-if="error">{{ error }}</p>
            <button class="btn blue ligthen-1" :disabled="loading">Submit</button>
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
            error: null,
            loading: false
        };
    },
    methods: {
        async login() {
            try {
                this.loading = true;
                const cred = await firebase.auth().signInWithEmailAndPassword(this.email, this.password);
                const snapshot = await getCurrentUser();

                snapshot.forEach(doc => {
                    setCurrentUser(doc.data());
                    this.$router.push({ name: `${doc.data().role}-home` });
                });
            } catch (error) {
                this.error = error.message || error;
            }
            this.loading = false;
        }
    }
};
</script>

<style scoped>
.container {
    max-width: 500px;
    margin-top: 60px;
}

h5 {
    margin-top: 0;
    margin-bottom: 35px;
}

.input-field {
    margin-bottom: 16px;
}

.btn:disabled {
    background-color: #8bbfe7 !important;
    color: white !important;
}
</style>
