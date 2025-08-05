document.addEventListener('DOMContentLoaded', function() {
    // Avatar upload functionality
    const changeAvatarBtn = document.querySelector('.btn-change-avatar');
    const avatarUpload = document.getElementById('avatar-upload');
    const avatarImg = document.querySelector('.avatar');

    if (changeAvatarBtn && avatarUpload) {
        changeAvatarBtn.addEventListener('click', function(e) {
            e.preventDefault();
            avatarUpload.click();
        });

        avatarUpload.addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(event) {
                    avatarImg.src = event.target.result;
                };
                reader.readAsDataURL(file);
            }
        });
    }

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
    const profileForm = document.querySelector('.profile-form');
    if (profileForm) {
        profileForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const firstName = document.getElementById('firstName');
            const lastName = document.getElementById('lastName');
            const email = document.getElementById('email');
            const phone = document.getElementById('phone');
            const password = document.getElementById('password');

            // Simple validation
            let isValid = true;

            if (!firstName.value.trim()) {
                isValid = false;
                firstName.style.borderColor = 'var(--error)';
            } else {
                firstName.style.borderColor = '';
            }

            if (!lastName.value.trim()) {
                isValid = false;
                lastName.style.borderColor = 'var(--error)';
            } else {
                lastName.style.borderColor = '';
            }

            if (!email.value.trim() || !email.value.includes('@')) {
                isValid = false;
                email.style.borderColor = 'var(--error)';
            } else {
                email.style.borderColor = '';
            }

            if (!phone.value.trim()) {
                isValid = false;
                phone.style.borderColor = 'var(--error)';
            } else {
                phone.style.borderColor = '';
            }

            if (password.value && password.value.length < 8) {
                isValid = false;
                password.style.borderColor = 'var(--error)';
            } else {
                password.style.borderColor = '';
            }

            if (isValid) {
                // In a real application, you would submit the form here
                alert('Profil muvaffaqiyatli yangilandi!');
                // window.location.href = '/profile';
            } else {
                alert('Iltimos, barcha maydonlarni toʻgʻri toʻldiring!');
            }
        });
    }
});