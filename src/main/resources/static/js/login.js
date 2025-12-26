document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.getElementById("loginForm");
  const togglePassword = document.getElementById("togglePassword");
  const senhaInput = document.getElementById("senha");
  const errorMessage = document.getElementById("errorMessage");

  // Toggle mostrar/ocultar senha
  togglePassword.addEventListener("click", () => {
    const type = senhaInput.type === "password" ? "text" : "password";
    senhaInput.type = type;
    togglePassword.textContent = type === "password" ? "👁️" : "🙈";
  });

  // Submissão do formulário
  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;
    const lembrarMe = document.getElementById("lembrarMe").checked;

    // Validação simples
    if (!email || !senha) {
      showError("Por favor, preencha todos os campos.");
      return;
    }

    // ✅ CORRIGIDO: Integração com API de login
    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, senha })
      });

      const data = await response.json();

      if (response.ok && data.success) {
        if (lembrarMe) {
          localStorage.setItem("lembrarMe", "true");
          localStorage.setItem("email", email);
        }

        // Salvar dados do usuário logado
        localStorage.setItem('currentUser', JSON.stringify(data.user));

        // Sucesso - redireciona
        alert("Login realizado com sucesso!");
        window.location.href = "../html/index.html";
      } else {
        showError(data.message || "Email ou senha incorretos.");
      }
    } catch (error) {
      console.error('Erro ao fazer login:', error);
      showError("Erro ao conectar com o servidor. Verifique se o backend está rodando.");
    }
  });

  // Função para mostrar erro
  function showError(message) {
    errorMessage.textContent = message;
    errorMessage.style.display = "block";

    setTimeout(() => {
      errorMessage.style.display = "none";
    }, 4000);
  }

  // Verificar se há "lembrar-me" salvo
  const lembrarMeSalvo = localStorage.getItem("lembrarMe");
  const emailSalvo = localStorage.getItem("email");

  if (lembrarMeSalvo === "true" && emailSalvo) {
    document.getElementById("email").value = emailSalvo;
    document.getElementById("lembrarMe").checked = true;
  }

  // Link "Esqueci minha senha"
  document.querySelector(".forgot-password").addEventListener("click", (e) => {
    e.preventDefault();
    alert("Funcionalidade de recuperação de senha será implementada.");
  });
});
