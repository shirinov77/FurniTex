document.addEventListener('DOMContentLoaded', () => {
    console.log('FurniTex - Sahifa to‘liq yuklandi');

    // Savat funksiyalari
    class ShoppingCart {
        constructor() {
            this.cart = JSON.parse(localStorage.getItem('furniTexCart')) || [];
            this.updateCartCount();
        }

        addItem(product) {
            const existingItem = this.cart.find(item => item.id === product.id);

            if (existingItem) {
                existingItem.quantity += 1;
            } else {
                this.cart.push({...product, quantity: 1});
            }

            this.saveCart();
            this.updateCartCount();
            this.showToast(`${product.name} savatga qo'shildi`);
        }

        removeItem(productId) {
            this.cart = this.cart.filter(item => item.id !== productId);
            this.saveCart();
            this.updateCartCount();
        }

        updateQuantity(productId, newQuantity) {
            const item = this.cart.find(item => item.id === productId);
            if (item) {
                item.quantity = newQuantity;
                this.saveCart();
            }
        }

        getTotalItems() {
            return this.cart.reduce((total, item) => total + item.quantity, 0);
        }

        getTotalPrice() {
            return this.cart.reduce((total, item) => total + (item.price * item.quantity), 0);
        }

        saveCart() {
            localStorage.setItem('furniTexCart', JSON.stringify(this.cart));
        }

        updateCartCount() {
            const countElements = document.querySelectorAll('.cart-count');
            const totalItems = this.getTotalItems();

            countElements.forEach(el => {
                el.textContent = totalItems;
                el.style.display = totalItems > 0 ? 'flex' : 'none';
            });
        }

        showToast(message) {
            const toast = document.createElement('div');
            toast.className = 'toast-notification';
            toast.innerHTML = `
                <i class="fas fa-check-circle"></i>
                <span>${message}</span>
            `;

            document.body.appendChild(toast);

            setTimeout(() => {
                toast.classList.add('show');
            }, 100);

            setTimeout(() => {
                toast.classList.remove('show');
                setTimeout(() => {
                    document.body.removeChild(toast);
                }, 300);
            }, 3000);
        }
    }

    // Savat obyektini yaratish
    const cart = new ShoppingCart();

    // Mahsulotlarni serverdan yuklash
    async function loadProducts() {
        try {
            const response = await fetch('/api/products');
            if (!response.ok) {
                throw new Error('Mahsulotlarni yuklashda xatolik yuz berdi');
            }
            return await response.json();
        } catch (error) {
            console.error('Xatolik:', error);
            return [];
        }
    }

    // Mahsulot kartalarini yaratish
    function renderProducts(products) {
        const productGrid = document.querySelector('.product-grid');

        if (!productGrid) return;

        productGrid.innerHTML = products.map(product => `
            <div class="product-card" data-id="${product.id}">
                ${product.discount > 0 ? `
                    <div class="product-badge">
                        <span>-${product.discount}%</span>
                    </div>
                ` : ''}
                <div class="product-image">
                    <img src="${product.imageUrl}" alt="${product.name}" />
                    <div class="product-actions">
                        <button class="action-btn wishlist-btn" title="Saralanganlarga qo'shish">
                            <i class="far fa-heart"></i>
                        </button>
                        <button class="action-btn quickview-btn" title="Tezkor ko'rish">
                            <i class="fas fa-eye"></i>
                        </button>
                    </div>
                </div>
                <div class="product-info">
                    <h3>${product.name}</h3>
                    <div class="product-price">
                        <span class="current-price">${product.price.toLocaleString('uz-UZ')} so'm</span>
                        ${product.oldPrice > 0 ? `
                            <span class="old-price">${product.oldPrice.toLocaleString('uz-UZ')} so'm</span>
                        ` : ''}
                    </div>
                    <button class="add-to-cart">
                        <i class="fas fa-cart-plus"></i> Savatga qo'shish
                    </button>
                </div>
            </div>
        `).join('');

        // Savatga qo'shish tugmalariga event listener qo'shish
        document.querySelectorAll('.add-to-cart').forEach(button => {
            button.addEventListener('click', (e) => {
                const productCard = e.target.closest('.product-card');
                const productId = parseInt(productCard.dataset.id);
                const product = products.find(p => p.id === productId);

                if (product) {
                    cart.addItem(product);
                }
            });
        });

        // Saralanganlarga qo'shish
        document.querySelectorAll('.wishlist-btn').forEach(button => {
            button.addEventListener('click', (e) => {
                const productCard = e.target.closest('.product-card');
                const productId = parseInt(productCard.dataset.id);
                const product = products.find(p => p.id === productId);

                if (product) {
                    addToWishlist(product);
                }
            });
        });

        // Tezkor ko'rish
        document.querySelectorAll('.quickview-btn').forEach(button => {
            button.addEventListener('click', (e) => {
                const productCard = e.target.closest('.product-card');
                const productId = parseInt(productCard.dataset.id);
                const product = products.find(p => p.id === productId);

                if (product) {
                    showQuickView(product);
                }
            });
        });
    }

    // Saralanganlarga qo'shish funksiyasi
    function addToWishlist(product) {
        let wishlist = JSON.parse(localStorage.getItem('furniTexWishlist')) || [];

        if (!wishlist.some(item => item.id === product.id)) {
            wishlist.push(product);
            localStorage.setItem('furniTexWishlist', JSON.stringify(wishlist));

            // Toast notification
            const toast = document.createElement('div');
            toast.className = 'toast-notification';
            toast.innerHTML = `
                <i class="fas fa-heart"></i>
                <span>${product.name} saralanganlarga qo'shildi</span>
            `;

            document.body.appendChild(toast);

            setTimeout(() => {
                toast.classList.add('show');
            }, 100);

            setTimeout(() => {
                toast.classList.remove('show');
                setTimeout(() => {
                    document.body.removeChild(toast);
                }, 300);
            }, 3000);
        } else {
            alert('Bu mahsulot allaqachon saralanganlaringizda mavjud');
        }
    }

    // Tezkor ko'rish modal oynasi
    function showQuickView(product) {
        const modal = document.createElement('div');
        modal.className = 'quickview-modal';
        modal.innerHTML = `
            <div class="modal-content">
                <button class="close-modal">&times;</button>
                <div class="modal-body">
                    <div class="product-images">
                        <img src="${product.imageUrl}" alt="${product.name}" />
                    </div>
                    <div class="product-details">
                        <h2>${product.name}</h2>
                        <div class="price">
                            <span class="current">${product.price.toLocaleString('uz-UZ')} so'm</span>
                            ${product.oldPrice > 0 ? `
                                <span class="old">${product.oldPrice.toLocaleString('uz-UZ')} so'm</span>
                            ` : ''}
                        </div>
                        <p class="description">${product.description || 'Bu ajoyib mahsulot uyingizga yangi ko\'rinish baxsh etadi. Qulaylik va zamonaviy dizayn bitta mahsulotda!'}</p>
                        <div class="actions">
                            <button class="add-to-cart">
                                <i class="fas fa-cart-plus"></i> Savatga qo'shish
                            </button>
                            <button class="add-to-wishlist">
                                <i class="far fa-heart"></i> Saralanganlarga qo'shish
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        document.body.appendChild(modal);
        document.body.style.overflow = 'hidden';

        // Yopish tugmasi
        modal.querySelector('.close-modal').addEventListener('click', () => {
            document.body.removeChild(modal);
            document.body.style.overflow = '';
        });

        // Modal tashqarisiga bosganda yopish
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                document.body.removeChild(modal);
                document.body.style.overflow = '';
            }
        });

        // Savatga qo'shish
        modal.querySelector('.add-to-cart').addEventListener('click', () => {
            cart.addItem(product);
            document.body.removeChild(modal);
            document.body.style.overflow = '';
        });

        // Saralanganlarga qo'shish
        modal.querySelector('.add-to-wishlist').addEventListener('click', () => {
            addToWishlist(product);
            document.body.removeChild(modal);
            document.body.style.overflow = '';
        });
    }

    // Foydalanuvchi menyusi
    const userDropdown = document.querySelector('.user-dropdown');
    if (userDropdown) {
        const userBtn = userDropdown.querySelector('.user-btn');

        userBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            userDropdown.classList.toggle('active');
        });

        // Tashqariga bosganda menyuni yopish
        document.addEventListener('click', () => {
            userDropdown.classList.remove('active');
        });
    }

    // Qidiruv funksiyasi
    const searchBar = document.querySelector('.search-bar');
    if (searchBar) {
        const searchInput = searchBar.querySelector('input');
        const searchBtn = searchBar.querySelector('.search-btn');

        searchBtn.addEventListener('click', () => {
            performSearch(searchInput.value);
        });

        searchInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                performSearch(searchInput.value);
            }
        });
    }

    function performSearch(query) {
        if (query.trim() !== '') {
            window.location.href = `/search?q=${encodeURIComponent(query)}`;
        }
    }

    // Mahsulotlarni yuklash va ko'rsatish
    (async function init() {
        const products = await loadProducts();
        renderProducts(products);
    })();

    // Toast notification uchun CSS qo'shish
    const style = document.createElement('style');
    style.textContent = `
        .toast-notification {
            position: fixed;
            bottom: 20px;
            right: 20px;
            background: #4CAF50;
            color: white;
            padding: 15px 20px;
            border-radius: 5px;
            display: flex;
            align-items: center;
            gap: 10px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            transform: translateY(100px);
            opacity: 0;
            transition: all 0.3s ease;
            z-index: 1000;
        }
        
        .toast-notification.show {
            transform: translateY(0);
            opacity: 1;
        }
        
        .toast-notification i {
            font-size: 20px;
        }
        
        .quickview-modal {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            display: flex;
            align-items: center;
            justify-content: center;
            z-index: 999;
        }
        
        .modal-content {
            background: white;
            border-radius: 10px;
            width: 90%;
            max-width: 800px;
            max-height: 90vh;
            overflow-y: auto;
            position: relative;
        }
        
        .close-modal {
            position: absolute;
            top: 15px;
            right: 15px;
            background: none;
            border: none;
            font-size: 24px;
            cursor: pointer;
        }
        
        .modal-body {
            display: flex;
            flex-direction: column;
            padding: 30px;
        }
        
        @media (min-width: 768px) {
            .modal-body {
                flex-direction: row;
                gap: 30px;
            }
        }
        
        .product-images {
            flex: 1;
        }
        
        .product-images img {
            width: 100%;
            border-radius: 8px;
        }
        
        .product-details {
            flex: 1;
        }
        
        .product-details h2 {
            margin-bottom: 15px;
        }
        
        .price {
            margin-bottom: 20px;
        }
        
        .price .current {
            font-size: 24px;
            font-weight: bold;
            color: #5d4037;
        }
        
        .price .old {
            font-size: 16px;
            text-decoration: line-through;
            color: #999;
            margin-left: 10px;
        }
        
        .description {
            margin-bottom: 25px;
            line-height: 1.6;
        }
        
        .actions {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
        }
        
        .actions button {
            padding: 12px 20px;
            border-radius: 5px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        
        .add-to-cart {
            background: #5d4037;
            color: white;
            border: none;
        }
        
        .add-to-cart:hover {
            background: #8d6e63;
        }
        
        .add-to-wishlist {
            background: white;
            color: #5d4037;
            border: 1px solid #ddd;
        }
        
        .add-to-wishlist:hover {
            background: #f5f5f5;
        }
    `;
    document.head.appendChild(style);
});