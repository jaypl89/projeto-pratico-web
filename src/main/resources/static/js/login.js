document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Impede o envio padrão do formulário

    // Obtém os valores dos campos do formulário
    const email = document.getElementById('floatingInput').value;
    const password = document.getElementById('floatingPassword').value;

    // Configura a requisição para o backend
    fetch('/api/authenticate', { // Ajuste a URL conforme necessário
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email,
            password: password
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Processa a resposta do backend
            console.log('Success:', data);
            // Redireciona ou mostra uma mensagem de sucesso, conforme necessário
        })
        .catch(error => {
            console.error('Error:', error);
            // Mostra uma mensagem de erro, conforme necessário
        });
});