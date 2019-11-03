<template>
    <div id="slide-out" class="sidenav sidenav-fixed">
        <div class="sidebar-header red lighten-1 valign-wrapper">
            <img
                class="center"
                src="https://cdn.discordapp.com/attachments/639928720274227231/640337425142644757/largeg.jpg"
            />
            <span class="white-text">{{ displayName }}</span>
        </div>
        <div class="container">
            <ul v-if="user && user.role === 'student'">
                <li @click="setCurrentComponent('StudentResume')">View resume</li>
                <li @click="setCurrentComponent('ResumeTips')">Resume tips</li>
                <li @click="setCurrentComponent('InterviewTips')">Interview tips</li>
                <li @click="setCurrentComponent('BrandAwareness')">Brand awareness</li>
                <li @click="setCurrentComponent('HowItWorks')">How it works</li>
            </ul>
            <ul v-if="user && user.role === 'employer'">
                <li @click="setCurrentComponent('StudentResume')">View all resumes</li>
                <li @click="setCurrentComponent('ResumeTips')">Resume tips</li>
                <li @click="setCurrentComponent('HowItWorks')">How it works</li>
            </ul>
        </div>
    </div>
</template>

<script>
import firebase from 'firebase/app';
import { mapActions } from 'vuex';

export default {
    name: 'SideNavbar',
    data() {
        return {
            user: null,
            displayName: null
        };
    },
    created() {
        firebase.auth().onAuthStateChanged(user => {
            if (user) {
                this.user = JSON.parse(localStorage.getItem('user'));
                if (this.user && this.user.role === 'student') {
                    this.displayName = `${this.user.firstName} ${this.user.lastName}`;
                } else if (this.user && this.user.role === 'employer') {
                    this.displayName = this.user.companyName;
                }
            }
        });
    },
    methods: {
        ...mapActions(['setCurrentComponent'])
    }
};
</script>

<style scoped>
.sidebar-header {
    height: 350px;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.sidebar-header img {
    border-radius: 100px;
    margin-bottom: 15px;
    max-width: 55%;
    max-height: 200px;
}
</style>