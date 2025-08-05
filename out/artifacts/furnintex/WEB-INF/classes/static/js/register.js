document.addEventListener('DOMContentLoaded', function() {
    // Password visibility toggle
    const toggleButtons = document.querySelectorAll('.toggle-password');

    toggleButtons.forEach(button => {
        button.addEventListener('click', function() {
            const input = this.parentElement.querySelector('input');
            const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
            input.setAttribute('type', type);

            // Toggle eye icon
            const icon = this.querySelector('i');
            icon.classList.toggle('fa-eye');
            icon.classList.toggle('fa-eye-slash');
        });
    });

    // Form validation
    const registerForm = document.querySelector('.register-form');
    if (registerForm) {
        registerForm.addEventListener('submit', function(e) {
            const password = document.getElementById('password');
            const confirmPassword = document.getElementById('confirmPassword');
            const terms = document.getElementById('terms');
            let isValid = true;

            // Reset error states
            password.style.borderColor = '';
            confirmPassword.style.borderColor = '';

            // Password validation
            if (password.value.length < 8) {
                isValid = false;
                password.style.borderColor = 'var(--error)';
                password.focus();
            }

            // Password match validation
            if (password.value !== confirmPassword.value) {
                isValid = false;
                confirmPassword.style.borderColor = 'var(--error)';
                if (isValid) confirmPassword.focus();
            }

            // Terms agreement validation
            if (!terms.checked) {
                isValid = false;
                alert("Iltimos, foydalanish shartlariga roziligingizni bildiring");
            }

            if (!isValid) {
                e.preventDefault();
            }
        });
    }

    // Social registration buttons
    const socialButtons = document.querySelectorAll('.social-btn');
    socialButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const platform = this.classList.contains('google') ? 'Google' : 'Facebook';
            alert(`${platform} orqali ro'yxatdan o'tish hozircha mavjud emas. Iltimos, oddiy hisob orqali ro'yxatdan o'ting.`);
        });
    });
});