<template>
    <div v-show="showSideNav">
        <div id="slide-out" class="sidenav sidenav-fixed">
            <div class="sidebar-header red lighten-1 valign-wrapper">
                <img
                    class="center"
                    src="https://cdn.discordapp.com/attachments/639928720274227231/640337425142644757/largeg.jpg"
                />
                <span class="white-text">{{ displayName }}</span>
            </div>
            <div class="container">
                <ul v-if="user.role === 'student'">
                    <li>View resume</li>
                    <li>Resume tips</li>
                    <li>Interview tips</li>
                    <li>Brand awareness</li>
                    <li>How it works</li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
import firebase from 'firebase/app';

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
                if (this.user.role === 'student') {
                    this.displayName = `${this.user.firstName} ${this.user.lastName}`;
                } else {
                    this.displayName = this.user.companyName;
                }
            }
        });
    }
};
</script>

<style scoped>
.sidebar-header {
    height: 350px;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.sidebar-header img {
    border-radius: 100px;
    margin-top: 50px;
    margin-bottom: 15px;
    max-width: 55%;
    max-height: 200px;
}
</style>