 async function loadUserInfo() {
                try {
                    const res = await fetch('/auth/user/details');
                    if (!res.ok) throw new Error('No autenticado');

                    const user = await res.json();
                    const usernameElements = document.getElementsByClassName('username');
                            for (let el of usernameElements) {
                                el.textContent = user.username;
                            }
                    document.getElementById('role').textContent = user.role ? user.role.name : 'N/A';
                } catch (err) {
                    console.error('No se pudo cargar usuario:', err);
                    //window.location.href = '/loginView';  // redirige al login si no está autenticado
                }
            }

            document.getElementById('logoutBtn').addEventListener('click', async () => {
                try {
                    const res = await fetch('/auth/logout', { method: 'POST' });
                    if (res.ok) {
                        window.location.href = '/loginView';
                    } else {
                        alert('Error al cerrar sesión');
                    }
                } catch (error) {
                    console.error('Error durante logout:', error);
                }
            });

            loadUserInfo();