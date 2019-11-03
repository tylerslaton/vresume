<template>
    <div>
        <h5>Scan QR Code</h5>
        <qrcode-stream @decode="onDecode"></qrcode-stream>
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