function onSignIn(googleUser ) {
  const profile = googleUser .getBasicProfile();
  const id_token = googleUser .getAuthResponse().id_token;

  // Enviar el token al backend para la verificación
  fetch('/api/auth/google', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ id_token }),
  })
  .then(response => response.json())
  .then(data => {
    if (data.success) {
      // Usuario autenticado
      console.log('Usuario autenticado:', profile.getName());
    } else {
      // Manejar error de autenticación
      console.error('Error de autenticación:', data.message);
    }
  });
}
