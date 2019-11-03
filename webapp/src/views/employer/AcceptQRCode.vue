<template>
    <div class="qr-code">
        <div v-show="!loading && !success">
            <h5 class="center">Scan QR Code</h5>
            <qrcode-stream class="camContainer" @decode="onDecode"></qrcode-stream>
        </div>
        <h5 v-if="loading">The resume is currently being scanned</h5>
        <div v-if="!loading && success" class="center">
            <h5>Resume scanned successfully</h5>
            <button class="btn blue darken-1" @click="success = false">Scan again</button>
        </div>
    </div>
</template>

<script>
import db from '../../firebase/init';
import firebase from 'firebase/app';
import { getCurrentUser } from '../../services/users';
import axios from 'axios';

import { QrcodeStream, QrcodeDropZone, QrcodeCapture } from 'vue-qrcode-reader';

export default {
    name: 'AcceptQRCode',
    components: {
        QrcodeStream,
        QrcodeDropZone,
        QrcodeCapture
    },
    data() {
        return {
            QRCode: '',
            success: false,
            loading: false
        };
    },
    methods: {
        async onDecode(decodedString) {
            try {
                this.success = false;
                this.loading = true;
                let employer;
                let user;

                this.QRCode = decodedString;
                const snapshot = await getCurrentUser();
                snapshot.forEach(doc => (employer = doc.data()));

                // get student associated with qr code
                const doc = await db
                    .collection('users')
                    .doc(decodedString)
                    .get();
                user = doc.data();

                // const res = await axios.post('35.230.183.218/process', {
                //     employerID: 'EeN2y5eR1VNaDlkRfASHjjdRiXk1',
                //     appName: 'Master_Resume_PDF-1',
                //     tags: ['JavaScript', 'Mongo.db'],
                //     expedite: true
                // });

                // console.log(res.data);

                // get applicants from employer and update tags
                const applicants = employer.applicants || [];
                applicants.push({
                    tags: [],
                    studentName: `${user.firstName} ${user.lastName}`,
                    studentID: user.studentID,
                    resume: user.resume
                });

                // update employer
                await db
                    .collection('users')
                    .doc(employer.uid)
                    .set({ applicants }, { merge: true });

                this.success = true;
            } catch (error) {
                this.error = 'Something went wrong, please try again!';
            }
            this.loading = false;
        }
    }
};
</script>

<style scoped>
.qr-code {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.camContainer {
    max-width: 550px;
}

.btn {
    margin-top: 15px;
}
</style>