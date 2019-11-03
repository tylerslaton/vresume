<template>
    <div>
        <h5 class="center">How it works</h5>
        <hr />
        <b>1. Sign into our portal and upload your resume.</b>
        <p>
            • This will create a QR code (or update a NFC tag)
            <br />• Your QR code or NFC tag is unique to you and is loaded with all your profile information
        </p>
        <b>2. Go to your career event and tap or scan your ID on the company’s QR scanner or NFC tag reader.</b>
        <p>• That will then send your profile to this portal and the employer will be able to view your information from the site</p>
        <b>3. Wait on the employer to get back to you!</b>
    </div>
</template>

<script>
export default {
    name: 'HowItWorks'
};
</script>

<style scoped>
hr {
    margin: 25px 0;
}
b,
p {
    line-height: 25px;
}

p {
    margin-top: 4px;
}
</style>