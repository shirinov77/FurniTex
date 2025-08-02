document.addEventListener('DOMContentLoaded', () => {
    console.log('Sahifa to‘liq yuklandi.');

    // Savatga qo‘shish tugmasi uchun funksiya
    const addToCartButtons = document.querySelectorAll('.product-card button');

    addToCartButtons.forEach(button => {
        button.addEventListener('click', (event) => {
            // Hozirda bu faqat konsolga xabar chiqaradi.
            // Kelajakda AJAX orqali serverga so‘rov yuborish mumkin.
            const productCard = event.target.closest('.product-card');
            const productName = productCard.querySelector('h3').textContent;
            console.log(`${productName} savatga qo‘shildi!`);

            // Foydalanuvchiga xabar berish
            alert(`${productName} savatga qo‘shildi.`);
        });
    });
});
