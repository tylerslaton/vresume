<template>
    <div class="container" style="margin-top: 50px;">
        <h3 v-if="user" class="center">Student Home</h3>
        <!-- <Resume /> -->
    </div>
</template>

<script>
import db from '../../firebase/init';
import firebase from 'firebase/app';

import Resume from '../../components/Resume.vue';

export default {
    name: 'home',
    components: {
        Resume
    }
};
</script>
