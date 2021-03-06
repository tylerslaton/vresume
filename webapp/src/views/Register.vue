<template>
    <div class="container">
        <form @submit.prevent="register" class="card-panel">
            <h5 class="center">Register</h5>
            <div class="input-field">
                <label for="email">Email:</label>
                <input type="email" name="email" v-model="email" required />
            </div>
            <div class="input-field">
                <label for="password">Password:</label>
                <input type="password" name="password" v-model="password" required />
            </div>
            <div v-if="isStudent" class="input-field">
                <label for="lastName">First name:</label>
                <input type="text" name="fistName" v-model="firstName" required />
            </div>
            <div v-if="isStudent" class="input-field">
                <label for="lastName">Last name:</label>
                <input type="text" name="lastName" v-model="lastName" required />
            </div>
            <div v-if="isStudent" class="input-field">
                <label for="studentID">Student ID</label>
                <input type="text" name="studentID" v-model="studentID" required />
            </div>
            <div v-if="isEmployer" class="input-field">
                <label for="companyName">Company name:</label>
                <input type="text" name="companyName" v-model="companyName" required />
            </div>
            <div style="margin-bottom: 35px;">
                <label style="margin-right: 25px;">
                    <input class="with-gap" name="role" type="radio" value="student" v-model="role" />
                    <span>Student</span>
                </label>
                <label>
                    <input
                        class="with-gap"
                        name="role"
                        type="radio"
                        value="employer"
                        v-model="role"
                    />
                    <span>Employer</span>
                </label>
            </div>

            <p class="red-text" v-if="error">{{ error }}</p>
            <button class="btn blue ligthen-1" :disabled="loading">Submit</button>
        </form>
    </div>
</template>

<script>
import db from '@/firebase/init';
import firebase from 'firebase/app';
import { setCurrentUser } from '../services/users';

export default {
    name: 'Register',
    data() {
        return {
            email: null,
            password: null,
            username: null,
            studentID: null,
            firstName: null,
            lastName: null,
            companyName: null,
            role: 'student',
            loading: false,
            error: null
        };
    },
    computed: {
        isStudent() {
            return this.role === 'student';
        },
        isEmployer() {
            return this.role === 'employer';
        }
    },
    methods: {
        async register() {
            try {
                this.loading = true;
                const cred = await firebase
                    .auth()
                    .createUserWithEmailAndPassword(this.email, this.password);
                const userToAdd = {
                    email: this.email,
                    role: this.role,
                    uid: cred.user.uid
                };

                if (this.role === 'student') {
                    userToAdd.firstName = this.firstName;
                    userToAdd.lastName = this.lastName;
                    userToAdd.studentID = this.studentID;
                } else {
                    userToAdd.companyName = this.companyName;
                }

                db.collection('users')
                    .doc(cred.user.uid)
                    .set(userToAdd);

                setCurrentUser(userToAdd);

                this.$router.push({ name: `${this.role}-home` });
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

[type='radio']:checked + span:after,
[type='radio'].with-gap:checked + span:before,
[type='radio'].with-gap:checked + span:after {
    border: 2px solid #41a5f5;
}

[type='radio']:checked + span:after,
[type='radio'].with-gap:checked + span:after {
    background-color: #41a5f5;
}

.btn:disabled {
    background-color: #8bbfe7 !important;
    color: white !important;
}
</style>
