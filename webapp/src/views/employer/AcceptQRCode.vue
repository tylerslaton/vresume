<template>
    <div class="qr-code">
        <h5>Scan QR Code</h5>
        {{QRCode}}
        <qrcode-stream @decode="onDecode"></qrcode-stream>
        <button class="btn blue darken-1 center">Accept</button>
    </div>
</template>

<script>
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
            QRCode: ''
        };
    },
    methods: {
        onDecode(decodedString) {
            this.QRCode = decodedString;
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
.btn {
    margin-top: 15px;
}
</style>