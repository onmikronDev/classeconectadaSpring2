document.addEventListener("DOMContentLoaded", async () => {
  const turmaList = document.getElementById("turmaList");
  const alunosList = document.getElementById("alunosList");

  // Modal de Aplicar Nota
  const notaModal = document.getElementById("notaModal");
  const closeNotaModal = document.getElementById("closeNotaModal");
  const alunoNotaInput = document.getElementById("alunoNota");
  const materiaNotaInput = document.getElementById("materiaNota");
  const descricaoNotaInput = document.getElementById("descricaoNota");
  const valorNotaInput = document.getElementById("valorNota");
  const notaForm = document.getElementById("notaForm");

  let turmaSelecionada = null; // Elemento de turma selecionada
  let turmaIdSelecionada = null; // ID da turma selecionada
  let alunoSelecionado = null; // Elemento de aluno selecionado
  let alunoIdSelecionado = null; // ID do aluno selecionado

  // ✅ CORRIGIDO: Carregar turmas da API
  await carregarTurmas();
  
  // ✅ CORRIGIDO: Carregar matérias da API
  await carregarMaterias();

  /**
   * ✅ CORRIGIDO: Carregar turmas da API
   */
  async function carregarTurmas() {
    try {
      const response = await fetch('http://localhost:8080/api/classes');
      if (!response.ok) throw new Error('Erro ao carregar turmas');
      
      const turmas = await response.json();
      
      turmaList.innerHTML = "";
      turmas.forEach((turma) => {
        const li = document.createElement("li");
        li.textContent = turma.name;
        li.dataset.turmaId = turma.id;
        li.addEventListener("click", () => selecionarTurma(li, turma.id));
        turmaList.appendChild(li);
      });
    } catch (error) {
      console.error('Erro ao carregar turmas:', error);
      alert('Erro ao carregar turmas. Verifique se o backend está rodando.');
    }
  }

  /**
   * ✅ CORRIGIDO: Carregar matérias da API para o select
   */
  async function carregarMaterias() {
    try {
      const response = await fetch('http://localhost:8080/api/subjects');
      if (!response.ok) throw new Error('Erro ao carregar matérias');
      
      const materias = await response.json();
      
      materiaNotaInput.innerHTML = '<option value="" disabled selected>Selecione uma matéria</option>';
      materias.forEach(materia => {
        const option = document.createElement('option');
        option.value = materia.id;
        option.textContent = materia.name;
        materiaNotaInput.appendChild(option);
      });
    } catch (error) {
      console.error('Erro ao carregar matérias:', error);
      // Manter matérias hardcoded como fallback
    }
  }

  /**
   * ✅ CORRIGIDO: Carregar alunos da turma selecionada
   */
  async function carregarAlunos(turmaId) {
    try {
      const response = await fetch(`http://localhost:8080/api/classes/${turmaId}/students`);
      if (!response.ok) throw new Error('Erro ao carregar alunos');
      
      const alunos = await response.json();
      
      alunosList.innerHTML = "";
      alunos.forEach((aluno) => {
        const li = document.createElement("li");
        li.innerHTML = `<span>${aluno.nome}</span>`;
        li.dataset.alunoId = aluno.id;
        li.dataset.alunoNome = aluno.nome;
        li.addEventListener("click", () => selecionarAluno(li, aluno.id, aluno.nome));
        alunosList.appendChild(li);
      });
    } catch (error) {
      console.error('Erro ao carregar alunos:', error);
      alert('Erro ao carregar alunos.');
    }
  }

  function selecionarTurma(li, turmaId) {
    if (turmaSelecionada) turmaSelecionada.classList.remove("selected");
    turmaSelecionada = li;
    turmaSelecionada.classList.add("selected");
    turmaIdSelecionada = turmaId;
    
    // ✅ CORRIGIDO: Carregar alunos da API
    carregarAlunos(turmaId);
  }

  function selecionarAluno(li, alunoId, alunoNome) {
    if (alunoSelecionado) alunoSelecionado.classList.remove("selected");
    alunoSelecionado = li;
    alunoSelecionado.classList.add("selected");
    alunoIdSelecionado = alunoId;
  }

  document.getElementById("notasBtn").addEventListener("click", () => {
    if (alunoSelecionado && turmaSelecionada) {
      alunoNotaInput.value = alunoSelecionado.dataset.alunoNome;
      materiaNotaInput.value = "";
      descricaoNotaInput.value = "";
      valorNotaInput.value = "";
      notaModal.style.display = "flex";
    } else {
      alert("Selecione uma turma e um aluno antes de aplicar uma nota.");
    }
  });

  closeNotaModal.addEventListener("click", () => {
    notaModal.style.display = "none";
  });

  // ✅ CORRIGIDO: Enviar nota para API
  notaForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const materiaId = materiaNotaInput.value;
    const descricao = descricaoNotaInput.value;
    const nota = parseFloat(valorNotaInput.value);

    if (!materiaId) {
      alert("Por favor, selecione uma matéria.");
      return;
    }

    // Validação de nota
    if (nota < 0 || nota > 10) {
      alert("Nota deve estar entre 0 e 10.");
      return;
    }

    // ✅ CORRIGIDO: Preparar DTO para API
    const gradeData = {
      studentId: alunoIdSelecionado,
      subjectId: parseInt(materiaId),
      value: nota,
      description: descricao,
      examDate: new Date().toISOString().split('T')[0] // yyyy-MM-dd
    };

    try {
      const response = await fetch('http://localhost:8080/api/grades', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(gradeData)
      });

      if (response.ok) {
        alert(`Nota enviada com sucesso!`);
        notaModal.style.display = "none";
      } else {
        const error = await response.json();
        alert(`Erro ao enviar nota: ${error.error || 'Erro desconhecido'}`);
      }
    } catch (error) {
      console.error('Erro ao enviar nota:', error);
      alert('Erro ao conectar com o servidor.');
    }
  });

  // Atualizar botão histórico para passar studentId
  document.getElementById("historicoBtn").addEventListener("click", (e) => {
    e.preventDefault();
    if (alunoIdSelecionado) {
      window.location.href = `../html/historico.html?studentId=${alunoIdSelecionado}`;
    } else {
      alert("Selecione um aluno para ver o histórico.");
    }
  });

  document.getElementById("voltarBtn").addEventListener("click", () => {
    window.location.href = "index.html";
  });
});
