document.addEventListener('DOMContentLoaded', function() {
    // Password visibility toggle
    const togglePassword = document.querySelector('.toggle-password');
    const passwordInput = document.getElementById('password');

    if (togglePassword && passwordInput) {
        togglePassword.addEventListener('click', function() {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);

            // Toggle eye icon
            const icon = this.querySelector('i');
            icon.classList.toggle('fa-eye');
            icon.classList.toggle('fa-eye-slash');
        });
    }

    // Form validation
    const loginForm = document.querySelector('.login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            const email = document.getElementById('email');
            const password = document.getElementById('password');
            let isValid = true;

            // Reset error states
            email.style.borderColor = '';
            password.style.borderColor = '';

            // Email validation
            if (!email.value || !email.value.includes('@')) {
                isValid = false;
                email.style.borderColor = 'var(--error)';
                email.focus();
            }

            // Password validation
            if (!password.value || password.value.length < 6) {
                isValid = false;
                password.style.borderColor = 'var(--error)';
                if (isValid) password.focus(); // Only focus if email is valid
            }

            if (!isValid) {
                e.preventDefault();
                // In a real app, you'd show error messages here
            }
        });
    }

    // Social login buttons
    const socialButtons = document.querySelectorAll('.social-btn');
    socialButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const platform = this.classList.contains('google') ? 'Google' : 'Facebook';
            alert(`${platform} orqali kirish hozircha mavjud emas. Iltimos, oddiy hisob orqali kiring.`);
        });
    });
});