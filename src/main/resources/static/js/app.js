const API_BASE = '/api/attractions';

// Elements
const attractionForm = document.getElementById('attractionForm');
const attractionsGrid = document.getElementById('attractionsGrid');
const searchInput = document.getElementById('searchInput');
const reviewModal = document.getElementById('reviewModal');
const modalTitle = document.getElementById('modalTitle');
const reviewsList = document.getElementById('reviewsList');
const reviewForm = document.getElementById('reviewForm');
const closeBtn = document.querySelector('.close-btn');

// Initial Load
document.addEventListener('DOMContentLoaded', () => loadAttractions());

// Search Event
let debounceTimer;
if (searchInput) {
    searchInput.addEventListener('input', (e) => {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            loadAttractions(e.target.value);
        }, 300);
    });
}

// Add Attraction
if (attractionForm) {
    attractionForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const data = {
            name: document.getElementById('name').value,
            city: document.getElementById('city').value,
            country: document.getElementById('country').value,
            category: document.getElementById('category').value,
            description: document.getElementById('description').value
        };

        try {
            const res = await fetch(API_BASE, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (res.ok) {
                attractionForm.reset();
                loadAttractions();
                alert('Attraction added!');
            } else if (res.status === 401 || res.status === 403) {
                alert('You must be an ADMIN to add attractions! Please login at /login');
                window.location.href = '/login';
            } else {
                const error = await res.json();
                alert('Error: ' + JSON.stringify(error.message));
            }
        } catch (err) {
            console.error(err);
            alert('Failed to connect to server');
        }
    });
}

// Load Attractions
async function loadAttractions(query = '') {
    attractionsGrid.innerHTML = '<p class="loading">Loading...</p>';
    try {
        const url = query ? `${API_BASE}?search=${encodeURIComponent(query)}` : API_BASE;
        const res = await fetch(url);

        if (!res.ok) throw new Error('Failed to fetch');

        const attractions = await res.json();
        renderAttractions(attractions);
    } catch (err) {
        console.error(err);
        attractionsGrid.innerHTML = '<p class="error">Failed to load attractions.</p>';
    }
}

function renderAttractions(attractions) {
    if (attractions.length === 0) {
        attractionsGrid.innerHTML = '<p>No places found. Be the first to add one!</p>';
        return;
    }

    attractionsGrid.innerHTML = attractions.map(attr => `
        <div class="card">
            <div class="card-body">
                <h3 class="card-title">${escapeHtml(attr.name)}</h3>
                <div class="card-location">📍 ${escapeHtml(attr.city)}, ${escapeHtml(attr.country)}</div>
                <div class="card-category">🏷️ ${escapeHtml(attr.category || 'General')}</div>
                <p class="card-desc">${escapeHtml(attr.description || '')}</p>
                <div class="card-actions">
                    <button class="btn-text" onclick="openReviews(${attr.id}, '${escapeHtml(attr.name)}')">
                        ⭐ Reviews
                    </button>
                    <button class="btn-text btn-delete" onclick="deleteAttraction(${attr.id})">
                        Delete
                    </button>
                </div>
            </div>
        </div>
    `).join('');

    // Auth Hook - Refresh UI to hide/show delete buttons
    if (typeof refreshAuthUI === 'function') {
        refreshAuthUI();
    }
}

// Delete Attraction
window.deleteAttraction = async (id) => {
    if (!confirm('Are you sure you want to delete this place?')) return;
    const res = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });

    if (res.ok) {
        loadAttractions();
    } else if (res.status === 401 || res.status === 403) {
        alert('Access Denied. You must be an ADMIN.');
        window.location.href = '/login';
    } else {
        alert('Failed to delete.');
    }
};

// Open Reviews Modal
window.openReviews = async (id, name) => {
    reviewModal.style.display = 'flex';
    modalTitle.textContent = `Reviews for ${name}`;
    document.getElementById('reviewAttractionId').value = id;
    loadReviews(id);
};

// Close Modal
if (closeBtn) {
    closeBtn.onclick = () => reviewModal.style.display = 'none';
}
window.onclick = (event) => {
    if (event.target == reviewModal) reviewModal.style.display = 'none';
};

// Load Reviews for an Attraction
async function loadReviews(id) {
    reviewsList.innerHTML = '<p>Loading reviews...</p>';
    const res = await fetch(`${API_BASE}/${id}/reviews`);
    const reviews = await res.json();

    if (reviews.length === 0) {
        reviewsList.innerHTML = '<p>No reviews yet. Add one below!</p>';
        return;
    }

    reviewsList.innerHTML = reviews.map(r => `
        <div class="review-item">
            <div class="review-header">
                <span class="review-author">${escapeHtml(r.authorName)}</span>
                <span class="review-rating">${'★'.repeat(r.rating)}${'☆'.repeat(5 - r.rating)}</span>
            </div>
            <div>${escapeHtml(r.comment)}</div>
        </div>
    `).join('');
}

// Add Review
if (reviewForm) {
    reviewForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const attractionId = document.getElementById('reviewAttractionId').value;
        const data = {
            authorName: document.getElementById('author').value,
            rating: parseInt(document.getElementById('rating').value),
            comment: document.getElementById('comment').value
        };

        const res = await fetch(`${API_BASE}/${attractionId}/reviews`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            document.getElementById('author').value = '';
            document.getElementById('comment').value = '';
            loadReviews(attractionId);
        } else if (res.status === 401 || res.status === 403) {
            alert('Please login to leave a review!');
            window.location.href = '/login';
        } else {
            alert('Error adding review');
        }
    });
}

// Utility
function escapeHtml(text) {
    if (!text) return '';
    return text
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}
