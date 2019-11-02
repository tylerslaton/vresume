<template>
    <div class="container" style="margin-top: 50px;">
        <h3 v-if="user" class="center">Landing page</h3>
    </div>
</template>

<script>
import db from '../firebase/init';
import firebase from 'firebase/app';

export default {
    name: 'home',
    data() {
        return {
            user: null
        };
    },
    async created() {
        const snapshot = await db.collection('users').where('uid', '==', firebase.auth().currentUser.uid).get();
        snapshot.forEach(doc => this.user = doc.data());
    }
};
</script>
