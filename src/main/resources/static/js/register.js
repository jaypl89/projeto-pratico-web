document.getElementById('registerForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const user = {
        name: name,
        email: email,
        password: password
    };

    axios.post('/users/register', user)
        .then(function (response) {
            document.getElementById('message').innerHTML = '<div class="alert alert-success">User registered successfully!</div>';
            document.getElementById('registerForm').reset();

            // Redireciona para a página inicial
            window.location.href = '/';
        })
        .catch(function (error) {
            let message = error.response.data || 'An error occurred';
            document.getElementById('message').innerHTML = '<div class="alert alert-danger">' + message + '</div>';
        });
});