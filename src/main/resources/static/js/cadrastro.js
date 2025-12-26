document.addEventListener("DOMContentLoaded", async () => {
  const tabButtons = document.querySelectorAll(".tab-button");
  const professorFields = document.getElementById("professorFields");
  const alunoFields = document.getElementById("alunoFields");
  const diretorFields = document.getElementById("diretorFields");
  const form = document.getElementById("cadastroForm");

  // ✅ CORRIGIDO: Carregar turmas e matérias da API
  await carregarTurmas();
  await carregarMaterias();

  // Gerenciar abas
  tabButtons.forEach((button) => {
    button.addEventListener("click", () => {
      // Remove ativo de todos
      tabButtons.forEach((btn) => btn.classList.remove("active"));
      button.classList.add("active");

      // Esconde todos os campos dinâmicos
      professorFields.style.display = "none";
      alunoFields.style.display = "none";
      diretorFields.style.display = "none";

      // Mostra campos específicos
      const tab = button.getAttribute("data-tab");
      if (tab === "professor") {
        professorFields.style.display = "flex";
      } else if (tab === "aluno") {
        alunoFields.style.display = "flex";
      } else if (tab === "diretor") {
        diretorFields.style.display = "block";
      }
    });
  });

  // ✅ CORRIGIDO: Carregar turmas dinamicamente
  async function carregarTurmas() {
    try {
      const response = await fetch('http://localhost:8080/api/classes');
      if (!response.ok) throw new Error('Erro ao carregar turmas');
      
      const turmas = await response.json();
      
      // Preencher select de turma para professor
      const selectProfessor = document.getElementById('turma');
      selectProfessor.innerHTML = '<option value="">Selecione uma turma</option>';
      turmas.forEach(turma => {
        const option = document.createElement('option');
        option.value = turma.id;
        option.textContent = turma.name;
        selectProfessor.appendChild(option);
      });

      // Preencher select de turma para aluno
      const selectAluno = document.getElementById('turmaAluno');
      selectAluno.innerHTML = '<option value="">Selecione uma turma</option>';
      turmas.forEach(turma => {
        const option = document.createElement('option');
        option.value = turma.id;
        option.textContent = turma.name;
        selectAluno.appendChild(option);
      });
    } catch (error) {
      console.error('Erro ao carregar turmas:', error);
      alert('Erro ao carregar turmas. Verifique se o backend está rodando.');
    }
  }

  // ✅ NOVO: Carregar matérias dinamicamente
  async function carregarMaterias() {
    try {
      const response = await fetch('http://localhost:8080/api/subjects');
      if (!response.ok) throw new Error('Erro ao carregar matérias');
      
      const materias = await response.json();
      
      // Preencher select de matéria para professor
      const selectMateria = document.getElementById('materia');
      selectMateria.innerHTML = '<option value="">Selecione uma matéria</option>';
      materias.forEach(materia => {
        const option = document.createElement('option');
        option.value = materia.id;
        option.textContent = materia.name;
        selectMateria.appendChild(option);
      });
    } catch (error) {
      console.error('Erro ao carregar matérias:', error);
      alert('Erro ao carregar matérias. Verifique se o backend está rodando.');
    }
  }

  // ✅ CORRIGIDO: Validação de CPF (completa com verificação de dígitos)
  function validarCPF(cpf) {
    cpf = cpf.replace(/[^\d]/g, '');
    if (cpf.length !== 11) return false;
    
    // Validação completa com verificação de dígitos verificadores
    let soma = 0;
    let resto;
    
    if (cpf === "00000000000") return false;
    
    for (let i = 1; i <= 9; i++) {
      soma += parseInt(cpf.substring(i-1, i)) * (11 - i);
    }
    resto = (soma * 10) % 11;
    
    if ((resto === 10) || (resto === 11)) resto = 0;
    if (resto !== parseInt(cpf.substring(9, 10))) return false;
    
    soma = 0;
    for (let i = 1; i <= 10; i++) {
      soma += parseInt(cpf.substring(i-1, i)) * (12 - i);
    }
    resto = (soma * 10) % 11;
    
    if ((resto === 10) || (resto === 11)) resto = 0;
    if (resto !== parseInt(cpf.substring(10, 11))) return false;
    
    return true;
  }

  // ✅ CORRIGIDO: Submissão integrada com API
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const activeTab = document.querySelector(".tab-button.active").getAttribute("data-tab");
    const formData = new FormData(form);

    // Validação de CPF
    const cpf = formData.get("cpf");
    if (!validarCPF(cpf)) {
      alert("CPF inválido!");
      return;
    }

    // Validação de email
    const email = formData.get("email");
    if (!email.includes('@')) {
      alert("Email inválido!");
      return;
    }

    // Preparar dados para envio
    const userData = {
      nome: formData.get("nomeCompleto"),
      email: email,
      senha: "123456", // Senha padrão
      cpf: cpf,
      telefone: formData.get("telefone"),
      tipo: activeTab.toUpperCase(),
      endereco: formData.get("endereco"),
      pai: formData.get("pai"),
      mae: formData.get("mae")
    };

    // ✅ CORRIGIDO: Adicionar turmaId para aluno e professor
    if (activeTab === "aluno") {
      const turmaId = formData.get("turmaAluno");
      if (turmaId) {
        userData.turmaId = parseInt(turmaId);
      }
    } else if (activeTab === "professor") {
      const turmaId = formData.get("turma");
      if (turmaId) {
        userData.turmaId = parseInt(turmaId);
      }
    }

    // ✅ CORRIGIDO: Enviar para API
    try {
      const response = await fetch('http://localhost:8080/api/users', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData)
      });

      if (response.ok) {
        const savedUser = await response.json();
        alert(`${activeTab.charAt(0).toUpperCase() + activeTab.slice(1)} cadastrado com sucesso!`);
        form.reset();
        // Resetar para aba professor
        tabButtons[0].click();
      } else {
        const error = await response.json();
        alert(`Erro ao cadastrar: ${error.error || 'Erro desconhecido'}`);
      }
    } catch (error) {
      console.error('Erro ao cadastrar:', error);
      alert('Erro ao conectar com o servidor. Verifique se o backend está rodando.');
    }
  });
});
