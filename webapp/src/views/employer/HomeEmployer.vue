<template>
    <div class="container" style="margin-top: 50px;">
        <!-- <keep-alive> -->
        <component :is="currentComponent || 'ViewResumes'" />
        <!-- </keep-alive> -->
    </div>
</template>

<script>
import db from '../../firebase/init';
import firebase from 'firebase/app';
import { mapState } from 'vuex';

import ViewResumes from './ViewResumes.vue';
import HowItWorks from './HowItWorks.vue';
import AcceptQRCode from './AcceptQRCode.vue';

export default {
    name: 'HomeEmployer',
    components: {
        ViewResumes,
        AcceptQRCode,
        HowItWorks
    },
    data() {
        return {
            user: null
        };
    },
    computed: {
        ...mapState(['currentComponent'])
    }
};
</script>
