<template>
    <div>
        <div class="container">
            <keep-alive>
                <component :is="currentComponent || 'StudentResume'" />
            </keep-alive>
        </div>
    </div>
</template>

<script>
import db from '../../firebase/init';
import firebase from 'firebase/app';
import { mapState } from 'vuex';

import StudentResume from './StudentResume.vue';
import ResumeTips from './ResumeTips.vue';
import InterviewTips from './InterviewTips';
import BrandAwareness from './BrandAwareness.vue';
import HowItWorks from './HowItWorks.vue';
import SideNavbar from '../../components/SideNavbar.vue';

export default {
    name: 'HomeStudent',
    components: {
        StudentResume,
        ResumeTips,
        InterviewTips,
        BrandAwareness,
        HowItWorks,
        SideNavbar
    },
    computed: {
        ...mapState(['currentComponent'])
    }
};
</script>

<style scoped>
.container {
    overflow: auto;
    margin-top: 25px;
}
</style>
