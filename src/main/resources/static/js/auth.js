// Auth State Management
const authState = {
    user: null,
    isAuthenticated: false,
    isAdmin: false
};

// Check Who Am I
async function checkAuth() {
    try {
        const res = await fetch('/api/auth/me');
        if (res.ok) {
            const user = await res.json();
            authState.user = user;
            authState.isAuthenticated = true;
            authState.isAdmin = user.role === 'ROLE_ADMIN';
            updateUI();
        } else {
            authState.user = null;
            authState.isAuthenticated = false;
            authState.isAdmin = false;
            updateUI();
        }
    } catch (err) {
        console.error('Auth check failed', err);
    }
}

// Update UI based on Auth State
function updateUI() {
    const navRight = document.getElementById('nav-right');
    const deleteBtns = document.querySelectorAll('.btn-delete');

    // Update Header
    if (authState.isAuthenticated) {
        navRight.innerHTML = `
            <span>👤 ${escapeHtml(authState.user.username)}</span>
            <form action="/perform_logout" method="POST" style="display:inline;">
                <button type="submit" class="btn-text">Logout</button>
            </form>
        `;
    } else {
        navRight.innerHTML = `
            <a href="/login.html" class="btn-text">Login / Register</a>
        `;
    }

    // Update Delete Buttons
    deleteBtns.forEach(btn => {
        if (authState.isAdmin) {
            btn.style.display = 'inline-block';
        } else {
            btn.style.display = 'none';
        }
    });

    // Show Admin Tabs
    const adminTabs = document.getElementById('adminTabs');
    if (adminTabs && authState.isAdmin) {
        adminTabs.style.display = 'flex';
        // Load users if we are already on users tab? No, wait for click.
    } else if (adminTabs) {
        adminTabs.style.display = 'none';
    }

    // Update Review Form identity if exists
    const authorInput = document.getElementById('author');
    if (authorInput && authState.isAuthenticated) {
        authorInput.value = authState.user.username;
        authorInput.readOnly = true; // Lock username field
    }
}

// Admin Panel Logic
window.switchMainTab = (tab) => {
    const tabs = document.querySelectorAll('#adminTabs .tab');
    const attrView = document.getElementById('attractionsView');
    const usersView = document.getElementById('usersView');

    if (tab === 'attractions') {
        attrView.style.display = 'block';
        usersView.style.display = 'none';
        tabs[0].classList.add('active');
        tabs[1].classList.remove('active');
    } else {
        attrView.style.display = 'none';
        usersView.style.display = 'block';
        tabs[0].classList.remove('active');
        tabs[1].classList.add('active');
        loadUsers();
    }
};

async function loadUsers() {
    const list = document.getElementById('usersList');
    list.innerHTML = '<p>Loading users...</p>';
    try {
        const res = await fetch('/api/auth/users');
        if (res.ok) {
            const users = await res.json();
            list.innerHTML = users.map(u => `
                <div class="card" style="padding: 1rem; border-left: 4px solid ${u.role === 'ROLE_ADMIN' ? 'var(--primary)' : 'var(--text-muted)'}">
                    <div style="font-weight: bold; font-size: 1.1rem; margin-bottom: 0.5rem;">${escapeHtml(u.username)}</div>
                    <div style="font-size: 0.8rem; color: var(--text-muted); text-transform: uppercase; letter-spacing: 1px;">${u.role}</div>
                    <div style="font-size: 0.8rem; color: #64748b; margin-top: 0.5rem;">ID: ${u.id}</div>
                </div>
            `).join('');
        } else {
            list.innerHTML = '<p class="error">Access Denied</p>';
        }
    } catch (e) {
        list.innerHTML = '<p class="error">Failed to load users</p>';
    }
}

// Run on load
document.addEventListener('DOMContentLoaded', checkAuth);

// Helper to re-run UI updates when list refreshes
function refreshAuthUI() {
    updateUI();
}
