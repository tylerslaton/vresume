<template>
    <div class="container" style="margin-top: 50px;">
        <keep-alive>
            <component :is="currentComponent || 'ViewResumes'" />
        </keep-alive>
    </div>
</template>

<script>
import db from '../../firebase/init';
import firebase from 'firebase/app';
import { mapState } from 'vuex';

import ViewResumes from './ViewResumes.vue';
import ConfigureBot from './ConfigureBot.vue';
import HowItWorks from './HowItWorks.vue';

export default {
    name: 'HomeEmployer',
    components: {
        ViewResumes,
        ConfigureBot,
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
