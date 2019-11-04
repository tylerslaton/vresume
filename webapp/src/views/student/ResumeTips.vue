<template>
    <div>
        <h5 class="center">Resume tips</h5>
        <hr />
        <b>1. Try to quantify achievements</b>
        <p>Whenever possible, use specific information to explain the magnitude of accomplishments. For example, avoid saying things like “increased team productivity” when you can say “increased team productivity by over 10% per quarter”.</p>

        <b>2. Use keywords from the job posting when applicable</b>
        <p>Most resumes get rejected by companies with applicant tracking systems (ATS) before any humans have a chance to review them. To avoid being rejected at this stage, it's recommended to include keywords from the job posting when appropriate. Remember, if your resume makes it past the automated scanners, a human will read it who will not be impressed but keyword stuffing.</p>

        <b>3. Place the most important accomplishments at the top portion of the resume</b>
        <p>Recruiters generally only spend a few seconds reviewing a resume, so it's important to make your best achievements stand out. One way to do this is to maximize your real estate by keeping your most impressive accomplishments near the beginning of the resume. Depending on who you ask, this prime real estate is either the top third or top half of your resume.</p>

        <b>4. Elaborate on experiences to highlight important skills</b>
        <p>As mentioned, keywords are very important on a resume. However, just saying that you can write code in Python is less effective than explaining a time when you used Python in a professional or academic environment. Focus on previous projects and explain how you used the skills that the employer is looking for to complete them.</p>

        <b>5. Proofread thoroughly</b>
        <p>It should go without saying that typos on a one-page document are blaringly obvious. Even if a recruiter doesn’t spend much time looking at your resume, a couple of seconds is plenty of time for a scanning eye to catch a misspelled word. Make sure to review your resume several times, and try to have a friend or two review it as well.</p>

        <div class="video-container">
            <iframe
                width="560"
                height="315"
                src="https://www.youtube.com/embed/bhwEsfXS6y8"
                frameborder="0"
                allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                allowfullscreen
            ></iframe>
        </div>
    </div>
</template>

<script>
export default {
    name: 'ResumeTips'
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

.video-container {
    margin: 50px 0;
}
</style>