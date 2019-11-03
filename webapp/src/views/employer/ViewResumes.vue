<template>
    <div>
        <!-- Modal Structure -->
        <div id="detailsModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h4
                        v-if="selectedUser.studentID"
                    >{{ selectedUser.firstName }} {{ selectedUser.lastName }}</h4>
                    <h4 v-else="selectedUser.studentID">{{ selectedUser.studentName }}</h4>
                    <span>{{ selectedUser.email }}</span>
                </div>
                <hr />

                <img class="center" v-if="selectedUser.resume" :src="selectedUser.resume" />
            </div>
            <div class="modal-footer">
                <!-- <a href="#" class="modal-close waves-effect waves-green btn-flat">Agree</a> -->
            </div>
        </div>
        <div v-if="user && user.applicants">
            <h5>Resumes Received ({{user.applicants.length}})</h5>
            <hr style="margin: 25px 0;" />
            <table>
                <thead>
                    <tr>
                        <th>Student name</th>
                        <th>Student ID</th>
                        <th>Resume</th>
                    </tr>
                </thead>

                <tbody>
                    <tr v-for="applicant in user.applicants">
                        <td>{{ applicant.studentName }}</td>
                        <td>{{ applicant.studentID || 'Unspecified'}}</td>
                        <td>
                            <a
                                @click="getApplicant(applicant)"
                                class="modal-trigger"
                                href="#detailsModal"
                            >See details</a>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div v-else>
            <h5 class="center" v-if="!loading">No resumes have been sumbitted to your company</h5>
        </div>
    </div>
</template>

<script>
import firebase from 'firebase/app';
import db from '../../firebase/init';
import { getCurrentUser } from '../../services/users';

export default {
    name: 'ViewResumes',
    data() {
        return {
            user: null,
            selectedUser: {},
            loading: false
        };
    },
    async created() {
        this.loading = true;
        const user = await db
            .collection('users')
            .doc(firebase.auth().currentUser.uid)
            .get();
        this.user = user.data();
        this.loading = false;
        M.AutoInit();
    },
    methods: {
        async getApplicant({ studentID, studentName }) {
            this.selectedUser = {};
            if (!studentID) {
                this.selectedUser.studentName = studentName;
                return;
            }
            const snapshot = await db
                .collection('users')
                .where('studentID', '==', studentID)
                .get();
            snapshot.forEach(doc => (this.selectedUser = doc.data()));
            if (!this.selectedUser.resume) return;
        }
    }
};
</script>

<style scoped>
hr {
    margin: 25px 0;
}

.modal {
    max-height: 90%;
    transform: translateY(-7%) !important;
    width: 70%;
    align-self: center;
    justify-self: center;
}

.modal-header {
    display: flex;
    align-items: center;
}

h4 {
    margin-bottom: 0;
}

.modal-header span {
    margin-left: auto;
}

img {
    display: block;
    margin: 0 auto;
    max-width: 700px;
    box-shadow: 0 0 2px 2px #d3d3d3;
}
</style>